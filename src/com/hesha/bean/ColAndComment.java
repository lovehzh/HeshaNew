package com.hesha.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ColAndComment {
	private Collection collection_info;
	private Comment comment;
	public Collection getCollection_info() {
		return collection_info;
	}
	public void setCollection_info(Collection collection_info) {
		this.collection_info = collection_info;
	}
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	
}
