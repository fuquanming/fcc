package com.fcc.web.sys.tag;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.RbacPermissionService;

/**
 * <p>Description:鉴权Tag</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class RbacPermissionTag extends BodyTagSupport {

	private static final long serialVersionUID = 5357399722954554280L;

	private static RbacPermissionService rbacPermissionService;
	/**
	 * 系统模块ID 
	 */
	private String moduleId;
	
	/**
	 * 系统模块ID 
	 */
	private String operateId;
	
	public RbacPermissionTag() {
		super();
	}

	/**
	 * 重载父类方法
	 * 进行鉴权和内容输出
	 */
	@Override
	public int doStartTag() throws JspException{
//		HttpSession session = pageContext.getSession();
		// 获取用户sessionBean
	    if (rbacPermissionService == null) {
	        rbacPermissionService = WebApplicationContextUtils
	                .getWebApplicationContext(pageContext.getServletContext())
	                .getBean(RbacPermissionService.class);
	    }
		SysUser user = CacheUtil.getSysUser((HttpServletRequest)pageContext.getRequest());
		if (rbacPermissionService.checkPermissionCache(user.getRoles(), moduleId, operateId)) {
			return EVAL_BODY_INCLUDE;
		}
		return SKIP_BODY;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
    @Override
	public final int doEndTag() throws JspException {
    	 return EVAL_PAGE;
    }


	public String getModuleId() {
		return moduleId;
	}


	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}


	public String getOperateId() {
		return operateId;
	}


	public void setOperateId(String operateId) {
		this.operateId = operateId;
	}

//	public RbacPermissionService getRbacPermissionService() {
//		return rbacPermissionService;
//	}
//	@Resource
//	public void setRbacPermissionService(RbacPermissionService rbacPermissionService) {
//		RbacPermissionTag.rbacPermissionService = rbacPermissionService;
//	}

}
