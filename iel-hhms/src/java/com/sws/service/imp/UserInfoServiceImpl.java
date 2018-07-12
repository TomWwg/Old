package com.sws.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.gk.essh.exception.ProgramException;
import com.gk.extend.hibernate.dao.LicParams;
import com.gk.extend.hibernate.entity.QueryEntity;
import com.gk.extend.hibernate.template.PageHibernateTemplate;
import com.sws.common.statics.SysStatics;
import com.sws.common.until.DateUtils;
import com.sws.common.until.StringUtil;
import com.sws.dao.LogInfoDao;
import com.sws.dao.UserInfoDao;
import com.sws.model.RoleInfo;
import com.sws.model.UserInfo;
import com.sws.service.LogInfoService;
import com.sws.service.UserInfoService;
import com.sys.core.util.CollectionUtils;
import com.sys.core.util.StringUtils;
import com.sys.core.util.bean.Page;

//@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
    
	@Autowired
	private LogInfoDao logInfoDao;
	
	@Autowired
    private UserInfoDao userInfoDao;
	@Autowired
    private LogInfoService logInfoService;
	@Autowired
	private PageHibernateTemplate pageHibernateTemplate;
	@Autowired
	private LicParams licParams;
	@Override
	public UserInfo getById(Long id) {
		return userInfoDao.get(id);
	}

	@Override
	public void save(UserInfo info) {
		userInfoDao.save(info);
	}

	@Override
	public void update(UserInfo info) {
		userInfoDao.update(info);
	}

	@Override
	public void deleteAll(String ids) {
		if (StringUtils.isNotBlank(ids)) {
            List<Long> idList = CollectionUtils.toLongList(ids);
            if (CollectionUtils.isNotEmpty(idList)) {
                for (Long id : idList) {
                	logInfoDao.deleteByUserId(id);
                	userInfoDao.deleteById(id);
                }
            }
        }
	}

	@Override
	public Page page(String departIds,QueryEntity queryEntity, int start, int limit,String sortname,String sortorder) {
		if(StringUtils.isNotBlank(queryEntity.getStr2())){
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserInfo.class);
			String[] userRoles=queryEntity.getStr2().split(",");
			List<RoleInfo> userRoleList=new ArrayList<RoleInfo>();
			RoleInfo roleInfo;
			for(String userRole:userRoles){
				roleInfo=new RoleInfo();
				roleInfo.setId(Long.valueOf(userRole));
				userRoleList.add(roleInfo);
			}
			detachedCriteria.add(Restrictions.in("roleInfo", userRoleList));
			if(StringUtils.isNotBlank(queryEntity.getStr1())){
				detachedCriteria.add(Restrictions.like("name", queryEntity.getStr1(),MatchMode.ANYWHERE));
			}
			if(queryEntity.getInt1()!=null){
				detachedCriteria.add(Restrictions.eq("userStatus", queryEntity.getInt1()));
			}
			if (sortorder.toLowerCase().equals("asc")) {
				detachedCriteria.addOrder(Order.asc(sortname));
			} else {
				detachedCriteria.addOrder(Order.desc(sortname));
			};
			List<Long> groupIds = new ArrayList<Long>();
			if(StringUtils.isNotBlank(queryEntity.getTreeId())&&!queryEntity.getTreeId().equals("1")){
				detachedCriteria.add(Restrictions.eq("groupTree.id", Long.valueOf(queryEntity.getTreeId())));
			}else{
				String[] array = departIds.split("\\*");
				if(!departIds.equals("1")){//非最高级别的管理员
					for(String dId:array){
						groupIds.add(Long.valueOf(dId));
					}
				}
				if(groupIds!=null&&groupIds.size()>0){
					detachedCriteria.add(Restrictions.in("groupTree.id", groupIds));
				}
			}
			Page page = pageHibernateTemplate.pageByDetachedCriteria(detachedCriteria, start, limit);
			return page;
		}
		return null;
		
		
	}
	/**
	 * 检查license证书是否上传，是否过期
	 */
	private boolean checkLicExpire() {
		String expireDateStr = licParams.getExpireDate();
		Date expireDate = null;
		if (!StringUtil.isNullOrEmpty(expireDateStr)) {
			expireDate = DateUtils.str2DateByYMD(expireDateStr);
		} else {
			return false;
		}
		if (expireDate.before(new Date())) {
			return false;
		}
		return true;
	}
	@Override
	public UserInfo login(String userName, String password) {
		int loginCounts = 0;
		UserInfo userInfo = userInfoDao.findUniqueBy("name", userName);
		if (userInfo == null) {
			throw new ProgramException("login.user", new String[] { userName });
		}
		if (!userInfo.getPassword().equals(password)) {
			throw new ProgramException("login.password");
		}
		if (userInfo.getUserStatus()==null||userInfo.getUserStatus() == SysStatics.USER_STATUS_STOP) {
			throw new ProgramException("login.stop");
		}
		if(userInfo.getUserStatus()==SysStatics.USER_STATUS_EXPRIE){
			throw new ProgramException("login.exprie");
		}else{
			if(!checkLicExpire()){
				throw new ProgramException("login.license");
			}
			if(userInfo.getExpireTime()!=null&&!userInfo.getName().equals("admin")){
				if(DateUtils.compareByYMD(new Date(), userInfo.getExpireTime())>0){
					userInfo.setUserStatus(SysStatics.USER_STATUS_EXPRIE);//过期
					update(userInfo);
					throw new ProgramException("login.exprie");
				}
			}
		}
		if(userInfo.getLoginCounts()!=null){
			loginCounts = userInfo.getLoginCounts();	// 更新用户登录时需要更新的信息
		}else{
			loginCounts = 0;
		}
		userInfo.setLastLoginTime(new Date());
		userInfo.setLoginCounts(loginCounts + 1);
		userInfoDao.update(userInfo);
		logInfoService.addLog(SysStatics.OPERATION_LOG_USER_LOGIN, userInfo.getName() + "登入", userInfo);// 写登录日志
		return userInfo;
	}

	@Override
	public void changeStatusByIds(String selectIds,Integer status) {
		List<Long> idList = new ArrayList<Long>();
		String[] idArray = selectIds.split(",");//人员类别名称
		for(String idStr:idArray){
			idList.add(Long.valueOf(idStr));
		}
		List <UserInfo> userList = userInfoDao.findAllEntitysByIds(idList);
		for(UserInfo user:userList){
			user.setUserStatus(status);
		}
		userInfoDao.updateStatus(userList, 0);
	}

	@Override
	public List<UserInfo> getByUserName(String userName) {
		if(StringUtils.isNotBlank(userName)){
			List<UserInfo> userInfos=userInfoDao.getByUserName(userName);
			return userInfos;
		}
		return null;
	}

	@Override
	public Page pageByDLevel(List<String> manageDepart, String departIds,
			QueryEntity queryEntity, int start, int limit, String sortname,
			String sortorder) {
		if(StringUtils.isNotBlank(queryEntity.getStr2())){
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserInfo.class);
			String[] userRoles=queryEntity.getStr2().split(",");
			List<RoleInfo> userRoleList=new ArrayList<RoleInfo>();
			RoleInfo roleInfo;
			for(String userRole:userRoles){
				roleInfo=new RoleInfo();
				roleInfo.setId(Long.valueOf(userRole));
				userRoleList.add(roleInfo);
			}
			detachedCriteria.add(Restrictions.in("roleInfo", userRoleList));
			if(StringUtils.isNotBlank(queryEntity.getStr1())){
				detachedCriteria.add(Restrictions.like("name", queryEntity.getStr1(),MatchMode.ANYWHERE));
			}
			if(queryEntity.getInt1()!=null){
				detachedCriteria.add(Restrictions.eq("userStatus", queryEntity.getInt1()));
			}
			if (sortorder.toLowerCase().equals("asc")) {
				detachedCriteria.addOrder(Order.asc(sortname));
			} else {
				detachedCriteria.addOrder(Order.desc(sortname));
			};
			List<Long> groupIds = new ArrayList<Long>();
			if(StringUtils.isNotBlank(queryEntity.getTreeId())&&!queryEntity.getTreeId().equals("1")){
				detachedCriteria.add(Restrictions.eq("groupTree.id", Long.valueOf(queryEntity.getTreeId())));
			}else{
				String[] array = departIds.split("\\*");
				if(!manageDepart.contains(departIds)){//非最高级别的管理员
					for(String dId:array){
						groupIds.add(Long.valueOf(dId));
					}
				}
				if(groupIds!=null&&groupIds.size()>0){
					detachedCriteria.add(Restrictions.in("groupTree.id", groupIds));
				}
			}
			Page page = pageHibernateTemplate.pageByDetachedCriteria(detachedCriteria, start, limit);
			return page;
		}
		return null;
	}
}
