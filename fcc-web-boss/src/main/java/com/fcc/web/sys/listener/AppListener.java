package com.fcc.web.sys.listener;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fcc.commons.web.service.ConfigService;
import com.fcc.commons.web.util.SpringContextUtil;
import com.fcc.web.sys.cache.QueueUtil;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.SysLog;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CacheService;

/**
 * 
 * <p>Description:应用监听器</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class AppListener implements ServletContextListener, HttpSessionListener {

	private Logger logger = Logger.getLogger(AppListener.class);

	private ConfigService configService;
	private CacheService cacheService;
	
	public void contextInitialized(ServletContextEvent event) {
		cacheService = (CacheService) SpringContextUtil.getBean(CacheService.class);
		configService = (ConfigService) SpringContextUtil.getBean(ConfigService.class);
		configService.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                cacheService.getModuleMap();
                cacheService.getRoleModuleRightMap();
                cacheService.getOperateMap();
                cacheService.getModuleUrlMap();
            }
        });
		event.getServletContext().setAttribute("APP_NAME", configService.getAppName());
	}
	
	public void contextDestroyed(ServletContextEvent event) {
	}

	public void sessionCreated(HttpSessionEvent event) {
//	    System.out.println("createsession=" + event.getSession().getId());
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		SysUser user = (SysUser) session.getAttribute(Constants.SysUserSession.loginUser);
		if (user != null && StringUtils.isNotEmpty(user.getUserId())) {
			try {
				SysLog sysLog = new SysLog();
				sysLog.setIpAddress(user.getIp());
				sysLog.setModuleName(Constants.Module.requestApp);
				sysLog.setOperateName(Constants.Operate.logout);
				sysLog.setUserId(user.getUserId());
				sysLog.setUserName(user.getUserName());
				sysLog.setEventParam(user.getUserId());
				sysLog.setEventResult(SysLog.EVENT_RESULT_OK);
				sysLog.setLogTime(new Date());
				QueueUtil.getCreateQueue().offer(sysLog);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
		session.getServletContext().removeAttribute(event.getSession().getId());
	}
}
