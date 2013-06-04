package com.hesha.bean;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommentsData {
	private ArrayList<Comment> other_comment;
	private ArrayList<Comment> expert_comment;
	public ArrayList<Comment> getOther_comment() {
		return other_comment;
	}
	public void setOther_comment(ArrayList<Comment> other_comment) {
		this.other_comment = other_comment;
	}
	public ArrayList<Comment> getExpert_comment() {
		return expert_comment;
	}
	public void setExpert_comment(ArrayList<Comment> expert_comment) {
		this.expert_comment = expert_comment;
	}
	
}
