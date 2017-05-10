/*
 * @(#)RoleDao.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-app-web Maven Webapp
 * 创建日期 : 2016年10月17日
 * 修改历史 : 
 *     1. [2016年10月17日]创建文件 by fqm
 */
package com.fcc.web.sys.dao;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.model.Role;

/**
 * 系统角色
 * @version v1.0
 * @author 傅泉明
 */
public interface RoleDao {
    /**
     * 添加用户角色
     * @param userId
     * @param roleIds
     */
    public void addRole(String userId, String[] roleIds);
    /**
     * 添加用户类型角色
     * @param userType
     * @param roleIds
     */
    public void addUserTypeRole(String userType, String[] roleIds);
    /**
     * 删除用户角色 
     * @param userId
     */
    public Integer deleteRoleByUserId(String userId);
    /**
     * 删除用户类型角色
     * @param userType
     * @return
     */
    public Integer deleteRoleByUserType(String userType);
    /**
     * 删除用户角色 
     * @param userId
     */
    public Integer deleteRoleByUserId(String[] userIds);
    
    /**
     * 删除用户角色 
     * @param userId
     */
    public Integer deleteRoleByRoleId(String[] roleIds);
    /**
     * 删除角色 
     * @param userId
     */
    public Integer delete(String[] roleIds);
    /**
     * 通过用户类型获取角色
     * @param userType
     * @return
     */
    public List<Role> getRoleByUserType(String userType);
    
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);
    
}
