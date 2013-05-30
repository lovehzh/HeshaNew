package com.hesha.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 图片
“image“:"http://123.jpg",	    //大图
“thumb”:"http://123.jpg“			//缩略图
“image_width”:”210” 				//大图长
“image_heigth”:”200”				//大图宽
“thumb_width”:”12” 				//缩略图长
“thumb_heigth”:”4”				//缩略图宽
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ImageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String image;
	private String thumb;
	private int image_width;
	private int image_heigth;
	private int thumb_width;
	private int thumb_heigth;
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public int getImage_width() {
		return image_width;
	}
	public void setImage_width(int image_width) {
		this.image_width = image_width;
	}
	public int getImage_heigth() {
		return image_heigth;
	}
	public void setImage_heigth(int image_heigth) {
		this.image_heigth = image_heigth;
	}
	public int getThumb_width() {
		return thumb_width;
	}
	public void setThumb_width(int thumb_width) {
		this.thumb_width = thumb_width;
	}
	public int getThumb_heigth() {
		return thumb_heigth;
	}
	public void setThumb_heigth(int thumb_heigth) {
		this.thumb_heigth = thumb_heigth;
	}
	
	
}
