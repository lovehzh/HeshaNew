package com.hesha.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 评论：
  "comment_id":"2",                     	         // 点评的id
  "comment _content":"这酒味道不错",       	 	 //点评内容
  "creation_date":"123458796311",                 //点评时间
  “status”   	：”1/0”                           //0:审核未通过 1：审核通过
  "user_info":{ 用户数据(基本数据结构 }		         // 用户信息
  “expert_video”:{ 视频基本结构}			         // 达人视频点评
  “sub_comments”:[{评论(基本结构)},{..}]            //子评论点
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Comment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int comment_id;         
	private String  comment_content;
	private long creation_date;
	private int  status;
	private User user_info;
	private Video expert_video;
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
	public User getUser_info() {
		return user_info;
	}
	public void setUser_info(User user_info) {
		this.user_info = user_info;
	}
	public Video getExpert_video() {
		return expert_video;
	}
	public void setExpert_video(Video expert_video) {
		this.expert_video = expert_video;
	}
	
	
}
