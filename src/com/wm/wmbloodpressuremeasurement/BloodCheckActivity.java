package com.wm.wmbloodpressuremeasurement;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class BloodCheckActivity extends ActionBarActivity{

	@InjectView(R.id.blood_check_bar)
	Toolbar mToolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blood_check);
		ButterKnife.inject(this);

		mToolbar.setTitle(getResources().getString(R.string.turgoscope));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	
}
