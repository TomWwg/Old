package com.sws.service;

import java.util.List;

import com.gk.essh.util.tree.TreeNode;
import com.sws.model.GroupTree;



public interface GroupTreeService {
	public GroupTree getById(Long id);
	public void save(GroupTree info);
	public void update(GroupTree info);
	public void deleteAll(String ids);
	public TreeNode getTree(List<String> manageDeparts,String departIds, boolean onlyDepart, Boolean isTreeOpen);
	public List<GroupTree> getDepart();
	public void saveAll(List<GroupTree> list);
	public List<GroupTree> getUserDepart();
	public List<String> getManageDeparts();
	
	/**
	 * 得到医院名称（目前的表结构设计只有一家医院，所以不需要返回List<String>）
	 * @return
	 */
	public String getHospitalName();
	
	/**
	 * 根据groupTree的Id得到对应的名字
	 * @return
	 */
	public String getNameById(Long id);

}
