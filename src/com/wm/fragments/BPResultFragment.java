package com.wm.fragments;

import java.util.UUID;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.activity.R;
import com.wm.blecore.BluetoothLeService;
import com.wm.db.HistoryDBManager;
import com.wm.entity.BPResult;
import com.wm.entity.BPResultException;
import com.wm.utils.DataConvertUtils;
import com.wm.utils.UUIDS;

public class BPResultFragment extends BaseResultFragment {

	@InjectView(R.id.lbl_ss)
	TextView mLblSS;
	@InjectView(R.id.lbl_sz)
	TextView mLblSZ;
	@InjectView(R.id.lbl_pressure)
	TextView mLblP;

	private boolean mNeedNewData = true;
	private BPResult mBPResult;
	private BPResultException mBPException;
	private Context mContext;
	private BluetoothLeService mBluetoothLeService;
	private BluetoothGattCharacteristic mInforCharacteristic;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bp_result, container,
				false);
		ButterKnife.inject(this, view);

		mContext = getActivity();

		return view;
	}

	@Override
	public void record() {
		HistoryDBManager.getInstance(getActivity()).addBpResult(mBPResult);
	}

	@Override
	public void handleData(String data, BluetoothLeService bluetoothLeService) {
		System.out.println(data);
		if(mBluetoothLeService == null) {
			mBluetoothLeService = bluetoothLeService;
			BluetoothGattService service = mBluetoothLeService
					.getServiceByUuid(UUIDS.BP_RESULT_SERVICE);
			mInforCharacteristic = service.getCharacteristic(UUID
					.fromString(UUIDS.BP_RESULT_CHARAC));
		}
		if (data.trim().length() == 38) {
			// 成功获得血压心率等数据
			mNeedNewData = false;
			mBPResult = new BPResult(data);
			mLblSS.setText(String.valueOf(mBPResult.ssValue));
			mLblSZ.setText(String.valueOf(mBPResult.szValue));
			// 将数据存入数据库
		} else if (data.trim().length() == 29) {
			// 获得异常信息
			mNeedNewData = false;
			mBPException = new BPResultException(data);
			Toast.makeText(mContext, mBPException.description,
					Toast.LENGTH_LONG).show();
		} else if (data.trim().length() == 8) {
			// 获得Pressure值
			mNeedNewData = true;
			String currentPressure = getPressureValue(data);
			System.out.println(currentPressure);
			mLblP.setText(currentPressure);
			
		}
		if (!mNeedNewData) {
			mBluetoothLeService.setCharacteristicNotification(
					mInforCharacteristic, false);
		}

	}

	// 计算Pressure值
	private String getPressureValue(String data) {
		String[] items = data.split(" ");
		int pressureH = Integer
				.valueOf(DataConvertUtils.hexToDecimal(items[2]));
		int pressureL = Integer
				.valueOf(DataConvertUtils.hexToDecimal(items[1]));
		return pressureH * 256 + pressureL + "";
	}


}
