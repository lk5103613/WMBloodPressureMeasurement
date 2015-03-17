package com.wm.fragments;

import android.bluetooth.BluetoothGattCharacteristic;
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
	@InjectView(R.id.lbl_hr)
	TextView mLblHr;

	private boolean mNeedNewData = true;
	private BPResult mBPResult;
	private BPResultException mBPException;
	private Context mContext;
	private BluetoothLeService mBluetoothLeService;
	private BluetoothGattCharacteristic mInforCharacteristic = getInfoCharacteristic(
			UUIDS.BP_RESULT_SERVICE, UUIDS.BP_RESULT_CHARAC);

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

	// ����Pressureֵ
	private String getPressureValue(String data) {
		String[] items = data.split(" ");
		int pressureH = Integer
				.valueOf(DataConvertUtils.hexToDecimal(items[2]));
		int pressureL = Integer
				.valueOf(DataConvertUtils.hexToDecimal(items[1]));
		return pressureH * 256 + pressureL + "";
	}

	@Override
	public void handleConnect() {

	}

	@Override
	public void handleDisconnect() {
	}

	@Override
	public void handleGetData(String data) {
		if (data.trim().length() == 38) {
			// �ɹ����Ѫѹ���ʵ�����
			mNeedNewData = false;
			mBPResult = new BPResult(data);
			mLblSS.setText(String.valueOf((int) mBPResult.ssValue));
			mLblSZ.setText(String.valueOf((int) mBPResult.szValue));
			mLblHr.setText(String.valueOf((int) mBPResult.heartRate));
			// �����ݴ������ݿ�
		} else if (data.trim().length() == 29) {
			// ����쳣��Ϣ
			mNeedNewData = false;
			mBPException = new BPResultException(data);
			Toast.makeText(mContext, mBPException.description,
					Toast.LENGTH_LONG).show();
		} else if (data.trim().length() == 8) {
			// ���Pressureֵ
			mNeedNewData = true;
			String currentPressure = getPressureValue(data);
			mLblP.setText(currentPressure);
		}
		if (!mNeedNewData) {
			mBluetoothLeService.setCharacteristicNotification(
					mInforCharacteristic, false);
		}
	}

	@Override
	public void handleServiceDiscover() {
		mBluetoothLeService.setCharacteristicNotification(mInforCharacteristic, true);
	}

}
