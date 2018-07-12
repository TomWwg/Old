package com.sws.service.imp;

import java.util.List;

import com.sws.dao.MenuInfoDao;
import com.sws.dao.RoleInfoDao;
import com.sws.model.MenuInfo;
import com.sws.model.StaffInfo;
import com.sws.service.UserMenuService;

public class UserMenuServiceImpl implements UserMenuService {
	private RoleInfoDao roleInfoDao;
	private MenuInfoDao menuInfoDao;
	

	@Override
	public List<MenuInfo> getUserMenu(StaffInfo staffInfo) {
		// TODO Auto-generated method stub
		return null;
	}


	public RoleInfoDao getRoleInfoDao() {
		return roleInfoDao;
	}


	public void setRoleInfoDao(RoleInfoDao roleInfoDao) {
		this.roleInfoDao = roleInfoDao;
	}


	public MenuInfoDao getMenuInfoDao() {
		return menuInfoDao;
	}


	public void setMenuInfoDao(MenuInfoDao menuInfoDao) {
		this.menuInfoDao = menuInfoDao;
	}
	

}
