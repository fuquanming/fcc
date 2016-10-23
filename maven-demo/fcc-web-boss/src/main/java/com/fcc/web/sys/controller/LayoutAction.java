package com.fcc.web.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>Description: 管理系统 登录后首页布局</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/layout.do")
@ApiIgnore
public class LayoutAction {
	
	@RequestMapping(params = "north", method = RequestMethod.GET)
	public String north() {
		return "/WEB-INF/manage/layout/north";
	}

	@RequestMapping(params = "west", method = RequestMethod.GET)
	public String west() {
		return "/WEB-INF/manage/layout/west";
	}

	@RequestMapping(params = "center", method = RequestMethod.GET)
	public String center() {
		return "/WEB-INF/manage/layout/center";
	}

	@RequestMapping(params = "south", method = RequestMethod.GET)
	public String south() {
		return "/WEB-INF/manage/layout/south";
	}

	/**
	 * 跳转到home页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "home", method = RequestMethod.GET)
	public String home() {
		return "/WEB-INF/manage/layout/home";
	}
}
