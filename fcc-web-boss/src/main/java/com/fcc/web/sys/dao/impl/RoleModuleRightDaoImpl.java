/*
 * @(#)RoleModuleRightDaoImpl.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-app-web Maven Webapp
 * 创建日期 : 2016年10月17日
 * 修改历史 : 
 *     1. [2016年10月17日]创建文件 by fqm
 */
package com.fcc.web.sys.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.web.sys.dao.RoleModuleRightDao;
import com.fcc.web.sys.model.RoleModuleRight;

/**
 *
 * @version v1.0
 * @author 傅泉明
 */
@SuppressWarnings("unchecked")
@Repository
public class RoleModuleRightDaoImpl implements RoleModuleRightDao {

    @Resource
    private BaseDao baseDao;
    /**
     * 
     * @see com.fcc.web.sys.dao.RoleModuleRightDao#deleteByRoleIds(java.lang.String[])
     **/
    @Override
    public Integer deleteByRoleIds(String[] roleIds) {
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("roleId", roleIds);
        return baseDao.executeHql("delete from RoleModuleRight where roleId in(:roleId)", param);
    }

    @Override
    public Integer deleteByModuleId(String moduleId) {
        return baseDao.executeHql("delete from RoleModuleRight where moduleId like ?", moduleId + "%");
    }
    
    @Override
    public Integer deleteByModuleId(String moduleId, String roleId) {
        return baseDao.executeHql("delete RoleModuleRight where moduleId=? and roleId=?", moduleId, roleId);
    }
    
    @Override
    public Integer deleteByNoModuleId(List<String> moduleIdList, String roleId) {
        Map<String, Object> param = new HashMap<String, Object>(2);
        param.put("moduleId", moduleIdList);
        param.put("roleId", roleId);
        return baseDao.executeHql("delete RoleModuleRight where moduleId not in(:moduleId) and roleId=:roleId", param);
    }
    
    @Override
    public List<RoleModuleRight> getModuleRightByModuleId(String moduleId) {
        List<RoleModuleRight> list = baseDao.find("select rm from RoleModuleRight as rm where rm.moduleId =? and rm.rightValue>0 order by moduleId ", moduleId);
        return list;
    }
    
    @Override
    public RoleModuleRight getModuleRightByKey(String roleId, String moduleId) {
        RoleModuleRight mr = null;
        List<RoleModuleRight> list = baseDao.find("from RoleModuleRight where roleId=? and moduleId=?", roleId, moduleId);
        if (list.size() > 0) mr = list.get(0);
        return mr;
    }
    
    @Override
    public List<RoleModuleRight> getRoleModuleRight(String roleId) {
        return baseDao.find("from RoleModuleRight where roleId=?", roleId);
    }
}
