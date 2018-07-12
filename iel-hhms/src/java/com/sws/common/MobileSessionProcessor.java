package com.sws.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gk.essh.www.session.GkSession;
import com.gk.essh.www.session.GkWebSession;
import com.gk.sws.cache.core.common.CacheConstants.DATA_TYPE;
import com.gk.sws.cache.manager.DataLoadException;
import com.gk.sws.cache.manager.DataManager;
import com.sys.core.util.StringUtils;

/**
 * 专门给移动端开发的Session管理类，移除了COOKIE相关处理
 * @author wangyabei
 * 
 */
public class MobileSessionProcessor {

	private DataManager dataManager;

	private Logger log = LoggerFactory.getLogger(MobileSessionProcessor.class);

	public GkWebSession getSession(String sessionId) {
		if (StringUtils.isEmpty(sessionId)) {
			return null;
		}
		GkWebSession gkSession = null;
		try {
			String session = (String) dataManager.get(sessionId, DATA_TYPE.SESSION);
			gkSession = parseFrom(session);
		}
		catch (DataLoadException e) {
			log.info("connect to cache error.", e);
		}
		return gkSession;
	}

	public boolean isSessionExist(String sessionId) {
		if (StringUtils.isEmpty(sessionId)) {
			return false;
		}
		try {
			String session = (String) dataManager.get(sessionId, DATA_TYPE.SESSION);
			if (!StringUtils.isEmpty(session)) {
				return true;
			}
		}
		catch (DataLoadException e) {
			log.info("connect to cache error.", e);
		}
		return false;
	}

	public boolean removeSession(String sessionId) {
		if (StringUtils.isEmpty(sessionId)) {
			return false;
		}
		try {
			dataManager.remove(sessionId, DATA_TYPE.SESSION);
		}
		catch (DataLoadException e) {
			log.info("connect to cache error.", e);
		}
		return true;
	}

	public boolean setSession(GkSession session, int expire) {
		String sessionId = session.getSessionId();
		if (StringUtils.isEmpty(sessionId)) {
			return false;
		}
		String sessionStr = session.toString();
		try {
			dataManager.set(sessionId, sessionStr, (long) expire, DATA_TYPE.SESSION);
		}
		catch (DataLoadException e) {
			log.info("connect to cache error.", e);
			return false;
		}
		return true;
	}

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	private GkWebSession parseFrom(String session) {
		if (StringUtils.isEmpty(session)) {
			return null;
		}
		String[] sessions = session.split(",");
		if (sessions.length > 0) {
			GkWebSession gkSession = new GkWebSession();
			for (String s : sessions) {
				String[] attr = s.split(":");
				if (attr.length != 2) {
					continue;
				}
				else {
					gkSession.setAttribute(attr[0], attr[1]);
				}
			}
			return gkSession;
		}
		return null;
	}
}
