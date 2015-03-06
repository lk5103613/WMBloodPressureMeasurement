package com.wm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {
	
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.inject(this);
		
		mContext = LoginActivity.this;
	}
	
	@OnClick(R.id.btn_login)
	public void login(View v) {
		Intent intent = new Intent(mContext, MainActivity.class);
		startActivity(intent);
	}

}
