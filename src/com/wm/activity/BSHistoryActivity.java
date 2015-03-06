package com.wm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.wm.entity.DeviceInfo;

public class BSHistoryActivity extends BaseActivity{
	@InjectView(R.id.bs_history_bar)
	Toolbar mToolbar;
	
	@InjectView(R.id.bs_history_chart)
	LineChart mChart;
	@InjectView(R.id.btn_begin_check)
	Button mbtnCheck;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bs_history);
		ButterKnife.inject(this);
		
		mToolbar.setTitle(getResources().getString(R.string.bs_text));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
		mChart.setNoDataTextDescription(getString(R.string.nodata));
		
		initLineChart();
		addEmptyData();
	}
	
	@OnClick(R.id.btn_begin_check)
	public void beginCheck(){
		Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra(DeviceInfo.INTENT_TYPE, DeviceInfo.TYPE_BS);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.scale_fade_out);
	}
	
	private void initLineChart(){
		// chart
		mChart.setDrawYValues(false);
		mChart.setDrawGridBackground(false);
		mChart.setDescription("");
		mChart.setGridColor(getResources().getColor(R.color.light_black));
		mChart.setBorderColor(getResources().getColor(R.color.light_black));
		
	}
	private void addEmptyData() {

		// create 30 x-vals
		String[] xVals = new String[30];

		for (int i = 0; i < 30; i++)
			xVals[i] = "" + i;

		LineData data = new LineData(xVals);
		mChart.setData(data);
		mChart.invalidate();
	}

	
	
}
