package com.wm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import com.wm.utils.TabPager;

/**
 * 
 * @author Like
 *
 */
public class LoginActivity extends BaseActivity {

	@InjectView(R.id.login_scroll)
	ScrollView mScrollView;
	@InjectView(R.id.scroll_inner)
	View mInner;
	@InjectView(R.id.login_toolbar)
	Toolbar mToolbar;
	private Context mContext;
	private TabPager mTabPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.inject(this);

		mToolbar.setTitle(getResources().getString(R.string.action_login));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
		
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

	@OnFocusChange({ R.id.txt_pwd, R.id.txt_username })
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
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
