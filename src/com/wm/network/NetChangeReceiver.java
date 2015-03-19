package com.wm.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wm.utils.NetUtils;

public class NetChangeReceiver extends BroadcastReceiver{
	
	private NetChangeCallBack mCallback;
	private static IntentFilter mIntentFilter;
	private static NetChangeReceiver mReceiver;
	
	private NetChangeReceiver(){};
	
	private NetChangeReceiver(NetChangeCallBack callback) {
		this.mCallback = callback;
	}
	
	public static NetChangeReceiver getInstance(NetChangeCallBack callback) {
		if(mReceiver == null) {
			mReceiver = new NetChangeReceiver();
		}
		mReceiver.mCallback = callback;
		return mReceiver;
	}
	
	public interface NetChangeCallBack {
		void onChange(int netType);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("receiver");
		int netType = NetUtils.getConnectState(context);
		mCallback.onChange(netType);
	}
	
	public static IntentFilter getIntentFilter() {
		if(mIntentFilter == null) {
			mIntentFilter = new IntentFilter();
			mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		}
		return mIntentFilter;
	}
	
}
