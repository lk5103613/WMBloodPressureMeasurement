package com.wm.entity;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.wm.utils.ASCIIData;
import com.wm.utils.DataConvertUtils;
import com.wm.utils.DateUtil;

public class BPResult {

	public static int HEART_RATE_STATE_NORMAL = 0;
	public static int HEART_RATE_STATE_NOT_NORMAL = 1;
	
	private final static int IDEAL_DBP_MIN = 60;
	private final static int IDEAL_DBP_MAX = 80;
	private final static int IDEAL_SBP_MIN = 90;
	private final static int IDEAL_SBP_MAX = 120;
	
	private final static int NORMAL_DBP_MIN = 60;
	private final static int NORMAL_DBP_MAX = 90;
	private final static int NORMAL_SBP_MIN = 100;
	private final static int NORMAL_SBP_MAX = 130;
	
	private final static int PRE_HBP_DBP_MIN = 84;
	private final static int PRE_HBP_DBP_MAX = 90;
	private final static int PRE_HBP_SBP_MIN = 129;
	private final static int PRE_HBP_SBP_MAX = 140;
	
	private final static int HBP_DBP = 90;
	private final static int HBP_SBP = 140;
	
	private final static int LBP_DBP = 60;
	private final static int LBP_SBP = 90;
	
	private final static int CRITICAL_HBP_DBP_MIN = 90;
	private final static int CRITICAL_HBP_DBP_MAX = 95;
	private final static int CRITICAL_HBP_SBP_MIN = 140;
	private final static int CRITICAL_HBP_SBP_MAX = 160;
	
	private final static String BP_IDEAL = "理想血压";
	private final static String BP_NORMAL = "正常血压";
	private final static String BP_PRE_HBP = "高血压前期";
	private final static String BP_HBP = "高血压";
	private final static String BP_LBP = "低血压";
	private final static String BP_CRI_HBP = "临界高血压";

	public int id;
	@Expose public String userCard;
	@Expose public float dbp; //舒张压 
	@Expose public float sbp; //收缩压
	@Expose public float pulse; //心率 
	public int heartRateState = HEART_RATE_STATE_NORMAL;
	public long date;
	@Expose public String measureTime;
	@Expose public String remarks;
	public int status;//0 为提交， 1已提交
	public String bpResult;

	public BPResult() {
	}

	public BPResult(float szValue, float ssValue) {
		this.dbp = szValue;
		this.sbp = ssValue;
	}

	public BPResult(float szValue, float ssValue, long date) {
		this(szValue, ssValue);
		this.date = date;
	}

	public BPResult(int id, float szValue, float ssValue, long date) {
		this(szValue, ssValue, date);
		this.id = id;
	}

	public BPResult(String userCard, float dbp, float sbp, float pulse,
			String measureTime, String remarks) {
		super();
		this.userCard = userCard;
		this.dbp = dbp;
		this.sbp = sbp;
		this.pulse = pulse;
		this.measureTime = measureTime;
		this.remarks = remarks;
	}
	public BPResult(int id, String userCard, float dbp, float sbp, float pulse,
			long date, String remarks) {
		this.id = id;
		this.userCard = userCard;
		this.dbp = dbp;
		this.sbp = sbp;
		this.pulse = pulse;
		this.date = date;
		this.measureTime = DateUtil.getFormatDate(DateUtil.DATA_FORMAT, date);
		this.id = id;
		
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
		this.date = new Date().getTime();
		this.remarks = "v0.0.1";
		this.userCard = "330310198611010909";
		this.bpResult = getResult(dbp, sbp);
	}
	
	private boolean isValueWithin(int min, int max, float value) {
		if(min == -1) {
			if(value <= max)
				return true;
			return false;
		}
		if(max == -1) {
			if(value >= min) 
				return true;
			return false;
		}
		if(value >= min && value <= max) 
			return true;
		return false;
	}
	
	private String getResult(float dbp, float sbp) {
		if(isValueWithin(IDEAL_DBP_MIN, IDEAL_DBP_MAX, dbp) 
				&& isValueWithin(IDEAL_SBP_MIN, IDEAL_SBP_MAX, sbp)) 
			return BP_IDEAL;
		if(isValueWithin(NORMAL_DBP_MIN, NORMAL_DBP_MAX, dbp) 
				&& isValueWithin(NORMAL_SBP_MIN, NORMAL_SBP_MAX, sbp)) 
			return BP_NORMAL;
		if(isValueWithin(PRE_HBP_DBP_MIN, PRE_HBP_DBP_MAX, dbp) 
				&& isValueWithin(PRE_HBP_SBP_MIN, PRE_HBP_SBP_MAX, sbp))
			return BP_PRE_HBP;
		if(isValueWithin(HBP_DBP, -1, dbp) && isValueWithin(HBP_SBP, -1, sbp)) 
			return BP_HBP;
		if(isValueWithin(-1, LBP_DBP, dbp) && isValueWithin(-1, LBP_SBP, sbp))
			return BP_LBP;
		if(isValueWithin(CRITICAL_HBP_DBP_MIN, CRITICAL_HBP_DBP_MAX, dbp) 
				&& isValueWithin(CRITICAL_HBP_SBP_MIN, CRITICAL_HBP_SBP_MAX, sbp))
			return BP_CRI_HBP;
		return null;
	}

}
