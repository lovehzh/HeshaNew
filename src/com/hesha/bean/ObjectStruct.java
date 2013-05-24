package com.hesha.bean;

public class ObjectStruct extends BaseStruct{
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + " " +  data.toString();
	}
	
}
