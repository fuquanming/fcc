package com.fcc.web.sys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.web.common.Constanst;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.model.SysUser;

/**
 * <p>Description:会话拦截器</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class SessionInterceptor implements HandlerInterceptor {

	/**
	 * 完成页面的render后调用
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {
	}

	/**
	 * 在调用controller具体方法后拦截
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
	}

	/**
	 * 在调用controller具体方法前拦截
	 */
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		// uploadify-3.2.1 使用flash上传文件 没有携带cookie 代码绑定 原来sessionId
		HttpSession session = request.getSession();
	    SysUser user = CacheUtil.getSysUser(request);
	    if (user == null) {
	    	String jsessionid = request.getParameter("JSESSIONID");
		    if (jsessionid != null) {
		    	session = (HttpSession) session.getServletContext().getAttribute(jsessionid);
		    	if (session != null) {
		    		user = (SysUser) session.getAttribute(Constanst.SysUserSession.USER_LOGIN);
		    		CacheUtil.setSysUser(request, user);
		    		session.setAttribute(CacheUtil.UPLOAD_SESSION, request.getSession());
		    	}
		    }
	    }
//		user = null;
		//未验证
		if (user == null) {
//			response.sendRedirect(basePath + "/overtime.jsp");
			request.getSession().setAttribute("filterMsg", "login");// 表示重新登录系统
			request.getRequestDispatcher("/overtime.jsp").forward(request, response);
			return false;
		}
		return true;
	}

}