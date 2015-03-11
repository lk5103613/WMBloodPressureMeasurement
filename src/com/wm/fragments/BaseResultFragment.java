package com.wm.fragments;

import android.support.v4.app.Fragment;

import com.wm.blecore.BluetoothLeService;

public abstract class BaseResultFragment extends Fragment {
	
	public abstract void record();
	
	public abstract void setCharacteristicNotification(BluetoothLeService bluetoothLeService);
	
	public abstract void handleData(String data);

}
