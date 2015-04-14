package com.wm.activity;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import com.wm.message.AppKeys;
import com.wm.message.MessageManager;
import com.wm.message.MessageManager.MessageCallback;
import com.wm.message.MessageManager.MessageReceiver;

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
	EditText mIdentity;
	@InjectView(R.id.reg_psw)
	EditText mRegPsw;
	@InjectView(R.id.reg_conform_psw)
	EditText mConformPsw;
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
	
	@OnClick(R.id.btn_reg)
	public void clickReg(View view) {
		String phone = mRegPhone.getText().toString();
		String code = mRegCode.getText().toString();
		mMsgManager.submitVerifyCode(countryZone, phone, code);
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

}
