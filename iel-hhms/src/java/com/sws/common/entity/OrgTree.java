package com.sws.common.entity;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author WangYabei 2015-10-1 上午12:15:29 
 * @version V1.0
 * @Edit {修改人} 2015-10-1
 */
public class OrgTree {
	
	private List<OrgTree> children  = new ArrayList<OrgTree>();
	String name;
	String id;
	String parentId;
	public List<OrgTree> getChildren() {
		return children;
	}
	public void setChildren(List<OrgTree> children) {
		this.children = children;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}
