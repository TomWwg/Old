package com.sws.service;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.MenuRole;
import com.sys.core.util.bean.Page;



public interface MenuRoleService {
	public MenuRole getById(Long id);
	public void save(MenuRole info);
	public void update(MenuRole info);
	public void deleteAll(String ids);
	public Page page(QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);

}
