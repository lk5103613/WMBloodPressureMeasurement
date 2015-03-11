package com.wm.entity;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.wm.utils.ASCIIData;
import com.wm.utils.DataConvertUtils;

public class BPResult {

	public static int HEART_RATE_STATE_NORMAL = 0;
	public static int HEART_RATE_STATE_NOT_NORMAL = 1;

	public int id;
	public float szValue;
	public float ssValue;
	public float heartRate;
	public int heartRateState = HEART_RATE_STATE_NORMAL;
	public long date;

	public BPResult() {
	}

	public BPResult(float szValue, float ssValue) {
		this.szValue = szValue;
		this.ssValue = ssValue;
	}

	public BPResult(float szValue, float ssValue, long date) {
		this(szValue, ssValue);
		this.date = date;
	}

	public BPResult(int id, float szValue, float ssValue, long date) {
		this(szValue, ssValue, date);
		this.id = id;
	}

	public BPResult(String result) {
		Map<String, String> AsciiTable = ASCIIData.getASCIITable();
		Locale defloc = Locale.getDefault();
		result = result.toUpperCase(defloc);
		String[] items = result.split(" ");
		ssValue = Float.valueOf(DataConvertUtils.hexToDecimal(AsciiTable
				.get(items[4]) + AsciiTable.get(items[3])));
		szValue = Float.valueOf(DataConvertUtils.hexToDecimal(AsciiTable
				.get(items[6]) + AsciiTable.get(items[5])));
		heartRate = Float.valueOf(DataConvertUtils.hexToDecimal(AsciiTable
				.get(items[8]) + AsciiTable.get(items[7])));
		heartRateState = HEART_RATE_STATE_NORMAL;
		if (items[9].toLowerCase(defloc).equals("55")) {
			heartRateState = HEART_RATE_STATE_NORMAL;
		} else if (items[9].toLowerCase(defloc).equals("aa")) {
			heartRateState = HEART_RATE_STATE_NOT_NORMAL;
		}
		this.date = new Date().getTime();
	}

}
