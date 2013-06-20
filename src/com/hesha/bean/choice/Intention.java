package com.hesha.bean.choice;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Intention implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int intention_id;
	private String intention_name;
	public int getIntention_id() {
		return intention_id;
	}
	public void setIntention_id(int intention_id) {
		this.intention_id = intention_id;
	}
	public String getIntention_name() {
		return intention_name;
	}
	public void setIntention_name(String intention_name) {
		this.intention_name = intention_name;
	}
	
}
