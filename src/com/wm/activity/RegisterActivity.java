package com.wm.activity;

import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.wm.message.AppKeys;
import com.wm.message.MessageManager;
import com.wm.message.MessageManager.MessageCallback;
import com.wm.message.MessageManager.MessageReceiver;
import com.wm.network.NetworkFactory;
import com.wm.utils.MD5Utils;

public class RegisterActivity extends ActionBarActivity implements MessageCallback {

	@InjectView(R.id.btn_send_code)
	Button mbtnSendCode;
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
	
	private CountDownTimer mCountTimer;
	private MessageManager mMsgManager;
	private MessageReceiver mMsgReceiver;
	private IntentFilter mMsgFilter;
	private Context mContext;
	private Handler mHandler;
	private String countryZone = "86";
	private final int SUCCESS = 1;
	private final int ERROR = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ButterKnife.inject(this);
		
		mContext = this;
		mMsgManager = MessageManager.getInstance(mContext, AppKeys.APP_KEY, AppKeys.APP_SECRET, this);
		mMsgReceiver = mMsgManager.getReceiver();
		mHandler = new Handler();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mMsgReceiver, createSmsFilter());
	}
	
	private IntentFilter createSmsFilter() {
		if(mMsgFilter == null) {
			mMsgFilter = new IntentFilter();
			mMsgFilter.setPriority(999);
			mMsgFilter.addAction(MessageReceiver.SMS_RECEIVED_ACTION);
		}
		return mMsgFilter;
	}

	@OnClick(R.id.btn_send_code)
	public void sendCode(View view) {
		String phone = mRegPhone.getText().toString();
		mMsgManager.sendMessage(countryZone, phone);
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
		unregisterReceiver(mMsgReceiver);
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
				showToast("请输入必填项", ERROR);
				return false;
			}
		}
		
		return true;
	}
	
	private boolean verifyPhone(String phoneNum){
		if(!Pattern.matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", phoneNum)){
			showToast("请输入正确的手机号码", ERROR);
			return false;
		}
		return true;
	}
	
	private boolean verifyCard(String idcard){
		if (!Pattern.matches(
				"^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$",
				idcard)){
			showToast("身份证格式不正切", ERROR);
			return false;
		}
		return true;
	}
	
	private boolean verifyPsw(String psw, String conformPsw){
		if(psw.length()<6 || psw.length()>20) {
			showToast("密码为6-20个字符", ERROR);
			return false;
		}
		
		if(!psw.equals(conformPsw)) {
			showToast("两次密码不匹配", ERROR);
			return false;
		}
		return true;
	}
	
	
	/**
	 * @param msg
	 * @param code 0 error,  1 success
	 */
	private void showToast(String msg, int code){
		LayoutInflater inflater = getLayoutInflater();
	    View layout = inflater.inflate(R.layout.submitsuccess_toast,
	    (ViewGroup) findViewById(R.id.toast_layout_root));
	    
	    ImageView image = (ImageView) layout.findViewById(R.id.toastcheck);
	    int imgId = code==SUCCESS?R.drawable.check32:R.drawable.error32;
	    image.setImageResource(imgId);
	    
	    TextView text = (TextView) layout.findViewById(R.id.toasttext);
	    text.setText(msg);
	    Toast toast = new Toast(getApplicationContext());
	    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	    toast.setDuration(Toast.LENGTH_SHORT);
	    toast.setView(layout);
	    toast.show();
	}

	@Override
	public void getSupportedCountriesSuccess(Object data) { }

	@Override
	public void submitVerificationCodeSuccess(Object data) {
	}

	@Override
	public void getVerificationCodeSuccess(Object data) {
	}

	@Override
	public void receiveMsg(String code) {
		if(code == null)
			return;
		mRegCode.setText(code);
	}

	@Override
	public void errorAppear() {
		Toast.makeText(mContext, "验证码错误，请重新输入", Toast.LENGTH_LONG).show();
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
		
		public RegisterTask(RequestEntity<RegisterEntity> request) {
			this.mRequest = request;
		}
		
		@Override
		protected Response doInBackground(Void... params) {
			return NetworkFactory.getAuthService().register(mRequest);
		}
		
		@Override
		protected void onPostExecute(Response result) {
			if(result.info.equals("success")) {
				Toast.makeText(mContext, "注册成功", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
			} else 
				Toast.makeText(mContext, result.info, Toast.LENGTH_LONG).show();
		}
	
	}

}
