package com.wm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wm.customview.ClearEditText;
import com.wm.entity.LoginEntity;
import com.wm.entity.RequestEntity;
import com.wm.entity.Response;
import com.wm.network.NetworkFactory;
import com.wm.utils.MD5Utils;
import com.wm.utils.StateSharePrefs;
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
	private StateSharePrefs mState;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.inject(this);
		
		mHandler = new Handler();
		mContext = LoginActivity.this;
		mState = StateSharePrefs.getInstance(mContext);
	}

	@OnClick({ R.id.txt_username, R.id.txt_pwd })
	public void clickUsername(View v) {
		if (v.isFocused()) {
			scrollToBottom();
		}
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
	
	@OnClick(R.id.register_txt)
	public void clickRegister(View v){
		Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.btn_login)
	public void login(View v) {
		new LoginTask().execute();
	}
	
	@OnClick(R.id.add_back)
	public void back(View v){
		finish();
	}
	
	
	private class LoginTask extends AsyncTask<Void, Void, Response> {

		@Override
		protected Response doInBackground(Void... params) {
			String userName = mUserName.getText().toString();
			String pwd = MD5Utils.string2MD5(mPwd.getText().toString());
			LoginEntity loginEntity = new LoginEntity(userName, pwd);
			RequestEntity<LoginEntity> request = new RequestEntity<LoginEntity>("test", "test", loginEntity);
			if(SystemUtils.getConnectState(mContext) == SystemUtils.TYPE_NONE) 
				return null;
			Response response = null;
			try {
				response = NetworkFactory.getAuthService().login(request);
			} catch(Exception e) {
				
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(Response result) {
			if(result == null) {
				Toast.makeText(mContext, "无网络或网络异常", Toast.LENGTH_LONG).show();
				return;
			}
			if(result.code == 0) {
				mState.saveState(StateSharePrefs.TYPE_LOGIN, true);
				Toast.makeText(mContext, "登陆成功", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(mContext, result.info, Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
}
