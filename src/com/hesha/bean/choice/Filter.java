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
	private int filter_id;
	private String name;
	private ArrayList<Value> value;
	public int getFilter_id() {
		return filter_id;
	}
	public void setFilter_id(int filter_id) {
		this.filter_id = filter_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Value> getValue() {
		return value;
	}
	public void setValue(ArrayList<Value> value) {
		this.value = value;
	}
	
}
