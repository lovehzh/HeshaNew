package com.hesha.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 专辑类型
  " collection_type_name":"配菜",              	  //专辑类型名称
  " collection_type_id": "1"                       //专辑类型id
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int collection_type_id;
	private String collection_type_name;
	public int getCollection_type_id() {
		return collection_type_id;
	}
	public void setCollection_type_id(int collection_type_id) {
		this.collection_type_id = collection_type_id;
	}
	public String getCollection_type_name() {
		return collection_type_name;
	}
	public void setCollection_type_name(String collection_type_name) {
		this.collection_type_name = collection_type_name;
	}
	
}
