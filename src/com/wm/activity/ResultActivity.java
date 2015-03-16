package com.wm.activity;

import java.util.List;
import java.util.Locale;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wm.blecore.BluetoothLeService;
import com.wm.blecore.BluetoothLeService.LocalBinder;
import com.wm.blecore.DeviceScanner.ScanCallback;
import com.wm.entity.DeviceInfo;
import com.wm.fragments.BaseResultFragment;
import com.wm.fragments.DeviceFragment;
import com.wm.fragments.TypeFactory;

public class ResultActivity extends BaseActivity implements ScanCallback {

	@InjectView(R.id.blood_check_bar)
	Toolbar mToolbar;
	@InjectView(R.id.btn_record)
	Button mBtnRecord;
	@InjectView(R.id.waiting_record)
	ProgressBar mProgressBar;

	private Context mContext;
	private BaseResultFragment mFragment;
	private String mType;
	private BluetoothLeService mBluetoothLeService;
	private DeviceInfo mDevice;
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBluetoothLeService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBluetoothLeService = ((LocalBinder) service).getService();
			if (!mBluetoothLeService.initialize()) {
				finish();
			}
		}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		ButterKnife.inject(this);

		mContext = ResultActivity.this;
		mType = getIntent().getStringExtra(DeviceInfo.INTENT_TYPE);
		mFragment = TypeFactory.getResultFragment(mType);

		mToolbar.setTitle(TypeFactory.getTitleByType(mContext, mType));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
		mDevice = getIntent()
				.getParcelableExtra(DeviceFragment.KEY_DEVICE_INFO);

		getSupportFragmentManager().beginTransaction()
				.add(R.id.result_container, mFragment).commit();
		// 绑定蓝牙服务
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(mServiceConnection != null) 
			unbindService(mServiceConnection);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			back();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void back(){
		Intent intent = new Intent(mContext, HistoryActivity.class);
		intent.putExtra("type", mType);
		intent.putExtra(DeviceFragment.KEY_DEVICE_INFO, mDevice);
		startActivity(intent);
		overridePendingTransition(R.anim.scale_fade_in,
				R.anim.slide_out_to_right);
		finish();
	}

	@OnClick(R.id.btn_record)
	public void record(View v) {
		mBtnRecord.setEnabled(false);
		mProgressBar.setVisibility(View.VISIBLE);
		mFragment.record();
		mBtnRecord.setEnabled(true);
		mProgressBar.setVisibility(View.GONE);
		back();
		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.slide_out_to_right);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in,
					R.anim.slide_out_to_right);
		}
		unregisterReceiver(mGattUpdateReceiver);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if (mBluetoothLeService != null) {
			if (isConnected())
				this.mBluetoothLeService.disconnect();
		}
		mBluetoothLeService = null;
	}

	private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				handleConFail();
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				String extraData = intent
						.getStringExtra(BluetoothLeService.EXTRA_DATA);
				mFragment.handleData(extraData, mBluetoothLeService);
			}
		}

	};
	

	/**
	 * 为广播接收者创建意图过滤器
	 * 
	 * @return
	 */
	private IntentFilter makeGattUpdateIntentFilter() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}

	@Override
	public void onScanStateChange(int scanState) {
		
	}

	@Override
	public void onScanSuccess(List<BluetoothDevice> devices) {
		boolean scanSuccess = false;
		for(BluetoothDevice device : devices) {
			if(device.getAddress().toUpperCase(Locale.getDefault()).equals(mDevice.address)) {
				scanSuccess = true;
				break;
			}
		}
		if(scanSuccess)
			mBluetoothLeService.connect(mDevice.address);
		else {
			handleConFail();
		}
	}

	@Override
	public void onScanFailed() {
		handleConFail();
	}
	
	// 如果当前状态为已连接，返回true，否则返回false
	private boolean isConnected() {
		if (this.mBluetoothLeService == null) {
			return false;
		}
		return this.mBluetoothLeService.getConnectState() == BluetoothLeService.STATE_CONNECTED;
	}
	
	private void handleConFail() {
		Toast.makeText(mContext, "链接已断开", Toast.LENGTH_LONG).show();
	}

}
