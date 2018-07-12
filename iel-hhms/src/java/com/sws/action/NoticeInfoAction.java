 package com.sws.action;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sws.common.AjaxUtil;
import com.sws.common.baseAction.BaseAction;
import com.sws.model.NoticeInfo;
import com.sws.service.NoticeInfoService;
import com.sys.core.util.JsonUtils;
import com.sys.core.util.bean.Page;

public class NoticeInfoAction extends BaseAction<NoticeInfo> {
	
	private static final long serialVersionUID = -2868312613376378663L;
	private Logger log = LoggerFactory.getLogger(NoticeInfoAction.class);
	@Autowired
	private NoticeInfoService noticeInfoService;
	/**
	 * 进入列表
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
		page = noticeInfoService.page(queryEntity, _page, rowNum, sortname,sortorder);
		page = new Page(_page, rowNum, page.getResult(),  page.getTotal());
		AjaxUtil.ajaxWrite(JsonUtils.object2Json(page));
	}
	
	/**
	 * 进入添加或者修改界面
	 * @return
	 */
	public String showDialog(){
		if(entity!=null){
			if (null != entity.getId()) { //修改界面
				entity=noticeInfoService.getById(entity.getId());
			}
		}
		return "showDialog";
	}
	/**
	 * 查看界面
	 * @return
	 */
	public String showNotice(){
		if(entity!=null){
			if (null != entity.getId()) { //修改界面
				entity=noticeInfoService.getById(entity.getId());
			}
		}
		return "showNotice";
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
					noticeInfoService.update(entity);
				}else{
					noticeInfoService.save(entity);
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
			noticeInfoService.deleteAll(ids);
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
