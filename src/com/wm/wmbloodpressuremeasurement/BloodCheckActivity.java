package com.wm.wmbloodpressuremeasurement;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class BloodCheckActivity extends ActionBarActivity{

	@InjectView(R.id.blood_check_bar)
	Toolbar mToolbar;
	
	@InjectView(R.id.btn_record)
	Button btnRecord;
	
	private ProgressDialog mDialog;
	
//	@InjectView(R.id.record_progress)
//	ProgressBar recordProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blood_check);
		ButterKnife.inject(this);

		mToolbar.setTitle(getResources().getString(R.string.turgoscope));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
		
		mDialog = ProgressDialog.show(this, null, "Sending...", true);
		mDialog.dismiss();
		
		btnRecord.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				recordProgress.setVisibility(View.VISIBLE);
				mDialog.show();
			}
		});
	}
	
	
}
