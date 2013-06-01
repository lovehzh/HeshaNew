package com.hesha.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public class ItemDetailData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BaseItem photo;
	private BaseItem link;
	private BaseItem subject;
	private ArrayList<User> like_users;
	private ArrayList<User> recollect_users;
	private ArrayList<Comment> other_comment;
	private ArrayList<Comment> expert_comment;
	private int other_comment_nums;
	private int expert_comment_nums;
	public BaseItem getPhoto() {
		return photo;
	}
	public void setPhoto(BaseItem photo) {
		this.photo = photo;
	}
	public BaseItem getLink() {
		return link;
	}
	public void setLink(BaseItem link) {
		this.link = link;
	}
	public BaseItem getSubject() {
		return subject;
	}
	public void setSubject(BaseItem subject) {
		this.subject = subject;
	}
	public ArrayList<User> getLike_users() {
		return like_users;
	}
	public void setLike_users(ArrayList<User> like_users) {
		this.like_users = like_users;
	}
	public ArrayList<User> getRecollect_users() {
		return recollect_users;
	}
	public void setRecollect_users(ArrayList<User> recollect_users) {
		this.recollect_users = recollect_users;
	}
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
	public int getOther_comment_nums() {
		return other_comment_nums;
	}
	public void setOther_comment_nums(int other_comment_nums) {
		this.other_comment_nums = other_comment_nums;
	}
	public int getExpert_comment_nums() {
		return expert_comment_nums;
	}
	public void setExpert_comment_nums(int expert_comment_nums) {
		this.expert_comment_nums = expert_comment_nums;
	}
	
}
