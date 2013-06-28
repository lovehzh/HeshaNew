package com.hesha.bean.choice;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hesha.bean.BaseItem;
@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchResultStruct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int result_nums;
	private ArrayList<BaseItem> items;
	public int getResult_nums() {
		return result_nums;
	}
	public void setResult_nums(int result_nums) {
		this.result_nums = result_nums;
	}
	public ArrayList<BaseItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<BaseItem> items) {
		this.items = items;
	}
	
}
