package com.wm.activity;

import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		ButterKnife.inject(this);
//		mToolbar.setTitle("关于我们");
//		setSupportActionBar(mToolbar);
//		mToolbar.setNavigationContentDescription("关于我们");
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
	}
	
	@OnClick(R.id.about_back)
	public void back() {
		finish();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in, R.anim.slide_out_to_right);
		}
	}

}
