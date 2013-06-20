package com.hesha.bean.choice;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WineCatBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int type_id;
	private String type_name;
	private String image;
	private String des;
	private ArrayList<Intention> intentions;
	private ArrayList<Filter> filters;
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public ArrayList<Intention> getIntentions() {
		return intentions;
	}
	public void setIntentions(ArrayList<Intention> intentions) {
		this.intentions = intentions;
	}
	public ArrayList<Filter> getFilters() {
		return filters;
	}
	public void setFilters(ArrayList<Filter> filters) {
		this.filters = filters;
	}
	
}
