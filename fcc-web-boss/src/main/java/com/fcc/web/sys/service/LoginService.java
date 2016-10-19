package com.fcc.web.sys.service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Description:登录系统数据初始化</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public interface LoginService {
	/**
	 * 通过登录
	 */
	void postLogin(HttpServletRequest request);
	
}
