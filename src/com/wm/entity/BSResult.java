package com.wm.entity;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.wm.utils.ASCIIData;
import com.wm.utils.DataConvertUtils;

public class BSResult {
	
	public final static byte[] COMMAND_GET_CURRENT = {(byte)0x7b, (byte)0x44, (byte)0x23, (byte)0xe7, (byte)0x7d};
	public final static byte[] COMMAND_GET_REC_NO = {(byte)0x7b, (byte)0x4c, (byte)0x23, (byte)0xef, (byte)0x7d};
	
	public int id;
	@Expose public String userCard;
	@Expose public String bg;
	public long date;
	@Expose public String measureTime;
	@Expose public String remarks;
	public int status;
	
	public BSResult(String bsValue, long date) {
		super();
		this.bg = bsValue;
		this.date = date;
	}

	public BSResult(int id, String bsValue, long date) {
		this(bsValue, date);
		this.id = id;
	}

	public BSResult(String userCard, String bg, long measureTime, String remarks) {
		super();
		this.userCard = userCard;
		this.bg = bg;
		this.date = measureTime;
		this.remarks = remarks;
	}
	
	public BSResult(int id, String userCard, String bg, long date, String remarks, String measureTime) {
		super();
		this.id = id;
		this.userCard = userCard;
		this.bg = bg;
		this.date = date;
		this.remarks = remarks;
		this.measureTime = measureTime;
	}
	
	public BSResult(String[] datas) {
		Map<String, String> AsciiTable = ASCIIData.getASCIITable();
		StringBuilder sb = new StringBuilder();
		StringBuilder timeSb = new StringBuilder();
		for(int i=2; i<6; i++) {
			sb.append(AsciiTable.get(datas[i].toUpperCase(Locale.getDefault())));
		}
		for(int i=11; i<18; i++) {
			timeSb.append(AsciiTable.get(datas[i].toUpperCase(Locale.getDefault())));
		}
		int mgPerDlValue = Integer.valueOf(sb.toString());
		String value = DataConvertUtils.format(mgPerDlValue * 1.0 / 18, 1);
		this.bg = value;
		this.date = new Date().getTime();
		this.userCard = "220502198611010011";
		this.remarks = "test";
		this.measureTime = timeSb.toString();
	}
	
	public void getMeasureTime(String[] datas) {
		Map<String, String> AsciiTable = ASCIIData.getASCIITable();
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<3; i++) {
			sb.append(AsciiTable.get(datas[i].toUpperCase(Locale.getDefault())));
		}
		this.measureTime += sb.toString();
	}
	
}
