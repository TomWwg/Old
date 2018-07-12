package com.sws.service;

import java.util.List;

import com.gk.essh.util.tree.TreeNode;
import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.MenuInfo;
import com.sws.model.RoleInfo;
import com.sys.core.util.bean.Page;



public interface RoleInfoService {
	public RoleInfo getById(Long id);
	public void save(RoleInfo info);
	public void update(RoleInfo info);
	public void deleteAll(String ids);
	public Page page(QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
	public TreeNode getMuneTree(Long roleId);
	public List<MenuInfo> getMuneList(Long userId);

}
