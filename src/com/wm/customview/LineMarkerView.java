
package com.wm.customview;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.MarkerView;
import com.github.mikephil.charting.utils.Utils;
import com.wm.activity.R;

/**
 * 自定义MarkerView
 * 
 */
public class LineMarkerView extends MarkerView {

    private TextView tvContent;
    private RelativeLayout relativeLayout;
    private LineChart mChart;
    private int markerBg;

    public LineMarkerView(Context context,LineChart chart, int layoutResource, int marker) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.tvContent);
        relativeLayout = (RelativeLayout)findViewById(R.id.marker_container);
        this.markerBg = marker;
        this.mChart = chart;
    }

    
    /* (non-Javadoc)
     * @see com.github.mikephil.charting.utils.MarkerView#refreshContent(com.github.mikephil.charting.data.Entry, int)
     * 
     * 每次重绘 markerview的时候会调用， 可以用户更新内容
     */
    @Override
    public void refreshContent(Entry e, int dataSetIndex) {
    	
    	int color = mChart.getData().getDataSetByIndex(dataSetIndex).getColor();
    	
    	if(color == getResources().getColor(R.color.fragment_bg)){
    		relativeLayout.setBackground(null);
    		return;
    	} else {
    		relativeLayout.setBackgroundResource(markerBg);
    	}
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 1, true));
        } else {
            tvContent.setText("" + Utils.formatNumber(e.getVal(), 1, true));
        }
    }

    @Override
    public float getXOffset() {
        return -(getWidth() / 2);//水平居中
    }

    @Override
    public float getYOffset() {
        return -getHeight(); // 让markerView居于被选择的数据之上
    }
}
