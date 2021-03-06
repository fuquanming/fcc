package com.fcc.web.sys.interceptor;

import java.util.Date;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.web.service.RequestIpService;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.cache.QueueUtil;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.SysLog;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CacheService;


/**
 * <p>Description:日志拦截器</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class LogInterceptor implements HandlerInterceptor {
    private Logger logger = Logger.getLogger(LogInterceptor.class);
    @Resource
    private RequestIpService requestIpService;
    @Resource
    private CacheService cacheService;
    
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) return;
        String messageKey = "message";
        Message message = (Message) modelAndView.getModelMap().get(messageKey);
        if (message == null) return;
        Module module = (Module) request.getAttribute(Constants.Request.module);
        Operate operate = (Operate) request.getAttribute(Constants.Request.operate);
        String moduleName = null;
        String operateName = null;
        try {
            moduleName = module != null ? module.getModuleId() : message.getModule();
            operateName = operate != null ? operate.getOperateId() : message.getOperate();
            boolean isLogin = false;
            // 主动退出系统不记录，session回话记录
            if (Constants.Module.requestApp.equals(moduleName) &&
                    Constants.Operate.logout.equals(operateName)) {
                return;
            }
            String status = message.isSuccess() ? SysLog.EVENT_RESULT_OK : SysLog.EVENT_RESULT_FAIL;
            if (Constants.Module.requestApp.equals(moduleName) &&
                    Constants.Operate.login.equals(operateName)) {
                // 访问系统，登录
                isLogin = true;
            }
            SysUser user = cacheService.getSysUser(request);
            SysLog sysLog = new SysLog();
            sysLog.setModuleName(moduleName);
            sysLog.setOperateName(operateName);
            if (user != null) {
                sysLog.setIpAddress(user.getIp());
                sysLog.setUserId(user.getUserId());
                sysLog.setUserName(user.getUserName());
            } else {
                sysLog.setIpAddress(requestIpService.getRequestIp(request));
            }
            Object obj = message.getObj();
            if (obj != null) {
                String str = obj.toString();
                if (str.length() > 2000) str = str.substring(0, 1996) + "...";
                sysLog.setEventObject(str);
            }
            
            StringBuilder sb = new StringBuilder();
            Enumeration<String> it = request.getParameterNames();
            while (it.hasMoreElements()) {
                String key = it.nextElement();
                String value = request.getParameter(key);
                if (isLogin && "password".equals(key)) {
                    value = "***";
                }
                String[] values = request.getParameterValues(key);
                if (values.length == 1) {
                    sb.append(key).append("=").append(value).append("\r\n");
                } else {
                    for (String v : values) {
                        sb.append(key).append("=").append(v).append("\r\n");
                    }
                }
            }
            String sbStr = sb.toString();
            if (sbStr.length() > 2000) sbStr = sbStr.substring(0, 1996) + "...";
            sysLog.setEventParam(sbStr);
            sysLog.setEventResult(status);
            sysLog.setLogTime(new Date());
            QueueUtil.getCreateQueue().offer(sysLog);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        return true;
    }
}