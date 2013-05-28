package com.hesha.constants;

public interface Constants{
	public static final String SERVER_URL = "http://192.168.1.41:8084/rest/index.php";
	public static final String IMAGE_BASE_URL = "http://192.168.1.41:8084/";
	public static final boolean D = true;
	
	public static final int ITEM_PHOTO = 1;     //普通图片
	public static final int ITEM_SUBJECT = 2;   //单品 
	public static final int ITEM_LINK = 3;      //来自淘宝，天猫等的商品
	
	public static final String GB_SUCCESS = "GB0000000";
	public static final String CONNECTION_TIMED_OUT = "connection_timed_out";
}
