package com.wm.entity;

public class BPResult {
	private float szValue;
	private float ssValue;
	
	public BPResult() {
	}
	
	public BPResult(float szValue, float ssValue) {
		this.szValue = szValue;
		this.ssValue = ssValue;
	}
	
	public float getSzValue() {
		return szValue;
	}
	public void setSzValue(float szValue) {
		this.szValue = szValue;
	}
	public float getSsValue() {
		return ssValue;
	}
	public void setSsValue(float ssValue) {
		this.ssValue = ssValue;
	}

}
