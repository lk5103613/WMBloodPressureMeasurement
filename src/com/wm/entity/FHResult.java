package com.wm.entity;

import java.util.List;

public class FHResult {
	private List<Float> fhValues;
	private String date;
	
	public FHResult(){}
	public FHResult(List<Float> fhValues, String date){
		this.fhValues = fhValues;
		this.date = date;
	}
	public List<Float> getFhValues() {
		return fhValues;
	}
	public void setFhValues(List<Float> fhValues) {
		this.fhValues = fhValues;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	

}
