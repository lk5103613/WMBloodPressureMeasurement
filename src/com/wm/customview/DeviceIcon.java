package com.wm.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wm.entity.TypeEnum;
import com.wm.wmbloodpressuremeasurement.R;

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

	public void setType(TypeEnum type) {
		switch (type) {
		case TURGOSCOPE:
			setImageResource(R.drawable.turgoscope_icon);
			break;
		case GLUCOMETER:
			setImageResource(R.drawable.glucometer_icon);
			break;
		default:
			break;
		}
	}

}
