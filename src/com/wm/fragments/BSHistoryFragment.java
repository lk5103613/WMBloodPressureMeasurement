package com.wm.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.bluetooth.BluetoothGattCharacteristic;
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
import com.wm.entity.BSResult;
import com.wm.utils.DateUtil;
import com.wm.utils.UUIDS;

public class BSHistoryFragment extends BaseHistoryFragment {
	
	public final static String BS_DATA = "bs_data";

	@InjectView(R.id.bs_history_chart)
	LineChart mChart;
	private Handler mHandler;
	private BluetoothGattCharacteristic mCommandCharac;
	private HistoryDBManager historyDBManager;
	private List<BSResult> bsResults;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bs_history, container,
				false);
		ButterKnife.inject(this, view);
		
		mHandler = new Handler();

		initLineChart();
		
		historyDBManager = HistoryDBManager.getInstance(getActivity());
		
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		bsResults = historyDBManager.getAllBsResults();
		System.out.println("result " + bsResults.size());
		addEmptyData();
		addDataSet();
		
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
		ArrayList<String> xVals = new ArrayList<>();
		for (int i = 0, size = bsResults.size(); i< size; i++) {
			Date date = DateUtil.longToDate(bsResults.get(i).date);
			String datestr = DateUtil.getFormatDate("MM.dd", date);
			xVals.add(datestr);
		}

		for (int i = bsResults.size(); i < 8; i++) {
			Calendar nowss = Calendar.getInstance();
			String datestr = nowss.get(Calendar.MONTH) + 1 + "."
					+ (nowss.get(Calendar.DAY_OF_MONTH) + i);
			xVals.add(datestr);
		}
		

		LineData data = new LineData(xVals);
		mChart.setData(data);
		mChart.invalidate();
	}
	
	private void addDataSet() {

		LineData data = mChart.getData();

		if (data != null) {
			// create 10 y-vals
			ArrayList<Entry> yValsBs = new ArrayList<Entry>();
			

			//mChart.setScaleMinima(fhValues.size() / 15, 1);// …Ë÷√Àı∑≈±»¿˝

			for (int i = 0; i < bsResults.size(); i++) {
				yValsBs.add(new Entry(Float.parseFloat(bsResults.get(i).bg), i));
			}

			LineDataSet set = new LineDataSet(yValsBs,
					getString(R.string.bs_text));
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
		System.out.println(data);
		return false;
	}

	@Override
	public boolean handleServiceDiscover() {
		mBluetoothLeService.setCharacteristicNotification(
				getInfoCharacteristic(UUIDS.BS_RESULT_SERVICE,
						UUIDS.BS_RESULT_CHARAC), true);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mCommandCharac = getInfoCharacteristic(
						UUIDS.BS_RESULT_SERVICE, UUIDS.BS_CHARAC_COMMAND);
				mCommandCharac.setValue(BSResult.COMMAND_GET_REC_NO);
				mBluetoothLeService.writeCharacteristic(mCommandCharac);
			}
		}, 100);
		return false;
	}
	
}
