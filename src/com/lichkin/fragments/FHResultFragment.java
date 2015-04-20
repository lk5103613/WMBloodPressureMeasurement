package com.lichkin.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.lichkin.activity.R;
import com.lichkin.db.HistoryDBManager;
import com.lichkin.entity.FHResult;
import com.lichkin.network.CheckNeedUploadTask;
import com.lichkin.utils.DataConvertUtils;
import com.lichkin.utils.PropertiesSharePrefs;
import com.lichkin.utils.SystemUtils;
import com.lichkin.utils.UUIDS;

public class FHResultFragment extends BaseResultFragment {

	@InjectView(R.id.embryo_result_chart)
	LineChart mChart;
	@InjectView(R.id.fh_heart)
	ImageView mFhHeart;

	private List<Integer> mFHValues;
	private List<Integer> mAllValues;
	private int recordIndex;
	private ArrayList<String> xVals;
	private Handler mHandler = new Handler();
	private boolean mBeginRecord = false;
	private PropertiesSharePrefs mProperties;
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mBeginRecord = false;
			saveAndJump();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fh_result, container,
				false);
		ButterKnife.inject(this, view);
		super.onCreateView(inflater, container, savedInstanceState);
		mFHValues = new ArrayList<Integer>();
		mAllValues = new ArrayList<Integer>();
		mProperties = PropertiesSharePrefs.getInstance(mContext);

		initLineChart();
		addEmptyData();
		mHandler.removeCallbacks(mRunnable);

		mBeginRecord = false;
		return view;
	}

	@Override
	public void record() {
		mBeginRecord = true;
		mCallback.setButtonState(BTN_STATE_UNAVAILABLE_WAITING);
		mHandler.postDelayed(mRunnable, 35000);

		Animation animation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.fh_heart);
		mFhHeart.startAnimation(animation);
	}

	private void initLineChart() {
		// chart
		mChart.setDrawYValues(false);
		mChart.setDrawGridBackground(false);
		mChart.setDoubleTapToZoomEnabled(false);
		mChart.setDescription("");
		mChart.setGridColor(getResources().getColor(R.color.fragment_bg));
		mChart.setBorderColor(getResources().getColor(R.color.fragment_bg));
		mChart.setStartAtZero(true);
		mChart.setDrawLegend(false);// 不绘制颜色标记
		mChart.setDrawXLabels(true);// 绘制X轴标签
		mChart.getXLabels().setPosition(XLabelPosition.BOTTOM);
		mChart.getYLabels().setLabelCount(5);
		mChart.setHighlightEnabled(false);
		mChart.setScaleMinima(40, 1);// 设置缩放比例
		mChart.centerViewPort(0, 200);
//		mChart.getXLabels().setSpaceBetweenLabels(2);
	}

	private void addEmptyData() {
		recordIndex = 0;

		// create 15 x-vals
		xVals = new ArrayList<String>();

		for (int i = 1; i <= 1000; i++) {
			xVals.add("");
		}

		LineData data = new LineData(xVals);
		mChart.setData(data);
		mChart.invalidate();
	}

	private void addEntry(float value) {
		recordIndex++;

		LineData data = mChart.getData();

		if (data != null) {

			LineDataSet set = data.getDataSetByIndex(0);

			if (set == null) {
				set = createSet();
				data.addDataSet(set);
			}
			int color = getResources().getColor(R.color.colorPrimary);
			set.setColor(color);
			set.setCircleColor(color);
			set.setLineWidth(2f);
			set.setCircleSize(2f);

			data.addEntry(new Entry(value, set.getEntryCount()), 0);// Math.random()
																	// * 50)
																	// +50f
			String xvalue = mFHValues.isEmpty() ? "" : mFHValues.size() + "";
			System.out.println("record index " + recordIndex + " " + xvalue);

			if ((xVals.size() - recordIndex) < 2) {
				xVals.add("");
			}

			if (recordIndex < xVals.size()) {
				mChart.getData().getXVals().set(recordIndex, xvalue);
			}

			if (recordIndex > 15) {
				mChart.centerViewPort(recordIndex - 9, 200);
			}

			if (recordIndex > 1000) {
				System.out.println("scale " + (recordIndex / 100) * 4.0f);
				mChart.setScaleMinima((recordIndex / 100) * 4, 1);
			}

			mChart.notifyDataSetChanged();

			// redraw the chart
			mChart.invalidate();
		}
	}

	private LineDataSet createSet() {
		LineDataSet set = new LineDataSet(null, getString(R.string.fh_value));
		set.setLineWidth(1f);
		set.setCircleSize(1.5f);
		set.setColor(Color.rgb(240, 99, 99));
		set.setCircleColor(Color.rgb(240, 99, 99));
		set.setHighLightColor(Color.rgb(190, 190, 190));
		return set;
	}

	private void saveAndJump() {
		mFhHeart.clearAnimation();

		if (mFHValues.size() < 60) {
			float average = getAverage();
			while (mFHValues.size() != 60 && average != 0) {
				mFHValues.add((int) average);
			}
		}
		new AsyncTask<Void, Void, Integer>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mCallback.setButtonState(BTN_STATE_AVAILABLE);
			}

			@Override
			protected Integer doInBackground(Void... params) {
				int count = mFHValues.size();
				if (!mFHValues.isEmpty()) {
					String card = mProperties.getProperty(
							PropertiesSharePrefs.TYPE_CARD, "");
					FHResult fhResult = new FHResult(mFHValues,
							new Date().getTime());
					fhResult.userCard = card;
					HistoryDBManager.getInstance(mContext)
							.addFhResult(fhResult);
				}
				return count;
			}

			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				mBeginRecord = false;
				mCallback.closeActivity();
			}
		}.execute();
		if (SystemUtils.getConnectState(mContext) == SystemUtils.TYPE_WIFI)
			new CheckNeedUploadTask(mContext, null, null, null,
					SystemUtils.TYPE_WIFI).execute();
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

		String fhValue = DataConvertUtils.hexToDecimal(data.split(" ")[1]);
		if (fhValue.trim().equals("0")) {// 获取值为0
			if (getAverage() != 0) {

				if (mBeginRecord) {
					mFHValues.add(getAverage());
				}
				addEntry(getAverage());
			}
		} else {// 获取值不为0
			mAllValues.add(Integer.valueOf(fhValue));
			if (mBeginRecord) {
				mFHValues.add(Integer.parseInt(fhValue));
			}
			addEntry(Integer.parseInt(fhValue));
		}
		if (mFHValues.size() >= 60) {
			mHandler.removeCallbacks(mRunnable);
			saveAndJump();
		}
		return false;
	}

	@Override
	public boolean handleServiceDiscover() {
		if (mBluetoothLeService != null) {
			mBluetoothLeService.setCharacteristicNotification(
					getInfoCharacteristic(UUIDS.FH_RESULT_SERVICE,
							UUIDS.FH_RESULT_CHARAC), true);
		}
		return false;
	}

	private int getAverage() {
		if (mAllValues.size() == 0)
			return 0;
		float sum = 0f;
		for (int i = 0; i < mAllValues.size(); i++) {
			sum += mAllValues.get(i);
		}
		return (int) sum / mAllValues.size();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
