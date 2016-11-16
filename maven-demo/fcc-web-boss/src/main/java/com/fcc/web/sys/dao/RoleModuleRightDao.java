/*
 * @(#)RoleModuleRightDao.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-app-web Maven Webapp
 * 创建日期 : 2016年10月17日
 * 修改历史 : 
 *     1. [2016年10月17日]创建文件 by fqm
 */
package com.fcc.web.sys.dao;

import java.util.Collection;
import java.util.List;

import com.fcc.web.sys.model.RoleModuleRight;

/**
 * 角色与模块权限
 * @version v1.0
 * @author 傅泉明
 */
public interface RoleModuleRightDao {

    /**
     * 删除角色模块权限通过角色ID 
     * @param roleIds
     * @return
     */
    public Integer deleteByRoleIds(String[] roleIds);
    
    /**
     * 删除角色模块权限通过模块ID 
     * @param roleIds
     * @return
     */
    public Integer deleteByModuleId(String moduleId);
    
    /**
     * 删除角色模块权限通过模块ID 
     * @param roleIds
     * @return
     */
    public Integer deleteByModuleId(String moduleId, String roleId);
    
    /**
     * 删除角色模块权限通过模块ID 
     * @param roleIds
     * @return
     */
    public Integer deleteByNoModuleId(List<String> moduleIdList, String roleId);
    
    public List<RoleModuleRight> getModuleRightByModuleId(String moduleId);
    
    public List<RoleModuleRight> getModuleRightByModuleIds(String[] moduleIds);
    
    public List<RoleModuleRight> getModuleRightByRoleId(Collection<String> roleIds);
    
    public RoleModuleRight getModuleRightByKey(String roleId, String moduleId);
    
    public List<RoleModuleRight> getRoleModuleRight(String roleId);
}
