package com.hesha.bean.knowledge;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Article category
 * @author hezhenhua
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ArticleCat implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ac_id;
	private int p_id;
	private String ac_name;
	
	public int getAc_id() {
		return ac_id;
	}

	public void setAc_id(int ac_id) {
		this.ac_id = ac_id;
	}

	public int getP_id() {
		return p_id;
	}

	public void setP_id(int p_id) {
		this.p_id = p_id;
	}

	public String getAc_name() {
		return ac_name;
	}

	public void setAc_name(String ac_name) {
		this.ac_name = ac_name;
	}

	@Override
	public String toString() {
		return ac_name;
	}
}
