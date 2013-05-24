package com.hesha.bean;

import java.util.ArrayList;

public class ArrayStruct extends BaseStruct{
	private ArrayList<Object> data;

	public ArrayList<Object> getData() {
		return data;
	}

	public void setData(ArrayList<Object> data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() +" " + data.toString();
	}
}
