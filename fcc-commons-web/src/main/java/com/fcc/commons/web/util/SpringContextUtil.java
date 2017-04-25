package com.fcc.commons.web.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */

public final class SpringContextUtil {
	private static WebApplicationContext wac;
	private static BeanFactory factory;
	static {
		wac = ContextLoader.getCurrentWebApplicationContext();
	}

	public static <T> T getBean(Class<T> clazz) {
	    if (wac == null) {
	        return factory.getBean(clazz);
	    }
	    return wac.getBean(clazz);
	}

	public static Object getBean(String beanName) {
		return wac.getBean(beanName);
	}
	
	public static WebApplicationContext getWebApplicationContext() {
		return wac;
	}
	
	public static void setBeanFactory(BeanFactory factory1) {
	    factory = factory1;
	}
}
