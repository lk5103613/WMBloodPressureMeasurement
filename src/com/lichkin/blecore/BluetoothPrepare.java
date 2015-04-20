package com.lichkin.blecore;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;

public class BluetoothPrepare {

	private Context mContext;
	private BluetoothAdapter mBluetoothAdapter;
	private static BluetoothPrepare mBlePrepare;

	private BluetoothPrepare(Context context, BluetoothAdapter bluetoothAdapter) {
		this.mContext = context;
		this.mBluetoothAdapter = bluetoothAdapter;
	}

	public static BluetoothPrepare getInstance(Context context,
			BluetoothAdapter bluetoothAdapter) {
		if (mBlePrepare == null) {
			mBlePrepare = new BluetoothPrepare(context, bluetoothAdapter);
		}
		return mBlePrepare;
	}

	/**
	 * ����豸�Ƿ�֧��BLE
	 * 
	 * @return ���֧�ַ���true, ����Ż�false
	 */
	public boolean checkSupport() {
		if (!mContext.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			return false;
		}
		return true;
	}

	/**
	 * �ж������Ƿ���
	 * 
	 * @return ��������true�����򷵻�false
	 */
	public boolean isBluetoothOpen() {
		return !(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled());
	}

}
