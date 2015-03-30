package com.wm.fragments;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.wm.activity.R;
import com.wm.db.HistoryDBManager;
import com.wm.entity.BSResult;
import com.wm.utils.UUIDS;

public class BSHistoryFragment extends BaseHistoryFragment {
	
	public final static String BS_DATA = "bs_data";

	@InjectView(R.id.bs_history_chart)
	LineChart mChart;
	private Handler mHandler;
	private BluetoothGattCharacteristic mCommandCharac;
	private HistoryDBManager historyDBManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bs_history, container,
				false);
		ButterKnife.inject(this, view);
		
		mHandler = new Handler();

		initLineChart();
		addEmptyData();
		historyDBManager = HistoryDBManager.getInstance(getActivity());
		
		return view;
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
