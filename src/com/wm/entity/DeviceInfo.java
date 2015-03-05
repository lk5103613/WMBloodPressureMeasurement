package com.wm.entity;

public class DeviceInfo {
	
	public static final String INTENT_TYPE = "type";
	public static final String TYPE_BP = "BLOOD_PRESSURE";//ÑªÑ¹
	public static final String TYPE_BS = "BLOOD_SUGAR";//ÑªÌÇ
	public static final String TYPE_FH  = "FETAL_HEART";//Ì¥ÐÄÒÇ
	
	public String type;
	public String name;
	
	public DeviceInfo(String type, String name) {
		super();
		this.type = type;
		this.name = name;
	}
	
}
