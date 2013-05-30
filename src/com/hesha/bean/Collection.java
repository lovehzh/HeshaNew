package com.hesha.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 专辑信息
"collection_name": "行走在路上",	         	 	//专辑名称
"collection_id": "18506",              	   	   	//专辑Id
"collection_image": "12.jpg"                     //专辑封面
"collection_des":"最爱旅游，走在路上",     		    //专辑描述
"like_num":"11",                             	//喜欢数目
"image_num":"3",                    	            //图片数目
"product_num":"2"                   	            //产品数目
“creation_date”:”213438834324”  	  	  	        //GMT
“update_date”:”213438834324”  	    		        //GMT
“status”   	：”1/0”                              //0:审核未通过 1：审核通过
"user_info":{ 用户数据(基本数据结构 }		        // 用户信息(有可能为空)
**/
@JsonIgnoreProperties(ignoreUnknown=true)
public class Collection implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String collection_name;
	private int collection_id;
	private String collection_image;
	private String collection_des;
	private int like_num;
	private int image_num;
	private int product_num;
	private long creation_date;
	private long update_date;
	private int status;
	private User user_info;
	private ArrayList<BaseItem> items;
	public String getCollection_name() {
		return collection_name;
	}
	public void setCollection_name(String collection_name) {
		this.collection_name = collection_name;
	}
	public int getCollection_id() {
		return collection_id;
	}
	public void setCollection_id(int collection_id) {
		this.collection_id = collection_id;
	}
	public String getCollection_image() {
		return collection_image;
	}
	public void setCollection_image(String collection_image) {
		this.collection_image = collection_image;
	}
	public String getCollection_des() {
		return collection_des;
	}
	public void setCollection_des(String collection_des) {
		this.collection_des = collection_des;
	}
	public int getLike_num() {
		return like_num;
	}
	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}
	public int getImage_num() {
		return image_num;
	}
	public void setImage_num(int image_num) {
		this.image_num = image_num;
	}
	public int getProduct_num() {
		return product_num;
	}
	public void setProduct_num(int product_num) {
		this.product_num = product_num;
	}
	public long getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(long creation_date) {
		this.creation_date = creation_date;
	}
	public long getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(long update_date) {
		this.update_date = update_date;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public User getUser_info() {
		return user_info;
	}
	public void setUser_info(User user_info) {
		this.user_info = user_info;
	}
	public ArrayList<BaseItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<BaseItem> items) {
		this.items = items;
	}
	
}
