
package com.wm.customview;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.MarkerView;
import com.github.mikephil.charting.utils.Utils;
import com.wm.activity.R;

/**
 * �Զ���MarkerView
 * 
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource, int marker) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.tvContent);
        findViewById(R.id.marker_container).setBackgroundResource(marker);
    }

    
    /* (non-Javadoc)
     * @see com.github.mikephil.charting.utils.MarkerView#refreshContent(com.github.mikephil.charting.data.Entry, int)
     * 
     * ÿ���ػ� markerview��ʱ�����ã� �����û���������
     */
    @Override
    public void refreshContent(Entry e, int dataSetIndex) {

        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {

            tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));
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