package com.wm.fragments;

import android.support.v4.app.Fragment;

import com.wm.blecore.BluetoothLeService;

public abstract class BaseHistoryFragment extends Fragment {
	
	public abstract void setCharacteristicNotification(BluetoothLeService bluetoothLeService);

}
