package com.wm.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.github.mikephil.charting.utils.YLabels;
import com.wm.activity.R;
import com.wm.customview.LineMarkerView;
import com.wm.db.HistoryDBManager;
import com.wm.entity.BPResult;
import com.wm.utils.PropertiesSharePrefs;
import com.wm.utils.SystemUtils;
import com.wm.utils.UUIDS;

public class BPHistoryFragment extends BaseHistoryFragment implements
		OnChartValueSelectedListener {

	@InjectView(R.id.bp_history_chart)
	LineChart mChart;
	@InjectView(R.id.text_heart)
	TextView textHeart;

	LineMarkerView mv;

	private int lineLastIndex = 0;
	private final int JUMPCOUMP=2;
	private HistoryCallback mCallback;

	private HistoryDBManager mHistoryDBManager;
	private List<BPResult> mBPResults = new ArrayList<>();
	String idCard = "";
	
	public interface HistoryCallback {
		
		void dataError();
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallback = (HistoryCallback) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bp_history, container,
				false);
		ButterKnife.inject(this, view);
		mHistoryDBManager = HistoryDBManager.getInstance(mContext);
		getBpHisory();

		// chart
		initLineChart();
		return view;
	}

	@Override
	public void onResume() {

		getBpHisory();
		addEmptyData();

		// initData();
		addDataSet();
		super.onResume();
	}

	public void initLineChart() {
		mChart.setOnChartValueSelectedListener(this);
		mChart.setPinchZoom(false);
		mChart.setDrawYValues(false);
		mChart.setScaleEnabled(false);
		mChart.setDrawGridBackground(false);
		mChart.setDoubleTapToZoomEnabled(false);
		mChart.setDescription("");// 单位
		mChart.setGridColor(getResources().getColor(R.color.fragment_bg));
		mChart.setBorderColor(getResources().getColor(R.color.fragment_bg));
		mChart.setDrawBorder(false);
		mChart.getXLabels().setPosition(XLabelPosition.BOTTOM);
		// mChart.setStartAtZero(false);
		mChart.setDrawLegend(false);
		mChart.setScaleMinima(1, 1f);// mBPResults.size()/20
		mChart.setDrawXLabels(true);// 绘制X轴标签
		mv = new LineMarkerView(getActivity(), mChart,
				R.layout.custom_marker_view);// 自定义标签
		mv.setOffsets(
				-mv.getMeasuredWidth() / 2 - 10
						* SystemUtils.getDensity(getActivity()),
				-mv.getMeasuredHeight() + 15
						* SystemUtils.getDensity(getActivity()));// 调整 数据 标签的位置
		mChart.setMarkerView(mv);// 设置标签
		mChart.getXLabels().setTextSize(12);
		mChart.getXLabels().setSpaceBetweenLabels(1);

		YLabels y = mChart.getYLabels(); // y轴的标示
		y.setLabelCount(4); // y轴上的标签的显示的个数

	}

	/**
	 * 添加X轴
	 */
	private void addEmptyData() {
		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < JUMPCOUMP; i++) {
			xVals.add("");
		}
		// 创建 x值
		for (int i = 0; i < mBPResults.size(); i++) {
			xVals.add("");
		}

		for (int i = mBPResults.size(); i < 15; i++) {
			xVals.add("");
		}

		LineData data = new LineData(xVals);
		mChart.setData(data);
		mChart.invalidate();
	}

	private void addDataSet() {

		LineData data = mChart.getData();

		if (data != null) {

			ArrayList<Entry> yValsSz = new ArrayList<Entry>();// 舒张
			ArrayList<Entry> yValsSs = new ArrayList<Entry>();// 收缩
			// ArrayList<Entry> yValsHr = new ArrayList<>();//心率

			for (int i = 0; i < mBPResults.size(); i++) {
				yValsSz.add(new Entry(mBPResults.get(i).dbp, i + 2));
				yValsSs.add(new Entry(mBPResults.get(i).sbp, i + 2));
				// yValsHr.add(new Entry(mBPResults.get(i).pulse, i));
			}

			LineDataSet ssSet = new LineDataSet(yValsSs,
					getString(R.string.systolic));
			int blue = getResources().getColor(R.color.colorPrimary);
			ssSet.setLineWidth(2f);
			ssSet.setCircleSize(3f);
			ssSet.setColor(blue);
			ssSet.setCircleColor(blue);
			ssSet.setHighLightColor(blue);
			data.addDataSet(ssSet);

			LineDataSet szSet = new LineDataSet(yValsSz,
					getString(R.string.diastolic));
			szSet.setLineWidth(2f);
			szSet.setCircleSize(3f);
			int yellowGreen = getResources().getColor(R.color.yellow_green);
			szSet.setColor(yellowGreen);
			szSet.setCircleColor(yellowGreen);
			szSet.setHighLightColor(yellowGreen);
			data.addDataSet(szSet);

			// LineDataSet hrSet = new LineDataSet(yValsHr,
			// getString(R.string.heart_rate));
			// hrSet.setLineWidth(2.5f);
			// hrSet.setCircleSize(3f);
			//
			// hrSet.setColor(getResources().getColor(R.color.yellow));
			// hrSet.setCircleColor(getResources().getColor(R.color.yellow));
			// hrSet.setHighLightColor(getResources().getColor(R.color.yellow));
			// data.addDataSet(hrSet);

			// 用于提高Y轴坐标的值
			ArrayList<Entry> yValsMax = new ArrayList<>();
			yValsMax.add(new Entry(180, 0));
			LineDataSet setMax = new LineDataSet(yValsMax, "");
			setMax.setLineWidth(2f);
			setMax.setCircleSize(2);
			int maxColor = getResources().getColor(R.color.fragment_bg);
			setMax.setCircleColor(maxColor);
			setMax.setColor(maxColor);
			data.addDataSet(setMax);

			if (!mBPResults.isEmpty()) {
				mChart.centerViewPort(1, mChart.getAverage() + 100);// 设置视角中心
			}
			mChart.notifyDataSetChanged();
			mChart.animateY(1000);// 设置Y轴动画 毫秒;
		}
	}

	public void getBpHisory() {
		idCard = PropertiesSharePrefs.getInstance(mContext).getProperty(PropertiesSharePrefs.TYPE_CARD, "");
		mBPResults = mHistoryDBManager.getBpResultsByUser(idCard);
	}

	// public void initData() {
	// mBPResults = new ArrayList<>();
	// for (int i = 0; i < 10; i++) {
	// float szValue = (float) Math.random() * 50f + 50f * 2;
	// float ssValue = (float) Math.random() * 50f + 50f * 2;
	// mBPResults.add(new BPResult(szValue, ssValue));
	// }
	// }

	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		if(e == null || e.getXIndex()-JUMPCOUMP > mBPResults.size()-1) {
			mChart.getData().getXVals().set(lineLastIndex, "");
			return;
		}

		float hr = mBPResults.get(e.getXIndex() - JUMPCOUMP).pulse;
		textHeart.setText((int) hr + "");
		mChart.getData().getXVals().set(lineLastIndex, "");
		mChart.getData()
				.getXVals()
				.set(e.getXIndex(),
						mBPResults.get(e.getXIndex() - JUMPCOUMP).measureTime);

		lineLastIndex = e.getXIndex();

		Highlight[] highs = new Highlight[2];
		highs[0] = new Highlight(e.getXIndex(), 0);
		highs[1] = new Highlight(e.getXIndex(), 1);
		mChart.highlightValues(highs);
	}

	@Override
	public void onNothingSelected() {
		mChart.getData().getXVals().set(lineLastIndex, "");
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
		System.out.println(data + "    " + data.length());
		if(data.length() != 38 && data.length() != 29 && data.length() != 8) {
			System.out.println("数据异常");
			mCallback.dataError();
			return true;
		}
		return false;
	}

	@Override
	public boolean handleServiceDiscover() {
		mBluetoothLeService.setCharacteristicNotification(
				getInfoCharacteristic(UUIDS.BP_RESULT_SERVICE,
						UUIDS.BP_RESULT_CHARAC), true);
		return false;
	}

}
