package com.hesha.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 用户数据
	 "user_id": "12",                        //用户id
	 "user_name": "酒庄大全",              	//用户名
     "user_avatar": "http://1.jpg ",         //用户头像
     “expert_type”:”1,3,4”                    //达人 1:评酒 2:知识 3:专辑 4:配菜
	 **/
	
	private int user_id;
	private String user_name;
	private String user_avatar;
	private int expert_type;
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_avatar() {
		return user_avatar;
	}
	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}
	public int getExpert_type() {
		return expert_type;
	}
	public void setExpert_type(int expert_type) {
		this.expert_type = expert_type;
	}
	
	
}
