package com.wm.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class RegisterActivity extends ActionBarActivity {

	@InjectView(R.id.btn_send_code)
	Button mbtnSendCode;
	@InjectView(R.id.reg_code_hint)
	TextView mAuthCode;
	@InjectView(R.id.reg_name)
	EditText mRegName;
	@InjectView(R.id.reg_phone)
	EditText mRegPhone;
	@InjectView(R.id.reg_code)
	EditText mRegCode;
	@InjectView(R.id.reg_identity)
	EditText mIdentity;
	@InjectView(R.id.reg_psw)
	EditText mRegPsw;
	@InjectView(R.id.reg_conform_psw)
	EditText mConformPsw;
	@InjectView(R.id.reg_scroll)
	ScrollView mScrollView;
	@InjectView(R.id.reg_content)
	View mInner;
	
	private CountDownTimer mCountTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ButterKnife.inject(this);
	}

	@OnClick(R.id.btn_send_code)
	public void sendCode(View view) {
		mbtnSendCode.setEnabled(false);
		if(mCountTimer == null) {
			mCountTimer = new CountDownTimer(60000, 1000) {
				@Override
				public void onFinish() {
					mbtnSendCode.setEnabled(true);
					mAuthCode.setText("(60秒后重发)");
				}

				@Override
				public void onTick(long arg0) {
					mAuthCode.setText("("+arg0/1000+"秒后重发)");
				}

			};
		}
		mCountTimer.start();
	}
	
	@OnClick(R.id.btn_reg)
	public void clickReg(View view) {
		
	}
	
	@OnClick(R.id.add_back)
	public void clickBack(View view) {
		finish();
	}
	
	@OnFocusChange({R.id.reg_identity,R.id.reg_psw,R.id.reg_conform_psw})
	public void clickFource(View view) {
		if(view.isFocused()) {
			scrollToBottom();
		}
	}
	
	
	@Override
	protected void onDestroy() {
		if(mCountTimer!=null) {
			mCountTimer.cancel();
		}
		
		super.onDestroy();
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
		}, 200);
	}

}
