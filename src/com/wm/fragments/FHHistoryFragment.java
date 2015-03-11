package com.wm.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.wm.activity.R;
import com.wm.entity.FHResult;

public class FHHistoryFragment extends Fragment implements OnChartValueSelectedListener {
	
	@InjectView(R.id.fh_history_chart)
	LineChart mChart;
	@InjectView(R.id.text_date)
	TextView mTxtDate;
	
	private Context mContext;
	private List<FHResult> mFHResults;
	private int mIndex = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fh_history, container, false);
		ButterKnife.inject(this, view);
		
		mContext = getActivity();
		
		initLineChart();
		addEmptyData();
		mChart.invalidate();
		initData();
		addDataSet(mIndex);

		
		return view;
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
		calendar.setTimeInMillis(mFHResults.get(position).date);
		mTxtDate.setText(calendar.get(Calendar.YEAR)+"."+
		(calendar.get(Calendar.MONTH)+1)+"."+(calendar.get(Calendar.DATE)+position));

		LineData data = mChart.getData();

		if (data != null) {
			// create 10 y-vals
			ArrayList<Entry> yValsFh = new ArrayList<Entry>();
			List<Float> fhValues = mFHResults.get(position).fhValues;
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
			mChart.animateY(1000);
		}
	}

	private void initData(){
		mFHResults = new ArrayList<>();
		for (int i = 0; i < 3; i ++) {
			ArrayList<Float> fhValues = new ArrayList<>();
			for (int j = 0; j < 10; j++){
				float fh = (float) (Math.random() * 50f + 50f * 2);
				
				fhValues.add(fh);
			}
			FHResult fhResult = new FHResult(fhValues, new Date().getTime());
			
			mFHResults.add(fhResult);
		}
	}
	
	@Override
	public void onValueSelected(Entry e, int dataSetmIndex) {
		float fh = mFHResults.get(0).fhValues.get(e.getXIndex());
		Toast.makeText(mContext, "Ì¥ÐÄÖµ£º " +(int)fh , Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected() {

	}
	@OnClick(R.id.btn_next)
	public void nextClick(){
		
		if (mIndex>= (mFHResults.size()-1)) {
			Toast.makeText(mContext, getString(R.string.msg_last_data), Toast.LENGTH_LONG).show();
			return;
		}
		mChart.getData().removeDataSet(0);
		addDataSet(++mIndex);
	}
	
	@OnClick(R.id.btn_previous)
	public void previousClick(){
		
		if (mIndex<=0) {
			Toast.makeText(mContext, getString(R.string.msg_fist_data), Toast.LENGTH_LONG).show();
			return;
		}
		mChart.getData().removeDataSet(0);
		addDataSet(--mIndex);
	}


}
