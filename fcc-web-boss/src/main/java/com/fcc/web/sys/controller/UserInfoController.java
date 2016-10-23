package com.fcc.web.sys.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.SysUserService;

/**
 * <p>Description: 管理系统 用户修改资料</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/sys/userInfo")
public class UserInfoController extends AppWebController {

	private static Logger logger = Logger.getLogger(UserInfoController.class);
	@Autowired
	private SysUserService sysUserService;
	
	/** 显示修改基本信息页面 */
	@RequestMapping("/view")
	public String view(HttpServletRequest request) {
		SysUser user = getSysUser(request);
		request.setAttribute("data", user);
		return "/WEB-INF/manage/sys/user_info";
	}
	
	/** 修改用户基本信息 */
	@RequestMapping("/edit")
	@ResponseBody
	public Message edit(SysUser sysUser, HttpServletRequest request) {
		Message message = new Message();
		try {
			SysUser user = getSysUser(request);
			Set<Role> roles = user.getRoles();
			sysUserService.update(sysUser);
			BeanUtils.copyProperties(sysUser, user);
			user.setRoles(roles);
			CacheUtil.setSysUser(request, user);
			message.setSuccess(true);
			message.setMsg("修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("修改失败！");
		}
		return message;
	}
}
