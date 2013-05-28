package com.hesha.bean;

import java.util.ArrayList;


public class CollectionDetailStruct extends BaseStruct{
	private ArrayList<CollectionInfoAndItems> data;

	public ArrayList<CollectionInfoAndItems> getData() {
		return data;
	}

	public void setData(ArrayList<CollectionInfoAndItems> data) {
		this.data = data;
	}
	
}
