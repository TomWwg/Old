package com.sws.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.sws.common.AjaxUtil;
import com.sws.common.baseAction.BaseAction;
import com.sws.common.statics.SysStatics;
import com.sws.dao.StaffInfoDao;
import com.sws.model.GroupTree;
import com.sws.model.StaffInfo;
import com.sws.service.StaffInfoService;
import com.sys.core.util.JsonUtils;
import com.sys.core.util.bean.Page;

public class StaffInfoAction extends BaseAction<StaffInfo> {
	
	private static final long serialVersionUID = -2868312613376378663L;
	private Logger log = LoggerFactory.getLogger(StaffInfoAction.class);
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
    private StaffInfoDao staffInfoDao;
	
	private String getDepartName(Long departId, List<GroupTree> deparList){
		String departName="";
		for(GroupTree info:deparList){
			if(info.getId().equals(departId)){
				if(info.getName()!=null){
					departName=info.getName();
				}
			}
		}
		return departName;
	}
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
	@SuppressWarnings("unchecked")
	public void listAjax() {
		Map<String, String> typeMap = getJobTypeMap();
		List<GroupTree> departList=groupTreeService.getDepart();
		//page = staffInfoService.page(getCommSession().getOrg(),queryEntity, _page, rowNum, sortname,sortorder);
		page = staffInfoService.pageByDLevel(getManageDepart(),getCommSession().getOrg(),queryEntity, _page, rowNum, sortname,sortorder);
		for(StaffInfo info:(List<StaffInfo>)page.getResult()){
			if(info.getGroupTree()!=null){
				info.setDepartName(getDepartName(info.getGroupTree().getId(),departList));
				if(info.getCategory()!=null){
					info.setCategoryName(typeMap.get(info.getCategory()));
				}
				if(info.getJobTitle()!=null){
					info.setJobTitleName(SysStatics.jobTitleMap.get(info.getJobTitle()));
				}
			}
		}
		page = new Page(_page, rowNum, page.getResult(),  page.getTotal());
		AjaxUtil.ajaxWrite(JsonUtils.object2Json(page));
	}
	
	/**
	 * 进入添加或者修改界面
	 * @return
	 */
	public String showDialog(){
		Map<String, String> typeMap = getJobTypeMap();
		ActionContext.getContext().put("jobTypeMap", typeMap);
		ActionContext.getContext().put("jobTitleMap", SysStatics.jobTitleMap);
		if(entity!=null){
			if (null != entity.getId()) {
				entity=staffInfoService.getById(entity.getId());
				if(entity.getGroupTree()!=null&&entity.getGroupTree().getId()!=null){
					setGroupTreeId(entity.getGroupTree().getId().toString());   
				}
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
		StaffInfo info = null, oldInfo = null;
		GroupTree groupTree = groupTreeService.getById(Long
				.valueOf(groupTreeId));
		try {
			if (entity != null) {
				entity.setGroupTree(groupTree);
				entity.getGroupTree().setId(Long.valueOf(groupTreeId));
				if (entity.getId() != null) {
					if (entity.getNo() != null) {
						oldInfo = staffInfoService.getById(entity.getId());
						if (!entity.getNo().equals("") && !oldInfo.getNo().equals(entity.getNo())) {// 员工编号已改变
							info = staffInfoDao.findUniqueBy("no",
									entity.getNo());
							if (info != null) {
								this.addFieldError("no", "该编号已存在!");
								throw new Exception("修改失败，编号已存在！");
							}
						}
					}
					entity.setUpdateTime(new Date());
					staffInfoService.update(entity);
				} else {
					if (entity.getNo() != null) {
						List<StaffInfo> infos = staffInfoService.findByStaffNo(entity.getNo());
						if (!entity.getNo().equals("") && infos.size() != 0) {
							this.addFieldError("no", "该编号已存在!");
							throw new Exception("添加失败，编号已存在！");
						}
					}
					staffInfoService.save(entity);
				}
			}
				
		}catch(Exception ex){
			log.error(ex.getMessage());
			success = false;
			msg = ex.getMessage();
		}
		log.info("save form use time:"+(System.currentTimeMillis()-begin)+"ms");
		AjaxUtil.ajaxWrite(success,msg);
		return SUCCESS;
	}
	
	/**
	 * 删除
	 * @return
	 */
	public String delData(){
		long begin = System.currentTimeMillis();
		try {
			staffInfoService.deleteAll(ids);
		}catch(Exception ex){
			log.error(ex.getMessage());
			success = false;
			msg = ex.getMessage();
		}
		log.info("delete data use time:"+(System.currentTimeMillis()-begin)+"ms");
		AjaxUtil.ajaxWrite(success,msg);
		return SUCCESS;
	}


	
}
