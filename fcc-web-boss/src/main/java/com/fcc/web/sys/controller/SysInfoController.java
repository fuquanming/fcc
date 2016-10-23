package com.fcc.web.sys.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.cache.CacheUtil;

/**
 * <p>Description: 管理系统 </p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
public class SysInfoController {

	private static Logger logger = Logger.getLogger(SysInfoController.class);
	/**
	 * 查询系统信息
	 * @return
	 */
	@RequestMapping("/manage/sys/sysInfo/view")
	public String infoView() {
		return "/WEB-INF/manage/sysinfo/sys_info";
	}
	
	/**
	 * 查询系统缓存
	 * @return
	 */
	@RequestMapping("/manage/sys/sysCache/view")
	public String cacheView() {
		return "/WEB-INF/manage/sysinfo/sys_cache";
	}
	
	/**
	 * 更新系统缓存
	 * @return
	 */
	@RequestMapping("/manage/sys/sysCache/reload")
	@ResponseBody
	public Message reloadCache() {
		Message message = new Message();
		try {
			CacheUtil.initModule();
			CacheUtil.initOperate();
			message.setSuccess(true);
			message.setMsg("更新成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("更新失败！");
		}
		return message;
	}
	
}
