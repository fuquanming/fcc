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

import java.util.Map;

import com.fcc.commons.data.ListPage;

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
     * 删除用户角色 
     * @param userId
     */
    public Integer deleteRoleByUserId(String userId);
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
    
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);
    
}
