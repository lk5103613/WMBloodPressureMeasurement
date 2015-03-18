package com.wm.activity;

import com.wm.utils.NetUtils;

import android.os.Bundle;


public class BaseActivity extends BLEBaseActivity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(NetUtils.getConnectState(mContext) != NetUtils.TYPE_NONE) {
			
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in,
					R.anim.slide_out_to_right);
		}
	}
}
