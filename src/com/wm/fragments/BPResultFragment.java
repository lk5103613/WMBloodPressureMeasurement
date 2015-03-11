package com.wm.fragments;

import java.util.UUID;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wm.activity.R;
import com.wm.blecore.BluetoothLeService;
import com.wm.utils.UUIDS;

public class BPResultFragment extends BaseResultFragment  {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_bp_result, container, false);
	}

	@Override
	public void record() {
		
	}

	@Override
	public void setCharacteristicNotification(
			BluetoothLeService bluetoothLeService) {
		BluetoothGattService service = bluetoothLeService
				.getServiceByUuid(UUIDS.BP_RESULT_SERVICE);
		if (service == null) {
			return;
		}
		BluetoothGattCharacteristic inforCharacteristic = service.getCharacteristic(UUID
				.fromString(UUIDS.BP_RESULT_CHARAC));
		if(inforCharacteristic != null) {
			bluetoothLeService.setCharacteristicNotification(inforCharacteristic, true);
		}
	}

	@Override
	public void handleData(String data) {
		System.out.println(data);
	}

}
