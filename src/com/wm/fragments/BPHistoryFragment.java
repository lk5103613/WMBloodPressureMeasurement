package com.wm.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.github.mikephil.charting.utils.YLabels;
import com.wm.activity.R;
import com.wm.customview.MyMarkerView;
import com.wm.db.HistoryDBManager;
import com.wm.entity.BPResult;
import com.wm.utils.DateUtil;
import com.wm.utils.UUIDS;

public class BPHistoryFragment extends BaseHistoryFragment implements OnChartValueSelectedListener {

	@InjectView(R.id.bp_history_chart)
	LineChart mChart;
	@InjectView(R.id.text_heart)
	TextView textHeart;

	private HistoryDBManager mHistoryDBManager;
	private List<BPResult> mBPResults = new ArrayList<>();

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
		System.out.println("bp oncreate");
		return view;
	}

	@Override
	public void onResume() {
		
		getBpHisory();
		addEmptyData();
		mChart.invalidate();

		// initData();
		addDataSet();
		System.out.println("bp onresume");
		super.onResume();
	}
	public void initLineChart() {
		mChart.setOnChartValueSelectedListener(this);
		mChart.setDrawYValues(false);
		mChart.setDrawGridBackground(false);
		mChart.setDoubleTapToZoomEnabled(false);
		mChart.setDescription("");
		mChart.setGridColor(getResources().getColor(R.color.fragment_bg));
		mChart.setBorderColor(getResources().getColor(R.color.fragment_bg));
		mChart.setDrawBorder(false);
		mChart.getXLabels().setPosition(XLabelPosition.BOTTOM);
		//mChart.setStartAtZero(false);
	
		mChart.setScaleMinima(1, 1.3f);//mBPResults.size()/20
		mChart.setDrawXLabels(true);//����X���ǩ
		MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view,R.drawable.mark_yellow);//�Զ����ǩ
        mv.setOffsets(-mv.getMeasuredWidth() / 2 +35, -mv.getMeasuredHeight()-5);//���� ���� ��ǩ��λ��
        mChart.setMarkerView(mv);// ���ñ�ǩ
        
        YLabels y = mChart.getYLabels(); //y��ı�ʾ
        y.setLabelCount(4); // y���ϵı�ǩ����ʾ�ĸ���
		
	}

	/**
	 * ���X��
	 */
	private void addEmptyData() {
		ArrayList<String> xVals = new ArrayList<String>();

		// ���� xֵ
		for (int i = 0; i < mBPResults.size(); i++) {
			Date date = DateUtil.longToDate(mBPResults.get(i).date);
			String datestr = (date.getMonth()+1)+"." + (date.getDate());
			xVals.add(datestr);
		}

		for (int i = mBPResults.size(); i < 15; i++) {
			Calendar nowss = Calendar.getInstance();
			String datestr = nowss.get(Calendar.MONTH) + 1 + "."
					+ (nowss.get(Calendar.DAY_OF_MONTH) + i);
			xVals.add(datestr);
		}

		LineData data = new LineData(xVals);
		mChart.setData(data);
		mChart.invalidate();
	}

	private void addDataSet() {

		LineData data = mChart.getData();

		if (data != null) {

			ArrayList<Entry> yValsSz = new ArrayList<Entry>();// ����
			ArrayList<Entry> yValsSs = new ArrayList<Entry>();// ����
//			ArrayList<Entry> yValsHr = new ArrayList<>();//����

			for (int i = 0; i < mBPResults.size(); i++) {
				yValsSz.add(new Entry(mBPResults.get(i).dbp, i));
				yValsSs.add(new Entry(mBPResults.get(i).sbp, i));
//				yValsHr.add(new Entry(mBPResults.get(i).pulse, i));
			}

			LineDataSet szSet = new LineDataSet(yValsSz,
					getString(R.string.diastolic));
			szSet.setLineWidth(2f);
			szSet.setCircleSize(3f);

			szSet.setColor(getResources().getColor(R.color.yellow_green));
			szSet.setCircleColor(getResources().getColor(R.color.yellow_green));
			szSet.setHighLightColor(getResources().getColor(R.color.yellow_green));
			data.addDataSet(szSet);

			LineDataSet ssSet = new LineDataSet(yValsSs,
					getString(R.string.systolic));
			ssSet.setLineWidth(2f);
			ssSet.setCircleSize(3f);
			ssSet.setColor(getResources().getColor(R.color.colorPrimary));
			ssSet.setCircleColor(getResources().getColor(R.color.colorPrimary));
			ssSet.setHighLightColor(getResources().getColor(R.color.colorPrimary));
			data.addDataSet(ssSet);

//			LineDataSet hrSet = new LineDataSet(yValsHr,
//					getString(R.string.heart_rate));
//			hrSet.setLineWidth(2.5f);
//			hrSet.setCircleSize(3f);
//
//			hrSet.setColor(getResources().getColor(R.color.yellow));
//			hrSet.setCircleColor(getResources().getColor(R.color.yellow));
//			hrSet.setHighLightColor(getResources().getColor(R.color.yellow));
//			data.addDataSet(hrSet);

			mChart.centerViewPort(1, mChart.getAverage()+100);//�����ӽ�����
			mChart.notifyDataSetChanged();
			mChart.animateY(1500);// ����Y�ᶯ�� ����;
		}
	}

	public void getBpHisory() {
		mBPResults = mHistoryDBManager.getAllBpResults();
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
		
		float hr =  mBPResults.get(e.getXIndex()).pulse;
		textHeart.setText((int)hr+"");
	}

	@Override
	public void onNothingSelected() {

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
				getInfoCharacteristic(UUIDS.BP_RESULT_SERVICE,
						UUIDS.BP_RESULT_CHARAC), true);
		return false;
	}

}
