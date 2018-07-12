package com.sws.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gk.extend.hibernate.dao.LicParams;
import com.sws.common.statics.SysStatics;
import com.sws.dao.ParameterInfoDao;
import com.sws.model.ParameterInfo;
import com.sws.service.LicenseService;

public class IniSystem implements ServletContextListener{
	public void contextDestroyed(ServletContextEvent sce) {
        
    }

    public void contextInitialized(ServletContextEvent sce) {
    	boolean flag=true;
    	ParameterInfo parameterInfo;
		List<ParameterInfo> parameterList = new ArrayList<ParameterInfo>();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		ParameterInfoDao parameterInfoDao = (ParameterInfoDao) ac.getBean("parameterInfoDao");
		
		LicParams licParams = (LicParams) ac.getBean("licParams");
		LicenseService licenseService = (LicenseService) ac.getBean("licenseService");
		// 初始化证书
		licenseService.loadLicence(licParams);
		
		List<ParameterInfo> allList = parameterInfoDao.getAll();
		if(allList!=null){
			for (Map.Entry<String,String> entry : SysStatics.jobTypeMap.entrySet()) {
				flag=true;
				for(ParameterInfo info:allList){
					if(entry.getKey().equals(info.getKey())){
						flag=false;
						break;
					}
				}
				if(flag){
					parameterInfo=new ParameterInfo();
					parameterInfo.setKey(entry.getKey());
					parameterInfo.setValue(entry.getValue());
					parameterInfo.setType(1);
					parameterList.add(parameterInfo);
				}
			}//人员类别
			for (Map.Entry<String,String> entry : SysStatics.serverMap.entrySet()) {
				flag=true;
				for(ParameterInfo info:allList){
					if(entry.getKey().equals(info.getKey())){
						flag=false;
						break;
					}
				}
				if(flag){
					parameterInfo=new ParameterInfo();
					parameterInfo.setKey(entry.getKey());
					parameterInfo.setValue(entry.getValue());
					parameterInfo.setType(2);
					parameterList.add(parameterInfo);
				}
			}//服务器参数
			if(parameterList.size()>0){
				parameterInfoDao.saveAll(parameterList);
			}
		}
		
    }
	
}
