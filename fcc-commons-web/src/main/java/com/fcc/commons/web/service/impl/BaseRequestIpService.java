package com.fcc.commons.web.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.fcc.commons.web.service.RequestIpService;
import com.fcc.commons.web.util.WebIpUtil;
/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
@Service
public class BaseRequestIpService implements RequestIpService {

	public String getRequestIp(HttpServletRequest request) {
		return WebIpUtil.getRequestIp(request);
	}

}
