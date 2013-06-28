package com.hesha.bean.choice;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Filter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int fliter_id;
	private String name;
	private ArrayList<Value> values;
	private String curValue;//当前的值 用于显示在list条目中
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Value> getValues() {
		return values;
	}
	public void setValues(ArrayList<Value> values) {
		this.values = values;
	}
	public String getCurValue() {
		return curValue;
	}
	public void setCurValue(String curValue) {
		this.curValue = curValue;
	}
	public int getFliter_id() {
		return fliter_id;
	}
	public void setFliter_id(int fliter_id) {
		this.fliter_id = fliter_id;
	}
	
	
}
