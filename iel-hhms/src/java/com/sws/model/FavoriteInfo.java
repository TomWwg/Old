package com.sws.model;

import com.gk.extend.hibernate.entity.BaseEntity;

public class FavoriteInfo extends BaseEntity {
	/**
	 * 收藏夹
	 */
	private static final long serialVersionUID = 1844345216569746917L;
	private String name;
	private String href;
	private String imgUrl;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
}
