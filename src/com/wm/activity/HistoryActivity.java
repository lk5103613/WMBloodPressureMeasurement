package com.wm.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

import com.wm.blecore.BleBroadcastReceiver;
import com.wm.blecore.BluetoothLeService;
import com.wm.blecore.BluetoothLeService.LocalBinder;
import com.wm.blecore.IHandleConnect;
import com.wm.entity.DeviceInfo;
import com.wm.fragments.BPHistoryFragment.IShareData;
import com.wm.fragments.BaseHistoryFragment;
import com.wm.fragments.DeviceFragment;
import com.wm.fragments.TypeFactory;

/**
 * 
 * 检测历史界面
 * @author Like
 *
 */
public class HistoryActivity extends BaseActivity implements IHandleConnect, IShareData {
	
	@InjectView(R.id.history_bar)
	Toolbar mToolbar;
	@InjectView(R.id.btn_begin_check)
	Button mBtnBeginCheck;
	@InjectView(R.id.waiting_connect)
	ProgressBar mWaitingConnect;
	
	private Context mContext;
	private BaseHistoryFragment mFragment;
	private String mType;
	private DeviceInfo mDeviceInfo;
	private BluetoothLeService mBluetoothLeService;
	private BleBroadcastReceiver mReceiver;
	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBluetoothLeService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBluetoothLeService = ((LocalBinder) service).getService();
			mFragment.setBluetoothLeService(mBluetoothLeService);
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
		mDeviceInfo = getIntent().getParcelableExtra(DeviceFragment.KEY_DEVICE_INFO);
		mFragment = TypeFactory.getHistoryFragment(mType);
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
		mReceiver = BleBroadcastReceiver.getInstance(mBluetoothLeService, this);
		registerReceiver(mReceiver, BleBroadcastReceiver.getIntentFilter());
		resetUI();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("history destory");
		if(mServiceConnection != null)
			unbindService(mServiceConnection);
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
	
	private void handleConFail() {
		if(mBluetoothLeService.getConnectState() != BluetoothLeService.STATE_DISCONNECTED) {
			mBluetoothLeService.disconnect();
		}
		connectFailUI();
		String rmdStr = getResources().getString(R.string.con_failed);
		Toast.makeText(mContext, rmdStr, Toast.LENGTH_LONG).show();
		return;
	}
	
	private void connect() {
		mBluetoothLeService.connect(mDeviceInfo.address, 10000);
	}

	@Override
	public boolean handleConnect() {
		if(mFragment.handleConnect()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean handleDisconnect() {
		if(mFragment.handleDisconnect()) {
			return true;
		}
		handleConFail();
		return true;
	}

	@Override
	public boolean handleGetData(String data) {
		if(mFragment.handleGetData(data)) {
			return true;
		}
		resetUI();
		jumpToResult();
		return true;
	}

	@Override
	public boolean handleServiceDiscover() {
		if(mFragment.handleServiceDiscover()) {
			return true;
		}
		return false;
	}

	@Override
	public DeviceInfo getDevice() {
		return mDeviceInfo;
	}
	
	@Override
	public void conFail() {
		handleConFail();
	}
	
}
