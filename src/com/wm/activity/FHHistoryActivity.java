package com.wm.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.wm.entity.DeviceInfo;
import com.wm.entity.FHResult;

public class FHHistoryActivity extends BaseActivity implements
		OnChartValueSelectedListener {

	@InjectView(R.id.embryo_history_bar)
	Toolbar mToolbar;
	@InjectView(R.id.btn_begin_check)
	Button btnBegin;
	@InjectView(R.id.btn_next)
	ImageButton btnNext;
	@InjectView(R.id.btn_previous)
	ImageButton btnPrevious;
	@InjectView(R.id.text_date)
	TextView dateText;
	@InjectView(R.id.embryo_history_chart)
	LineChart mChart;
	int[] mColors = ColorTemplate.VORDIPLOM_COLORS;
	List<FHResult> fhResults;
	int index = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fh_history);
		ButterKnife.inject(this);

		mToolbar.setTitle(getResources().getString(R.string.fh_text));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);

		initLineChart();
		addEmptyData();
		mChart.invalidate();
		
		initData();
		addDataSet(index);
		dateText.setText(fhResults.get(index).getDate());

	}
	
	@OnClick(R.id.btn_begin_check)
	public void beginCheck(){
		Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra(DeviceInfo.INTENT_TYPE, DeviceInfo.TYPE_FH);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.scale_fade_out);
	}

	private void initLineChart(){
		// chart
		mChart.setOnChartValueSelectedListener(this);
		mChart.setDrawYValues(false);
		mChart.setDrawGridBackground(false);
		mChart.setDescription("");
		mChart.setGridColor(getResources().getColor(R.color.light_black));
		mChart.setBorderColor(getResources().getColor(R.color.light_black));
		
	}
	private void addEmptyData() {

		// create 30 x-vals
		ArrayList<String> xVals = new ArrayList<>();

		for (int i = 1; i <= 10; i++)
			xVals.add(i+"");

		LineData data = new LineData(xVals);
		mChart.setData(data);
		mChart.invalidate();
	}

	private void addDataSet(int position) {

		LineData data = mChart.getData();

		if (data != null) {
			// create 10 y-vals
			ArrayList<Entry> yValsFh = new ArrayList<Entry>();
			List<Float> fhValues = fhResults.get(position).getFhValues();
			for (int i = 0 ; i < fhValues.size(); i++) {
				yValsFh.add(new Entry(fhValues.get(i),i));
			}

			LineDataSet set = new LineDataSet(yValsFh, getString(R.string.fh_value));
			set.setLineWidth(2.5f);
			set.setCircleSize(3f);
			int color = getResources().getColor(R.color.red);
			set.setColor(color);
			set.setCircleColor(color);
			set.setHighLightColor(color);

			data.addDataSet(set);
			mChart.notifyDataSetChanged();
			mChart.animateY(1500);
		}
	}

	private void initData(){
		fhResults = new ArrayList<>();
		for (int i = 0; i < 3; i ++) {
			ArrayList<Float> fhValues = new ArrayList<>();
			for (int j = 0; j < 10; j++){
				float fh = (float) (Math.random() * 50f + 50f * 2);
				
				fhValues.add(fh);
				System.out.println("value--" + fh);
			}
			Calendar calendar =Calendar.getInstance();
			String date = calendar.get(Calendar.YEAR)+
					"."+(calendar.get(Calendar.MONTH))+"."+(calendar.get(Calendar.DATE)+i);
			
			FHResult fhResult = new FHResult(fhValues, date);
			
			fhResults.add(fhResult);
		}
	}
	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		float fh = fhResults.get(0).getFhValues().get(e.getXIndex());
		Toast.makeText(this, "胎心值： " +(int)fh , Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub

	}
	@OnClick(R.id.btn_next)
	public void nextClick(){
		mChart.clear();
		if (index>= fhResults.size()) {
			Toast.makeText(this, "已经是最后一条数据", Toast.LENGTH_LONG).show();
			return;
		}
		addDataSet(++index);
	}
	
	@OnClick(R.id.btn_previous)
	public void previousClick(){
		mChart.clear();
		if (index<=0) {
			Toast.makeText(this, "已经是第一条数据", Toast.LENGTH_LONG).show();
			return;
		}
		addDataSet(--index);
	}
	
	
	
}
