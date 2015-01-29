package com.wm.wmbloodpressuremeasurement;

import butterknife.InjectView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class BloodHistoryActivity extends ActionBarActivity{

	@InjectView(R.id.blood_history_bar)
	Toolbar mToolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_to_right);
    }
}
