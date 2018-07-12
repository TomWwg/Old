package com.sws.common;

import java.util.ArrayList;
import java.util.List;

import com.sws.bo.MenuBo;


public class MenuMaker {
	
	public static List<MenuBo> make(){
			
	   //long menuId, long parentId, String name, String href, int active, int openType, String css, String baseUrl, String img, int type
		List<MenuBo> ms = new ArrayList<MenuBo>();
		MenuBo front = new MenuBo(321, 0, "首页", "goHomeInfo.action", 1, 0, 0);	//显示设备列表registerUI.jsp
		front.setCustomHomeMenuUrl("goHomeInfo.action");
		MenuBo help = new MenuBo(322, 0, "帮助", "module/help/list.action", 2, 0, 0);
		help.setCustomHomeMenuUrl("module/help/list.action");
		MenuBo mb1 = new MenuBo(1, 0, "系统", "module/device/list.action", 1, 0, 0);	//显示设备列表registerUI.jsp
		ms.add(front);
		ms.add(mb1);
		ms.add(help);
		List<MenuBo> secmb = new ArrayList<MenuBo>();
		MenuBo mb2 = new MenuBo(2, 1, "业务管理", "module/device/list.action", 0, 0, 0);
		MenuBo mb3 = new MenuBo(3, 1, "实时状态", "module/device/queryAlarmList.action", 0, 0, 0);
		MenuBo mb4 = new MenuBo(4, 1, "查询统计", "module/washHand/eventList.action", 0, 0, 0);
		MenuBo mb5 = new MenuBo(5, 1, "报表管理", "module/reportManage/goRateByTime.action", 0, 0, 0);
		MenuBo mb6 = new MenuBo(6, 1, "系统管理", "module/user/pageUser.action", 0, 0, 0);
		secmb.add(mb2);
		secmb.add(mb3);
		secmb.add(mb4);
		secmb.add(mb5);
		secmb.add(mb6);
		mb1.setData(secmb);
		List<MenuBo> thrmb2 = new ArrayList<MenuBo>();
		MenuBo mb21 = new MenuBo(21, 2, "设备列表", "module/device/list.action", 0, 0, 0);
		MenuBo mb22 = new MenuBo(22, 2, "职工管理", "module/staff/list.action", 0, 0, 0);
		MenuBo mb23 = new MenuBo(23, 2, "AP设置", "module/apSet/list.action", 0, 0, 0);
		thrmb2.add(mb21);
		thrmb2.add(mb22);
		thrmb2.add(mb23);
		mb2.setData(thrmb2);	
		List<MenuBo> thrmb3 = new ArrayList<MenuBo>();
		MenuBo mb31 = new MenuBo(31, 3, "设备预警", "module/device/queryAlarmList.action", 0, 0, 0);
		MenuBo mb32 = new MenuBo(32, 3, "人员实时位置", "module/location/goStaffLocation.action", 0, 0, 0);
		MenuBo mb33 = new MenuBo(33, 3, "所有人员位置", "module/location/allStaffLocation.action", 0, 0, 0);
		thrmb3.add(mb31);
		thrmb3.add(mb32);
		thrmb3.add(mb33);
		mb3.setData(thrmb3);
		
		List<MenuBo> thrmb4 = new ArrayList<MenuBo>();
		MenuBo mb41 = new MenuBo(41, 4, "事件查询", "module/washHand/eventList.action", 0, 0, 0);
		MenuBo mb42 = new MenuBo(42, 4, "日志查询", "module/signalLog/list.action", 0, 0, 0);
		MenuBo mb43 = new MenuBo(43, 4, "人员依从率统计", "module/washHand/rateByStaffList.action", 0, 0, 0);
		MenuBo mb44 = new MenuBo(44, 4, "人员类别依从率统计", "module/washHand/rateByStaffTypeList.action", 0, 0, 0);
		MenuBo mb45 = new MenuBo(45, 4, "科室依从率统计", "module/washHand/rateByDepartList.action", 0, 0, 0);
		MenuBo mb46 = new MenuBo(46, 4, "事件比较", "module/washHand/eventCompareList.action", 0, 0, 0);
		MenuBo mb47 = new MenuBo(47, 4, "洗手液使用量统计", "module/sanitizer/sanitizerByStaffList.action", 0, 0, 0);
		MenuBo mb48 = new MenuBo(48, 4, "洗手液使用量统计(部门)", "module/sanitizer/sanitizerByDeptList.action", 0, 0, 0);
		thrmb4.add(mb41);
		thrmb4.add(mb42);
		thrmb4.add(mb43);
		thrmb4.add(mb44);
		thrmb4.add(mb45);
		thrmb4.add(mb46);
		thrmb4.add(mb47);
		thrmb4.add(mb48);
		mb4.setData(thrmb4);
		
		List<MenuBo> thrmb5 = new ArrayList<MenuBo>();
		MenuBo mb51 = new MenuBo(51, 5, "时间统计", "module/reportManage/goRateByTime.action", 0, 0, 0);
		MenuBo mb52 = new MenuBo(52, 5, "大屏显示", "module/reportManage/goShowScreen.action", 0, 0, 0);
		MenuBo mb53 = new MenuBo(53, 5, "手卫生统计", "module/reportManage/goWashChart.action", 0, 0, 0);
		thrmb5.add(mb51);
		thrmb5.add(mb52);
		thrmb5.add(mb53);
		mb5.setData(thrmb5);
		
		List<MenuBo> thrmb6 = new ArrayList<MenuBo>();
		MenuBo mb61 = new MenuBo(61, 6, "用户管理", "module/user/pageUser.action", 0, 0, 0);
		MenuBo mb62 = new MenuBo(62, 6, "角色管理", "module/role/list.action", 0, 0, 0);
		MenuBo mb63 = new MenuBo(63, 6, "日志查看", "module/log/logInfoList.action", 0, 0, 0);
		MenuBo mb64 = new MenuBo(64, 6, "公告管理", "module/notice/list.action", 0, 0, 0);
		MenuBo mb65 = new MenuBo(65, 6, "App新闻管理", "module/appNews/list.action", 0, 0, 0);
		MenuBo mb66 = new MenuBo(66, 6, "参数管理", "module/parameter/list.action", 0, 0, 0);
		MenuBo mb67 = new MenuBo(67, 6, "排班模板管理", "module/schedule/list.action", 0, 0, 0);
		MenuBo mb68 = new MenuBo(68, 6, "排班管理", "module/dateTemplate/list.action", 0, 0, 0);
		thrmb6.add(mb61);
		thrmb6.add(mb62);
		thrmb6.add(mb63);
		thrmb6.add(mb64);
		thrmb6.add(mb65);
		thrmb6.add(mb66);
		thrmb6.add(mb67);
		thrmb6.add(mb68);
		mb6.setData(thrmb6);	
		return ms;
	}
	
}