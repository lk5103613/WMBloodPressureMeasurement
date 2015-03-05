package com.wm.wmbloodpressuremeasurement;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wm.fragments.BPResultFragment;
import com.wm.fragments.BSResultFragment;

public class ResultActivity extends ActionBarActivity{

	@InjectView(R.id.blood_check_bar)
	Toolbar mToolbar;
	
	private ProgressDialog mDialog;
	private Fragment mBPResultFragment;
	private Fragment mBSResultFragment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		ButterKnife.inject(this);
		
		mBPResultFragment = new BPResultFragment();
		mBSResultFragment = new BSResultFragment();

		mToolbar.setTitle(getResources().getString(R.string.turgoscope));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
		
		mDialog = ProgressDialog.show(this, null, "«Î…‘∫Û...", true);
		mDialog.dismiss();
		
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mBSResultFragment).commit();
		
	}
	
	@OnClick(R.id.btn_record)
	public void record(View v) {
		mDialog.show();
		//test
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mDialog.dismiss();
					}
				});
			}
		}).start();
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
