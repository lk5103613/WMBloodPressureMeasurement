package com.wm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class LoginActivity extends Activity {
	
	@InjectView(R.id.login_scroll)
	ScrollView mScrollView;
	@InjectView(R.id.scroll_inner)
	View mInner;

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
		finish();
	}
	
	@OnClick({R.id.txt_username, R.id.txt_pwd})
	public void clickUsername(View v) {
		if(v.isFocused()) {
			scrollToBottom();
		}
	}
	
	@OnFocusChange({R.id.txt_pwd, R.id.txt_username})
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus) {
			scrollToBottom();
		}
	}
	
	private void scrollToBottom() {
		Handler mHandler = new Handler();

		mHandler.postDelayed(new Runnable() {
			public void run() {
				if (mScrollView == null || mInner == null) {
					return;
				}
				int offset = mInner.getMeasuredHeight() - mScrollView.getHeight();
				if (offset < 0) {
					offset = 0;
				}
				mScrollView.scrollTo(0, offset);
			}
		}, 100);
	}

}
