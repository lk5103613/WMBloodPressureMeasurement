
package com.wm.customview;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.MarkerView;
import com.github.mikephil.charting.utils.Utils;
import com.wm.activity.R;

/**
 * �Զ���MarkerView
 * 
 */
public class LineMarkerView extends MarkerView {

    private TextView tvContent;
    private LineChart mChart;

    public LineMarkerView(Context context,LineChart chart, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
        this.mChart = chart;
    }

    
    /* (non-Javadoc)
     * @see com.github.mikephil.charting.utils.MarkerView#refreshContent(com.github.mikephil.charting.data.Entry, int)
     * 
     * ÿ���ػ� markerview��ʱ�����ã� �����û���������
     */
    @Override
    public void refreshContent(Entry e, int dataSetIndex) {
    	
    	int dataSetCount = mChart.getData().getDataSetCount();
    	String values = "";
    	for (int i = 0; i < dataSetCount-1; i++) {
    		Entry tempE = mChart.getData().getDataSetByIndex(i).getEntryForXIndex(e.getXIndex());
            values =values+ "," + Utils.formatNumber(tempE.getVal(), 0, true);
    	}
    	
    	tvContent.setText(values.subSequence(1, values.length()));
    	
    	int color = mChart.getData().getDataSetByIndex(dataSetIndex).getColor();
    	if(color == getResources().getColor(R.color.fragment_bg)){
    		tvContent.setText("");
    	} 
    }

    @Override
    public float getXOffset() {                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
        return -(getWidth() / 2);//ˮƽ����
    }

    @Override
    public float getYOffset() {
        return -getHeight(); // ��markerView���ڱ�ѡ�������֮��
    }
}
