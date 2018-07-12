package com.sws.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sws.common.AjaxUtil;
import com.sws.common.baseAction.BaseAction;
import com.sws.dao.StaffInfoDao;
import com.sws.model.ScheduleTemplate;
import com.sws.service.ScheduleService;
import com.sys.core.util.JsonUtils;
import com.sys.core.util.bean.Page;

public class ScheduleAction extends BaseAction<ScheduleTemplate>{

	private Logger log = LoggerFactory.getLogger(AppNewsAction.class);
	private static final long serialVersionUID = -7139943602278810004L;
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
    private StaffInfoDao staffInfoDao;
	private String startHm;
	private String endHm;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	private SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm"); 
	
	public String list(){
		return PAGE;
	}
	
	@SuppressWarnings("unchecked")
	public void listAjax() throws ParseException{
		page = scheduleService.page(getCommSession().getOrg(),queryEntity, _page, rowNum, sortname,sortorder);
		/*for(RoleInfo info:(List<RoleInfo>)page.getResult()){
		}*/
		page = new Page(_page, rowNum, page.getResult(),  page.getTotal());
		
		for(ScheduleTemplate scheduleTemple : (List<ScheduleTemplate>)page.getResult()){
			scheduleTemple.setsHm(sdf1.format(scheduleTemple.getStartHm()));
			scheduleTemple.seteHm(sdf1.format(scheduleTemple.getEndHm()));
		}
		AjaxUtil.ajaxWrite(JsonUtils.object2Json(page));
	}
	
	
	public String showDialog(){
		if(entity!=null){
			if (null != entity.getId()) { //修改界面
				entity=scheduleService.getById(entity.getId());
				startHm = sdf1.format(entity.getStartHm());
				endHm = sdf1.format(entity.getEndHm());
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
					Date startTime = sdf.parse("2011-01-11 "+startHm); 
					Date endTime = sdf.parse("2011-01-11 "+endHm);
					entity.setStartHm(startTime);
					entity.setEndHm(endTime);
					entity.setUpdateTime(new Date());
					scheduleService.update(entity);
				}else{					 
					Date startTime = sdf.parse("2011-01-11 "+startHm); 
					Date endTime = sdf.parse("2011-01-11 "+endHm);
					entity.setStartHm(startTime);
					entity.setEndHm(endTime);
					scheduleService.save(entity);
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
			scheduleService.deleteAll(ids);
		}catch(Exception ex){
			log.error(ex.getMessage());
			success = false;
			msg = ex.getMessage();
		}
		log.info("delete data use time:"+(System.currentTimeMillis()-begin)+"ms");
		AjaxUtil.ajaxWrite(success,msg);
		return SUCCESS;
	}

	public String getStartHm() {
		return startHm;
	}

	public void setStartHm(String startHm) {
		this.startHm = startHm;
	}

	public String getEndHm() {
		return endHm;
	}

	public void setEndHm(String endHm) {
		this.endHm = endHm;
	}


}
