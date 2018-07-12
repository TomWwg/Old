package com.sws.service;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.UserRole;
import com.sys.core.util.bean.Page;



public interface UserRoleService {
	public UserRole getById(Long id);
	public void save(UserRole info);
	public void update(UserRole info);
	public void deleteAll(String ids);
	public Page page(QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);

}
