package com.sws.dao;

import java.util.List;

import com.gk.extend.hibernate.dao.IEntityDao;
import com.sws.model.UserInfo;


public interface UserInfoDao extends IEntityDao<UserInfo> {

	List<UserInfo> getByUserName(String userName);
	
}
