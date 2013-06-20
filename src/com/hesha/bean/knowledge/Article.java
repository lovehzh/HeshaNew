package com.hesha.bean.knowledge;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hesha.bean.ImageBean;
import com.hesha.bean.User;
import com.hesha.utils.Utils;


/**
 * 
 id : 1                                  //文章id
 title : ”高丽雅酒庄（Bodegas Callia)”   	//文章标题（带有英文名）
 creation_date : 3438834324”       	    //创建日期	
 like_num : “200”                        //被喜欢的次数
 image : {图片基本结构}              	    // 图片信息
 “intro”:”文章简介”			            //文章简介
 “user_info:”  {用户数据 基本结构}	        //作者
 content : ”xxxxxx”                      //正文内容，html格式
 ac_info:{文章分类基本结构}		             // 文章分类信息（第二层）
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Article implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private long creation_date;
	private int like_num;
	private ImageBean image;
	private String intro;
	private User user_info;
	private String content;
	private ArticleCat ac_info;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public long getCreation_date() {
		return creation_date;
	}


	public void setCreation_date(long creation_date) {
		this.creation_date = creation_date;
	}


	public int getLike_num() {
		return like_num;
	}


	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}


	public ImageBean getImage() {
		return image;
	}


	public void setImage(ImageBean image) {
		this.image = image;
	}


	public String getIntro() {
		return intro;
	}


	public void setIntro(String intro) {
		this.intro = intro;
	}


	public User getUser_info() {
		return user_info;
	}


	public void setUser_info(User user_info) {
		this.user_info = user_info;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public ArticleCat getAc_info() {
		return ac_info;
	}


	public void setAc_info(ArticleCat ac_info) {
		this.ac_info = ac_info;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Utils.decodeSpecial(title);
	}
}
