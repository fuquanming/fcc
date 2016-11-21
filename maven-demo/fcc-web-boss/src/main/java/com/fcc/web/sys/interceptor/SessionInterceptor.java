package com.fcc.web.sys.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.config.ConfigUtil;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CacheService;

/**
 * <p>Description:会话拦截器</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class SessionInterceptor implements HandlerInterceptor {

    private Logger logger = Logger.getLogger(SessionInterceptor.class);
    @Resource
    private CacheService cacheService;
    // TODO 耗时改造
    ThreadLocal<StopWatch> stopWatchLocal = new ThreadLocal<StopWatch>();
    ThreadLocal<Long> timeLocal = new ThreadLocal<Long>();
    String timePageStr = "耗时毫秒：%d,page=%s";
    String timeControllerStr = "耗时毫秒：%d,controller=%s";
	/**
	 * 完成页面的render后调用
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {
	    if (ConfigUtil.isDev()) {
	        StopWatch stopWatch = stopWatchLocal.get();
	        if (stopWatch != null) {
	            stopWatch.stop();
	            long time = stopWatch.getTotalTimeMillis();
	            logger.info(String.format(timePageStr, time, request.getRequestURI()));
	            stopWatch = null;
	            stopWatchLocal.set(null);
	        }
	    }
	}

	/**
	 * 在调用controller具体方法后拦截
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
	    if (ConfigUtil.isDev()) {
	        StopWatch stopWatch = stopWatchLocal.get();
	        if (stopWatch != null) {
	            stopWatch.stop();
	            long time = stopWatch.getTotalTimeMillis();
	            logger.info(String.format(timeControllerStr, time, request.getRequestURI()));
	            stopWatch.start();
	        }        
	    }
	}

	/**
	 * 在调用controller具体方法前拦截
	 */
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
	    if (ConfigUtil.isDev()) {
	        StopWatch stopWatch = new StopWatch(request.getRequestURI());
	        if (stopWatch != null) {
	            stopWatch.start(request.getRequestURI());
	        }
	    }
        
	    SysUser user = cacheService.getSysUser(request);
//	    // uploadify-3.2.1 使用flash上传文件 没有携带cookie 代码绑定 原来sessionId
//		HttpSession session = request.getSession();
//	    if (user == null) {
//	    	String jsessionid = request.getParameter("JSESSIONID");
//		    if (jsessionid != null) {
//		    	session = (HttpSession) session.getServletContext().getAttribute(jsessionid);
//		    	if (session != null) {
//		    		user = (SysUser) session.getAttribute(Constants.SysUserSession.USER_LOGIN);
//		    		CacheUtil.setSysUser(request, user);
//		    		session.setAttribute(CacheUtil.UPLOAD_SESSION, request.getSession());
//		    	}
//		    }
//	    }
//		user = null;
		//未验证
		if (user == null) {
	        if (object instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) object;
                if (handlerMethod.getMethod().isAnnotationPresent(ResponseBody.class) || handlerMethod.getMethod().getReturnType() == ModelAndView.class) {// json
//                    text/html;charset=UTF-8
                    response.setContentType("application/json;charset=UTF-8");
                    Message message = new Message();
                    message.setMsg(Constants.StatusCode.Sys.sessionTimeout);
                    message.setObj(Constants.StatusCode.Sys.sessionTimeout);
                    byte[] bytes = JSON.toJSONBytes(message, SerializerFeature.DisableCircularReferenceDetect);
                    response.getOutputStream().write(bytes);
                    return false;
                } else if (handlerMethod.getMethod().getReturnType() == String.class) {// 跳转页面
                    request.getSession().setAttribute("filterMsg", "login");// 表示重新登录系统
                    request.getRequestDispatcher("/overtime.jsp").forward(request, response);
                    return false;
                }
            }
//	        response.sendRedirect(basePath + "/overtime.jsp");
	        request.getSession().setAttribute("filterMsg", "login");// 表示重新登录系统
            request.getRequestDispatcher("/overtime.jsp").forward(request, response);
            return false;
		}
		return true;
	}

}
