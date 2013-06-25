package com.hesha.bean.choice;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hesha.bean.BaseItem;
@JsonIgnoreProperties(ignoreUnknown=true)
public class FilterResultStruct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int count;
	private ArrayList<BaseItem> subjects;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public ArrayList<BaseItem> getSubjects() {
		return subjects;
	}
	public void setSubjects(ArrayList<BaseItem> subjects) {
		this.subjects = subjects;
	}
	
	
}
