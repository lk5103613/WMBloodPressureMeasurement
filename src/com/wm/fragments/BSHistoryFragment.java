package com.wm.fragments;

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
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.wm.activity.R;
import com.wm.customview.BarMarkerView;
import com.wm.db.HistoryDBManager;
import com.wm.entity.BSResult;
import com.wm.utils.DateUtil;
import com.wm.utils.SystemUtils;
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
		bsResults = historyDBManager.getAllBsResults();
		mChart.setScaleMinima(bsResults.size()/7, 1);
		initBarChart();
//		setData(20, 50);
		
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		bsResults = historyDBManager.getAllBsResults();
		setData();
		mChart.animateY(1000);//Y轴动画
	}

	private void initBarChart() {
			colors = new int[]{getResources().getColor(R.color.yellow_green),
					getResources().getColor(R.color.colorPrimary)};

	        // enable the drawing of values
	        mChart.setDrawYValues(false);
	        mChart.setDrawValueAboveBar(true);
	        mChart.setDescription("");

	        // if more than 400 entries are displayed in the chart, no values will be
	        // drawn
	        mChart.setMaxVisibleValueCount(400);
	        mChart.set3DEnabled(false);//关闭3D效果
	        mChart.setDoubleTapToZoomEnabled(false);
	        mChart.setPinchZoom(false);// x y 轴单独缩放
	        mChart.setScaleEnabled(true);//设置可缩放
	        mChart.setDrawBarShadow(false);//柱状图阴影
	        mChart.setDrawGridBackground(false);
	        mChart.setDrawHorizontalGrid(false);//不绘制水平网格
	        mChart.setDrawVerticalGrid(false);
	        mChart.setValueTextSize(10f);
	        mChart.setDrawLegend(false);//不绘制颜色标记
	        mChart.setGridColor(getResources().getColor(R.color.fragment_bg));//网格颜色
			mChart.setBorderColor(getResources().getColor(R.color.dark_gray));//边框颜色
	        mChart.setBorderPositions(new BorderPosition[]{BorderPosition.BOTTOM,BorderPosition.LEFT});//绘制边框位置， 左、下
	        BarMarkerView mv = new BarMarkerView(getActivity(), R.layout.custom_marker_view,R.drawable.mark_yellow);//自定义标签
	        mv.setOffsets(-mv.getMeasuredWidth() / 2-20*SystemUtils.getDensity(getActivity()), -mv.getMeasuredHeight()-5);//调整 数据 标签的位置
	        mChart.setMarkerView(mv);// 设置标签
	        
	        XLabels xl = mChart.getXLabels();
	        xl.setPosition(XLabelPosition.BOTTOM);//x 坐标位置

	}
	
	public void setData(){
		ArrayList<String> xVals = new ArrayList<String>();
		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		
		System.out.println("size " + bsResults.size());
		for(int i = 0; i < bsResults.size();i++) {
			xVals.add(DateUtil.getFormatDate( DateUtil.DATA_FORMAT_ENGLISH,bsResults.get(i).date));
			yVals1.add(new BarEntry(Float.parseFloat(bsResults.get(i).bg), i));
		}
		
		for(int j = xVals.size(); j < 7; j++) {
			Calendar nowss = Calendar.getInstance();
			String datestr = nowss.get(Calendar.MONTH) + 1 + "."
					+ (nowss.get(Calendar.DAY_OF_MONTH) + j);
			xVals.add(datestr);
		}
		BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setBarSpacePercent(35f);
        set1.setColors(colors);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
       
        BarData data = new BarData(xVals, dataSets);
        mChart.setData(data);
        
       
	}
//	private void setData(int count, float range) {
//
//        ArrayList<String> xVals = new ArrayList<String>();
//        for (int i = 0; i < count; i++) {
//            xVals.add(i+"");
//        }
//
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//
//        for (int i = 0; i < count; i++) {
//            float mult = (range + 1);
//            float val = (float) (Math.random() * mult);
//            yVals1.add(new BarEntry(val, i));
//        }
//
//        BarDataSet set1 = new BarDataSet(yVals1, "");
//        set1.setBarSpacePercent(35f);
//        set1.setColors(colors);
//
//        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
//        dataSets.add(set1);
//
//        BarData data = new BarData(xVals, dataSets);
//
//        mChart.setData(data);
//    }

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
