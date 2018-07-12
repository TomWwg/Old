package com.sws.service;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.AppNews;
import com.sws.model.NoticeInfo;
import com.sys.core.util.bean.Page;

public interface AppNewsService {
	public AppNews getById(Long id);
	public void save(AppNews appNews);
	public void update(AppNews appNews);
	public void deleteAll(String ids);
	public Page page(String departIds,QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
}
