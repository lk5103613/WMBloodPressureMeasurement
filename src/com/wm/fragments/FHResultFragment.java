package com.wm.fragments;

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
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.wm.activity.R;
import com.wm.db.HistoryDBManager;
import com.wm.entity.FHResult;
import com.wm.utils.DataConvertUtils;
import com.wm.utils.UUIDS;

public class FHResultFragment extends BaseResultFragment {

	@InjectView(R.id.embryo_result_chart)
	LineChart mChart;

	private List<Integer> mFHValues;
	private int recordIndex;
	private ArrayList<String> xVals;
	private Handler mHandler;
	private boolean mBeginRecord = false;
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

		initLineChart();
		addEmptyData();
		
		mHandler = new Handler();

		return view;
	}

	@Override
	public void record() {
		mBeginRecord = true;
		mCallback.setButtonState(BTN_STATE_UNAVAILABLE_WAITING);
		mHandler.postDelayed(mRunnable, 35000);
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
		mChart.setDrawLegend(false);//不绘制颜色标记
		mChart.setDrawXLabels(true);//绘制X轴标签
		mChart.getXLabels().setPosition(XLabelPosition.BOTTOM);
		mChart.getYLabels().setLabelCount(5);
	    mChart.setHighlightEnabled(false);
		mChart.setScaleMinima(2, 1);// 设置缩放比例
		mChart.centerViewPort(0, 200);

	}

	private void addEmptyData() {
		recordIndex = 0;

		// create 15 x-vals
		xVals = new ArrayList<String>();

		for (int i = 1; i <= 15; i++) {
			xVals.add(i + "");
		}

		LineData data = new LineData(xVals);
		mChart.setData(data);
		mChart.invalidate();
	}

	private void addEntry(float value) {
		recordIndex ++;

		LineData data = mChart.getData();

		if (data != null) {

			LineDataSet set = data.getDataSetByIndex(0);

			if (set == null) {
				set = createSet();
				data.addDataSet(set);
			}
			set.setColor(getResources().getColor(R.color.red));
			set.setCircleColor(getResources().getColor(R.color.red));
			set.setLineWidth(1.5f);
			set.setCircleSize(1.5f);

			data.addEntry(new Entry(value, set.getEntryCount()), 0);// Math.random() * 50) +50f
			System.out.println("xvals size " + xVals.size() +" " + mFHValues.size());
			
			if ((xVals.size() - recordIndex) < 2) {
				xVals.add((xVals.size() + 1) + "");
			}
			if (recordIndex>10) {
				mChart.centerViewPort(recordIndex+1, 200);
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
//		if (mFHValues.size() == 0 || getAverage()==0) {
//			Toast.makeText(getActivity(), getResources().getString(R.string.no_data_available), Toast.LENGTH_LONG).show();
//			mCallback.setButtonState(BTN_STATE_UNAVAILABLE);
//			return;
//		}
		if(mFHValues.size() < 60) {
			float average = getAverage();
			while(mFHValues.size() != 60 && average != 0) {
				mFHValues.add((int)average);
			}
		}
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mCallback.setButtonState(BTN_STATE_AVAILABLE);
			}
			
			@Override
			protected Void doInBackground(Void... params) {
				if (!mFHValues.isEmpty()){
					FHResult fhResult = new FHResult(mFHValues, new Date().getTime());
					HistoryDBManager.getInstance(mContext).addFhResult(fhResult);
				}
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				mBluetoothLeService.disconnect();
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
		String fhValue = DataConvertUtils.hexToDecimal(data.split(" ")[1]);
		if(!mBeginRecord) {
			return true;
		}
		if (fhValue.trim().equals("0")) {//获取值为0
			if(getAverage()!=0) {
				addEntry(getAverage());
				mFHValues.add(getAverage());
			}
		} else {//获取值不为0
			addEntry(Integer.parseInt(fhValue));
			mFHValues.add(Integer.parseInt(fhValue));
		}
		if(mFHValues.size() >= 60) {
			mHandler.removeCallbacks(mRunnable);
			saveAndJump();
		}
		return false;
	}

	@Override
	public boolean handleServiceDiscover() {
		if(mBluetoothLeService != null) {
			mBluetoothLeService.setCharacteristicNotification(
					getInfoCharacteristic(UUIDS.FH_RESULT_SERVICE,
							UUIDS.FH_RESULT_CHARAC), true);
		}
		return false;
	}
	
	private int getAverage() {
		if(mFHValues.size() == 0)
			return 0;
		float sum = 0f;
		for(int i=0; i<mFHValues.size(); i++) {
			sum += mFHValues.get(i);
		}
		return (int)sum/mFHValues.size();
	}

}
