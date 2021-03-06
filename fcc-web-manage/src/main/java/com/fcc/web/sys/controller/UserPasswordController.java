package com.fcc.web.sys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.common.StatusCode;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.SysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>Description: 管理系统 用户修改密码</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Api(value = "修改登录用户密码", produces = "修改密码")
@Controller
@RequestMapping("/manage/sys/userPassword")
public class UserPasswordController extends AppWebController {

	private Logger logger = Logger.getLogger(UserPasswordController.class);
	@Resource
	private SysUserService sysUserService;
	
	/** 显示修改密码页面 */
	@ApiOperation(value = "显示修改密码页面", notes = "")
	@RequestMapping(value = {"/view.do"}, method = RequestMethod.GET)
	@Permissions("view")
	public String view(HttpServletRequest request) {
		return "manage/sys/user_password";
	}
	
	/** 修改登录用户密码 */
	@ApiOperation(value = "修改密码", notes = "")
	@RequestMapping(value = {"/edit.do"}, method = RequestMethod.POST)
	@Permissions("edit")
	public ModelAndView edit(HttpServletRequest request,
            @ApiParam(required = true, value = "旧密码") @RequestParam(name = "oldPassword") String oldPassword,
            @ApiParam(required = true, value = "新密码") @RequestParam(name = "newPassword") String newPassword,
            @ApiParam(required = true, value = "确认码") @RequestParam(name = "confirmPassword") String confirmPassword) {
		Message message = new Message();
		try {
			if (StringUtils.isEmpty(oldPassword)) throw new RefusedException(StatusCode.UserPassword.emptyOldPassword);
			if (StringUtils.isEmpty(newPassword)) throw new RefusedException(StatusCode.UserPassword.emptyNewPassword);
			if (StringUtils.isEmpty(confirmPassword)) throw new RefusedException(StatusCode.UserPassword.emptyConfirmPassword);
			if (!newPassword.equals(confirmPassword)) throw new RefusedException(StatusCode.UserPassword.errorPasswordConsistent);
			SysUser user = getSysUser();
			if (!sysUserService.checkPassword(user, oldPassword)) {
			    throw new RefusedException(StatusCode.UserPassword.errorOldPassword);
			}
			sysUserService.resetPassword(user.getUserId(), newPassword);
			message.setSuccess(true);
			message.setMsg(StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改密码失败！", e.getCause());
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
}
