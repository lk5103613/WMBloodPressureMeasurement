package com.wm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import butterknife.ButterKnife;

public class RegistActivity extends Activity {
	
	private Context mContext;
	private IntentFilter mMsgFilter;
	private String countryZone = "86";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		ButterKnife.inject(this);
		
		mContext = this;
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	
	
}
