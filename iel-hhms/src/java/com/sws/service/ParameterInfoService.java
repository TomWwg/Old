package com.sws.service;

import java.util.List;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.ParameterInfo;
import com.sys.core.util.bean.Page;



public interface ParameterInfoService {
	public ParameterInfo getById(Long id);
	public void save(ParameterInfo info);
	public void update(ParameterInfo info);
	public void deleteAll(String ids);
	public Page page(QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
	boolean saveOrUpdateAll(List <ParameterInfo> list);
	public ParameterInfo findByTypeAndKey(Integer type, String key);
	public List<ParameterInfo> findByType(Integer type);
}
