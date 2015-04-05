package com.wm.fragments;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.activity.R;
import com.wm.blecore.BluetoothLeService;
import com.wm.customview.ImageTextView;
import com.wm.db.HistoryDBManager;
import com.wm.entity.BSResult;
import com.wm.utils.UUIDS;

public class BSResultFragment extends BaseResultFragment {
	
	/**
	 * 
	 * 
	 * 1. 在未获得数据之前，将记录按钮设置为不可用
	 * 2. 获得数据之后，将记录按钮设置为可用
	 * 3. 点击按钮之后，显示progressbar并finish
	 * 
	 * 阅读后删除
	 * 
	 * 
	 * 
	 */
	@InjectView(R.id.bs_value)
	ImageTextView mBsValue;
	
	private BluetoothGattCharacteristic mCommandCharac;
	private BSResult mBSResult;
	private HistoryDBManager mDBManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bs_result, container, false);
		ButterKnife.inject(this, view);
		
		mCallback.setButtonState(BTN_STATE_UNAVAILABLE);
		
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
		new AsyncTask<Void, Void, Void>() {
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mCallback.setButtonState(BTN_STATE_UNAVAILABLE_WAITING);
			}

			@Override
			protected Void doInBackground(Void... params) {
				if(mBSResult != null)
					mDBManager.addBsResult(mBSResult);
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				mCallback.closeActivity();
			}
		}.execute();
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
		} else if(datas.length == 6) {
			if(mBSResult == null) {
				return false;
			}
			mBSResult.getMeasureTime(datas);
			mCallback.setButtonState(BTN_STATE_AVAILABLE);
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
