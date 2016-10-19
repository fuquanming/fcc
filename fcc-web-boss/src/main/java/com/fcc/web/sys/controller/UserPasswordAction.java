package com.fcc.web.sys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.utils.EncryptionUtil;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.SysUserService;

/**
 * <p>Description: 管理系统 用户修改密码</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/sys/userPassword")
public class UserPasswordAction extends AppWebController {

	private static Logger logger = Logger.getLogger(UserPasswordAction.class);
	@Resource
	private SysUserService sysUserService;
	
	/** 显示修改密码页面 */
	@RequestMapping("/view")
	public String view(HttpServletRequest request) {
		return "/WEB-INF/manage/sys/user_password";
	}
	
	/** 修改用户密码 */
	@RequestMapping("/edit")
	@ResponseBody
	public Message edit(HttpServletRequest request) {
		Message message = new Message();
		try {
			String newPassword = request.getParameter("newPassword");
			String oldPassword = request.getParameter("oldPassword");
			if (newPassword == null || "".equals(newPassword)) {
				throw new RefusedException("请输入新密码！");
			} else if (oldPassword == null || "".equals(oldPassword)) {
				throw new RefusedException("请输入旧密码！");
			}
			SysUser user = getSysUser(request);
			String password = user.getPassword();
			if (!EncryptionUtil.encodeMD5(oldPassword).toLowerCase().equals(password)) {
				throw new RefusedException("旧密码不正确！");
			}
			sysUserService.updatePassword(user.getUserId(), EncryptionUtil.encodeMD5(newPassword).toLowerCase());
			message.setSuccess(true);
			message.setMsg("修改成功！");
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("修改失败！");
		}
		return message;
	}
}
