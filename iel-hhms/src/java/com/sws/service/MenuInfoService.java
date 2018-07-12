package com.sws.service;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.MenuInfo;
import com.sys.core.util.bean.Page;



public interface MenuInfoService {
	public MenuInfo getById(Long id);
	public void save(MenuInfo info);
	public void update(MenuInfo info);
	public void deleteAll(String ids);
	public Page page(QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);

}
