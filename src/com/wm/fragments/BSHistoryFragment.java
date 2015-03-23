package com.wm.fragments;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.wm.activity.R;
import com.wm.utils.UUIDS;

public class BSHistoryFragment extends BaseHistoryFragment {
	
	@InjectView(R.id.bs_history_chart)
	LineChart mChart;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bs_history, container,
				false);
		ButterKnife.inject(this, view);

		initLineChart();
		addEmptyData();

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
		return false;
	}

	@Override
	public boolean handleServiceDiscover() {
		BluetoothGattCharacteristic characteristic = getInfoCharacteristic(UUIDS.BS_RESULT_SERVICE,
						UUIDS.BS_RESULT_CHARAC2);
		byte[] bs = new byte[5];
		bs[0] = (byte) 0x7b;
		bs[1] = (byte) 0x49;
		bs[2] = (byte) 0x23;
		bs[3] = (byte) 0xea;
		bs[4] = (byte) 0x7d;
		characteristic.setValue(bs);
		mBluetoothLeService.writeCharacteristic(characteristic);
		BluetoothGattCharacteristic characteristic2 = getInfoCharacteristic(UUIDS.BS_RESULT_SERVICE,
				UUIDS.BS_RESULT_CHARAC);
		mBluetoothLeService.setCharacteristicNotification(characteristic2, true);
		return false;
	}

}
