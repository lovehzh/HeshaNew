package com.hesha.bean;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

/**
 * 
 Item公共元素
"item_image": ["http://123.jpg",  "http://123.jpg", ]  	//元素图片
"item_des": "",   	                                     //元素内容(预留)
"item_id": "132",
"item_type": "1",    	        			                 // 1:普通图片 2：单品(达人点评)3：产品
"item_name": "试酒室,美女服务员",		                     //标题  (产品中文名)
"like_num": "11", 	                      			     //喜欢数目
"collect_num": "1",	                  			         //收藏数目
"comment_num": "11",                			             //评论数目
“creation_date”:”213438834324”  			                 // GMT
“update_date”:”213438834324”			                     // GMT
“status”   	：”1/0”  
"collection_type_id": "1"                          		 //专辑类型id(预留)
"collection_id": "18506",              	   	   	         //专辑Id(预留)                               		//0:审核未通过 1：审核通过
 *
 */

@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "item_type")
@JsonSubTypes({
	@Type(value = PhotoItem.class, name = "1"),
    @Type(value = SubjectItem.class, name = "2"),
    @Type(value = LinkItem.class, name = "3") })
@JsonIgnoreProperties(ignoreUnknown=true)
public class BaseItem {
	private ArrayList<ImageBean> item_image;
	private String item_des;
	private int item_id;
	private int item_type;  	       
	private String item_name;
	private int like_num;
	private int collect_num;                
	private int comment_num;              
	private long creation_date;			
	private long update_date;
	private int status; 
	private int collection_type_id;                        		
	private int collection_id;
	
	
	public ArrayList<ImageBean> getItem_image() {
		return item_image;
	}
	public void setItem_image(ArrayList<ImageBean> item_image) {
		this.item_image = item_image;
	}
	public String getItem_des() {
		return item_des;
	}
	public void setItem_des(String item_des) {
		this.item_des = item_des;
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
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public int getLike_num() {
		return like_num;
	}
	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}
	public int getCollect_num() {
		return collect_num;
	}
	public void setCollect_num(int collect_num) {
		this.collect_num = collect_num;
	}
	public int getComment_num() {
		return comment_num;
	}
	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
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
	public int getCollection_type_id() {
		return collection_type_id;
	}
	public void setCollection_type_id(int collection_type_id) {
		this.collection_type_id = collection_type_id;
	}
	public int getCollection_id() {
		return collection_id;
	}
	public void setCollection_id(int collection_id) {
		this.collection_id = collection_id;
	}
	
	
}
