package com.wm.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.wm.entity.BPResult;
import com.wm.entity.DeviceInfo;

public class BPHistoryActivity extends BaseActivity implements
		OnChartValueSelectedListener {

	@InjectView(R.id.blood_history_bar)
	Toolbar mToolbar;
	@InjectView(R.id.blood_history_chart)
	LineChart mChart;
	List<BPResult> bpResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bp_history);
		ButterKnife.inject(this);

		mToolbar.setTitle(getResources().getString(R.string.bp_text));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);

		// chart
		initLineChart();
		addEmptyData();
		mChart.invalidate();

		initData();
		addDataSet();
	}

	@OnClick(R.id.btn_begin_check)
	public void beginCheck(View v) {
		Intent intent = new Intent(BPHistoryActivity.this, ResultActivity.class);
		intent.putExtra(DeviceInfo.INTENT_TYPE, DeviceInfo.TYPE_BP);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.scale_fade_out);
	}

	public void initLineChart(){
		mChart.setOnChartValueSelectedListener(this);
		mChart.setDrawYValues(false);
		mChart.setDrawGridBackground(false);
		mChart.setDescription("");
		mChart.setGridColor(getResources().getColor(R.color.light_black));
		mChart.setBorderColor(getResources().getColor(R.color.light_black));
	}
	
	/**
	 * 添加X轴
	 */
	private void addEmptyData() {
		ArrayList<String> xVals = new ArrayList<String>();
		
		// 创建 x值
		for (int i = 1; i < 8; i++) {
			Calendar nowss = Calendar.getInstance();
			String datestr = nowss.get(Calendar.MONTH) + 1 + "."
					+ (nowss.get(Calendar.DAY_OF_MONTH)+i);
			xVals.add(datestr);
		}
		
		LineData data = new LineData(xVals);
		mChart.setData(data);
		mChart.invalidate();
	}

	private void addDataSet() {

		LineData data = mChart.getData();

		if (data != null) {
			//舒张
			ArrayList<Entry> yValsSz = new ArrayList<Entry>();
			ArrayList<Entry> yValsSs = new ArrayList<Entry>();
			for (int i = 0; i < bpResults.size(); i++) {
				yValsSz.add(new Entry(bpResults.get(i).getSzValue(),i));
				yValsSs.add(new Entry(bpResults.get(i).getSsValue(),i));
			}

			
			LineDataSet szSet = new LineDataSet(yValsSz, getString(R.string.diastolic));
			szSet.setLineWidth(2.5f);
			szSet.setCircleSize(3f);

			szSet.setColor(getResources().getColor(R.color.dark_green));
			szSet.setCircleColor(getResources().getColor(R.color.dark_green));
			szSet.setHighLightColor(getResources().getColor(R.color.dark_green));

			data.addDataSet(szSet);
			
			LineDataSet ssSet = new LineDataSet(yValsSs, getString(R.string.systolic));
			ssSet.setLineWidth(2.5f);
			ssSet.setCircleSize(3f);

			ssSet.setColor(getResources().getColor(R.color.sky_blue));
			ssSet.setCircleColor(getResources().getColor(R.color.sky_blue));
			ssSet.setHighLightColor(getResources().getColor(R.color.sky_blue));

			data.addDataSet(ssSet);
			
			mChart.notifyDataSetChanged();
			mChart.animateY(1500);//设置Y轴动画 毫秒;
		}
	}
	
	public void initData(){
		bpResults = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			float szValue = (float)Math.random() * 50f + 50f * 2;
			float ssValue = (float)Math.random() * 50f + 50f * 2;
			bpResults.add(new BPResult(szValue, ssValue));
		}
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		float sz = bpResults.get(e.getXIndex()).getSzValue();
		float ss = bpResults.get(e.getXIndex()).getSsValue();
		Toast.makeText(this, "舒张压： " + (int)sz + "  收缩压： " + (int)ss, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected() {

	}

}
