package com.wm.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wm.adapter.SpinnerAdapter;
import com.wm.blecore.DeviceScanner;
import com.wm.blecore.DeviceScanner.ScanCallback;
import com.wm.db.DeviceDBManager;
import com.wm.entity.DeviceInfo;
import com.wm.utils.DialogUtils;

public class AddDeviceActivity extends BaseActivity implements ScanCallback {

	@InjectView(R.id.type_spinner)
	Spinner mTypeSpinner;

	private DeviceScanner mScanner;
	private DeviceDBManager mDeviceDBManager;
	private String mBPText;
	private String mBSText;
	private String mFHText;
	private ProgressDialog mProgressDialog;
	private List<String> mDBAddresses = new ArrayList<String>();
	private int mCurrentScanState = DeviceScanner.STATE_END_SCAN;
	private int mCurrentScanTime = 0; // scan number after failed

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_device);
		ButterKnife.inject(this);

		mScanner = DeviceScanner.getInstance(mBluetoothAdapter, this);
		mDeviceDBManager = DeviceDBManager.getInstance(mContext);
		mBPText = getResources().getString(R.string.bp_text);
		mBSText = getResources().getString(R.string.bs_text);
		mFHText = getResources().getString(R.string.fh_text);
		mProgressDialog = DialogUtils.createProgressDialog(mContext, "",
				getResources().getString(R.string.scaning));
		
		ArrayList<String> type= new ArrayList<>();
		type.add(mBPText);
		type.add(mBSText);
		type.add(mFHText);
		
		ArrayAdapter<String> adapter = new SpinnerAdapter(mTypeSpinner, this, 
				R.layout.simple_spinner_item,type);
		mTypeSpinner.setAdapter(adapter);
				
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in,
					R.anim.slide_out_to_right);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		hideProgress();
		mProgressDialog = null;
	}

	@OnClick(R.id.add_back)
	public void back(){
		finish();
	}
	private String getDeviceType() {
		String type = null;
		String selectedType = mTypeSpinner.getSelectedItem().toString();
		if (selectedType.equals(mBPText)) {
			type = DeviceInfo.TYPE_BP;
		} else if (selectedType.equals(mBSText)) {
			type = DeviceInfo.TYPE_BS;
		} else if (selectedType.equals(mFHText)) {
			type = DeviceInfo.TYPE_FH;
		}
		return type;
	}

	private void hideProgress() {
		if (mProgressDialog == null) {
			return;
		}
		if (mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	@OnClick(R.id.btn_match)
	public void match(View v) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<DeviceInfo> devices = mDeviceDBManager
						.getDeviceByType(getDeviceType());
				for (DeviceInfo device : devices) {
					mDBAddresses.add(device.address.trim());
				}
			}
		}).start();
		mProgressDialog.show();
		mScanner.scanLeDevice(true);
	}

	@Override
	public void onScanStateChange(int scanState, List<BluetoothDevice> devices) {
		if (mCurrentScanState == DeviceScanner.STATE_BEGIN_SCAN
				&& scanState == DeviceScanner.STATE_END_SCAN) {
			if (devices == null || devices.size() == 0) {
				handleConFailed();
				return;
			}
			mCurrentScanTime = 0; // set scan number to 0
			System.out.println("scan success");
			
			final List<DeviceInfo> needSaveDevices = new ArrayList<DeviceInfo>();
			for (BluetoothDevice device : devices) {
				final String address = device.getAddress()
						.toUpperCase(Locale.getDefault()).trim();
				
				if (mDBAddresses.contains(address) || !isMatchedDevice(device)) {
					continue;
				}
				needSaveDevices.add(new DeviceInfo(getDeviceType(), 
						getDeviceName(getDeviceType()),
						address));
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (DeviceInfo deviceInfo : needSaveDevices) {
						mDeviceDBManager.addDevice(deviceInfo);
					}
				}
			}).start();

			hideProgress();
			if (needSaveDevices.size() != 0) {
				String rmdStr = getResources().getString(R.string.scan_success);
				Toast.makeText(mContext, rmdStr, Toast.LENGTH_LONG).show();
				finish();
			} else {
				
				Toast.makeText(mContext, getResources().getString(R.string.not_found_device), 
						Toast.LENGTH_LONG).show();
			}

		}
		mCurrentScanState = scanState;
	}

	private String getDeviceName(String type){
		String name = "";
		switch (type) {
		case DeviceInfo.TYPE_FH:
			name = getResources().getString(R.string.fh_text);
			break;
		case DeviceInfo.TYPE_BS:
			name =getResources().getString(R.string.bs_text);
			break;
		case DeviceInfo.TYPE_BP:
			name = getResources().getString(R.string.bp_text);
			break;
		default:
			name = getResources().getString(R.string.bp_text);
			break;
		}
		return name;
	}
	private void handleConFailed() {
		System.out.println("scan failed");
		if (mCurrentScanTime < 1) { // scan again
			mCurrentScanTime++;
			System.out.println("scan again");
			mScanner.scanLeDevice(true);
		} else {
			hideProgress();
			String rmdStr = getResources().getString(R.string.scan_failed);
			Toast.makeText(mContext, rmdStr, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (mScanner.isScanning())
			mScanner.scanLeDevice(false);
	}

	private boolean isMatchedDevice(BluetoothDevice device) {
		String deviceName = device.getName().trim().toLowerCase(Locale.getDefault());
		System.out.println("device name " + deviceName);
		if (getDeviceType().equals(DeviceInfo.TYPE_FH)) {
			if(deviceName.equals("bolutek"))//ÅÐ¶ÏÀàÐÍÓëÃû×ÖÊÇ·ñÆ¥Åä Ì¥ÐÄ
				return true;
		} else if(getDeviceType().equals(DeviceInfo.TYPE_BP)) {//ÑªÑ¹
			
			/*if(deviceName.equals("bolutek") || deviceName.equals("abg-bxxx")) {
				return false;
			}
				return true;*/
			//ÑªÑ¹ Ãû³Æ¼ì²é
//			if(deviceName.equals("lt-xy")){
				return true;
//			}
			
		} else if(getDeviceType().equals(DeviceInfo.TYPE_BS)) {//ÑªÌÇ
			if(deviceName.equals("abg-bxxx"))
				return true;
		}
		return false;
	}

}
