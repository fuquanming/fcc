package com.fcc.web.sys.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.SysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>Description: 管理系统 用户修改资料</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Api(value = "登录用户信息修改")
@Controller
@RequestMapping("/manage/sys/userInfo")
public class UserInfoController extends AppWebController {

	private static Logger logger = Logger.getLogger(UserInfoController.class);
	@Autowired
	private SysUserService sysUserService;
	
	/** 显示修改基本信息页面 */
	@ApiOperation(value = "显示登录用户信息")
	@RequestMapping(value = {"/view.do"}, method = RequestMethod.GET)
	@Permissions("view")
	public String view(HttpServletRequest request) {
		SysUser user = getSysUser(request);
		request.setAttribute("data", user);
		return "manage/sys/user_info";
	}
	
	/** 修改用户基本信息 */
	@ApiOperation(value = "修改登录用户信息")
	@RequestMapping(value = {"/edit.do"}, method = RequestMethod.POST)
	@Permissions("edit")
	public ModelAndView edit(HttpServletRequest request, SysUser sysUser) {
		Message message = new Message();
		try {
			SysUser user = getSysUser(request);
			Set<Role> roles = user.getRoles();
			sysUserService.update(sysUser);
			BeanUtils.copyProperties(sysUser, user);
			user.setRoles(roles);
			CacheUtil.setSysUser(request, user);
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改登录用户信息失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
}
