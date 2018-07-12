package com.sws.common;

import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gk.essh.core.localejs.ILocaleTextService;
import com.gk.essh.extend.impl.SwsInfoFinder;
import com.gk.essh.extend.impl.notify.ws.SwsConnectException;
import com.gk.essh.extend.impl.view.ws.ConstantDaoBySws;
import com.gk.sws.cache.manager.DataLoadException;
import com.gk.sws.cache.manager.DataManager;
import com.gk.sys.ws.client.ClientUtil;
import com.sys.core.util.StringUtils;
import com.sys.core.util.cm.ConfigManager;

public class LocaleTextService extends SwsInfoFinder implements ILocaleTextService {
	
	private static final Logger log = LoggerFactory.getLogger(ConstantDaoBySws.class);
	private DataManager dataManager;
	
	private String getText(String code,String language){
		String url= "http://"+getWSLinkInfo()+"services/CommonService";
		
		String method="getLocaleText";
		Object[] args = new Object[]{code,language};
		String result = "";
		try {
			if(log.isDebugEnabled()){
	        	log.debug("request sws:" + url + ":" + method );
	        }
	        result =  ClientUtil.callWs(url, method, namespaceURI, args);
	        if(log.isDebugEnabled()){
	        	log.debug(result);
	        }
	        if(result.contains("<?xml")){
	        	return code;
	        }
        } catch (AxisFault e) {
        	log.error("call sws error",e);
        	SwsConnectException e4throw = new SwsConnectException(e);
 	        e4throw.setCode("-2");
 	        throw e4throw;
        }
		return result;
	}
	
	
	@Override
	public String getLocalText(String code, String language) {
		if(code == null || code.contains(" ")){
			return code;
		}
		String value = null;
//		 String key = code+language;
//		try {
//			 value = dataManager.get(key);
//		} catch (DataLoadException e) {
//			log.error("connect 2 cacheserver error");	
//		}
//		try{
//			if(value == null) {
//				value = getText(code, language);
//				dataManager.set(key, value);
//			}
//		} catch (DataLoadException e) {
//			log.error("rewrite cache error");	
//		}
		return value;
	}
	
	private String getDefaultLocale(){
		String locale = "zh_CN";
		String value = ConfigManager.getConfiguration("sysparam", "language.default");
		if(!StringUtils.isEmpty(value)){
			locale = StringUtils.trim(value);
		}
		return locale;
	}

	@Override
    public String getLocalText(String code, Object[] args) {
		if(code == null || code.contains(" ")){
			return code;
		}
		String value = null;
		String language = getDefaultLocale();
		 String key = code+language;
		try {
			 value = dataManager.get(key);
			
		} catch (DataLoadException e) {
		}
		try {
			 if(value == null) {
					value = getText(code, language);
					dataManager.set(key, value);
				}
		} catch (DataLoadException e) {
		}
		value = replaceHoldPlace(args, value);
		return value;
	}


	private String replaceHoldPlace(Object[] args, String value) {
	    if(args != null && args.length > 0){
			for(int i = 0; i < args.length; i++){
				String replaceValue = String.valueOf(args[i]);
				value = value.replace("{" + i + "}", replaceValue);
			}
		}
	    return value;
    }

	@Override
    public String getLocalText(String code, String language, Object[] args) {
		if(code == null || code.contains(" ")){
			return code;
		}
		String value = null;
		 String key = code+language;
		try {
			 value = dataManager.get(key);
		} catch (DataLoadException e) {
		}
		try {
			if(value == null) {
				value = getText(code, language);
				dataManager.set(key, value);
			}
		} catch (DataLoadException e) {
		}
		value = replaceHoldPlace(args, value);
		return value;
	}
	
    public DataManager getDataManager() {
    	return dataManager;
    }

    public void setDataManager(DataManager dataManager) {
    	this.dataManager = dataManager;
    }
	
	

}
