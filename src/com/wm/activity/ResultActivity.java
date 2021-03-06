package com.wm.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wm.blecore.BleBroadcastReceiver;
import com.wm.blecore.BluetoothLeService;
import com.wm.blecore.BluetoothLeService.LocalBinder;
import com.wm.blecore.IHandleConnect;
import com.wm.entity.DeviceInfo;
import com.wm.fragments.BaseResultFragment;
import com.wm.fragments.BaseResultFragment.Interaction;
import com.wm.fragments.DeviceFragment;
import com.wm.fragments.TypeFactory;

public class ResultActivity extends BaseActivity implements IHandleConnect, Interaction {

	@InjectView(R.id.blood_check_bar)
	Toolbar mToolbar;
	@InjectView(R.id.btn_record)
	Button mBtnRecord;
	@InjectView(R.id.waiting_record)
	ProgressBar mProgressBar;
	@InjectView(R.id.result_suggest)
	TextView mResult;

	private BaseResultFragment mFragment;
	private String mType;
	private BluetoothLeService mBluetoothLeService;
	private DeviceInfo mDevice;
	private BroadcastReceiver mReceiver;
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBluetoothLeService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBluetoothLeService = ((LocalBinder) service).getService();
			mFragment.onBindService(mBluetoothLeService);
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
		// ����������
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mReceiver = BleBroadcastReceiver.getInstance(mBluetoothLeService, this);
		registerReceiver(mReceiver, BleBroadcastReceiver.getIntentFilter());
	}
	
	@Override
	protected void onDestroy() {
		if(mServiceConnection != null) 
			unbindService(mServiceConnection);
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return true;
	}
	
	@OnClick(R.id.btn_record)
	public void record(View v) {
		mBtnRecord.setEnabled(false);
		mProgressBar.setVisibility(View.VISIBLE);
		mFragment.record();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		mBluetoothLeService.close();
		overridePendingTransition(0, R.anim.slide_out_to_right);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in,
					R.anim.slide_out_to_right);
		}
		unregisterReceiver(mReceiver);
	}
	
	@Override
	public boolean handleConnect() {
		if(mFragment.handleConnect()) {
			return true;
		}
		System.out.println("connect success");
		return true;
	}

	@Override
	public boolean handleDisconnect() {
		if(mFragment.handleDisconnect()) {
			return true;
		}
		System.out.println("disconnect in result activity");
		mBluetoothLeService.connect(mDevice.address, 5000);
		return true;
	}

	@Override
	public boolean handleGetData(String data) {
		System.out.println(data);
		if(mFragment.handleGetData(data)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean handleServiceDiscover() {
		if(mFragment.handleServiceDiscover()) {
			return true;
		}
		return false;
	}

	@Override
	public void showResult(String data) {
		this.mResult.setText(data);
	}

}
