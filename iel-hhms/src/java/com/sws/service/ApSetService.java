package com.sws.service;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.ApSet;
import com.sys.core.util.bean.Page;



public interface ApSetService {
	public ApSet getById(Long id);
	public void save(ApSet info);
	public void update(ApSet info);
	public void deleteAll(String ids);
	public Page page(QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);

}
