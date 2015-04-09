package com.wm.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.wm.activity.R;
import com.wm.customview.LineMarkerView;
import com.wm.db.HistoryDBManager;
import com.wm.entity.FHResult;
import com.wm.utils.DateUtil;
import com.wm.utils.SystemUtils;
import com.wm.utils.UUIDS;

public class FHHistoryFragment extends BaseHistoryFragment {// implements OnChartValueSelectedListener

	@InjectView(R.id.fh_history_chart)
	LineChart mChart;
	@InjectView(R.id.text_date)
	TextView mTxtDate;

	private HistoryDBManager mDbManager;
	private List<FHResult> mFHResults;
	private int mIndex = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_fh_history, container,
				false);
		ButterKnife.inject(this, view);

		mDbManager = HistoryDBManager.getInstance(mContext);
		mFHResults = mDbManager.getAllFhResults();
		initLineChart();
		addEmptyData(-1);
		mChart.invalidate();
//		initData();
		return view;
	}

	@Override
	public void onResume() {
		mIndex = 0;
		mDbManager = HistoryDBManager.getInstance(mContext);
		mFHResults = mDbManager.getAllFhResults();
		
		if (!mFHResults.isEmpty()) {
			mChart.getData().removeDataSet(0);
			addDataSet(mIndex);
		}
		super.onResume();
	}
	private void initLineChart() {
		// chart
//		mChart.setOnChartValueSelectedListener(this);
		mChart.setDrawYValues(false);
		mChart.setDrawGridBackground(false);
		mChart.setDoubleTapToZoomEnabled(false);
		mChart.setDescription("");
		mChart.setGridColor(getResources().getColor(R.color.fragment_bg));
		mChart.setBorderColor(getResources().getColor(R.color.fragment_bg));
		mChart.setStartAtZero(true);
		mChart.setScaleMinima(2, 1);
		mChart.setDrawLegend(false);//不绘制颜色标记
		mChart.setDrawXLabels(true);//绘制X轴标签
		mChart.getXLabels().setPosition(XLabelPosition.BOTTOM);
		LineMarkerView mv = new LineMarkerView(getActivity(),mChart, R.layout.custom_marker_view,R.drawable.mark_blue);//自定义标签
        mv.setOffsets(-mv.getMeasuredWidth() / 2-18*SystemUtils.getDensity(getActivity()), -mv.getMeasuredHeight()-5);//调整 数据 标签的位置
        mChart.setMarkerView(mv);// 设置标签
        mChart.setHighlightEnabled(true);
        mChart.centerViewPort(0, 200);
        mChart.setScaleEnabled(false);
        mChart.getYLabels().setLabelCount(5);
	}

	private void addEmptyData(int position) {

		// create 30 x-vals
		ArrayList<String> xVals = new ArrayList<>();
		int size = position == -1? 60:mFHResults.get(position).fhValues.size();
		
		for (int i = 1; i <= size; i++)
			xVals.add(i + "");

		LineData data = new LineData(xVals);
		mChart.setData(data);
		mChart.invalidate();
	}

	private void addDataSet(int position) {
		addEmptyData(position);
		long date = mFHResults.get(position).date;
		mTxtDate.setText(DateUtil.getFormatDate(DateUtil.DATA_FORMAT_CHINESE, date));

		LineData data = mChart.getData();

		if (data != null) {
			// create 10 y-vals
			ArrayList<Entry> yValsFh = new ArrayList<Entry>();
			List<Integer> fhValues = mFHResults.get(position).fhValues;

			for (int i = 0; i < fhValues.size(); i++) {
				yValsFh.add(new Entry(fhValues.get(i), i));
			}

			LineDataSet set = new LineDataSet(yValsFh,
					getString(R.string.fh_value));
			set.setLineWidth(2f);
			set.setCircleSize(2.5f);
			int color = getResources().getColor(R.color.colorPrimary);
			set.setColor(color);
			set.setCircleColor(color);
			set.setHighLightColor(color);
			data.addDataSet(set);
			
			//用于提高Y轴坐标的值
			ArrayList<Entry> yValsMax = new ArrayList<>();
			yValsMax.add(new Entry(300, 0));
			LineDataSet setMax = new LineDataSet(yValsMax, "");
			setMax.setLineWidth(2f);
			setMax.setCircleSize(2);
			int maxColor = getResources().getColor(R.color.fragment_bg);
			setMax.setCircleColor(maxColor);
			setMax.setHighLightColor(maxColor);
			setMax.setColor(maxColor);
			data.addDataSet(setMax);
			
			mChart.notifyDataSetChanged();
			mChart.centerViewPort(1, mChart.getAverage()+100);
			mChart.animateY(600);
		}
	}

//	private void initData() {
//		mFHResults = new ArrayList<>();
//		for (int i = 0; i < 3; i++) {
//			ArrayList<Float> fhValues = new ArrayList<>();
//			for (int j = 0; j < 110; j++) {
//				float fh = (float) (Math.random() * 50f + 50f * 2);
//
//				fhValues.add(fh);
//			}
//			FHResult fhResult = new FHResult(fhValues, new Date().getTime());
//
//			mFHResults.add(fhResult);
//		}
//	}

//	@Override
//	public void onValueSelected(Entry e, int dataSetmIndex) {
//		float fh = mFHResults.get(0).fhValues.get(e.getXIndex());
//		Toast.makeText(mContext, "胎心值： " + (int) fh, Toast.LENGTH_SHORT).show();
//	}
//
//	@Override
//	public void onNothingSelected() {
//
//	}

	@OnClick(R.id.btn_next)
	public void nextClick() {
		if(mFHResults.isEmpty()){
			Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_LONG).show();
			return;
		}
		if (mIndex >= (mFHResults.size() - 1)) {
			Toast.makeText(mContext, getString(R.string.msg_last_data),
					Toast.LENGTH_LONG).show();
			return;
		}
		mChart.getData().removeDataSet(0);
		addDataSet(++mIndex);
	}

	@OnClick(R.id.btn_previous)
	public void previousClick() {
		if(mFHResults.isEmpty()){
			Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_LONG).show();
			return;
		}
		if (mIndex <= 0) {
			Toast.makeText(mContext, getString(R.string.msg_fist_data),
					Toast.LENGTH_LONG).show();
			return;
		}
		mChart.getData().removeDataSet(0);
		addDataSet(--mIndex);
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
		return false;
	}

	@Override
	public boolean handleServiceDiscover() {
		mBluetoothLeService.setCharacteristicNotification(
				getInfoCharacteristic(UUIDS.FH_RESULT_SERVICE,
						UUIDS.FH_RESULT_CHARAC), true);
		return false; 
	}

}
