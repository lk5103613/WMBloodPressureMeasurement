package com.wm.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
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
import com.wm.utils.PropertiesSharePrefs;
import com.wm.utils.SystemUtils;
import com.wm.utils.UUIDS;

public class BPResultFragment extends BaseResultFragment implements
		View.OnClickListener {

	/**
	 * 
	 * 
	 * 
	 * 
	 * 1. ��onCreateView�����ü�¼��ť������ 2. �ڻ����ȷ���ݻ��쳣����ʱ���ð�ť�ɵ�� 3.
	 * ���BPResult��Ϊ�գ������¼��ť����ʾProgressBar��Ȼ��finish 4. ���BPResultΪ�գ�ֱ��finish 5.
	 * Ϊ��С�̵߳�Ӱ�죬��record��ʹ����asynctask
	 * 
	 * �ĺ󼴷�
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
	private PropertiesSharePrefs mProperties;
	private View mDialogView;
	private TextView mErrorMsg;
	private Button mBtnRetry;
	private AlertDialog mDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bp_result, container,
				false);
		ButterKnife.inject(this, view);
		super.onCreateView(inflater, container, savedInstanceState);
		mContext = getActivity();
		mPressure.startRotate();
		mProperties = PropertiesSharePrefs.getInstance(mContext);
		//
		// mHandler = new Handler(new Handler.Callback() {
		// @Override
		// public boolean handleMessage(Message msg) {
		// return false;
		// }
		// });

		mCallback.setButtonState(BTN_STATE_UNAVAILABLE);
		return view;
	}

	@Override
	public void record() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (mBPResult != null)
					mCallback.setButtonState(BTN_STATE_UNAVAILABLE_WAITING);
			}

			@Override
			protected Void doInBackground(Void... params) {
				if (mBPResult != null) {
					String card = mProperties.getProperty(
							PropertiesSharePrefs.TYPE_CARD, "");
					mBPResult.userCard = card;
					HistoryDBManager.getInstance(getActivity()).addBpResult(
							mBPResult);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				mCallback.closeActivity();
				super.onPostExecute(result);
			}
		}.execute();
		if (SystemUtils.getConnectState(mContext) == SystemUtils.TYPE_WIFI)
			new CheckNeedUploadTask(mContext, null, null, null,
					SystemUtils.TYPE_WIFI).execute();
	}

	// ����Pressureֵ
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
			// �ɹ����Ѫѹ���ʵ�����
			mNeedNewData = false;
			mPressure.stopRotate();
			mBPResult = new BPResult(data);
			mLblSS.setText(String.valueOf((int) mBPResult.sbp));
			mLblSZ.setText(String.valueOf((int) mBPResult.dbp));
			mLblPlues.setText(String.valueOf((int) mBPResult.pulse));
			mCallback.showResult(mBPResult.bpResult);
			// �����ݴ������ݿ�
		} else if (data.trim().length() == 29) {

			// ����쳣��Ϣ
			mCallback.setButtonState(BTN_STATE_UNAVAILABLE);
			mNeedNewData = false;
			mPressure.stopRotate();
			mBPException = new BPResultException(data);
			showDialog(mBPException.description);
			// Toast.makeText(mContext, mBPException.description,
			// Toast.LENGTH_LONG).show();
		} else if (data.trim().length() == 8) {
			// ���Pressureֵ
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

	@SuppressLint("InflateParams")
	public void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCancelable(false);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mDialogView = inflater.inflate(R.layout.dialog_bp_failed, null);

		mErrorMsg = (TextView) mDialogView.findViewById(R.id.bp_error_message);
		mBtnRetry = (Button) mDialogView.findViewById(R.id.btn_retry);
		if (mErrorMsg == null)
			System.out.println("textview is null");
		if (msg == null)
			System.out.println("msg is null");
		mErrorMsg.setText(msg);
		mBtnRetry.setOnClickListener(this);
		mDialog = builder.create();
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setView(mDialogView, 0, 0, 0, 0);
		mDialog.show();
		Window dialogWindow = mDialog.getWindow();
		WindowManager m = getActivity().getWindowManager();
		Display d = m.getDefaultDisplay(); // ��ȡ��Ļ������
		WindowManager.LayoutParams params = dialogWindow.getAttributes();
		Point p = new Point();
		d.getSize(p);
		params.width = (int) (p.x * 0.8); // �������Ϊ��Ļ��0.65
		params.height = (int) (p.y * 0.25);
		dialogWindow.setAttributes(params);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_retry) {
			mDialog.dismiss();
			mCallback.closeActivity();
		}
	}

}
