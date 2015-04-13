package com.wm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import butterknife.ButterKnife;

import com.wm.message.AppKeys;
import com.wm.message.MessageManager;
import com.wm.message.MessageManager.MessageCallback;
import com.wm.message.MessageManager.MessageReceiver;

public class RegistActivity extends Activity implements MessageCallback {
	
	private Context mContext;
	private MessageManager mMsgManager;
	private IntentFilter mMsgFilter;
	private MessageReceiver mMsgReceiver;
	private String countryZone = "86";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		ButterKnife.inject(this);
		
		mContext = this;
		
		mMsgManager = MessageManager.getInstance(mContext, AppKeys.APP_KEY, AppKeys.APP_SECRET, this);
		mMsgReceiver = mMsgManager.getReceiver();
		
		mMsgManager.sendMessage(countryZone, "15895026586");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mMsgReceiver, createSmsFilter());
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(mMsgReceiver);
	}
	
	private IntentFilter createSmsFilter() {
		if(mMsgFilter == null) {
			mMsgFilter = new IntentFilter();
			mMsgFilter.setPriority(999);
			mMsgFilter.addAction(MessageReceiver.SMS_RECEIVED_ACTION);
		}
		return mMsgFilter;
	}

	@Override
	public void getSupportedCountriesSuccess(Object data) {
	}

	@Override
	public void submitVerificationCodeSuccess(final Object data) {
		System.out.println("submit verification code success:       " + data);
	}

	@Override
	public void getVerificationCodeSuccess(Object data) {
		System.out.println("get verification code success:    " + data);
	}

	@Override
	public void receiveMsg(String code) {
		if(code != null)
		mMsgManager.submitVerifyCode(countryZone, "15895026586", code);
	}

	@Override
	public void errorAppear() {
		
	}
	
}
