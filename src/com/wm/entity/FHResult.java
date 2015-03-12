package com.wm.entity;

import java.util.Date;
import java.util.List;

public class FHResult {
	
	public int id;
	public List<Float> fhValues;
	public long date;
	public int status;
	
	public FHResult(){}
	
	public FHResult(List<Float> fhValues) {
		this.fhValues = fhValues;
		this.date = new Date().getTime();
	}
	
	public FHResult(List<Float> fhValues, long date){
		this.fhValues = fhValues;
		this.date = date;
	}
	
	public FHResult(int id, List<Float> fhValues, long date) {
		this(fhValues, date);
		this.id = id;
	}
	
	
}
