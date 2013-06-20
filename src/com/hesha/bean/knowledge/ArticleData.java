package com.hesha.bean.knowledge;


import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hesha.bean.BaseStruct;
@JsonIgnoreProperties(ignoreUnknown=true)
public class ArticleData extends BaseStruct{
	private ArrayList<Article> articles;

	public ArrayList<Article> getArticles() {
		return articles;
	}

	public void setArticles(ArrayList<Article> articles) {
		this.articles = articles;
	}
	
}
