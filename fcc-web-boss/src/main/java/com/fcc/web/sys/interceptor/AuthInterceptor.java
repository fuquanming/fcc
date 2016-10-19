package com.fcc.web.sys.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.web.common.Constanst;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.RbacPermissionService;

/**
 * <p>Description:权限拦截器</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class AuthInterceptor implements HandlerInterceptor {

	private static final Logger logger = Logger.getLogger(AuthInterceptor.class);
	@Resource
	private RbacPermissionService rbacPermissionService;
	
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
//		HttpSession session = request.getSession();
	    SysUser user = CacheUtil.getSysUser(request);
		// 对没有配置URL-MODULEID的请求不进行鉴权
		String servletPath = request.getServletPath();
		if (servletPath.substring(0, 1).equals("/")) servletPath = servletPath.substring(1);
		int separatorCharIndex = servletPath.lastIndexOf("/") + 1;
		String tempServletPath = servletPath.substring(0, separatorCharIndex);
		tempServletPath += "view.do";
		Module module = CacheUtil.moduleUrlMap.get(tempServletPath);
		if (module != null) {
			request.setAttribute(Constanst.REQUEST.MODULE, module);
			request.setAttribute("rightModuleId", module.getModuleId());
			// 获取执行的方法
			String operateStr = servletPath.substring(separatorCharIndex, servletPath.lastIndexOf("."));
			Operate operate = CacheUtil.operateMap.get(operateStr);
			String operateId = null;
			if (operate != null) {
				request.setAttribute(Constanst.REQUEST.OPERATE, operate);
				operateId = operate.getOperateId();
				// 鉴权
				if (!rbacPermissionService.checkPermission(user.getRoles(), module.getModuleId(), operateId)) {
					request.getSession().setAttribute("filterMsg", "right");
					logger.info("用户：" + user.getUserId() + "，您没有权限！执行：" + module.getModuleName() + "，" + operate.getOperateName());
					request.getRequestDispatcher("/overtime.jsp").forward(request, response);
					return false;
				} 
			}
		}
		return true;
	}

}
