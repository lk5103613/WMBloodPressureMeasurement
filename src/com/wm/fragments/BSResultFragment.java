package com.wm.fragments;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.activity.R;
import com.wm.blecore.BluetoothLeService;
import com.wm.db.HistoryDBManager;
import com.wm.entity.BSResult;
import com.wm.utils.UUIDS;

public class BSResultFragment extends BaseResultFragment {
	
	@InjectView(R.id.bs_value)
	TextView mBsValue;
	
	private BluetoothGattCharacteristic mCommandCharac;
	private BSResult mBSResult;
	private HistoryDBManager mDBManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bs_result, container, false);
		ButterKnife.inject(this, view);
		
		mDBManager = HistoryDBManager.getInstance(mContext);
		
		return view;
	}
	
	private void sendCommand() {
		if(mCommandCharac == null)
			mCommandCharac = getInfoCharacteristic(
					UUIDS.BS_RESULT_SERVICE, UUIDS.BS_CHARAC_COMMAND);
		mCommandCharac.setValue(BSResult.COMMAND_GET_CURRENT);
		mBluetoothLeService.writeCharacteristic(mCommandCharac);
	}
	
	@Override
	public void onBindService(BluetoothLeService bluetoothLeService) {
		super.onBindService(bluetoothLeService);
		sendCommand();
	}

	@Override
	public void record() {
		if(mBSResult != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					mDBManager.addBsResult(mBSResult);
				}
			}).start();
		}
		getActivity().finish();
	}

	@Override
	public boolean handleConnect() {
		return false;
	}

	@Override
	public boolean handleDisconnect() {
		return false;
	}

	@Override
	public boolean handleGetData(String data) {
		String[] datas = data.split(" ");
		if(datas.length == 18) {
			mBSResult = new BSResult(datas);
			mBsValue.setText(mBSResult.bg);
		}
		return false;
	}

	@Override
	public boolean handleServiceDiscover() {
		mBluetoothLeService.setCharacteristicNotification(
				getInfoCharacteristic(UUIDS.BS_RESULT_SERVICE,
						UUIDS.BS_RESULT_CHARAC), true);
		return false;
	}

}
