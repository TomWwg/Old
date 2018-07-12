 package com.sws.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.sws.common.AjaxUtil;
import com.sws.common.baseAction.BaseAction;
import com.sws.common.entity.DepartLevel;
import com.sws.common.entity.XmlBo;
import com.sws.common.statics.SysStatics;
import com.sws.common.until.CommUtil;
import com.sws.dao.UserInfoDao;
import com.sws.model.DeviceInfo;
import com.sws.model.GroupTree;
import com.sws.model.StaffInfo;
import com.sws.model.UserInfo;
import com.sws.service.DeviceInfoService;
import com.sws.service.StaffInfoService;
import com.sys.core.util.JsonUtils;
import com.sys.core.util.StringUtils;
import com.sys.core.util.bean.Page;

public class DeviceInfoAction extends BaseAction<DeviceInfo> {
	
	private static final long serialVersionUID = -2868312613376378663L;
	private Logger log = LoggerFactory.getLogger(DeviceInfoAction.class);
	@Autowired
	private DeviceInfoService deviceInfoService;
	private Integer typeLevel;
	List<XmlBo> xmlList = new ArrayList<XmlBo>();
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
    private UserInfoDao userInfoDao;
	private String addType;
	private String deviceType;
	private Integer count;
	/***********************************树的操作********************************/
	/**
	 * 进入病床、水洗区等
	 * @return
	 */
	public String addBedTree() {
		if (null != groupTree) {
			parentGroup = groupTreeService.getById(groupTree.getParentId());
		}
		return "addBedTree";
	}
	/**
	 * 保存病床、水洗区等
	 * @return
	 */
	public String saveBedTree() {
		String name="";
		GroupTree bedTree;
		List<GroupTree> bedTreeList = new ArrayList<GroupTree>();
		try {
			// 新增保存
			if (CommUtil.isNullOrEmpty(groupTree.getId())) {
				if(groupTree.getAddType()!=null){
					if(groupTree.getAddType().equals("1")){//批量
						if(groupTree.getCount()!=null){
							for(Integer i=1;i<=groupTree.getCount();i++){
								bedTree=new GroupTree();
								if(!StringUtils.isBlank(groupTree.getDeviceType())){
									switch(Integer.valueOf(groupTree.getDeviceType())){
									case 1:
										name="病床"+i.toString();
										break;
									case 2:
										name="水洗区"+i.toString();
										break;
									case 3:
										name="洁净区"+i.toString();
										break;
									case 4:
										name="污染区"+i.toString();
										break;
									case 5:
										name="走廊区"+i.toString();
										break;
									}
								}
								bedTree.setName(name);
								bedTree.setParentId(groupTree.getParentId());
								bedTree.setType(groupTree.getType());
								bedTree.setStatus((long) 0);
								bedTreeList.add(bedTree);
							}
							groupTreeService.saveAll(bedTreeList);
						}else{
							msg = "保存失败：数量不能为空！";
							success = false;
							AjaxUtil.ajaxWrite(success,msg);
							return ERROR;
						}
					}else{ 	//单个添加
						if(!StringUtils.isBlank(groupTree.getName())){
							groupTree.setStatus((long) 0);
							groupTreeService.save(groupTree);
						}else{
							msg = "保存失败：名称不能为空！";
							success = false;
							AjaxUtil.ajaxWrite(success,msg);
							return ERROR;
						}
					}
				}
			}
			return SUCCESS;
		}
		catch (Exception e) {
			log.error(e.getMessage());
			msg = "保存组织信息失败！";
			success = false;
			AjaxUtil.ajaxWrite(success,msg);
			return ERROR;
		}
	}
	/**
	 * 添加组织树页面
	 * @return
	 */
	public String selectGroupTree() {
		
		if (null != groupTree) {
			// 修改的话Id不为空
			if (null != groupTree.getId()) {
				groupTree = groupTreeService.getById(groupTree.getId());
			}
			parentGroup = groupTreeService.getById(groupTree.getParentId());
		}
		ActionContext.getContext().put("typeLevel",typeLevel);
		return "selectGroupTree";
	}
	
	
	/**
	 * 修改组织树页面
	 * @return
	 */
	public String updateSelectGroupTree() {
		DepartLevel dl=new DepartLevel();
		if (null != groupTree) {
			// 修改的话Id不为空
			if (null != groupTree.getId()) {
				groupTree = groupTreeService.getById(groupTree.getId());
				dl.setDepartLevel(String.valueOf(groupTree.getDepartLevel()));
				dl.setDepartLevelName(SysStatics.departLevelMap.get(String.valueOf(groupTree.getDepartLevel())));
			}
			parentGroup = groupTreeService.getById(groupTree.getParentId());
		}
		
		ActionContext.getContext().put("typeLevel",typeLevel);
		ActionContext.getContext().put("dl",dl);
		return "updateSelectGroupTree";
	}

