package com.hesha.bean.knowledge;


import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hesha.bean.BaseStruct;
@JsonIgnoreProperties(ignoreUnknown=true)
public class ArticleCatData extends BaseStruct {
	private ArrayList<ArticleCat> acs;

	public ArrayList<ArticleCat> getAcs() {
		return acs;
	}

	public void setAcs(ArrayList<ArticleCat> acs) {
		this.acs = acs;
	}
	
}
