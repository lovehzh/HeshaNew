package com.hesha.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BaseStruct {
	private String success;
	private String error_code;
	private String error_des;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_des() {
		return error_des;
	}
	public void setError_des(String error_des) {
		this.error_des = error_des;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return success + " " + error_code + " " + error_des;
	}
	
}
