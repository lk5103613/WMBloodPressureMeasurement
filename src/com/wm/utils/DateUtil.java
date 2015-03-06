package com.wm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	
	public static Date longToDate(long longDate) {
		Date date = new Date(longDate);
		return date;
	}
	
	public static long dateToLong(Date date) {
		return date.getTime();
	}
	
	public static String getFormatDate(String format, Date date) {
		SimpleDateFormat sdf= new SimpleDateFormat(format, Locale.getDefault());
		return sdf.format(date);
	}
	
	public static String getFormatDate(String format, long longDate) {
		return getFormatDate(format, longToDate(longDate));
	}

}