	/**
	 * 保存分组信息
	 * @return
	 */
	public String saveGroupTree() {
		try {
			// 新增保存
			if (CommUtil.isNullOrEmpty(groupTree.getId())) {
				groupTree.setStatus((long) 0);
				groupTreeService.save(groupTree);
			}
			// 修改保存
			else {
				GroupTree gTree = groupTreeService.getById(groupTree.getId());
				gTree.setName(groupTree.getName());
				gTree.setRemark(groupTree.getRemark());
				gTree.setParentId(groupTree.getParentId());
				gTree.setDepartLevel(groupTree.getDepartLevel());
				groupTreeService.update(gTree);
			}
			return SUCCESS;
		}
		catch (Exception e) {
			log.error(e.getMessage());
			msg = "保存组织信息失败！";
			success = false;
			AjaxUtil.ajaxWrite(success,msg);
			return ERROR;
		}
	}



	/**
	 * 删除
	 * @return
	 */
	public String deleteGroupTree() {
		long begin = System.currentTimeMillis();
		try {
			if(ids!=null){
				List<DeviceInfo>  deviceList = deviceInfoDao.findBy("groupTree.id", Long.valueOf(ids));
				if(deviceList!=null&&deviceList.size()>0){
					throw new Exception("删除失败，请先删除其下属的设备信息！");
				}
				List<StaffInfo>  staffList = staffInfoDao.findBy("groupTree.id", Long.valueOf(ids));
				if(staffList!=null&&staffList.size()>0){
					throw new Exception("删除失败，请先删除其下属的人员信息！");
				}
				List<UserInfo>  userList = userInfoDao.findBy("groupTree.id", Long.valueOf(ids));
				if(userList!=null&&userList.size()>0){
					throw new Exception("删除失败，请先删除其下属的用户信息！");
				}
			}
			groupTreeService.deleteAll(ids);
		}catch(Exception ex){
			log.error(ex.getMessage());
			success = false;
			msg = ex.getMessage();
		}
		log.info("delete data use time:"+(System.currentTimeMillis()-begin)+"ms");
		AjaxUtil.ajaxWrite(success,msg);
		return SUCCESS;
	}
	/***********************************树的操作********************************/
	/**
	 * 部门配置
	 * @author yxnne
	 */
	public String department() {
		ActionContext.getContext().put("deviceTypeMap", SysStatics.deviceTypeMap);
		return "department";
	}
	
