package com.wm.fragments;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.activity.R;
import com.wm.customview.ImageTextView;
import com.wm.db.HistoryDBManager;
import com.wm.entity.BPResult;
import com.wm.entity.BPResultException;
import com.wm.network.CheckNeedUploadTask;
import com.wm.utils.DataConvertUtils;
import com.wm.utils.SystemUtils;
import com.wm.utils.UUIDS;

public class BPResultFragment extends BaseResultFragment {
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * 1. 在onCreateView中设置记录按钮不可用
	 * 2. 在获得正确数据或异常数据时设置按钮可点击
	 * 3. 如果BPResult不为空，点击记录按钮后显示ProgressBar，然后finish
	 * 4. 如果BPResult为空，直接finish
	 * 5. 为减小线程的影响，在record中使用了asynctask
	 * 
	 * 阅后即焚
	 * 
	 * 
	 * 
	 * 
	 */
	@InjectView(R.id.lbl_ss)
	ImageTextView mLblSS;
	@InjectView(R.id.lbl_sz)
	ImageTextView mLblSZ;
	@InjectView(R.id.lbl_pulse)
	ImageTextView mLblPlues;
	@InjectView(R.id.bp_pressure)
	ImageTextView mPressure;

	private boolean mNeedNewData = true;
	private BPResult mBPResult;
	private BPResultException mBPException;
	private BluetoothGattCharacteristic mInforCharacteristic;
//	private Handler mHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bp_result, container,
				false);
		ButterKnife.inject(this, view);
		super.onCreateView(inflater, container, savedInstanceState);
		mContext = getActivity();
		mPressure.startRotate();
//		
//		mHandler = new Handler(new Handler.Callback() {
//			
//			@Override
//			public boolean handleMessage(Message msg) {
//				return false;
//			}
//		});
		
		mCallback.setButtonState(BTN_STATE_UNAVAILABLE);

		return view;
	}

	@Override
	public void record() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if(mBPResult != null) 
					mCallback.setButtonState(BTN_STATE_UNAVAILABLE_WAITING);
			}

			@Override
			protected Void doInBackground(Void... params) {
				if(mBPResult != null) 
					HistoryDBManager.getInstance(getActivity()).addBpResult(mBPResult);
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				mCallback.closeActivity();
				super.onPostExecute(result);
			}
		}.execute();
		if(SystemUtils.getConnectState(mContext) == SystemUtils.TYPE_WIFI)
			new CheckNeedUploadTask(mContext, null, null, null, SystemUtils.TYPE_WIFI).execute();
	}

	// 计算Pressure值
	private String getPressureValue(String data) {
		String[] items = data.split(" ");
		int pressureH = Integer
				.valueOf(DataConvertUtils.hexToDecimal(items[2]));
		int pressureL = Integer
				.valueOf(DataConvertUtils.hexToDecimal(items[1]));
		return (pressureH * 256 + pressureL) + "";
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
		if (mInforCharacteristic == null && mBluetoothLeService != null) {
			mInforCharacteristic = getInfoCharacteristic(
					UUIDS.BP_RESULT_SERVICE, UUIDS.BP_RESULT_CHARAC);
		}
		if (data.trim().length() == 38) {
			mCallback.setButtonState(BTN_STATE_AVAILABLE);
			// 成功获得血压心率等数据
			mNeedNewData = false;
			mPressure.stopRotate();
			mBPResult = new BPResult(data);
			mLblSS.setText(String.valueOf((int) mBPResult.sbp));
			mLblSZ.setText(String.valueOf((int) mBPResult.dbp));
			mLblPlues.setText(String.valueOf((int)mBPResult.pulse));
			mCallback.showResult(mBPResult.bpResult);
			// 将数据存入数据库
		} else if (data.trim().length() == 29) {
			
			// 获得异常信息
			mCallback.setButtonState(BTN_STATE_UNAVAILABLE);
			mNeedNewData = false;
			mPressure.stopRotate();
			mBPException = new BPResultException(data);
			Toast.makeText(mContext, mBPException.description,
					Toast.LENGTH_LONG).show();
		} else if (data.trim().length() == 8) {
			// 获得Pressure值
			mNeedNewData = true;
			String currentPressure = getPressureValue(data);
			mPressure.setText(currentPressure);
		}
		if (!mNeedNewData) {
			if (mBluetoothLeService == null)
				return false;
			mBluetoothLeService.setCharacteristicNotification(
					mInforCharacteristic, false);
		}
		return false;
	}

	@Override
	public boolean handleServiceDiscover() {
		mLblSS.setText("");
		mLblSZ.setText("");
		mBluetoothLeService.setCharacteristicNotification(mInforCharacteristic,
				true);
		return false;
	}

}
