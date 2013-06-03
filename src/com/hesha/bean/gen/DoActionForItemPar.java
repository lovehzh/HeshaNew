package com.hesha.bean.gen;

public class DoActionForItemPar {
	private String token;
	private int item_id;
	private int item_type;
	private int action_type;    //1:收藏(预留) 2:喜欢 
	private int add_flag;       //1:增加 2:移除
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getItem_id() {
		return item_id;
	}
	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}
	public int getItem_type() {
		return item_type;
	}
	public void setItem_type(int item_type) {
		this.item_type = item_type;
	}
	public int getAction_type() {
		return action_type;
	}
	public void setAction_type(int action_type) {
		this.action_type = action_type;
	}
	public int getAdd_flag() {
		return add_flag;
	}
	public void setAdd_flag(int add_flag) {
		this.add_flag = add_flag;
	}
	
	
}
