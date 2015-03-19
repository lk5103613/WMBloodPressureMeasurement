package com.wm.activity;

import android.app.AlertDialog;
import android.os.Bundle;

import com.wm.network.CheckNeedUploadTask;
import com.wm.network.NetChangeReceiver;
import com.wm.network.NetChangeReceiver.NetChangeCallBack;
import com.wm.utils.DialogUtils;

public class BaseActivity extends BLEBaseActivity implements NetChangeCallBack {
	
	protected AlertDialog mAlertDialog;
	private NetChangeReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(mAlertDialog == null) 
			mAlertDialog = DialogUtils.showAlertDialog(mContext, -1, "上传", "上传将会耗费您一定的流量，是否确定上传？", "是", "否", null, null);
		if(mReceiver == null)
			mReceiver = NetChangeReceiver.getInstance(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, NetChangeReceiver.getIntentFilter());
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		if (isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in,
					R.anim.slide_out_to_right);
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onChange(int netType) {
		new CheckNeedUploadTask(mContext, netType, mAlertDialog).execute();
	}
}
