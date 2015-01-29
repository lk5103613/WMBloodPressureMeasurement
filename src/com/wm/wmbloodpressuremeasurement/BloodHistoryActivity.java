package com.wm.wmbloodpressuremeasurement;

import butterknife.ButterKnife;
import butterknife.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class BloodHistoryActivity extends ActionBarActivity {

	@InjectView(R.id.blood_history_bar)
	Toolbar mToolbar;
	
	@InjectView(R.id.btn_begin_check)
	Button btnBegin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blood_history);
		ButterKnife.inject(this);

		mToolbar.setTitle(getResources().getString(R.string.turgoscope));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		btnBegin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BloodHistoryActivity.this, BloodCheckActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.slide_out_to_right);
	}

}
