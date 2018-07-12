package com.sws.model;

import java.util.Date;

import com.gk.extend.hibernate.entity.BaseEntity;

public class AppNews extends BaseEntity{


	private static final long serialVersionUID = 2066698272274663172L;
	/**
	 * APP新闻
	 */

	private String title;
	private String content;
	private String image;
	private Date createTime;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	

}
