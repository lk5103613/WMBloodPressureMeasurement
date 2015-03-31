package com.wm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class WelcomeActivity extends Activity {
	
	@InjectView(R.id.welcome_logo)
	ImageView mLogo;
	@InjectView(R.id.welcome_company_name)
	ImageView mCompanyName;
	
	private Context mContext;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		ButterKnife.inject(this);
		
		mContext = this;
		
		Animation logoAnimation = AnimationUtils.loadAnimation(mContext, R.anim.welcome_logo_anim);
		mLogo.startAnimation(logoAnimation);
		Animation companyNameAnimation = AnimationUtils.loadAnimation(mContext, R.anim.welcome_company_name_anim);
		mCompanyName.startAnimation(companyNameAnimation);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, 5000);
	}
	
}
