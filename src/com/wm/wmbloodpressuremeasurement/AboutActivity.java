package com.wm.wmbloodpressuremeasurement;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class AboutActivity extends ActionBarActivity {
	
	@InjectView(R.id.about_toolbar)
	Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		ButterKnife.inject(this);
		mToolbar.setTitle("关于我们");
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationContentDescription("关于我们");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in, R.anim.slide_out_to_right);
		}
	}

}
