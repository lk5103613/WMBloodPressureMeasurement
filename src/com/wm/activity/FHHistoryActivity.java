package com.wm.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageButton;
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

	@InjectView(R.id.embryo_history_chart)
	LineChart mChart;
	int[] mColors = ColorTemplate.VORDIPLOM_COLORS;
	List<List<Float>> fhResults;
	
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

		addDataSet(0);

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
		String[] xVals = new String[30];

		for (int i = 0; i < 30; i++)
			xVals[i] = "" + i;

		LineData data = new LineData(xVals);
		mChart.setData(data);
		mChart.invalidate();
	}

	private void addDataSet(int colorPosition) {

		LineData data = mChart.getData();

		if (data != null) {

			int count = (data.getDataSetCount() + 1);

			// create 10 y-vals
			ArrayList<Entry> yVals = new ArrayList<Entry>();

			for (int i = 0; i < data.getXValCount(); i++)
				yVals.add(new Entry(
						(float) (Math.random() * 50f) + 50f * count, i));

			LineDataSet set = new LineDataSet(yVals, getString(R.string.fh_value));
			set.setLineWidth(2.5f);
			set.setCircleSize(3f);
			int color = getResources().getColor(R.color.red);
			set.setColor(color);
			set.setCircleColor(color);
			set.setHighLightColor(color);

			data.addDataSet(set);
			mChart.notifyDataSetChanged();
			mChart.invalidate();
		}
	}

	private void initData(){
		fhResults = new ArrayList<>();
		for (int i = 0; i < 3; i ++) {
			ArrayList<Float> fhValues = new ArrayList<>();
//			for (int i = 0; i < 10)
		}
	}
	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub

	}
	
}
