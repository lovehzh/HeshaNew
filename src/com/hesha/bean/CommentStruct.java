package com.hesha.bean;

import java.util.ArrayList;

public class CommentStruct extends BaseStruct{
	private CommentsData data;
	private ArrayList<Comment> comments;

	public CommentsData getData() {
		return data;
	}

	public void setData(CommentsData data) {
		this.data = data;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
	
	
	
}
