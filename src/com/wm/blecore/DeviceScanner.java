package com.wm.blecore;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

/**
 * ����ɨ��Ĺ�����
 * @author Like
 *
 */
public class DeviceScanner {
	
	public final static int STATE_BEGIN_SCAN = 0;
	public final static int STATE_END_SCAN = 1;

	// ɨ��ʱ�䣬�������ʱ��û��ɨ�赽�豸���϶�ɨ��ʧ��
	private final static int SCAN_PERIOD = 5000;
	private static DeviceScanner mDeviceScanner = null;
	// ���ɨ�赽���豸
	private List<BluetoothDevice> mDevices = null;
	private Handler mHandler = null;
	private BluetoothAdapter mBluetoothAdapter = null;
	private boolean mScanning = false;
	private ScanCallback mCallback = null;

	private DeviceScanner(BluetoothAdapter bluetoothAdapter) {
		this.mHandler = new Handler();
		this.mBluetoothAdapter = bluetoothAdapter;
		this.mDevices = new ArrayList<BluetoothDevice>();
	}

	public static DeviceScanner getInstance(BluetoothAdapter bluetoothAdapter,
			ScanCallback callback) {
		if (mDeviceScanner == null)
			mDeviceScanner = new DeviceScanner(bluetoothAdapter);
		mDeviceScanner.mCallback = callback;
		return mDeviceScanner;
	}

	public interface ScanCallback {
		
		void onScanStateChange(int scanState);

		void onScanSuccess(List<BluetoothDevice> mDevices);

		void onScanFailed();

	}

	/**
	 * ����ɨ��Ŀ�ʼ�����
	 * 
	 * @param enable
	 *            ���Ϊtrue��ʼɨ�裬false����ɨ��
	 */
	@SuppressWarnings("deprecation")
	public void scanLeDevice(final boolean enable) {
		if (enable) {
			if(mScanning)
				return;
			mDevices.clear();
			mCallback.onScanStateChange(STATE_BEGIN_SCAN);
			// ��ָ��ʱ��֮��ֹͣɨ��
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if(mScanning)
						scanLeDevice(false);
				}
			}, SCAN_PERIOD);
			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			if(!mScanning)
				return;
			mCallback.onScanStateChange(STATE_END_SCAN);
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			if(mDevices == null || mDevices.size() == 0)
				mCallback.onScanFailed();
			else
				mCallback.onScanSuccess(mDevices);
		}
	}

	private LeScanCallback mLeScanCallback = new LeScanCallback() {
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			if(!mDevices.contains(device))
				mDevices.add(device);
		}
	};

	/**
	 * �����ж��Ƿ�������ɨ��״̬
	 * @return �������ɨ�践��true�����ڷ���false
	 */
	public boolean isScanning() {
		return mScanning;
	}

}
