package com.fcc.web.sys.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>Description: 管理系统 登录首页</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@ApiIgnore
public class IndexController {

	/**
	 * 登录后首页
	 * @return
	 */
	@RequestMapping(value = {"/manage/index.do"})
	public String index(HttpServletRequest request) {
		return "/WEB-INF/manage/index";
	}
	
}
