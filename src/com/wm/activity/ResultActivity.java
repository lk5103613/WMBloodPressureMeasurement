package com.wm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wm.entity.DeviceInfo;
import com.wm.fragments.BaseResultFragment;
import com.wm.fragments.TypeFactory;

public class ResultActivity extends BaseActivity{

	@InjectView(R.id.blood_check_bar)
	Toolbar mToolbar;
	@InjectView(R.id.btn_record)
	Button mBtnRecord;
	@InjectView(R.id.waiting_record)
	ProgressBar mProgressBar;
	
	private BaseResultFragment mFragment;
	String type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		ButterKnife.inject(this);
		
		type = getIntent().getStringExtra(DeviceInfo.INTENT_TYPE);
		mFragment = TypeFactory.getResultFragment(type);

		mToolbar.setTitle(getTitle(type));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
		
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragment).commit();
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	Intent intent = null;
	    	switch (type) {
			case DeviceInfo.TYPE_BP://Ѫѹ��
				intent = new Intent(this, BPHistoryActivity.class);
				break;
			case DeviceInfo.TYPE_BS://Ѫ����
				intent = new Intent(this, BSHistoryActivity.class);
				break;
			case DeviceInfo.TYPE_FH://̥�ļ�
				intent = new Intent(this, FHHistoryActivity.class);
				break;
			}
	        intent.putExtra("type", type);
	        startActivity(intent);
	        overridePendingTransition(R.anim.scale_fade_in, R.anim.slide_out_to_right);
	    }
	    return super.onOptionsItemSelected(item);
	}

	private String getTitle(String type) {
		String title = "";
		switch (type) {
		case DeviceInfo.TYPE_BP:
			title = getResources().getString(R.string.bp_text);
			break;
		case DeviceInfo.TYPE_BS:
			title = getResources().getString(R.string.bs_text);
			break;
		case DeviceInfo.TYPE_FH:
			title = getResources().getString(R.string.fh_text);
			break;
		}
		return title;
	}
	
	@OnClick(R.id.btn_record)
	public void record(View v) {
		mBtnRecord.setEnabled(false);
		mProgressBar.setVisibility(View.VISIBLE);
		mFragment.record();
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
