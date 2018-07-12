package com.sws.service;

import java.util.List;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.DeviceInfo;
import com.sys.core.util.bean.Page;



public interface DeviceInfoService {
	public DeviceInfo getById(Long id);
	public void save(DeviceInfo info);
	public void update(DeviceInfo info);
	public void deleteAll(String ids);
	public Page page(String departIds,QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
	public DeviceInfo findByNoType(String no, String type);
	public Page pageByDLevel(List<String> manageDepart,String departIds,QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
	
	/**
	 * 通过departId查询对应的deviceInfos
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
	com.sws.common.entity.GroupTree findByTreeId(Integer id);
}
