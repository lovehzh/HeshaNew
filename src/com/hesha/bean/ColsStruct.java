package com.hesha.bean;

import java.util.ArrayList;

/**
 * data结构里是Collection集合
 * @author zhenhua
 *
 */
public class ColsStruct extends BaseStruct{
	private ArrayList<Collection> data;

	public ArrayList<Collection> getData() {
		return data;
	}

	public void setData(ArrayList<Collection> data) {
		this.data = data;
	}
	
}
