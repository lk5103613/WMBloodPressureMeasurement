package com.wm.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
import com.wm.fragments.BaseResultFragment.ActivityCallback;
import com.wm.fragments.DeviceFragment;
import com.wm.fragments.TypeFactory;

public class ResultActivity extends BaseActivity implements IHandleConnect,
		ActivityCallback {

	@InjectView(R.id.btn_record)
	Button mBtnRecord;
	@InjectView(R.id.record_img)
	ImageView mRecordImg;
	@InjectView(R.id.result_suggest)
	TextView mResult;
	@InjectView(R.id.title)
	TextView mTitle;

	private BaseResultFragment mFragment;
	private String mType;
	private BluetoothLeService mBluetoothLeService;
	private DeviceInfo mDevice;
	private BroadcastReceiver mReceiver;
	private String mLastType = "";
	private Animation mRecordAnim;

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

		// ����������
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		
		mRecordAnim = AnimationUtils.loadAnimation(this, R.anim.record_img);
		System.out.println("result oncreate" );
	}

	@Override
	protected void onResume() {
		super.onResume();
		mType = getIntent().getStringExtra(DeviceInfo.INTENT_TYPE);
		if(!mType.equals(mLastType)) {
			mLastType = mType;
			mFragment = TypeFactory.getResultFragment(mType);
	
			mTitle.setText(TypeFactory.getTitleByType(mContext, mType));
			mDevice = getIntent()
					.getParcelableExtra(DeviceFragment.KEY_DEVICE_INFO);
	
			getSupportFragmentManager().beginTransaction()
					.add(R.id.result_container, mFragment).commit();
		}
		mReceiver = BleBroadcastReceiver.getInstance(mBluetoothLeService, this);
		registerReceiver(mReceiver, BleBroadcastReceiver.getIntentFilter());
	}

	@Override
	protected void onDestroy() {
		if (mServiceConnection != null)
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

	private void setRecordBtnState(int state) {
		switch (state) {
		case BaseResultFragment.BTN_STATE_AVAILABLE://δ��֮ǰ����
			mBtnRecord.setEnabled(true);
			mRecordImg.clearAnimation();
			break;
		case BaseResultFragment.BTN_STATE_UNAVAILABLE://��ý��֮ǰ��������
			mBtnRecord.setEnabled(false);
			mRecordImg.clearAnimation();
			break;
		case BaseResultFragment.BTN_STATE_UNAVAILABLE_WAITING://���֮�� չʾ����
			mBtnRecord.setEnabled(false);
			mRecordImg.startAnimation(mRecordAnim);//���֮�����ö���
			System.out.println("set animate");
			break;
		default:
			break;
		}
	}

	@OnClick(R.id.btn_record)
	public void record(View v) {
		mFragment.record();
	}

	@OnClick(R.id.result_back)
	public void back() {
		mBluetoothLeService.disconnect();
		finish();
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
		if (mFragment.handleConnect()) {
			return true;
		}
		System.out.println("connect success");
		return true;
	}

	@Override
	public boolean handleDisconnect() {
		if (mFragment.handleDisconnect()) {
			return true;
		}
		System.out.println("disconnect in result activity");
		mBluetoothLeService.connect(mDevice.address, 5000);
		return true;
	}

	@Override
	public boolean handleGetData(String data) {
		System.out.println(data);
		if (mFragment.handleGetData(data)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean handleServiceDiscover() {
		if (mFragment.handleServiceDiscover()) {
			return true;
		}
		return false;
	}

	@Override
	public void showResult(String data) {
		this.mResult.setText(data);
	}

	@Override
	public void setButtonState(int state) {
		setRecordBtnState(state);
	}

	@Override
	public void closeActivity() {
		finish();
	}

}
