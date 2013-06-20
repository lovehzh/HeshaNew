package com.hesha.bean.knowledge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hesha.bean.BaseStruct;
@JsonIgnoreProperties(ignoreUnknown=true)
public class ArticleDetailsData extends BaseStruct{
	private Article article;

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
	
}
