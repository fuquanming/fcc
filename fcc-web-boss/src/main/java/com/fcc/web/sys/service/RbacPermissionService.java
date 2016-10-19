package com.fcc.web.sys.service;

import java.util.Set;

import com.fcc.web.sys.model.Role;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface RbacPermissionService {
	/**
	 * 鉴权 数据库
	 * @param userRoleSet
	 * @param moduleId
	 * @param operateId
	 * @return
	 */
	public boolean checkPermission(Set<Role> userRoleSet , String moduleId , String operateId);
	/**
	 * 鉴权 缓存
	 * @param userRoleSet
	 * @param moduleId
	 * @param operateId
	 * @return
	 */
	public boolean checkPermissionCache(Set<Role> userRoleSet, String moduleId, String operateId);
	
}
