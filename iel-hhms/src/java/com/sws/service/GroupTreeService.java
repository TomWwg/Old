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

}
