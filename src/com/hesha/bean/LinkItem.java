package com.hesha.bean;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
  专辑商品元素(link)
   "price": "¥196.00",       	                       	//产品价格
   “from_type”:”1”                                       // 1:淘宝 2：天猫
   “product_url”:www.taobao.com/1.html 	                //产品URL
   “area”: "上海",                    	                //地区
   “shop_name”:”也买酒”                  	   	            //商店名称
   “stock”:” 12”        	                       	  	    //剩余数目
   “refer_items”:[“商品”, ....]        	      	        // 关联的产品实体
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class LinkItem extends BaseItem{
	 private String  price;      	             
     private int from_type;                                    
	 private String product_url;
 	 private String area;                
     private String shop_name;
	 private String stock;
 	 private ArrayList<LinkItem> refer_items;
 	 
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getFrom_type() {
		return from_type;
	}
	public void setFrom_type(int from_type) {
		this.from_type = from_type;
	}
	public String getProduct_url() {
		return product_url;
	}
	public void setProduct_url(String product_url) {
		this.product_url = product_url;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public ArrayList<LinkItem> getRefer_items() {
		return refer_items;
	}
	public void setRefer_items(ArrayList<LinkItem> refer_items) {
		this.refer_items = refer_items;
	}
 	 
 	 
}
