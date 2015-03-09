package com.wm.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wm.blecore.DeviceScanner;
import com.wm.blecore.DeviceScanner.ScanCallback;
import com.wm.db.DeviceDBManager;
import com.wm.entity.DeviceInfo;
import com.wm.utils.DialogUtils;

public class AddDeviceActivity extends BaseActivity implements ScanCallback {
	
	@InjectView(R.id.type_spinner)
	Spinner mTypeSpinner;
	@InjectView(R.id.add_device_toolbar)
	Toolbar mToolbar;
	
	private DeviceScanner mScanner;
	private DeviceDBManager mDeviceDBManager;
	private String mBPText;
	private String mBSText;
	private String mFHText;
	private ProgressDialog mProgressDialog;
	private List<String> mDBAddresses = new ArrayList<String>();
			
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_device);
		ButterKnife.inject(this);
		
		mToolbar.setTitle(getResources().getString(R.string.add_new_device));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
		mScanner = DeviceScanner.getInstance(mBluetoothAdapter, this);
		mDeviceDBManager = DeviceDBManager.getInstance(mContext);
		mBPText = getResources().getString(R.string.bp_text);
		mBSText = getResources().getString(R.string.bs_text);
		mFHText = getResources().getString(R.string.fh_text);
		
		String[] type = new String[]{mBPText, mBSText, mFHText};
		
		 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.type_item, type); 
		 arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 mTypeSpinner.setAdapter(arrayAdapter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in, R.anim.slide_out_to_right);
		}
	}
	
	private String getDeviceType() {
		String type = null;
		String selectedType = mTypeSpinner.getSelectedItem().toString();
		if(selectedType.equals(mBPText)) {
			type = DeviceInfo.TYPE_BP;
		} else if(selectedType.equals(mBSText)) {
			type = DeviceInfo.TYPE_BS;
		} else if(selectedType.equals(mFHText)) {
			type = DeviceInfo.TYPE_FH;
		}
		return type;
	}
	
	@OnClick(R.id.btn_match)
	public void match(View v) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				 List<DeviceInfo> devices = mDeviceDBManager.getDeviceByType(getDeviceType());
				 for(DeviceInfo device : devices) {
					 mDBAddresses.add(device.address.trim());
				 }
			}
		}).start();
		mProgressDialog = DialogUtils.showProgressDialog(mContext, "", "正在扫描设备");
		mScanner.scanLeDevice(true);
	}

	@Override
	public void onScanStateChange(int scanState) {
		if(scanState == DeviceScanner.STATE_END_SCAN) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onScanSuccess(List<BluetoothDevice> devices) {
		System.out.println("scan success    " + devices.size());
		for(BluetoothDevice device : devices) {
			
			final String address = device.getAddress().toUpperCase(Locale.getDefault()).trim();
			if(mDBAddresses.contains(address) || !isMatchedDevice(device)) {
				continue;
			}
			final String name = device.getName();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					mDeviceDBManager.addDevice(new DeviceInfo(getDeviceType(), name, address));
				}
			}).start();
		}
	}

	@Override
	public void onScanFailed() {
		System.out.println("scan failed");
		mProgressDialog.dismiss();
		String rmdStr = getResources().getString(R.string.scan_failed);
		Toast.makeText(mContext, rmdStr, Toast.LENGTH_LONG).show();
	}
	
	private boolean isMatchedDevice(BluetoothDevice device) {
		return true;
	}
	
}
