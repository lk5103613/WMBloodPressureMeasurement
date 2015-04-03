package com.wm.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.wm.activity.R;
import com.wm.activity.R.color;
import com.wm.db.HistoryDBManager;
import com.wm.entity.BSResult;
import com.wm.utils.DateUtil;
import com.wm.utils.UUIDS;

public class BSHistoryFragment extends BaseHistoryFragment {
	
	public final static String BS_DATA = "bs_data";

	@InjectView(R.id.bs_barchart)
	BarChart mChart;
	private Handler mHandler;
	private BluetoothGattCharacteristic mCommandCharac;
	private HistoryDBManager historyDBManager;
	private List<BSResult> bsResults;
	private int[] colors;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bs_history, container,
				false);
		ButterKnife.inject(this, view);
		
		mHandler = new Handler();
		historyDBManager = HistoryDBManager.getInstance(getActivity());
		initBarChart();
		setData(20, 50);
		
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		bsResults = historyDBManager.getAllBsResults();
	}

	private void initBarChart() {
			colors = new int[]{getResources().getColor(R.color.yellow_green),
					getResources().getColor(R.color.colorPrimary)};

	        // enable the drawing of values
	        mChart.setDrawYValues(false);
	        mChart.setDrawValueAboveBar(true);
	        mChart.setDescription("");

	        // if more than 60 entries are displayed in the chart, no values will be
	        // drawn
	        mChart.setMaxVisibleValueCount(300);
	        mChart.set3DEnabled(false);//关闭3D效果
	        mChart.setPinchZoom(false);// x y 轴单独缩放
	        mChart.setDrawBarShadow(false);//柱状图阴影
	        mChart.setDrawGridBackground(false);
	        mChart.setDrawHorizontalGrid(true);
	        mChart.setDrawVerticalGrid(false);
	        mChart.setValueTextSize(10f);
	        mChart.setDrawLegend(false);

	        mChart.setDrawBorder(false);// 不绘制边框
	        XLabels xl = mChart.getXLabels();
	        xl.setPosition(XLabelPosition.BOTTOM);//x 坐标位置
	        mChart.animateY(1000);//Y轴动画

	}
	
	private void setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(i+"");
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            yVals1.add(new BarEntry(val, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setBarSpacePercent(35f);
        set1.setColors(colors);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        mChart.setData(data);
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
				mCommandCharac = getInfoCharacteristic(
						UUIDS.BS_RESULT_SERVICE, UUIDS.BS_CHARAC_COMMAND);
				mCommandCharac.setValue(BSResult.COMMAND_GET_REC_NO);
				mBluetoothLeService.writeCharacteristic(mCommandCharac);
			}
		}, 100);
		return false;
	}
	
}
