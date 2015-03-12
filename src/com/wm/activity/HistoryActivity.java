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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wm.blecore.BluetoothLeService;
import com.wm.blecore.BluetoothLeService.LocalBinder;
import com.wm.blecore.DeviceScanner;
import com.wm.blecore.DeviceScanner.ScanCallback;
import com.wm.entity.DeviceInfo;
import com.wm.fragments.BaseHistoryFragment;
import com.wm.fragments.DeviceFragment;
import com.wm.fragments.TypeFactory;

public class HistoryActivity extends BaseActivity implements ScanCallback {

	private final static int MAX_CONNECT_TIME = 3;

	@InjectView(R.id.history_bar)
	Toolbar mToolbar;
	@InjectView(R.id.btn_begin_check)
	Button mBtnBeginCheck;
	@InjectView(R.id.waiting_connect)
	ProgressBar mWaitingConnect;
	
	private Context mContext;
	private int mCurrentConnectTime = 0;
	private BaseHistoryFragment mFragment;
	private String mType;
	private DeviceScanner mScanner;
	private DeviceInfo mDeviceInfo;
	private BluetoothLeService mBluetoothLeService;
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
		setContentView(R.layout.activity_history);
		ButterKnife.inject(this);
		
		mContext = HistoryActivity.this;
		mType = getIntent().getStringExtra(DeviceInfo.INTENT_TYPE);
		mFragment = TypeFactory.getHistoryFragment(mType);
		mDeviceInfo = getIntent().getParcelableExtra(DeviceFragment.KEY_DEVICE_INFO);
		mScanner = DeviceScanner.getInstance(mBluetoothAdapter, this);
		
		getSupportFragmentManager().beginTransaction().add(R.id.history_container, mFragment).commit();
		
		mToolbar.setTitle(TypeFactory.getTitleByType(mContext, mType));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
		// 绑定蓝牙服务
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		resetUI();
		mCurrentConnectTime = 0;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mGattUpdateReceiver);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("destory");
		if(mServiceConnection != null) 
			unbindService(mServiceConnection);
	}
	
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
	
	private void beginCheckUI() {
		mBtnBeginCheck.setEnabled(false);
		mBtnBeginCheck.setText("正在连接");
		mWaitingConnect.setVisibility(View.VISIBLE);
	}
	
	private void resetUI() {
		mBtnBeginCheck.setText("开始检测");
		mBtnBeginCheck.setEnabled(true);
		mWaitingConnect.setVisibility(View.GONE);
	}
	
	private void connectFailUI() {
		mBtnBeginCheck.setText("点击重试");
		mBtnBeginCheck.setEnabled(true);
		mWaitingConnect.setVisibility(View.GONE);
	}
	
	@OnClick(R.id.btn_begin_check)
	public void beginCheck(){
		beginCheckUI();
		mCurrentConnectTime = 0;
		connect();
	}
	
	private void jumpToResult() {
		Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra(DeviceInfo.INTENT_TYPE, mType);
		intent.putExtra(DeviceFragment.KEY_DEVICE_INFO, mDeviceInfo);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.scale_fade_out);
	}
	
	private boolean needResponse() {
		if(mWaitingConnect.getVisibility() == View.GONE) {
			return false;
		}
		return true;
	}
	
	private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(!needResponse()) {
				return;
			}
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				mCurrentConnectTime = 0;
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				handleConFail();
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				mFragment.setCharacteristicNotification(mBluetoothLeService);
				resetUI();
				jumpToResult();
			}
		}

	};
	
	private void handleConFail() {
		mCurrentConnectTime++;
		if(mCurrentConnectTime >= MAX_CONNECT_TIME) {
			if(mBluetoothLeService.getConnectState() != BluetoothLeService.STATE_DISCONNECTED) {
				mBluetoothLeService.disconnect();
			}
			connectFailUI();
			String rmdStr = getResources().getString(R.string.con_failed);
			Toast.makeText(mContext, rmdStr, Toast.LENGTH_LONG).show();
			return;
		}
		connect();
	}
	
	private void connect() {
		mScanner.scanLeDevice(true);
	}

	@Override
	public void onScanStateChange(int scanState) {
		
	}

	@Override
	public void onScanSuccess(List<BluetoothDevice> devices) {
		boolean isCorrectDevice = false;
		for(BluetoothDevice device : devices) {
			if(device.getAddress().toUpperCase(Locale.getDefault())
					.equals(mDeviceInfo.address)) {
				isCorrectDevice = true;
				break;
			}
		}
		if(isCorrectDevice)
			mBluetoothLeService.connect(mDeviceInfo.address);
		else
			handleConFail();
	}

	@Override
	public void onScanFailed() {
		handleConFail();
	}
	
}
