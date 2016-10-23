package com.fcc.web.sys.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.utils.EncryptionUtil;
import com.fcc.commons.web.common.Constanst;
import com.fcc.commons.web.service.RequestIpService;
import com.fcc.commons.web.util.ModelAndViewUtil;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.LoginService;
import com.fcc.web.sys.service.SysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>Description: 管理系统 登录、退出</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@Api(value = "登录接口", produces = "登录接口")
public class LoginController extends AppWebController {

	private static Logger logger = Logger.getLogger(LoginController.class);
	@Resource
	private SysUserService sysUserService;
	@Resource
	private RequestIpService requestIpService;
	@Resource
	private LoginService loginService;
	
	// 请求 /login.do?login
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	// 返回JSON数据
	@ApiOperation(value = "用户登录")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response,
	        @ApiParam(required = true, value = "登录帐号") @RequestParam(name = "username", required = true) String userId,
	        @ApiParam(required = true, value = "登录密码") @RequestParam(name = "password", required = true) String password,
	        @ApiParam(required = true, value = "验证码") @RequestParam(name = "randCode", required = true) String subCode) {
//	    Assert.hasLength("");
        String sesCode = (String)request.getSession().getAttribute(Constanst.RAND_CODE_KEY);
        Message message = new Message();
        ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
        try {
        	if (subCode == null || !subCode.equalsIgnoreCase(sesCode)) {
//        		throw new RefusedException("验证码错误！");
        	}
        	SysUser user = null;
        	password = EncryptionUtil.encodeMD5(password).toLowerCase();
        	user = sysUserService.getLoninUser(userId, password);
        	user.setIp(requestIpService.getRequestIp(request));
        	logger.info("系统登录成功，userId:" + user.getUserId());
        	message.setSuccess(true);
        	message.setMsg("登录成功!");
        	CacheUtil.initLoginUser(request, user);
        } catch (RefusedException e) {
        	message.setMsg(e.getMessage());
        	message.setObj(e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	logger.error(e);
        	message.setMsg("登录失败！原因：" + e.getMessage());
        	message.setObj(e.getMessage());
        }
        message.setModule(Constanst.MODULE.REQUEST_APP);
        message.setOperate(Constanst.OPERATE.LOGIN);
		return mav;
	}
	
	@RequestMapping("/logout.do")
	@ResponseBody
	public Message logout(HttpServletRequest request) throws Exception {
	    System.out.println("outout");
		SysUser user = getSysUser(request);
		String userId = null;
		Message message = new Message();
		message.setSuccess(true);
		if (user != null) {
			userId = user.getUserId();
			logger.info("退出系统成功，userId:" + userId);
			message.setObj("退出系统成功，userId:" + userId);
		}
		request.getSession().invalidate();
		return message;
	}
}