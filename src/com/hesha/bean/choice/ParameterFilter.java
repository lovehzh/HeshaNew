package com.hesha.bean.choice;

/**
 * 筛选时传递给服务器端的参数
 * @author zhenhua
 *
 */
public class ParameterFilter {
	private int fliter_id;
	private int value_id;
	
	public int getFliter_id() {
		return fliter_id;
	}
	public void setFliter_id(int fliter_id) {
		this.fliter_id = fliter_id;
	}
	public int getValue_id() {
		return value_id;
	}
	public void setValue_id(int value_id) {
		this.value_id = value_id;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return fliter_id + " " + value_id;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof ParameterFilter)) return false;
		ParameterFilter other = (ParameterFilter)o;
		if(other.getFliter_id() == fliter_id && other.getValue_id() == value_id)
		{
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
}
