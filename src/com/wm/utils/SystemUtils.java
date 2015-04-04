package com.wm.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class SystemUtils {

	public static float getDensity(Activity activity) {
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.density;
	}

}
