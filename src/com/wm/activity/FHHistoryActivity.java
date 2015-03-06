package com.wm.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
//		XLabels xLabels = mChart.getXLabels();
//		xLabels.setPosition(XLabelPosition.BOTTOM);
		mChart.invalidate();
		initData();
		addDataSet(index);
		

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
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(fhResults.get(position).date);
		dateText.setText(calendar.get(Calendar.YEAR)+"."+
		(calendar.get(Calendar.MONTH)+1)+"."+(calendar.get(Calendar.DATE)+position));

		LineData data = mChart.getData();

		if (data != null) {
			// create 10 y-vals
			ArrayList<Entry> yValsFh = new ArrayList<Entry>();
			List<Float> fhValues = fhResults.get(position).fhValues;
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
			}
			FHResult fhResult = new FHResult(fhValues, new Date().getTime());
			
			fhResults.add(fhResult);
		}
	}
	
	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		float fh = fhResults.get(0).fhValues.get(e.getXIndex());
		Toast.makeText(this, "Ì¥ÐÄÖµ£º " +(int)fh , Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub

	}
	@OnClick(R.id.btn_next)
	public void nextClick(){
		
		if (index>= (fhResults.size()-1)) {
			Toast.makeText(this, getString(R.string.msg_last_data), Toast.LENGTH_LONG).show();
			return;
		}
		mChart.getData().removeDataSet(0);
		addDataSet(++index);
	}
	
	@OnClick(R.id.btn_previous)
	public void previousClick(){
		
		if (index<=0) {
			Toast.makeText(this, getString(R.string.msg_fist_data), Toast.LENGTH_LONG).show();
			return;
		}
		mChart.getData().removeDataSet(0);
		addDataSet(--index);
	}
}
