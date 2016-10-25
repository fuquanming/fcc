package com.fcc.web.sys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.utils.EncryptionUtil;
import com.fcc.commons.web.common.Constanst;
import com.fcc.commons.web.service.RequestIpService;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.common.Constants;
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
@Api(value = "登录接口", produces = "登录接口")
@RestController
public class LoginController extends AppWebController {

    private static Logger logger = Logger.getLogger(LoginController.class);

    @Resource
    private SysUserService sysUserService;

    @Resource
    private RequestIpService requestIpService;

    @Resource
    private LoginService loginService;

    @ApiOperation(value = "用户登录")
    // 请求 /login.do?login
    //	@RequestMapping(value = "/login.do" , method = RequestMethod.POST)
    @PostMapping(value = "/login.do")
    @ResponseBody
    public Message login(HttpServletRequest request,
            @ApiParam(required = true, value = "登录帐号") @RequestParam(name = "username") String userId,
            @ApiParam(required = true, value = "登录密码") @RequestParam(name = "password") String password,
            @ApiParam(required = true, value = "验证码") @RequestParam(name = "randCode") String subCode) {
        //	    Assert.notNull(userId, LoginErrorEnums.usernameIsNull.getInfo());
        String sesCode = (String) request.getSession().getAttribute(Constanst.RAND_CODE_KEY);
        Message message = new Message();
        try {
            if (StringUtils.isEmpty(userId)) throw new RefusedException(Constants.StatusCode.Login.emptyUserName);
            if (StringUtils.isEmpty(password)) throw new RefusedException(Constants.StatusCode.Login.emptyPassword);
            if (StringUtils.isEmpty(sesCode))  throw new RefusedException(Constants.StatusCode.Login.emptyRandCode);
            if (!subCode.equalsIgnoreCase(sesCode)) throw new RefusedException(Constants.StatusCode.Login.errorRandCode);
            SysUser user = null;
            password = EncryptionUtil.encodeMD5(password).toLowerCase();
            user = sysUserService.getLoninUser(userId, password);
            user.setIp(requestIpService.getRequestIp(request));
            logger.info("系统登录成功，userId:" + user.getUserId());
            message.setSuccess(true);
            message.setMsg(Constants.StatusCode.Sys.success);
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
        return message;
    }

    @ApiOperation(value = "用户退出")
    @GetMapping("/logout.do")
    @ResponseBody
    public Message logout(HttpServletRequest request) throws Exception {
        SysUser user = getSysUser(request);
        String userId = null;
        Message message = new Message();
        message.setSuccess(true);
        if (user != null) {
            userId = user.getUserId();
            logger.info("退出系统成功，userId:" + userId);
            message.setMsg(Constants.StatusCode.Sys.success);
        }
        request.getSession().invalidate();
        return message;
    }
}