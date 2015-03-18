package com.wm.entity;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.wm.utils.ASCIIData;
import com.wm.utils.DataConvertUtils;

public class BPResult {

	public static int HEART_RATE_STATE_NORMAL = 0;
	public static int HEART_RATE_STATE_NOT_NORMAL = 1;

	public int id;
	@Expose public String userCard;
	@Expose public float dbp; //舒张压 
	@Expose public float sbp; //收缩压
	@Expose public float pulse; //心率 
	public int heartRateState = HEART_RATE_STATE_NORMAL;
	@Expose public long measureTime;
	@Expose public String remarks;
	public int status;//0 为提交， 1已提交

	public BPResult() {
	}

	public BPResult(float szValue, float ssValue) {
		this.dbp = szValue;
		this.sbp = ssValue;
	}

	public BPResult(float szValue, float ssValue, long date) {
		this(szValue, ssValue);
		this.measureTime = date;
	}

	public BPResult(int id, float szValue, float ssValue, long date) {
		this(szValue, ssValue, date);
		this.id = id;
	}

	public BPResult(String userCard, float dbp, float sbp, float pulse,
			long measureTime, String remarks) {
		super();
		this.userCard = userCard;
		this.dbp = dbp;
		this.sbp = sbp;
		this.pulse = pulse;
		this.measureTime = measureTime;
		this.remarks = remarks;
	}

	public BPResult(String result) {
		Map<String, String> AsciiTable = ASCIIData.getASCIITable();
		Locale defloc = Locale.getDefault();
		result = result.toUpperCase(defloc);
		String[] items = result.split(" ");
		sbp = Float.valueOf(DataConvertUtils.hexToDecimal(AsciiTable
				.get(items[4]) + AsciiTable.get(items[3])));
		dbp = Float.valueOf(DataConvertUtils.hexToDecimal(AsciiTable
				.get(items[6]) + AsciiTable.get(items[5])));
		pulse = Float.valueOf(DataConvertUtils.hexToDecimal(AsciiTable
				.get(items[8]) + AsciiTable.get(items[7])));
		heartRateState = HEART_RATE_STATE_NORMAL;
		if (items[9].toLowerCase(defloc).equals("55")) {
			heartRateState = HEART_RATE_STATE_NORMAL;
		} else if (items[9].toLowerCase(defloc).equals("aa")) {
			heartRateState = HEART_RATE_STATE_NOT_NORMAL;
		}
		this.measureTime = new Date().getTime();
		this.remarks = "v0.0.1";
		this.userCard = "330310198611010909";
	}

}
