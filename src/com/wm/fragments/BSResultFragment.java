package com.wm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wm.activity.R;
import com.wm.utils.UUIDS;

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
	public void handleConnect() {

	}

	@Override
	public void handleDisconnect() {

	}

	@Override
	public void handleGetData(String data) {

	}

	@Override
	public void handleServiceDiscover() {
		mBluetoothLeService.setCharacteristicNotification(
				getInfoCharacteristic(UUIDS.BS_RESULT_SERVICE,
						UUIDS.BS_RESULT_CHARAC), true);
	}

}
