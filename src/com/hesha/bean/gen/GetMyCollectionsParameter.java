package com.hesha.bean.gen;

public class GetMyCollectionsParameter {
	private String token;
	private int page_num;
	private int begin_index;
	private int sort_type;       //排序方法1:时间 2:喜欢  
	private int order_type;      //1:增序 0：降序(目前没用到该参数, 排序默认为降序)
	private int type;            //1：自己创建 2：收藏的 3 所有
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getPage_num() {
		return page_num;
	}
	public void setPage_num(int page_num) {
		this.page_num = page_num;
	}
	public int getBegin_index() {
		return begin_index;
	}
	public void setBegin_index(int begin_index) {
		this.begin_index = begin_index;
	}
	public int getSort_type() {
		return sort_type;
	}
	public void setSort_type(int sort_type) {
		this.sort_type = sort_type;
	}
	public int getOrder_type() {
		return order_type;
	}
	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
