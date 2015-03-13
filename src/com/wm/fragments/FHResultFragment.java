package com.wm.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
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
import com.wm.blecore.BluetoothLeService;
import com.wm.db.HistoryDBManager;
import com.wm.entity.FHResult;
import com.wm.utils.DataConvertUtils;

public class FHResultFragment extends BaseResultFragment {
	@InjectView(R.id.embryo_result_chart)
	LineChart mChart;
	
	private List<Float> mFHValues;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fh_result, container,
				false);
		ButterKnife.inject(this, view);
		initLineChart();
		addEmptyData();
		
		mFHValues = new ArrayList<Float>();
		
		return view;
	}

	@Override
	public void record() {
		FHResult fhResult = new FHResult(mFHValues, new Date().getTime());
		HistoryDBManager.getInstance(getActivity()).addFhResult(fhResult);
	}

	private void initLineChart() {
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

	private void addEntry() {

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

			data.addEntry(new Entry((float) (Math.random() * 50) + 50f, set
							.getEntryCount()), 0);

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

	@Override
	public void handleData(String data, BluetoothLeService bluetoothLeService) {
		String fhValue = DataConvertUtils.hexToDecimal(data.split(" ")[1]);
		System.out.println("Ì¥ÐÄÒÇ: " + fhValue);
		if(!fhValue.trim().equals("0")) 
			mFHValues.add(Float.valueOf(fhValue));
		
	}

}
