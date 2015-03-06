package com.wm.entity;

public class BPResult {
	
	public int id;
	public float szValue;
	public float ssValue;
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

}
