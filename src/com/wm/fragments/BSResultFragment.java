package com.wm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wm.activity.R;

public class BSResultFragment extends BaseResultFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_bs_result, container, false);
	}

	public void saveData() {

	}

	@Override
	public void record() {

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
//		mBluetoothLeService.setCharacteristicNotification(
//				getInfoCharacteristic(UUIDS.BS_RESULT_SERVICE,
//						UUIDS.BS_RESULT_CHARAC), true);
		return false;
	}

}
