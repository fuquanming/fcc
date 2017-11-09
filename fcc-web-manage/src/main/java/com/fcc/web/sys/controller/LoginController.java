package com.fcc.web.sys.controller;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Login;
import com.fcc.commons.web.service.RequestIpService;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.common.StatusCode;
import com.fcc.web.sys.config.ConfigUtil;
import com.fcc.web.sys.model.SysUser;
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
@Api(value = "登录接口", produces = "登录接口")
@Controller
public class LoginController extends AppWebController {

    private Logger logger = Logger.getLogger(LoginController.class);

    @Resource
    private SysUserService sysUserService;

    @Resource
    private RequestIpService requestIpService;

    @ApiOperation(value = "用户登录")
    @Login
    // 请求 /login.do?login
    @RequestMapping(value = {"/login.do"}, method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request,
            @ApiParam(required = true, value = "登录帐号") @RequestParam(name = "username") String userId,
            @ApiParam(required = true, value = "登录密码") @RequestParam(name = "password") String password,
            @ApiParam(required = true, value = "验证码") @RequestParam(name = "randCode", defaultValue = "") String subCode) {
        //	    Assert.notNull(userId, LoginErrorEnums.usernameIsNull.getInfo());
        String sesCode = (String) request.getSession().getAttribute(Constants.randCodeKey);
        Message message = new Message();
        try {
            if (StringUtils.isEmpty(userId)) throw new RefusedException(StatusCode.Login.emptyUserName);
            if (StringUtils.isEmpty(password)) throw new RefusedException(StatusCode.Login.emptyPassword);
            if (StringUtils.isEmpty(subCode))  throw new RefusedException(StatusCode.Login.emptyRandCode);
            if (!ConfigUtil.isDev()) {
                if (!subCode.equalsIgnoreCase(sesCode)) throw new RefusedException(StatusCode.Login.errorRandCode);
            }
            SysUser user = null;
            user = sysUserService.getLoginUser(userId, password);
            user.setIp(requestIpService.getRequestIp(request));
            logger.info("系统登录成功，userId:" + user.getUserId());
            message.setSuccess(true);
            message.setMsg(StatusCode.Sys.success);
            request.getSession().invalidate();
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    cookie.setMaxAge(0);
                }
            }
            setSysUser(user, request);
        } catch (RefusedException e) {
            if (e.getMsg() != null) {
                message.setMsg(e.getMsg());
            } else {
                message.setMsg(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            message.setMsg("登录失败！原因：" + e.getMessage());
            message.setObj(e.getMessage());
        }
        message.setModule(Constants.Module.requestApp);
        message.setOperate(Constants.Operate.login);
        return getModelAndView(message);
    }

    @ApiOperation(value = "用户退出")
    @RequestMapping(value = {"/logout.do"}, method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request) throws Exception {
        SysUser user = getSysUser();
        String userId = null;
        Message message = new Message();
        message.setSuccess(true);
        if (user != null) {
            userId = user.getUserId();
            logger.info("退出系统成功，userId:" + userId);
            message.setMsg(StatusCode.Sys.success);
            message.setModule(Constants.Module.requestApp);
            message.setOperate(Constants.Operate.logout);
        }
        request.getSession().invalidate();
        return getModelAndView(message);
    }
}