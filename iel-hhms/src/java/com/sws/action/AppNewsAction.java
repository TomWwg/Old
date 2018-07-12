package com.sws.action;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sws.common.AjaxUtil;
import com.sws.common.baseAction.BaseAction;
import com.sws.model.AppNews;
import com.sws.service.AppNewsService;
import com.sys.core.util.JsonUtils;
import com.sys.core.util.bean.Page;

public class AppNewsAction extends BaseAction<AppNews>{

	private Logger log = LoggerFactory.getLogger(AppNewsAction.class);
	private static final long serialVersionUID = 2827956228898193501L;
	@Autowired
	private AppNewsService appNewsService;
	
	
	public String list(){
		return PAGE;
	}
	
	
	public void listAjax(){
		page = appNewsService.page(getCommSession().getOrg(),queryEntity, _page, rowNum, sortname,sortorder);
		/*for(RoleInfo info:(List<RoleInfo>)page.getResult()){
		}*/
		page = new Page(_page, rowNum, page.getResult(),  page.getTotal());
		AjaxUtil.ajaxWrite(JsonUtils.object2Json(page));
	}
	
	
	public String showDialog(){
		if(entity!=null){
			if (null != entity.getId()) { //修改界面
				entity=appNewsService.getById(entity.getId());
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
					appNewsService.update(entity);
				}else{
					appNewsService.save(entity);
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
			appNewsService.deleteAll(ids);
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
