package com.wm.entity;

public class DeviceInfo {
	
	public static final String INTENT_TYPE = "type";
	public static final String TYPE_BP = "BLOOD_PRESSURE";//Ѫѹ
	public static final String TYPE_BS = "BLOOD_SUGAR";//Ѫ��
	public static final String TYPE_FH  = "FETAL_HEART";//̥����
	
	public String type;
	public String name;
	
	public DeviceInfo(String type, String name) {
		super();
		this.type = type;
		this.name = name;
	}
	
}
