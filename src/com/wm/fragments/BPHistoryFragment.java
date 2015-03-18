package com.wm.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
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
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.wm.activity.R;
import com.wm.db.HistoryDBManager;
import com.wm.entity.BPResult;
import com.wm.utils.DateUtil;
import com.wm.utils.UUIDS;

public class BPHistoryFragment extends BaseHistoryFragment implements
		OnChartValueSelectedListener {

	@InjectView(R.id.bp_history_chart)
	LineChart mChart;

	private Context mContext;
	private HistoryDBManager mHistoryDBManager;
	private List<BPResult> mBPResults = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bp_history, container,
				false);
		ButterKnife.inject(this, view);
		mContext = getActivity();
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
		mChart.invalidate();

		// initData();
		addDataSet();
		super.onResume();
	}
	public void initLineChart() {
		mChart.setOnChartValueSelectedListener(this);
		mChart.setDrawYValues(false);
		mChart.setDrawGridBackground(false);
		mChart.setDoubleTapToZoomEnabled(false);
		mChart.setDescription("");
		mChart.setGridColor(getResources().getColor(R.color.light_black));
		mChart.setBorderColor(getResources().getColor(R.color.light_black));
		mChart.setStartAtZero(false);
		mChart.setScaleMinima(mBPResults.size()/12, 1);
	}

	/**
	 * 添加X轴
	 */
	private void addEmptyData() {
		ArrayList<String> xVals = new ArrayList<String>();

		// 创建 x值
		for (int i = 0; i < mBPResults.size(); i++) {
			Date date = DateUtil.longToDate(mBPResults.get(i).date);
			String datestr = DateUtil.getFormatDate("MM.dd", date);
			xVals.add(datestr);
		}

		for (int i = mBPResults.size(); i < 8; i++) {
			Calendar nowss = Calendar.getInstance();
			String datestr = nowss.get(Calendar.MONTH) + 1 + "."
					+ (nowss.get(Calendar.DAY_OF_MONTH) + i);
			xVals.add(datestr);
		}
		System.out.println("size " + xVals.size());

		LineData data = new LineData(xVals);
		mChart.setData(data);
		mChart.invalidate();
	}

	private void addDataSet() {

		LineData data = mChart.getData();

		if (data != null) {

			ArrayList<Entry> yValsSz = new ArrayList<Entry>();// 舒张
			ArrayList<Entry> yValsSs = new ArrayList<Entry>();// 收缩

			for (int i = 0; i < mBPResults.size(); i++) {
				System.out.println("收缩 " + mBPResults.get(i).ssValue + " 舒张 "
						+ mBPResults.get(i).szValue);
				yValsSz.add(new Entry(mBPResults.get(i).szValue, i));
				yValsSs.add(new Entry(mBPResults.get(i).ssValue, i));
			}

			LineDataSet szSet = new LineDataSet(yValsSz,
					getString(R.string.diastolic));
			szSet.setLineWidth(2.5f);
			szSet.setCircleSize(3f);

			szSet.setColor(getResources().getColor(R.color.dark_green));
			szSet.setCircleColor(getResources().getColor(R.color.dark_green));
			szSet.setHighLightColor(getResources().getColor(R.color.dark_green));

			data.addDataSet(szSet);

			LineDataSet ssSet = new LineDataSet(yValsSs,
					getString(R.string.systolic));
			ssSet.setLineWidth(2.5f);
			ssSet.setCircleSize(3f);

			ssSet.setColor(getResources().getColor(R.color.sky_blue));
			ssSet.setCircleColor(getResources().getColor(R.color.sky_blue));
			ssSet.setHighLightColor(getResources().getColor(R.color.sky_blue));

			data.addDataSet(ssSet);

			mChart.notifyDataSetChanged();
			mChart.animateY(1500);// 设置Y轴动画 毫秒;
		}
	}

	public void getBpHisory() {
		mBPResults = mHistoryDBManager.getAllBpResults();
		System.out.println("size + " + mBPResults.size());
	}

	public void initData() {
		mBPResults = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			float szValue = (float) Math.random() * 50f + 50f * 2;
			float ssValue = (float) Math.random() * 50f + 50f * 2;
			mBPResults.add(new BPResult(szValue, ssValue));
		}
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		float sz = mBPResults.get(e.getXIndex()).szValue;
		float ss = mBPResults.get(e.getXIndex()).ssValue;
		Toast.makeText(mContext, "舒张压： " + (int) sz + "  收缩压： " + (int) ss,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected() {

	}

	@Override
	public void handleConnect() {

	}

	@Override
	public void handleDisconnect() {

	}

	@Override
	public void handleGetData(String data) {

	}

	@Override
	public void handleServiceDiscover() {
		mBluetoothLeService.setCharacteristicNotification(
				getInfoCharacteristic(UUIDS.BP_RESULT_SERVICE,
						UUIDS.BP_RESULT_CHARAC), true);
	}

}
