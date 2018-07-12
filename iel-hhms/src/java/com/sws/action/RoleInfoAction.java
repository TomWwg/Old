package com.sws.action;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sws.common.AjaxUtil;
import com.sws.common.baseAction.BaseAction;
import com.sws.common.until.StringUtil;
import com.sws.dao.RoleInfoDao;
import com.sws.model.RoleInfo;
import com.sws.service.RoleInfoService;
import com.sys.core.util.JsonUtils;
import com.sys.core.util.bean.Page;

public class RoleInfoAction extends BaseAction<RoleInfo> {
	
	private static final long serialVersionUID = -2868312613376378663L;
	private Logger log = LoggerFactory.getLogger(RoleInfoAction.class);
	@Autowired
	private RoleInfoService roleInfoService;
	@Autowired
    private RoleInfoDao roleInfoDao;
	private String menuIds;
	/**
	 * 进入类表
	 * 
	 */
	public String list() {
		return PAGE;
	}
	/**
	 * 列表显示
	 * @return
	 */
	public void listAjax() {
		page = roleInfoService.page(queryEntity, _page, rowNum, sortname,sortorder);
		page = new Page(_page, rowNum, page.getResult(),  page.getTotal());
		AjaxUtil.ajaxWrite(JsonUtils.object2Json(page));
	}
	
	/**
	 * 进入添加或者修改界面
	 * @return
	 */
	public String showDialog(){
		if(entity!=null){
			if (null != entity.getId()) {
				entity=roleInfoService.getById(entity.getId());
			}
		}
		return "showDialog";
	}
	/**
	 * 添加或者修改提交
	 * @return
	 */
	public String saveForm(){
		long begin = System.currentTimeMillis();
		try {
			if (entity != null) {
				if(entity.getId()!=null){
					entity.setUpdateTime(new Date());
					roleInfoService.update(entity);
				}else{
					roleInfoService.save(entity);
				}
			} 
		} catch (Exception ex) {
			log.error(ex.getMessage());
			success = false;
			msg = ex.getMessage();
		}
		log.info("save form role time:" + (System.currentTimeMillis() - begin)
				+ "ms");
		AjaxUtil.ajaxWrite(success, msg);
		return SUCCESS;
	}

	/**
	 * 删除
	 * @return
	 */
	public String delData(){
		long begin = System.currentTimeMillis();
		try {
			roleInfoService.deleteAll(ids);
		}catch(Exception ex){
			log.error(ex.getMessage());
			success = false;
			msg = ex.getMessage();
		}
		log.info("delete data use time:"+(System.currentTimeMillis()-begin)+"ms");
		AjaxUtil.ajaxWrite(success,msg);
		return SUCCESS;
	}
	
	
	
	/**
	 * 生成菜单树
	 * @return
	 */
	public String getMuneTree() {
		if(StringUtil.isNotBlank(ids)){
			treeNode = roleInfoService.getMuneTree(Long.valueOf(ids));
		}
		return TREE;
	}
	/**
	 * 进入权限非配界面
	 * @return
	 */
	public String assignMenuDialog(){
		return "assignMenuDialog";
	}
	/**
	 * 权限分配
	 * @return
	 */
	public String assignMenu(){
		long begin = System.currentTimeMillis();
		try {
			if(StringUtil.isNotBlank(menuIds)){
				if(StringUtil.isNotBlank(ids)){
					RoleInfo roleInfo = roleInfoService.getById(Long.valueOf(ids));
					roleInfo.setMenuIds(menuIds);
					roleInfoService.update(roleInfo);  //更新菜单ids
				}
			}
		}catch(Exception ex){
			log.error(ex.getMessage());
			success = false;
			msg = ex.getMessage();
		}
		log.info("assign menu use time:"+(System.currentTimeMillis()-begin)+"ms");
		AjaxUtil.ajaxWrite(success,msg);
		return SUCCESS;
	}
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	

	
}
