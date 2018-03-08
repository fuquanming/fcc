package com.fcc.commons.web.listener;

import java.util.Set;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * <p>Description:CAS应用监听器,获取CAS地址</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class CasListener implements ServletContextListener {

	private String casServerLoginUrl;
	
	public void contextInitialized(ServletContextEvent event) {
	    Set<String> keySet = event.getServletContext().getFilterRegistrations().keySet();
        for (String key : keySet) {
            Object obj = event.getServletContext().getFilterRegistrations().get(key);
            if (obj instanceof FilterRegistration) {
                FilterRegistration filter = (FilterRegistration) obj;
                if (filter.getClassName().equals("org.jasig.cas.client.authentication.AuthenticationFilter")) {
                    casServerLoginUrl = filter.getInitParameter("casServerLoginUrl");
                    event.getServletContext().setAttribute("CAS_URL", casServerLoginUrl);
                    return;
                }
            }
        }
		
	}
	
	public void contextDestroyed(ServletContextEvent event) {
	}

}