	/**
	 * 进入设备预警列表
	 */
	public String queryAlarmList(){
		ActionContext.getContext().put("deviceTypeMap", SysStatics.deviceTypeMap);
		return "queryAlarmList";
	}
	/**
	 * 设备预警列表显示
	 */
	@SuppressWarnings("unchecked")
	public void queryAlarmListAjax(){
		page = deviceInfoService.pageByDLevel(getManageDepart(),getCommSession().getOrg(),queryEntity, _page, rowNum, sortname,sortorder);
		page = new Page(_page, rowNum, page.getResult(),  page.getTotal());
		for(DeviceInfo info:(List<DeviceInfo>)page.getResult()){
			if(info.getDeviceStatus()!=null){
				if(info.getDeviceStatus()==0){
					info.setDeviceStatusName("正常");
				}else if(info.getDeviceStatus()==1){
					info.setDeviceStatusName("未启用");
				}else if(info.getDeviceStatus()==2){
					info.setDeviceStatusName("欠压");
				}else if(info.getDeviceStatus()==3){
					info.setDeviceStatusName("通讯故障");
				}
			}
			info.setShowName(getShowName(info));
			info.setTypeName(SysStatics.deviceTypeMap.get(info.getType()));
		}

		AjaxUtil.ajaxWrite(JsonUtils.object2Json(page));
	}
	/**
	 * 进入列表
	 * 
	 */
	public String list() {
		ActionContext.getContext().put("deviceTypeMap", SysStatics.deviceTypeMap);
		return PAGE;
	}
	
	/**
	 * 得到用户部门ID
	 */
	public void getUserDepartId(){
		String userDepartId= getCommSession().getOrg();
		AjaxUtil.ajaxWrite(userDepartId);
	}
	
