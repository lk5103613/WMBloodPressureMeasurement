package com.wm.entity;

public class BSResult {

	public int id;
	public int bsValue;
	public long date;
	
	public BSResult(int bsValue, long date) {
		super();
		this.bsValue = bsValue;
		this.date = date;
	}

	public BSResult(int id, int bsValue, long date) {
		this(bsValue, date);
		this.id = id;
	}
	
}
