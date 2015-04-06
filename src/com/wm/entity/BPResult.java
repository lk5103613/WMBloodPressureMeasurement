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
	
	public int id;
	@Expose public String userCard;
	@Expose public float dbp; //����ѹ 
	@Expose public float sbp; //����ѹ
	@Expose public float pulse; //���� 
	public int heartRateState = HEART_RATE_STATE_NORMAL;
	public long date;
	@Expose public String measureTime;
	@Expose public String remarks;
	public int status;//0 Ϊ�ύ�� 1���ύ
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
		this.bpResult = getAdvise(dbp, sbp) +"Ѫѹ";
	}
	
	/** ����ѹ�ٽ�ֵ */
	public static final int[] SBPS = {
		90, 105, 120, 130, 140
	};

	/** ����ѹ�ٽ�ֵ */
	public static final int[] DBPS = {
		60, 65, 80, 85, 90
	};

	/** ���� */
	public static final String[] ADVISES = {
		"��", "ƫ��", "����", "����", "ƫ��", "��"
	};


	/**
	 * ��ȡ����
	 * @param sbp ����ѹ
	 * @param dbp ����ѹ
	 * @return ����
	 */
	public static String getAdvise(float sbp, float dbp) {
		for (int i = 0; i < SBPS.length; i++) {
			if ((sbp <= SBPS[i]) || (dbp <= DBPS[i])) {
				return ADVISES[i];
			}
		}
		return ADVISES[ADVISES.length - 1];
	}

}
