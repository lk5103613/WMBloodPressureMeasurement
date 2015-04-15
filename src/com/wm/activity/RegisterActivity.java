package com.wm.activity;

import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import com.wm.entity.RegisterEntity;
import com.wm.entity.RequestEntity;
import com.wm.entity.Response;
import com.wm.network.NetworkFactory;
import com.wm.utils.DialogUtils;
import com.wm.utils.MD5Utils;
import com.wm.utils.StateSharePrefs;
import com.wm.utils.SystemUtils;

public class RegisterActivity extends ActionBarActivity implements OnCheckedChangeListener{

	@InjectView(R.id.btn_send_code)
	Button mbtnSendCode;
	@InjectView(R.id.btn_reg)
	Button mBtnReg;
	@InjectView(R.id.reg_code_hint)
	TextView mAuthCode;
	@InjectView(R.id.reg_name)
	EditText mRegName;
	@InjectView(R.id.reg_phone)
	EditText mRegPhone;
	@InjectView(R.id.reg_code)
	EditText mRegCode;
	@InjectView(R.id.reg_identity)
	EditText mRegIdentity;
	@InjectView(R.id.reg_psw)
	EditText mRegPsw;
	@InjectView(R.id.reg_conform_psw)
	EditText mRegConformPsw;
	@InjectView(R.id.reg_scroll)
	ScrollView mScrollView;
	@InjectView(R.id.reg_content)
	View mInner;
	@InjectView(R.id.reg_checkbox)
	CheckBox mRegCheckBox;
	@InjectView(R.id.reg_service_item)
	TextView mRegServiceItem;
	
	private CountDownTimer mCountTimer;
	private Context mContext;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ButterKnife.inject(this);
		
		mRegCheckBox.setOnCheckedChangeListener(this);
		mContext = this;
		
