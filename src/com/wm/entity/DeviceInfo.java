package com.wm.entity;

public class DeviceInfo {
	private TypeEnum type;
	private String name;
	
	public DeviceInfo(TypeEnum type, String name) {
		this.type = type;
		this.name = name;
	}

	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
