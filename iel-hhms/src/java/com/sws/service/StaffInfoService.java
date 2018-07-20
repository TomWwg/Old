package com.sws.service;

import java.util.List;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.StaffInfo;
import com.sys.core.util.bean.Page;



public interface StaffInfoService {
	public StaffInfo getById(Long id);
	public void save(StaffInfo info);
	public void update(StaffInfo info);
	public void deleteAll(String ids);
	public Page page(String departIds,QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
	public Page page(List<Long> staffIds,int start, int limit,String sortname,String sortorder);
	public Page pageByDLevel(List<String> manageDepart, String departIds,
			QueryEntity queryEntity, int start, int limit, String sortname,
			String sortorder);
	
	/**
	 * 通过员工编号查找员工（为空时可能有多个员工，所以用list）
	 * @param staffNo
	 * @return
	 * @author wwg
	 */
	List<StaffInfo> findByStaffNo(String staffNo);

	/**
	 * 通过rfid从device_info,staff_info,wash_hand_log查询员工姓名
	 * @param rfid
	 * @return
	 */
	String findNameByRfid(String rfid);
	
	/**
	 * 通过GroupId查找该科室下的人员信息
	 * @return
	 */
	List<StaffInfo> findByGroupId(Long groupId);
}
