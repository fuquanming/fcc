package com.fcc.commons.web.util;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */

@SuppressWarnings("unchecked")
public final class SpringContextUtil {
	private static WebApplicationContext wac;
	static {
		wac = ContextLoader.getCurrentWebApplicationContext();
	}

	public static Object getBean(Class clazz) {
		return wac.getBean(clazz);
	}

	public static Object getBean(String beanName) {
		return wac.getBean(beanName);
	}
	
	public static WebApplicationContext getWebApplicationContext() {
		return wac;
	}
}