	//根据设备类型显示
	public String getShowName(DeviceInfo info){
		StringBuffer sb = new StringBuffer("");
		if(info.getType().equals("10")||info.getType().equals("29")){ 
		    if(info.getRoomId()!=null){
	            groupTree = groupTreeService.getById(info.getRoomId());
	            sb.append(groupTree.getName());
	        }
		}else{
		    if(info.getDepartId()!=null){
	            groupTree = groupTreeService.getById(info.getDepartId());
	            sb.append(groupTree.getName());
	        }
		    if(info.getRoomId()!=null){
	            groupTree = groupTreeService.getById(info.getRoomId());
	            sb.append("-"+groupTree.getName());
	        }
		}
		if(info.getBedId()!=null){
			groupTree = groupTreeService.getById(info.getBedId());
			sb.append("-"+groupTree.getName());
		}
		if(info.getStaffId()!=null){
			StaffInfo staff = staffInfoDao.findUniqueBy("id", info.getStaffId());
			if(staff!=null){
				sb.append("-"+staff.getName());
			}
		}
		return sb.toString();
	}
	/**
	 * 列表显示
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void listAjax() {
		page = deviceInfoService.pageByDLevel(getManageDepart(),getCommSession().getOrg(),queryEntity, _page, rowNum, sortname,sortorder);
		page = new Page(_page, rowNum, page.getResult(),  page.getTotal());
		for(DeviceInfo info:(List<DeviceInfo>)page.getResult()){
			info.setShowName(getDeviceLocation(info));
			info.setTypeName(SysStatics.deviceTypeMap.get(info.getType()));
		}
		AjaxUtil.ajaxWrite(JsonUtils.object2Json(page));
	}
	
	private String getDeviceLocation(DeviceInfo info){
		String deviceLocation="";
		 if(info!=null){
				if(info.getDepartId()!=null){
					deviceLocation+=getGroupTreeNameByID(info.getDepartId());
				}
				if(info.getRoomId()!=null){
					deviceLocation+="-"+getGroupTreeNameByID(info.getRoomId());
				}
				if(info.getBedId()!=null){
					deviceLocation+="-"+getGroupTreeNameByID(info.getBedId());
				}
				if(info.getStaffId()!=null){
					StaffInfo staff = staffInfoDao.findUniqueBy("id", info.getStaffId());
					if(staff!=null){
						deviceLocation += "-"+staff.getName();
					}
				}
			}else{
				deviceLocation="该设备尚未添加";
			}
		
		return deviceLocation;
	}
	private XmlBo addXmlBo(String value,String name){
		XmlBo xmlBo = new XmlBo();
		xmlBo.setName(name);
		xmlBo.setValue(value);
		return xmlBo;
	}
	/**
	 * 进入添加或者修改界面：胸牌添加时，关联的医生只能是未添加胸牌的医生；修改时，只能关联自己以及未添加胸牌的医生
	 * @return
	 */
	public String showDialog(){
		boolean flag=true;
		if (entity != null) {
			if (null != entity.getId()) { // 修改界面
				entity = deviceInfoService.getById(entity.getId());
				if (entity.getGroupTree() != null
						&& entity.getGroupTree().getId() != null) {
					setGroupTreeId(entity.getGroupTree().getId().toString()); // 把组织id返回到页面
					if (entity.getStaffId() != null) { // 胸牌添加
						setTypeLevel(1);
					}
				}
			}
		}
		if (StringUtils.isNotBlank(groupTreeId)) {
			groupTree = groupTreeService.getById(Long.valueOf(groupTreeId));
			if (groupTree.getType() != null) {
				List<StaffInfo> staffList = staffInfoDao.findBy("groupTree.id", Long.valueOf(groupTreeId));
				List<DeviceInfo> rfidList = deviceInfoDao.findBy("type", "40");//胸牌
				if (groupTree.getType().equals(0)) { // 科室
					if (entity != null&&null != entity.getId()) {//修改界面增加自己
						if(entity.getStaffId()!=null){
							StaffInfo sInfo = staffInfoDao.findUniqueBy("id", entity.getStaffId());
							if(sInfo==null){
							}else{
								xmlList.add(addXmlBo(sInfo.getId().toString(),sInfo.getName()));
							}
						}
					}
					for (StaffInfo staff : staffList) {
						flag=true;
						for (DeviceInfo rfid : rfidList) {
							if(rfid.getStaffId()!=null&&staff.getId().equals(rfid.getStaffId())){
								flag=false;
								break;
							}
						}
						if(flag){
							xmlList.add(addXmlBo(staff.getId().toString(),staff.getName()));// 人员信息Id
						}
					}
					StaffInfo fInfo=new StaffInfo();
					fInfo.setId(-1L);
					fInfo.setName("缺省");
					xmlList.add(addXmlBo(fInfo.getId().toString(),fInfo.getName()));//用于暂不关联用户时的缺省值
				} else if (groupTree.getType().equals(2)) {// 病床
					xmlList.add(addXmlBo("10", "床信号识别器"));
					xmlList.add(addXmlBo("29", "液瓶感应器"));
				} else { // 病房
					xmlList.add(addXmlBo("38", "无线接入点"));
					xmlList.add(addXmlBo("09", "门外发射器"));
					xmlList.add(addXmlBo("0a", "门内发射器"));
					xmlList.add(addXmlBo("20", "无菌区识别器"));
					xmlList.add(addXmlBo("10", "病床区识别器"));
					xmlList.add(addXmlBo("29", "液瓶感应器"));
					xmlList.add(addXmlBo("18", "无菌区识别器"));
					xmlList.add(addXmlBo("2b", "外门液瓶识别器"));
					xmlList.add(addXmlBo("48", "仪器识别器"));
				}
			}
			ActionContext.getContext().put("typeList", xmlList);
		}
		return "showDialog";
	}

	
	/**
	 * 添加或者修改提交
	 * @return
	 */
	public String saveForm(){
		long begin = System.currentTimeMillis();
		DeviceInfo info =null,oldInfo = null;
		groupTree = groupTreeService.getById(Long.valueOf(groupTreeId));
		try {
			if(entity!=null){
				entity.setGroupTree(groupTree);
				entity.getGroupTree().setId(Long.valueOf(groupTreeId));
				if(entity.getId()!=null){//修改
					if(groupTree.getType()!=null){
						if(groupTree.getType().equals(0)){ //科室
							entity.setStaffId(Long.valueOf(entity.getType()));
							entity.setType("40");
						}
					}
					if(entity.getNo()!=null&&entity.getType()!=null){
						oldInfo = deviceInfoService.getById(entity.getId());
						if(!oldInfo.getNo().equals(entity.getNo())){//设备编号已改变
							info = deviceInfoService.findByNoType(entity.getNo(), entity.getType());
							if(info!=null){
								this.addFieldError("no", "改设备编号已存在!");
								throw new Exception("修改失败，设备编号已存在！");	
							}
						}
					}
					entity.setUpdateTime(new Date());
					deviceInfoService.update(entity);
				}else{
					if(groupTree.getType()!=null){
						if(groupTree.getType().equals(0)){ //科室
							entity.setDepartId(Long.valueOf(groupTreeId));
							entity.setStaffId(Long.valueOf(entity.getType()));
							entity.setType("40");
						}else if(groupTree.getType().equals(2)){//病床
							entity.setBedId(Long.valueOf(groupTreeId));
							if(groupTree.getParentId()!=null){
								entity.setRoomId(groupTree.getParentId());
								GroupTree tree = groupTreeService.getById(groupTree.getParentId());
								if(tree!=null){
									entity.setDepartId(tree.getParentId());
								}
							}
						}else{   //病房
							entity.setRoomId(Long.valueOf(groupTreeId));
							if(groupTree.getParentId()!=null){
								entity.setDepartId(groupTree.getParentId());
							}
						}
					}
					if(entity.getNo()!=null&&entity.getType()!=null){
						info = deviceInfoService.findByNoType(entity.getNo(), entity.getType());
						if(info!=null){
							this.addFieldError("no", "该设备编号已存在!");
							throw new Exception("添加失败，设备编号已存在！");	
						}
					}
					entity.setDeviceStatus(0);
					deviceInfoService.save(entity);
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
			deviceInfoService.deleteAll(ids);
		}catch(Exception ex){
			log.error(ex.getMessage());
			success = false;
			msg = ex.getMessage();
		}
		log.info("delete data use time:"+(System.currentTimeMillis()-begin)+"ms");
		AjaxUtil.ajaxWrite(success,msg);
		return SUCCESS;
	}
	//进入添加胸牌的对话框
	public String rfidDialog(){
		return "rfidDialog";
	}
	public String addRfid(){
		long begin = System.currentTimeMillis();
		try {
			if(entity.getStaffId()!=null&&entity.getGroupTree()!=null){
				entity.setType("40");
				entity.setDepartId(entity.getGroupTree().getId());
				if(entity.getNo()!=null&&entity.getType()!=null){
					DeviceInfo info = deviceInfoService.findByNoType(entity.getNo(), entity.getType());
					if(info!=null){
						this.addFieldError("no", "该设备编号已存在!");
						throw new Exception("添加失败，设备编号已存在！");	
					}
				}
				entity.setDeviceStatus(0);
				deviceInfoService.save(entity);
				//更新人员信息表的rfid值
				StaffInfo staffInfo = staffInfoService.getById(entity.getStaffId());
				if(staffInfo!=null){
					staffInfo.setNo(entity.getNo());
					staffInfo.setRfid(entity.getNo());
					staffInfoService.update(staffInfo);
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
	 *  通过id查询带有parentName的groupTree的信息
	 * @return
	 * @author wwg
	 */
	public String findGroupTreeById(){
		Integer gid = Integer.valueOf(groupTreeId); 
		com.sws.common.entity.GroupTree groupTree = deviceInfoService.findByTreeId(gid);
		AjaxUtil.ajaxWrite(JsonUtils.object2Json(groupTree));
		return SUCCESS;
	}
	
	public Integer getTypeLevel() {
		return typeLevel;
	}
	public void setTypeLevel(Integer typeLevel) {
		this.typeLevel = typeLevel;
	}
	public String getAddType() {
		return addType;
	}
	public void setAddType(String addType) {
		this.addType = addType;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
}
