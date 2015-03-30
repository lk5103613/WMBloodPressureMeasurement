package com.wm.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.wm.activity.R;
import com.wm.db.HistoryDBManager;
import com.wm.entity.FHResult;
import com.wm.utils.DataConvertUtils;
import com.wm.utils.UUIDS;

public class FHResultFragment extends BaseResultFragment {

	@InjectView(R.id.embryo_result_chart)
	LineChart mChart;

	private List<Float> mFHValues;
	private ArrayList<String> xVals;
	private Handler mHandler;
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			saveAndJump();
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fh_result, container,
				false);
		ButterKnife.inject(this, view);
		mFHValues = new ArrayList<Float>();

		initLineChart();
		addEmptyData();
		
		mHandler = new Handler();

		return view;
	}

	@Override
	public void record() {
		mHandler.postDelayed(mRunnable, 35000);
	}

	private void initLineChart() {
		// chart
		mChart.setDrawYValues(false);
		mChart.setDrawGridBackground(false);
		mChart.setDescription("");
		mChart.setGridColor(getResources().getColor(R.color.light_black));
		mChart.setBorderColor(getResources().getColor(R.color.light_black));
		mChart.setStartAtZero(false);
		mChart.setScaleMinima(2, 1);// …Ë÷√Àı∑≈±»¿˝

	}

	private void addEmptyData() {

		// create 30 x-vals
		xVals = new ArrayList<String>();

		for (int i = 0; i < 30; i++) {
			xVals.add(i + "");
		}

		LineData data = new LineData(xVals);
		mChart.setData(data);
		mChart.invalidate();
	}

	private void addEntry(float value) {

		LineData data = mChart.getData();

		if (data != null) {

			LineDataSet set = data.getDataSetByIndex(0);

			if (set == null) {
				set = createSet();
				data.addDataSet(set);
			}
			set.setColor(getResources().getColor(R.color.red));
			set.setCircleColor(getResources().getColor(R.color.red));
			set.setLineWidth(2.5f);
			set.setCircleSize(3f);

			data.addEntry(new Entry(value, set.getEntryCount()), 0);// Math.random()
																	// * 50) +
																	// 50f
			if ((xVals.size() - mFHValues.size()) < 2) {
				xVals.add((xVals.size() + 1) + "");
			}

			mChart.notifyDataSetChanged();

			// redraw the chart
			mChart.invalidate();
		}
	}

	private LineDataSet createSet() {

		LineDataSet set = new LineDataSet(null, getString(R.string.fh_value));
		set.setLineWidth(1f);
		set.setCircleSize(2.5f);
		set.setColor(Color.rgb(240, 99, 99));
		set.setCircleColor(Color.rgb(240, 99, 99));
		set.setHighLightColor(Color.rgb(190, 190, 190));

		return set;
	}
	
	private void saveAndJump() {
		FHResult fhResult = new FHResult(mFHValues, new Date().getTime());
		HistoryDBManager.getInstance(getActivity()).addFhResult(fhResult);
		mBluetoothLeService.disconnect();
		getActivity().finish();
	}

	@Override
	public boolean handleConnect() {
		return false;
	}

	@Override
	public boolean handleDisconnect() {
		return false;
	}

	@Override
	public boolean handleGetData(String data) {
		String fhValue = DataConvertUtils.hexToDecimal(data.split(" ")[1]);
		if (!fhValue.trim().equals("0")) {
			mFHValues.add(Float.valueOf(fhValue));
			addEntry(Float.parseFloat(fhValue));
		}
		if(mFHValues.size() >= 60) {
			mHandler.removeCallbacks(mRunnable);
			saveAndJump();
		}
		return false;
	}

	@Override
	public boolean handleServiceDiscover() {
		if(mBluetoothLeService != null) {
			mBluetoothLeService.setCharacteristicNotification(
					getInfoCharacteristic(UUIDS.FH_RESULT_SERVICE,
							UUIDS.FH_RESULT_CHARAC), true);
		}
		return false;
	}

}
