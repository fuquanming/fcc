package com.fcc.commons.web.service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Description:请求IP</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public interface RequestIpService {

	public String getRequestIp(HttpServletRequest request); 
	
}
