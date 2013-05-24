package com.hesha.bean;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 专辑单品元素（subject）
   "price": "¥196.00",       	                         //产品价格
   “price_range”: "¥16.00－¥96.00",                    	//价格区间
   “en_name”:” Luis Felipe Edwards Pupilla”     	  		//产品英文名
   “year”:”2011”                       	                //产品年份
   “wanted_num”:”1”      	  	                        //想喝的人数
   “drank_num”:”1”         	  	                        //喝过的人数
   “category”:”白葡萄酒”                      	            // 酒类别名称
   “category_id”:”类别id”      	                        	// 酒类别ID(预留)
   “introduction_des”:”品牌\:埃德华兹酒园  智利 规格750ml”	//商品介绍  
   “introduction_image”:”http://1.jpg”                   //商品介绍图片 
   “refer_items”:[“商品”, ....]  	                        	// 关联的产品实体
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class SubjectItem extends BaseItem{
	private String price;
	private String price_range;
	private String en_name;
	private String year;
	private int wanted_num;      
	private int drank_num;
	private String category;
	private int category_id;  
	private String introduction_des;
	private String introduction_image;
	private ArrayList<LinkItem>   refer_items;
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrice_range() {
		return price_range;
	}
	public void setPrice_range(String price_range) {
		this.price_range = price_range;
	}
	public String getEn_name() {
		return en_name;
	}
	public void setEn_name(String en_name) {
		this.en_name = en_name;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getWanted_num() {
		return wanted_num;
	}
	public void setWanted_num(int wanted_num) {
		this.wanted_num = wanted_num;
	}
	public int getDrank_num() {
		return drank_num;
	}
	public void setDrank_num(int drank_num) {
		this.drank_num = drank_num;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public String getIntroduction_des() {
		return introduction_des;
	}
	public void setIntroduction_des(String introduction_des) {
		this.introduction_des = introduction_des;
	}
	public String getIntroduction_image() {
		return introduction_image;
	}
	public void setIntroduction_image(String introduction_image) {
		this.introduction_image = introduction_image;
	}
	public ArrayList<LinkItem> getRefer_items() {
		return refer_items;
	}
	public void setRefer_items(ArrayList<LinkItem> refer_items) {
		this.refer_items = refer_items;
	}
	
	
}
