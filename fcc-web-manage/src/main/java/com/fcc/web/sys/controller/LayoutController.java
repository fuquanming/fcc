package com.fcc.web.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
public class LayoutController {
	
	@RequestMapping(params = "north")
	public String north() {
		return "manage/layout/north";
	}

	@RequestMapping(params = "west")
	public String west() {
		return "manage/layout/west";
	}

	@RequestMapping(params = "center")
	public String center() {
		return "manage/layout/center";
	}

	@RequestMapping(params = "south")
	public String south() {
		return "manage/layout/south";
	}

	/**
	 * 跳转到home页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "home")
	public String home() {
		return "/manage/layout/home";
	}
}
