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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
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
	
	private boolean verify(){
		String name = mRegName.getText().toString().trim();
		String phone = mRegPhone.getText().toString().trim();
		String code = mRegCode.getText().toString().trim();
		String identityCard = mRegIdentity.getText().toString().trim();
		String psw = mRegPsw.getText().toString().trim();
		String conformPsw = mRegConformPsw.getText().toString().trim();
		
		String[] fields = new String[]{name, phone, code, identityCard, psw, conformPsw};
		boolean result = isEmpty(fields)&&verifyPhone(phone)&&verifyCard(identityCard)&&verifyPsw(psw, conformPsw);
		return result;
	}
	
	private boolean isEmpty(String[] fields){
		for (int i = 0, size = fields.length; i < size; i++) {
			if ("".equals(fields[i])) {
				DialogUtils.showToast(this,"请输入必填项", DialogUtils.ERROR);
				return false;
			}
		}
		
		return true;
	}
	
	private boolean verifyPhone(String phoneNum){
		if(!Pattern.matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", phoneNum)){
			DialogUtils.showToast(this,"请输入正确的手机号码", DialogUtils.ERROR);
			return false;
		}
		return true;
	}
	
	private boolean verifyCard(String idcard){
		if (!Pattern.matches(
				"^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$",
				idcard)){
			DialogUtils.showToast(this,"身份证格式不正切", DialogUtils.ERROR);
			return false;
		}
		return true;
	}
	
	private boolean verifyPsw(String psw, String conformPsw){
		if(psw.length()<6 || psw.length()>20) {
			DialogUtils.showToast(this,"密码为6-20个字符", DialogUtils.ERROR);
			return false;
		}
		
		if(!psw.equals(conformPsw)) {
			DialogUtils.showToast(this,"两次密码不匹配", DialogUtils.ERROR);
			return false;
		}
		return true;
	}
	

	
	@OnClick(R.id.btn_reg)
	public void clickReg(View view) {
		boolean result = verify();
		if(!result)
			return;
		String phone = mRegPhone.getText().toString();
		String code = mRegCode.getText().toString();
		String userName = mRegName.getText().toString();
		String userCard = mRegIdentity.getText().toString();
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
				DialogUtils.showToast(RegisterActivity.this,"无网络或网络异常", DialogUtils.ERROR);
				return;
			}
			if(result.info.equals("success")) {
				DialogUtils.showToast(RegisterActivity.this,"注册成功", DialogUtils.SUCCESS);
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
