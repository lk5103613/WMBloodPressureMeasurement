package com.lichkin.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.lichkin.activity.R;
import com.lichkin.customview.BarMarkerView;
import com.lichkin.db.HistoryDBManager;
import com.lichkin.entity.BSResult;
import com.lichkin.utils.DateUtil;
import com.lichkin.utils.PropertiesSharePrefs;
import com.lichkin.utils.SystemUtils;
import com.lichkin.utils.UUIDS;

public class BSHistoryFragment extends BaseHistoryFragment implements OnChartValueSelectedListener{

	public final static String BS_DATA = "bs_data";

	@InjectView(R.id.bs_barchart)
	BarChart mChart;
	private Handler mHandler;
	private BluetoothGattCharacteristic mCommandCharac;
	private HistoryDBManager historyDBManager;
	private List<BSResult> bsResults;
	private int[] colors;
	private int mBarLastIndex = 0;
	String idCard = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bs_history, container,
				false);
		ButterKnife.inject(this, view);

		mHandler = new Handler();
		historyDBManager = HistoryDBManager.getInstance(getActivity());
		getBsResult();
		mChart.setScaleMinima(bsResults.size() / 7, 1);
		initBarChart();
		// setData(20, 50);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		getBsResult();
		setData();
		mChart.animateY(1000);// Y�ᶯ��
	}
	
	private void getBsResult(){
		idCard = PropertiesSharePrefs.getInstance(mContext).getProperty(PropertiesSharePrefs.TYPE_CARD, "");
		bsResults = historyDBManager.getBsResultsByUser(idCard);
	}

	private void initBarChart() {
		colors = new int[] { getResources().getColor(R.color.yellow_green),
				getResources().getColor(R.color.colorPrimary) };

		// enable the drawing of values
		mChart.setDrawYValues(false);
		mChart.setDrawValueAboveBar(true);
		mChart.setDescription("");

		// if more than 400 entries are displayed in the chart, no values will
		// be
		// drawn
		mChart.setMaxVisibleValueCount(400);
		mChart.set3DEnabled(false);// �ر�3DЧ��
		mChart.setDoubleTapToZoomEnabled(false);
		mChart.setPinchZoom(false);// x y �ᵥ������
		mChart.setScaleEnabled(false);// ���ò�������
		mChart.setDrawBarShadow(false);// ��״ͼ��Ӱ
		mChart.setDrawGridBackground(false);
		mChart.setDrawHorizontalGrid(false);// ������ˮƽ����
		mChart.setDrawVerticalGrid(false);
		mChart.setValueTextSize(10f);
		mChart.setDrawLegend(false);// ��������ɫ���
		mChart.setGridColor(getResources().getColor(R.color.fragment_bg));// ������ɫ
		mChart.setBorderColor(getResources().getColor(R.color.dark_gray));// �߿���ɫ
		mChart.setBorderPositions(new BorderPosition[] { BorderPosition.BOTTOM,
				BorderPosition.LEFT });// ���Ʊ߿�λ�ã� ����
		mChart.setOnChartValueSelectedListener(this);
		BarMarkerView mv = new BarMarkerView(getActivity(),
				R.layout.custom_marker_view);// �Զ����ǩ
		mv.setOffsets(
				-mv.getMeasuredWidth() / 2 - 8
						* SystemUtils.getDensity(getActivity()),
				-mv.getMeasuredHeight() + 10
						* SystemUtils.getDensity(getActivity()));// ���� ���� ��ǩ��λ��
		mChart.setMarkerView(mv);// ���ñ�ǩ
		mChart.setDescription("");// ���õ�λ

		XLabels xl = mChart.getXLabels();
		xl.setCenterXLabelText(true);
		xl.setPosition(XLabelPosition.BOTTOM);// x ����λ��

	}

	public void setData() {
		ArrayList<String> xVals = new ArrayList<String>();
		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

		System.out.println("size " + bsResults.size());
		for (int i = 0; i < bsResults.size(); i++) {
			xVals.add( " ");
			yVals1.add(new BarEntry(Float.parseFloat(bsResults.get(i).bg), i));
		}

		for (int j = xVals.size(); j < 7; j++) {
			Calendar nowss = Calendar.getInstance();
			String datestr = nowss.get(Calendar.MONTH) + 1 + "."
					+ (nowss.get(Calendar.DAY_OF_MONTH) + j);
			xVals.add(" ");
		}
		BarDataSet set1 = new BarDataSet(yVals1, "");
		set1.setBarSpacePercent(35f);
		set1.setColors(colors);

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);

		BarData data = new BarData(xVals, dataSets);
		mChart.setData(data);

	}

	// private void setData(int count, float range) {
	//
	// ArrayList<String> xVals = new ArrayList<String>();
	// for (int i = 0; i < count; i++) {
	// xVals.add(i+"");
	// }
	//
	// ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
	//
	// for (int i = 0; i < count; i++) {
	// float mult = (range + 1);
	// float val = (float) (Math.random() * mult);
	// yVals1.add(new BarEntry(val, i));
	// }
	//
	// BarDataSet set1 = new BarDataSet(yVals1, "");
	// set1.setBarSpacePercent(35f);
	// set1.setColors(colors);
	//
	// ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
	// dataSets.add(set1);
	//
	// BarData data = new BarData(xVals, dataSets);
	//
	// mChart.setData(data);
	// }

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
		System.out.println(data);
		return false;
	}

	@Override
	public boolean handleServiceDiscover() {
		mBluetoothLeService.setCharacteristicNotification(
				getInfoCharacteristic(UUIDS.BS_RESULT_SERVICE,
						UUIDS.BS_RESULT_CHARAC), true);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mCommandCharac = getInfoCharacteristic(UUIDS.BS_RESULT_SERVICE,
						UUIDS.BS_CHARAC_COMMAND);
				mCommandCharac.setValue(BSResult.COMMAND_GET_REC_NO);
				mBluetoothLeService.writeCharacteristic(mCommandCharac);
			}
		}, 100);
		return false;
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		if(null == e||e.getXIndex() >bsResults.size()-1) {
			mChart.getData().getXVals().set(mBarLastIndex, " ");
			return;
		}
		String date = DateUtil.getFormatDate(DateUtil.DATA_FORMAT,bsResults.get(e.getXIndex()).date);
		mChart.getData().getXVals().set(mBarLastIndex, " ");
		mChart.getData().getXVals().set(e.getXIndex(), date);
		mBarLastIndex = e.getXIndex();
	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub
		
	}

}