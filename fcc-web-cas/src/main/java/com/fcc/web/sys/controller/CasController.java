package com.fcc.web.sys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.service.RequestIpService;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.common.StatusCode;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CasService;
import com.fcc.web.sys.service.SysUserService;
import com.fcc.web.sys.view.CasUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>Description: 管理系统 登录、退出</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Api(value = "CAS登录接口", produces = "CAS登录接口")
@Controller
public class CasController extends AppWebController {

    private Logger logger = Logger.getLogger(CasController.class);

    @Resource
    private SysUserService sysUserService;
    @Resource
    private RequestIpService requestIpService;
    @Resource
    private CasService casService;
    
    public CasController() {
        // 添加用户类型
        Constants.userTypes.put(CasUser.userTypeKey, CasUser.userTypeName);
    }

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = {"/cas/login.do"}, method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request) throws Exception {
        Message message = new Message();
        ModelAndView view = null;
        try {
            Object obj = request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);  
            if (obj != null) {
                view = new ModelAndView("redirect:/manage/index.do");
                Assertion assertion = (Assertion) obj;  
//                AttributePrincipal p= assertion.getPrincipal();
                logger.info("-----cas loginName=" + assertion.getPrincipal().getName());
                SysUser user = cacheService.getSysUser(request);
                if (user == null) {
                    // 接入第三方权限
                    user = casService.login(assertion);
                    user.setIp(requestIpService.getRequestIp(request));
                    logger.info("系统登录成功，userId:" + user.getUserId());
                    message.setSuccess(true);
                    message.setMsg(StatusCode.Sys.success);
//                    request.getSession().invalidate();
//                    if (request.getCookies() != null) {
//                        for (Cookie cookie : request.getCookies()) {
//                            cookie.setMaxAge(0);
//                        }
//                    }
                    setSysUser(user, request);
                }
                return view;
            }
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
    
}