package com.hesha.bean;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 评论：
  "comment_id":"2",                     	         // 点评的id
  "comment _content":"这酒味道不错",       	 	 //点评内容
  "creation_date":"123458796311",                 //点评时间
  “status”   	：”1/0”                           //0:审核未通过 1：审核通过
  “sub_comments”:｛｝                              //子评论点
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Comment {
	private int comment_id;         
	private String  comment_content;
	private long creation_date;
	private int  status;
	private ArrayList<Comment>  sub_comments;
	
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public long getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(long creation_date) {
		this.creation_date = creation_date;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public ArrayList<Comment> getSub_comments() {
		return sub_comments;
	}
	public void setSub_comments(ArrayList<Comment> sub_comments) {
		this.sub_comments = sub_comments;
	}
	
	
}
