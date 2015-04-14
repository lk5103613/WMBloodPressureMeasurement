
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
    	
    	String value = Utils.formatNumber(e.getVal(), 0, true);
    	
    	tvContent.setText(value);
    	
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