		mHandler = new Handler();
		
//		String url = "<a href=\"http://www.leadingtechmed.cn/dataUploadProtocol.html\">"
//				+ mRegServiceItem.getText() + "</a> ";
//		mRegServiceItem.setText(Html.fromHtml(url));
//		mRegServiceItem.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@OnClick(R.id.btn_send_code)
	public void sendCode(View view) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String phone = mRegPhone.getText().toString();
//				NetworkFactory.getAuthService().sendMessage();
			}
		}).start();
		mbtnSendCode.setEnabled(false);
		if(mCountTimer == null) {
			mCountTimer = new CountDownTimer(60000, 1000) {
				@Override
				public void onFinish() {
					mbtnSendCode.setEnabled(true);
					mAuthCode.setText("(60秒后重发)");
				}
				@Override
				public void onTick(long millisUntilFinished) {
					mAuthCode.setText("("+millisUntilFinished/1000+"秒后重发)");
				}
			};
		}
		mCountTimer.start();
	}
	
	@OnClick(R.id.add_back)
	public void clickBack(View view) {
		finish();
	}
	
	@OnFocusChange({R.id.reg_identity,R.id.reg_psw,R.id.reg_conform_psw})
	public void clickFource(View view) {
		if(view.isFocused()) {
			scrollToBottom();
		}
	}
	
	@Override
	protected void onDestroy() {
		if(mCountTimer!=null) {
			mCountTimer.cancel();
		}
		super.onDestroy();
	}
	
	/**
	 * 滚动到底部
	 */
	private void scrollToBottom() {
		mHandler.postDelayed(new Runnable() {
			public void run() {
				if (mScrollView == null || mInner == null) {
					return;
				}
				int offset = mInner.getMeasuredHeight()
						- mScrollView.getHeight();
				if (offset < 0) {
					offset = 0;
				}
				mScrollView.scrollTo(0, offset);
			}
		}, 200);
	}
	
	/**
	 * 输入内容校验
	 * 
	 * @return
	 */
	private boolean verify(){
		String name = mRegName.getText().toString().trim();
		String phone = mRegPhone.getText().toString().trim();
		String code = mRegCode.getText().toString().trim();
		String identityCard = mRegIdentity.getText().toString().trim();
		String psw = mRegPsw.getText().toString().trim();
		String conformPsw = mRegConformPsw.getText().toString().trim();
		
		String[] fields = new String[]{name, phone, code, identityCard, psw, conformPsw};
		boolean result = isEmpty(fields)&&verifyName(name)&&verifyPhone(phone)
				&&verifyCard(identityCard)&&verifyPsw(psw, conformPsw);
		return result;
	}
	
	private boolean isEmpty(String[] fields){
		for (int i = 0, size = fields.length; i < size; i++) {
			if ("".equals(fields[i])) {
				DialogUtils.showToast(this,getString(R.string.required_msg), DialogUtils.ERROR);
				return false;
			}
		}
		return true;
	}
	
	
	private boolean verifyName(String name){
		if(!Pattern.matches("^[\u4e00-\u9fa5a-zA-Z]{2,20}$", name)){
			DialogUtils.showToast(this,getString(R.string.name_fromat_error), DialogUtils.ERROR);
			return false;
		}
		return true;
	}
	
	private boolean verifyPhone(String phoneNum){
		if(!Pattern.matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", phoneNum)){
			DialogUtils.showToast(this,getString(R.string.phone_format_error), DialogUtils.ERROR);
			return false;
		}
		return true;
	}
	
	private boolean verifyCard(String idcard){
		if (!Pattern.matches(
				"^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$",
				idcard)){
			DialogUtils.showToast(this,getString(R.string.idcard_format_error), DialogUtils.ERROR);
			return false;
		}
		return true;
	}
	
	private boolean verifyPsw(String psw, String conformPsw){
		if(psw.length()<6 || psw.length()>20) {
			DialogUtils.showToast(this,getString(R.string.pwd_format_error), DialogUtils.ERROR);
			return false;
		}
		
		if(!psw.equals(conformPsw)) {
			DialogUtils.showToast(this,getString(R.string.pwd_conform_error), DialogUtils.ERROR);
			return false;
		}
		return true;
	}
	

	
	@OnClick(R.id.btn_reg)
	public void clickReg(View view) {
		boolean result = verify();
		if(!result)
			return;
		String phone = mRegPhone.getText().toString().trim();
		String code = mRegCode.getText().toString().trim();
		String userName = mRegName.getText().toString().trim();
		String userCard = mRegIdentity.getText().toString().trim();
		String pwd = MD5Utils.string2MD5(mRegPsw.getText().toString());
		RegisterEntity registerEntity = new RegisterEntity(userName, phone, userCard, pwd, code);
		RequestEntity<RegisterEntity> request = new RequestEntity<>("test", "test", registerEntity);
		new RegisterTask(request).execute();
	}
	
	public class RegisterTask extends AsyncTask<Void, Void, Response> {
		
		private RequestEntity<RegisterEntity> mRequest;
		private ProgressDialog mProgress;
		
		public RegisterTask(RequestEntity<RegisterEntity> request) {
			this.mRequest = request;
		}
		
		@Override
		protected void onPreExecute() {
			mProgress = DialogUtils.createProgressDialog(mContext, "正在注册", "请等待...");
			mProgress.show();
		}
		
		@Override
		protected Response doInBackground(Void... params) {
			if(SystemUtils.getConnectState(mContext) == SystemUtils.TYPE_NONE) {
				return null;
			}
			Response response = null;
			try {
				response = NetworkFactory.getAuthService().register(mRequest);
			} catch(Exception e) {
				
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(Response result) {
			mProgress.dismiss();
			if(result == null) {
				DialogUtils.showToast(RegisterActivity.this,getString(R.string.network_error), DialogUtils.ERROR);
				return;
			}
			if(result.info.equals("success")) {
				DialogUtils.showToast(RegisterActivity.this,getString(R.string.register_success), DialogUtils.SUCCESS);
				StateSharePrefs.getInstance(RegisterActivity.this).saveStr(StateSharePrefs.TYPE_USER_PHONE,
						mRegPhone.getText().toString().trim());
				mBtnReg.setEnabled(false);
				
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
			} else {
				DialogUtils.showToast(RegisterActivity.this,result.info, DialogUtils.ERROR);
			}
		}
	
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		mBtnReg.setEnabled(isChecked);
	}

}
