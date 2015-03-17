package com.wm.fragments;

import java.util.UUID;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.support.v4.app.Fragment;

import com.wm.blecore.BluetoothLeService;
import com.wm.blecore.IHandleConnect;


public abstract class BaseResultFragment extends Fragment implements IHandleConnect {
	
	protected BluetoothLeService mBluetoothLeService;
	
	public BaseResultFragment() {}
	
	public BaseResultFragment(BluetoothLeService bluetoothLeService) {
		this.mBluetoothLeService = bluetoothLeService;
	}
	
	public void setBluetoothLeService(BluetoothLeService bluetoothLeService) {
		this.mBluetoothLeService = bluetoothLeService;
	}
	
	protected BluetoothGattCharacteristic getInfoCharacteristic(String serviceUUID, String characUUID) {
		BluetoothGattService service = mBluetoothLeService
				.getServiceByUuid(serviceUUID);
		return service.getCharacteristic(UUID.fromString(characUUID));
	}

	public abstract void record();
	
}
