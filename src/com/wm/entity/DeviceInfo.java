package com.wm.entity;

public class DeviceInfo {
	
	public static String TYPE_TURGOSCOPE = "TURGOSCOPE";
	public static String TYPE_GLUCOMETER = "GLUCOM_ETER";
	
	public String type;
	public String name;
	
	public DeviceInfo(String type, String name) {
		super();
		this.type = type;
		this.name = name;
	}
	
}
