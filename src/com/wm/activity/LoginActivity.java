package com.wm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ScrollView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wm.utils.TabPager;

/**
 * 
 * @author Like
 *
 */
public class LoginActivity extends ActionBarActivity {

	@InjectView(R.id.login_scroll)
	ScrollView mScrollView;
	@InjectView(R.id.scroll_inner)
	View mInner;
	private Context mContext;
	private TabPager mTabPager;
	Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.inject(this);
		mHandler = new Handler();
		mContext = LoginActivity.this;
		mTabPager = TabPager.getInstance(mContext);
	}

	@OnClick(R.id.btn_login)
	public void login(View v) {
		mTabPager.clear();
		Intent intent = new Intent(mContext, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@OnClick({ R.id.txt_username, R.id.txt_pwd })
	public void clickUsername(View v) {
		if (v.isFocused()) {
			scrollToBottom();
		}
	}
	
	@OnClick(R.id.register_txt)
	public void clickRegister(View v){
		Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
		startActivity(intent);
	}

//	@OnFocusChange({ R.id.txt_pwd, R.id.txt_username })
//	public void onFocusChange(View v, boolean hasFocus) {
//		if (hasFocus) {
//			scrollToBottom();
//		}
//	}

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
		}, 100);
	}
	
}
