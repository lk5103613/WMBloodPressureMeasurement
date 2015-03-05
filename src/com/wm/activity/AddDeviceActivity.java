package com.wm.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddDeviceActivity extends ActionBarActivity{
	
	@InjectView(R.id.type_spinner)
	Spinner mTypeSpinner;
	@InjectView(R.id.add_device_toolbar)
	Toolbar mToolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_device);
		ButterKnife.inject(this);
		
		mToolbar.setTitle(getResources().getString(R.string.add_new_device));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
		
		String[] type = new String[]{
			getResources().getString(R.string.bp_text),
			getResources().getString(R.string.bs_text),
			getResources().getString(R.string.fh_text)
		};
		
		 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.type_item, type); 
		 arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 mTypeSpinner.setAdapter(arrayAdapter);
	}
	
	@Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(0, R.anim.slide_out_to_right);
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		if(isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in, R.anim.slide_out_to_right);
		}
	}
	
}
