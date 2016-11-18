package com.fcc.web.sys.service;

import java.util.Set;

import com.fcc.web.sys.model.Role;

public interface RbacPermissionService {

    /**
     * 鉴权 数据库查询
     * @param userRoleSet 用户的角色列表
     * @param moduleId 模块ID
     * @param operateId 操作ID
     * @return
     */
    boolean checkPermission(Set<Role> userRoleSet, String moduleId, String operateId);

    /**
     * 鉴权 缓存查询
     * @param userRoleSet 用户的角色列表
     * @param moduleId 模块ID
     * @param operateId 操作ID
     * @return true 可以访问
     */
    boolean checkPermissionCache(Set<Role> userRoleSet, String moduleId, String operateId);

}