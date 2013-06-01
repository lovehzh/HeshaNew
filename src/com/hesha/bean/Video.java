package com.hesha.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
“video_url“:"http://123.jpg",			//视频地址
“video_image”:"http://123.jpg“		    //缩略图
**/
@JsonIgnoreProperties(ignoreUnknown=true)
public class Video implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String video_url;
	private String video_image;
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public String getVideo_image() {
		return video_image;
	}
	public void setVideo_image(String video_image) {
		this.video_image = video_image;
	}
	
	

}
