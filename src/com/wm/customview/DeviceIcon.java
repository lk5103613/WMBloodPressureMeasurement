package com.wm.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wm.activity.R;
import com.wm.entity.DeviceInfo;

public class DeviceIcon extends ImageView {

	public DeviceIcon(Context context) {
		super(context);
	}

	public DeviceIcon(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DeviceIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public void setType(String type) {
		if(type.equals(DeviceInfo.TYPE_BS)) {
			setImageResource(R.drawable.glucometer_icon);
		} else if(type.equals(DeviceInfo.TYPE_BP)) {
			setImageResource(R.drawable.turgoscope_icon);
		}
	}

}
