package com.wm.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 
 * 1. sendMessage
 * 2. Receiver获取消息的验证码
 * 3. 调用submitVerifyCode发送并验证
 * 4. 在毁掉方法中做处理
 * @author Like
 *
 */
public class MessageManager {
	
	private Context mContext;
	private static MessageManager mManager;
	private static MessageCallback mCallback;
	private MessageReceiver mReceiver;
	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			if(result == SMSSDK.RESULT_COMPLETE) {
				if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					mCallback.submitVerificationCodeSuccess(data);
				} else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					mCallback.getVerificationCodeSuccess(data);
				} else if(event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
					mCallback.getSupportedCountriesSuccess(data);
				}
			} else {
				mCallback.errorAppear();
			}
			return false;
		}
	});
	
	public static MessageManager getInstance(Context context, String appKey, String appSecret, MessageCallback callback) {
		if(mManager == null) {
			mManager = new MessageManager(context, appKey, appSecret);
		}
		mCallback = callback;
		return mManager;
	}
	
	private MessageManager(Context context, String appKey, String appSecret) {
		mReceiver = new MessageReceiver();
		this.mContext = context;
		SMSSDK.initSDK(mContext, appKey, appSecret);
		EventHandler eh = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = mHandler.obtainMessage();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				mHandler.sendMessage(msg);
			}
		};
		SMSSDK.registerEventHandler(eh);
	}
	
	public MessageReceiver getReceiver() {
		if(mReceiver == null)
			mReceiver = new MessageReceiver();
		return mReceiver;
	}
	
	public void getSupportCountries() {
		SMSSDK.getSupportedCountries();
	}
	
	public void sendMessage(String country, String phone) {
		SMSSDK.getVerificationCode(country, phone);
	}
	
	public void submitVerifyCode(String country, String phone, String code) {
		SMSSDK.submitVerificationCode(country, phone, code);
	}
	
	public interface MessageCallback {
		
		void getSupportedCountriesSuccess(Object data);
		
		void submitVerificationCodeSuccess(Object data);
		
		void getVerificationCodeSuccess(Object data);
		
		void receiveMsg(String code);
		
		void errorAppear();

	}
	
	public class MessageReceiver extends BroadcastReceiver {
		
		public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
				Bundle bundle = intent.getExtras();  
		        SmsMessage msg = null;
		        if(bundle == null)
		        	return;
		        Object[] smsObjs = (Object[]) bundle.get("pdus");
		        if(smsObjs == null || smsObjs.length == 0) 
		        	return;
		        msg = SmsMessage.createFromPdu((byte[]) smsObjs[0]);  
		        String msgBody = msg.getDisplayMessageBody();
		        String code = getVerifyCode(msgBody);
		        if(code == null)
		        	return;
		        mCallback.receiveMsg(code);
			}
		}
		
		private String getVerifyCode(String msg) {
			if(msg.indexOf("领泰") == -1)
				return null;
			int length = msg.length();
			StringBuilder sb = new StringBuilder();
			for(int i=length-4; i<length; i++) {
				sb.append(msg.charAt(i));
			}
			return sb.toString();
		}
		
	}

}
