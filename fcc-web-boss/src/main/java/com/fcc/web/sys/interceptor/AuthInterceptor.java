package com.fcc.web.sys.interceptor;

import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.web.annotation.Logical;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.common.StatusCode;
import com.fcc.commons.web.interceptor.BaseInterceptor;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CacheService;
import com.fcc.web.sys.service.RbacPermissionService;

/**
 * <p>Description:权限拦截器</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class AuthInterceptor extends BaseInterceptor {

	private Logger logger = Logger.getLogger(AuthInterceptor.class);
	@Resource
	private RbacPermissionService rbacPermissionService;
	@Resource
	private CacheService cacheService;
	
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
	    SysUser user = cacheService.getSysUser(request);
	    try {
            if (object instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) object;
                // 对没有配置URL-MODULEID的请求不进行鉴权
                String servletPath = request.getServletPath();
                if (servletPath.substring(0, 1).equals("/")) servletPath = servletPath.substring(1);
                int separatorCharIndex = servletPath.lastIndexOf("/") + 1;
                String tempServletPath = servletPath.substring(0, separatorCharIndex);
                tempServletPath += "view.do";
                String moduleId = cacheService.getModuleUrlMap().get(tempServletPath);
                if (moduleId != null) {
                    Module module = cacheService.getModuleMap().get(moduleId);
                    if (module != null) {
                        request.setAttribute(Constants.Request.module, module);
                        request.setAttribute("rightModuleId", module.getModuleId());
                        // 获取执行的方法
                        Permissions permissions = handlerMethod.getMethodAnnotation(Permissions.class);
                        boolean flag = true;
                        String moduleName = module.getModuleName();
                        String operateName = "";
                        if (permissions != null) {
                            String[] operates = permissions.value();
                            Logical logical = permissions.logical();
                            Operate operate = null;
                            for (String operateId : operates) {
                                Set<Operate> operateSet = module.getOperates();
                                for (Operate o : operateSet) {
                                    if (o.getOperateId().equals(operateId)) {
                                        operate = o;
                                        break;
                                    }
                                }
                                if (operate != null) {
                                    request.setAttribute(Constants.Request.operate, operate);
                                    // 鉴权
                                    flag = rbacPermissionService.checkPermissionCache(user.getRoles(), moduleId, operate.getOperateId());
//                                    flag = false;
                                    if (Logical.OR == logical) {// 有一个权限就可以通过
                                        if (flag == false) {
                                            continue;
                                        } else {
                                            break;
                                        }
                                    } else {// 有所有权限才可以通过
                                        if (flag == false) break;
                                    }
                                    operateName = operate.getOperateName();
                                }
                            }
                        }

                        if (module.getModuleStatus() == false) {// 不显示该模块
                            flag = false;
                        }
                        // 上级模块是否显示
                        String parentId = module.getParentId();
                        while (true) {
                            Module parentModule = cacheService.getModuleMap().get(parentId);
                            if (parentModule != null) {
                                if (parentModule.getModuleStatus() == false) {
                                    flag = false;
                                    break;
                                } else {
                                    parentId = parentModule.getParentId();
                                }
                            } else {
                                break;
                            }
                        }
//                      flag = false;
                        if (flag == false) {
                            logger.info("用户：" + user.getUserId() + "，您没有权限！执行：" + moduleName + "，" + operateName);
                            Message message = new Message();
                            message.setMsg(StatusCode.Sys.noPermissions);
                            message.setObj(module.getModuleName() + "：" + operateName);
                            output(request, response, handlerMethod, message, "right", goPage);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	    
//	    SysUser user = CacheUtil.getSysUser(request);
//		// 对没有配置URL-MODULEID的请求不进行鉴权
//		String servletPath = request.getServletPath();
//		if (servletPath.substring(0, 1).equals("/")) servletPath = servletPath.substring(1);
//		int separatorCharIndex = servletPath.lastIndexOf("/") + 1;
//		String tempServletPath = servletPath.substring(0, separatorCharIndex);
//		tempServletPath += "view.do";
//		Module module = CacheUtil.moduleUrlMap.get(tempServletPath);
//		if (module != null) {
//			request.setAttribute(Constants.REQUEST.MODULE, module);
//			request.setAttribute("rightModuleId", module.getModuleId());
//			// 获取执行的方法
//			String operateStr = servletPath.substring(separatorCharIndex, servletPath.lastIndexOf("."));
//			Operate operate = CacheUtil.operateMap.get(operateStr);
//			String operateId = null;
//			if (operate != null) {
//				request.setAttribute(Constants.REQUEST.OPERATE, operate);
//				operateId = operate.getOperateId();
//				// 鉴权
//				if (!rbacPermissionService.checkPermission(user.getRoles(), module.getModuleId(), operateId)) {
//					request.getSession().setAttribute("filterMsg", "right");
//					logger.info("用户：" + user.getUserId() + "，您没有权限！执行：" + module.getModuleName() + "，" + operate.getOperateName());
//					request.getRequestDispatcher("/overtime.jsp").forward(request, response);
//					return false;
//				} 
//			}
//		}
	    
//	    SysUser user = CacheUtil.getSysUser(request);
//        // 对没有配置URL-MODULEID的请求不进行鉴权
//	    String queryString = request.getQueryString();
//        String servletPath = request.getServletPath() + "?" + queryString;
//        if (servletPath.substring(0, 1).equals("/")) servletPath = servletPath.substring(1);
//        int separatorCharIndex = servletPath.indexOf("?") + 1;
//        String tempServletPath = servletPath.substring(0, separatorCharIndex);
//        tempServletPath += "view";
//        Module module = CacheUtil.moduleUrlMap.get(tempServletPath);
//        if (module != null) {
//            request.setAttribute(Constanst.REQUEST.MODULE, module);
//            request.setAttribute("rightModuleId", module.getModuleId());
//            String operateStr = "";
//            // 获取执行的方法
//            int paramIndex = servletPath.indexOf("&");
//            if (paramIndex == -1) {// 无&
//                operateStr = servletPath.substring(separatorCharIndex);
//            } else {
//                operateStr = servletPath.substring(separatorCharIndex, paramIndex);
//            }
//            Operate operate = CacheUtil.operateMap.get(operateStr);
//            String operateId = null;
//            if (operate != null) {
//                request.setAttribute(Constanst.REQUEST.OPERATE, operate);
//                operateId = operate.getOperateId();
//                // 鉴权
//                if (!rbacPermissionService.checkPermission(user.getRoles(), module.getModuleId(), operateId)) {
//                    request.getSession().setAttribute("filterMsg", "right");
//                    logger.info("用户：" + user.getUserId() + "，您没有权限！执行：" + module.getModuleName() + "，" + operate.getOperateName());
//                    request.getRequestDispatcher("/overtime.jsp").forward(request, response);
//                    return false;
//                } 
//            }
//        }
		return true;
	}

}
