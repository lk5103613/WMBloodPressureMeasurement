package com.wm.activity;

import com.wm.blecore.BluetoothPrepare;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

/**
 * 
 * @author Like
 *
 */
public class BLEBaseActivity extends ActionBarActivity {

	// request code to open bluetooth
	public static int REQUEST_ENABLE_BT = 1;

	protected BluetoothAdapter mBluetoothAdapter;
	protected BluetoothPrepare mBluetoothPrepare;
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = BLEBaseActivity.this;
		BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		mBluetoothPrepare = BluetoothPrepare.getInstance(mContext,
				mBluetoothAdapter);
		// ����ֻ���֧��BLE���ر�Ӧ��
		if (!mBluetoothPrepare.checkSupport()) {
			String remindStr = getResources().getString(
					R.string.remind_device_not_support);
			Toast.makeText(mContext, remindStr, Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// ÿ�ο���Ӧ��ʱ��������Ƿ���
		requestBluetooth();
		// ע������ߣ����������Ƿ񱣳ֿ���
		registerReceiver(mBleStateReceiver, makeBleStateIntentFilter());
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mBleStateReceiver);
	}

	/**
	 * ���������
	 */
	private void requestBluetooth() {
		if (!mBluetoothPrepare.isBluetoothOpen()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_ENABLE_BT) {
			// ����û���ͬ����������ر�Ӧ��
			if (resultCode == RESULT_CANCELED) {
				String remindStr = getResources().getString(
						R.string.remind_ble_must_open);
				Toast.makeText(mContext, remindStr, Toast.LENGTH_SHORT).show();
				finish();
				System.exit(0);
			}
		}
	}

	// �����ֻ�����״̬�������;�ر��������ٴ�����
	private final BroadcastReceiver mBleStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			requestBluetooth();
		}
	};

	private static IntentFilter makeBleStateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
		return intentFilter;
	}

}
