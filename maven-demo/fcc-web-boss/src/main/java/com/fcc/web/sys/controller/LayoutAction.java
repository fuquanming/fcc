package com.fcc.web.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>Description: 管理系统 登录后首页布局</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/layout")
public class LayoutAction {
	
	@RequestMapping(params = "north")
	public String north() {
		return "/WEB-INF/manage/layout/north";
	}

	@RequestMapping(params = "west")
	public String west() {
		return "/WEB-INF/manage/layout/west";
	}

	@RequestMapping(params = "center")
	public String center() {
		return "/WEB-INF/manage/layout/center";
	}

	@RequestMapping(params = "south")
	public String south() {
		return "/WEB-INF/manage/layout/south";
	}

	/**
	 * 跳转到home页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "home")
	public String home() {
		return "/WEB-INF/manage/layout/home";
	}
}
