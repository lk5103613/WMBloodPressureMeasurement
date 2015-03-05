package com.wm.wmbloodpressuremeasurement;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.wm.entity.DeviceInfo;

public class BloodHistoryActivity extends ActionBarActivity implements OnChartValueSelectedListener{

	@InjectView(R.id.blood_history_bar)
	Toolbar mToolbar;

	@InjectView(R.id.blood_history_chart)
	LineChart mChart;
	
	int[] mColors = ColorTemplate.VORDIPLOM_COLORS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blood_history);
		ButterKnife.inject(this);

		mToolbar.setTitle(getResources().getString(R.string.turgoscope));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);

		//chart
		mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawYValues(false);
        mChart.setDrawGridBackground(false);
        mChart.setDescription("");
        mChart.setGridColor(getResources().getColor(R.color.light_black));
        mChart.setBorderColor(getResources().getColor(R.color.light_black));
        addEmptyData();
        mChart.invalidate();
        
		addDataSet(0);
		addDataSet(1);
	}
	
	@OnClick(R.id.btn_begin_check)
	public void beginCheck(View v) {
		Intent intent = new Intent(BloodHistoryActivity.this,
				ResultActivity.class);
		intent.putExtra(DeviceInfo.INTENT_TYPE, DeviceInfo.TYPE_GLUCOMETER);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.scale_fade_out);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.slide_out_to_right);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in,
					R.anim.slide_out_to_right);
		}
	}
	
	 private void addEmptyData() {
	        
	        // create 30 x-vals
	        String[] xVals = new String[30];

	        for (int i = 0; i < 30; i++)
	            xVals[i] = "" + i;

	        // create a chartdata object that contains only the x-axis labels (no entries or datasets)
	        LineData data = new LineData(xVals);
	        mChart.setData(data);
	        mChart.invalidate();
	    }
	 
	private void addDataSet(int colorPosition) {

        LineData data = mChart.getData();
        
        if(data != null) {

            int count = (data.getDataSetCount() + 1);

            // create 10 y-vals
            ArrayList<Entry> yVals = new ArrayList<Entry>();

            for (int i = 0; i < data.getXValCount(); i++)
                yVals.add(new Entry((float) (Math.random() * 50f) + 50f * count, i));

            LineDataSet set = new LineDataSet(yVals, "DataSet " + count);
            set.setLineWidth(2.5f);
            set.setCircleSize(3f);
            

            int color = mColors[count % mColors.length];

            set.setColor(color);
            set.setCircleColor(color);
            set.setHighLightColor(color);

            data.addDataSet(set);
            mChart.notifyDataSetChanged();
            mChart.invalidate();   
        }
    }

	@Override
    public void onValueSelected(Entry e, int dataSetIndex) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

}
