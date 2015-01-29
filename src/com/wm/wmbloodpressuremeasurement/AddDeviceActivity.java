package com.wm.wmbloodpressuremeasurement;

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
		setContentView(R.layout.add_device);
		ButterKnife.inject(this);
		
		mToolbar.setTitle(getResources().getString(R.string.add_new_device));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		String[] type = new String[]{
			getResources().getString(R.string.turgoscope)
		};
		
		 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.type_item, type); 
		 mTypeSpinner.setAdapter(arrayAdapter);
	}
	
	@Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_to_right);
    }
	
}
