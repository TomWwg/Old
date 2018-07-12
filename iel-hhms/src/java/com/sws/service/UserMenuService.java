package com.sws.service;

import java.util.List;

import com.sws.model.MenuInfo;
import com.sws.model.StaffInfo;

public interface UserMenuService {
	public List<MenuInfo> getUserMenu(StaffInfo staffInfo);

}
