package com.wm.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTouch;

import com.wm.customview.ClearEditText;
import com.wm.db.UserInfoDBManager;
import com.wm.entity.LoginEntity;
import com.wm.entity.RequestEntity;
import com.wm.entity.Response;
import com.wm.network.NetworkFactory;
import com.wm.utils.DialogUtils;
import com.wm.utils.MD5Utils;
import com.wm.utils.PropertiesSharePrefs;
import com.wm.utils.SystemUtils;

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
	@InjectView(R.id.txt_username)
	ClearEditText mUserName;
	@InjectView(R.id.txt_pwd)
	ClearEditText mPwd;

	private Context mContext;
	private PropertiesSharePrefs mProperties;
	private Handler mHandler;
	private UserInfoDBManager mUserInfoDBManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.inject(this);

		mHandler = new Handler();
		mContext = LoginActivity.this;
		mProperties = PropertiesSharePrefs.getInstance(mContext);
		mUserInfoDBManager = UserInfoDBManager.getInstance(mContext);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String phone = PropertiesSharePrefs.getInstance(this).getProperty(PropertiesSharePrefs.TYPE_USER_PHONE, "");
		mUserName.setText(phone);
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

	@OnClick(R.id.register_txt)
	public void clickRegister(View v) {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btn_login)
	public void login(View v) {
		if (verify()) {
			new LoginTask().execute();
		}
	}

	@OnClick(R.id.add_back)
	public void back(View v) {
		finish();
	}

	private boolean verify() {
		String name = mUserName.getText().toString().trim();
		String pwd = mPwd.getText().toString().trim();
		if ("".equals(name) || "".equals(pwd)) {
			DialogUtils.showToast(this, getString(R.string.login_check),
					DialogUtils.ERROR);
			return false;
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in,
					R.anim.slide_out_to_right);
		}
	}
	
	private class LoginTask extends AsyncTask<Void, Void, Response> {

		private ProgressDialog mProgress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgress = DialogUtils.createProgressDialog(mContext, "", "ÕýÔÚµÇÂ¼...");
			mProgress.show();
		}

		@Override
		protected Response doInBackground(Void... params) {
			String userName = mUserName.getText().toString().trim();
			String pwd = MD5Utils.string2MD5(mPwd.getText().toString());
			LoginEntity loginEntity = new LoginEntity(userName, pwd);
			RequestEntity<LoginEntity> request = new RequestEntity<LoginEntity>(
					"test", "test", loginEntity);
			if (SystemUtils.getConnectState(mContext) == SystemUtils.TYPE_NONE)
				return null;
			Response response = null;
			try {
				response = NetworkFactory.getAuthService().login(request);
			} catch (Exception e) {

			}
			return response;
		}

		@Override
		protected void onPostExecute(final Response result) {
			mProgress.dismiss();
			if (result == null) {
				DialogUtils.showToast(LoginActivity.this,
						getString(R.string.network_error), DialogUtils.ERROR);
				return;
			}
			if (result.code == 0) {
				mProperties.saveProperty(PropertiesSharePrefs.TYPE_LOGIN, true);
				mProperties.saveProperty(PropertiesSharePrefs.TYPE_CARD,
						result.datas.userInfo.userCard);
				DialogUtils.showToast(LoginActivity.this,
						getString(R.string.login_success), DialogUtils.SUCCESS);
				new Thread(new Runnable() {
					@Override
					public void run() {
						mUserInfoDBManager.saveUser(result.datas.userInfo);
					}
				}).start();
				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				DialogUtils.showToast(LoginActivity.this, result.info,
						DialogUtils.ERROR);
			}
		}
	}
	
	@OnTouch(R.id.scroll_inner)
	public boolean mInnerTouch(View v) {
		System.out.println("touch");
		v.setFocusable(true);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
		//Òþ²Ø¼üÅÌ
		InputMethodManager imm = (InputMethodManager)
		this.getSystemService(Context.INPUT_METHOD_SERVICE); 
		imm.hideSoftInputFromWindow(mUserName.getWindowToken(), 0); 
		return false;
	}

}
