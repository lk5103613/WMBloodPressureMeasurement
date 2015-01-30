package com.wm.wmbloodpressuremeasurement;

import butterknife.ButterKnife;
import butterknife.InjectView;
import android.content.Context;
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
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blood_history);
		ButterKnife.inject(this);
		
		this.context = context;

		mToolbar.setTitle(getResources().getString(R.string.turgoscope));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
		
		btnBegin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BloodHistoryActivity.this, BloodCheckActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_fade_out);
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.slide_out_to_right);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in, R.anim.slide_out_to_right);
		}
	}

}
