package com.sws.dao;

import java.util.List;

import com.gk.extend.hibernate.dao.IEntityDao;
import com.sws.model.DeviceInfo;
import com.sws.model.GroupTree;


public interface DeviceInfoDao extends IEntityDao<DeviceInfo> {
	public DeviceInfo findByNoType(String no, String type);

	public List<DeviceInfo> findByDepart(List<String> manageDepart, String org);

	/**
	 * 通过科室Id查找设备信息
	 * @param departId
	 * @return
	 * @author wwg
	 */
	List<DeviceInfo> findByDepartId(Long departId);
	
	/**
	 * 查询全部的设备信息
	 * @return
	 * @author wwg
	 */
	List<DeviceInfo> findAll();
	
	/**
	 * 根据节点的ID查询该节点信息
	 * @param id
	 * @return
	 * @author wwg
	 */
	List<GroupTree> findByTreeId(Integer id);
	
	/**
	 * 根据staffIds查找对应的device信息
	 * @param staffIds
	 * @return
	 * @author wwg
	 */
	List<DeviceInfo> findByStaffId(List<Long> staffIds);

	/**
	 * 通过category查询对应的RFID
	 * @param category
	 * @return
	 */
	List<String> getRFIDByStaffIds(List<Long> staffIds);
}
