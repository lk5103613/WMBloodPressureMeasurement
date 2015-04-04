package com.wm.activity;

import com.wm.utils.SharedPfUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class WelcomeActivity extends Activity implements
		OnCheckedChangeListener, OnClickListener {

	@InjectView(R.id.welcome_logo)
	ImageView mLogo;
	@InjectView(R.id.welcome_company_name)
	ImageView mCompanyName;

	private Context mContext;
	private AlertDialog mAalertDialog;
	private View mDialogView;
	private CheckBox mAuthCheckBox;
	private Button mBtnYes, mBtnNo;
	private TextView mLinkText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		ButterKnife.inject(this);

		mContext = this;

		Animation logoAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.welcome_logo_anim);
		logoAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Animation shakeAnimation = AnimationUtils.loadAnimation(
						mContext, R.anim.welcome_logo_shake);
				mLogo.startAnimation(shakeAnimation);
			}
		});
		mLogo.startAnimation(logoAnimation);
		Animation companyNameAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.welcome_company_name_anim);
		mCompanyName.startAnimation(companyNameAnimation);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				showAuthDialog();
			}
		}, 3000);
	}

	public void showAuthDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		LayoutInflater inflater = getLayoutInflater();
		mDialogView = inflater.inflate(R.layout.authority_layout, null);

		mAuthCheckBox = (CheckBox) mDialogView.findViewById(R.id.auth_checkbox);
		mLinkText = (TextView) findViewById(R.id.link);
		mBtnNo = (Button) mDialogView.findViewById(R.id.btn_no);
		mBtnYes = (Button) mDialogView.findViewById(R.id.btn_yes);
		mAalertDialog = builder.create();
		mAalertDialog.setCanceledOnTouchOutside(false);
		mAalertDialog.setView(mDialogView, 0, 0, 0, 0);
		mAalertDialog.show();
		
		mAuthCheckBox.setOnCheckedChangeListener(this);
		mBtnNo.setOnClickListener(this);
		mBtnYes.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			mBtnYes.setEnabled(true);
		} else {
			mBtnYes.setEnabled(false);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yes:
			SharedPfUtil.setValue(this, "auth", true);
			break;
		case R.id.btn_no:
			SharedPfUtil.setValue(this, "auth", false);
			break;
		default:
			SharedPfUtil.setValue(this, "auth", false);
			break;
		}
		
		mAalertDialog.dismiss();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
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
