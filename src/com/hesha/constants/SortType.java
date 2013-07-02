package com.hesha.constants;

public enum SortType {
	//time("时间",1), like("热门",2), price("价格",3), alph("首字符",3);
	TIME(1), LIKE(2), PRICE(3), ALPHABET(4);
	
	private int value;
	private SortType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
