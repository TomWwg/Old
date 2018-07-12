package com.sws.service;

import java.util.List;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.UserInfo;
import com.sys.core.util.bean.Page;



public interface UserInfoService {
	public UserInfo getById(Long id);
	public void save(UserInfo info);
	public void update(UserInfo info);
	public void deleteAll(String ids);
	public List<UserInfo> getByUserName(String userName);
	public Page page(String departIds,QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
	public UserInfo login(String userName, String password);
	public void changeStatusByIds(String selectIds,Integer status);
	public Page pageByDLevel(List<String> manageDepart, String departIds,
			QueryEntity queryEntity, int start, int limit, String sortname,
			String sortorder);
}
