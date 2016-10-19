/*
 * @(#)RoleDaoImpl.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-app-web Maven Webapp
 * 创建日期 : 2016年10月17日
 * 修改历史 : 
 *     1. [2016年10月17日]创建文件 by fqm
 */
package com.fcc.web.sys.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.dao.RoleDao;

/**
 * 
 * @version v1.0
 * @author 傅泉明
 */
@Repository
public class RoleDaoImpl implements RoleDao {

    @Resource
    private BaseDao baseDao;
    
    @Override
    public void createUserRole(String userId, String[] roleIds) {
        int length = roleIds.length;
        for (int i = 0; i < length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into sys_rbac_usertorole (user_id, role_id) values('")
            .append(userId).append("','").append(roleIds[i]).append("')");
            baseDao.executeSql(sb.toString());
        }
    }
    
    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.dao.RoleDao#deleteUserRoleByUserId(java.lang.String)
     **/
    @Override
    public Integer deleteUserRoleByUserId(String userId) {
        return baseDao.executeSql("delete from sys_rbac_usertorole where user_id =?", userId);
    }

    @Override
    public Integer deleteUserRoleByUserId(String[] userIds) {
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("userId", userIds);
        return baseDao.executeSql("delete from sys_rbac_usertorole where user_id in(:userId)", param);
    }
    
    @Override
    public Integer deleteUserRoleByRoleId(String[] roleIds) {
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("roleId", roleIds);
        return baseDao.executeSql("delete from sys_rbac_usertorole where role_Id in(:roleId)", param);
    }
    
    @Override
    public Integer delete(String[] roleIds) {
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("roleId", roleIds);
        return baseDao.executeHql("delete from Role where roleId in(:roleId)", param);
    }
    
    @Override
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param) {
        StringBuilder cHql = new StringBuilder();
        StringBuilder bHql = new StringBuilder();
        HashMap<String, Object> map = new HashMap<String, Object>();
        cHql.append("select count(r) from Role r where 1=1 ");
        bHql.append("from Role r where 1=1 ");
        if (param != null) {
            String roleName = (String) param.get("roleName");
            if (roleName != null && !"".equals(roleName)) {
                map.put("roleName", "%" + roleName + "%");
                cHql.append(" and r.roleName like:roleName");
                bHql.append(" and r.roleName like:roleName");
            }
            Object roleId = (String) param.get("roleId");
            if (roleId != null) {
                if (roleId instanceof String && !"".equals(roleId)) {
                    map.put("roleId", roleId);
                    cHql.append(" and r.roleId =:roleId");
                    bHql.append(" and r.roleId =:roleId");
                } else if (roleId instanceof Collection) {
                    map.put("roleId", roleId);
                    cHql.append(" and r.roleId in(:roleId)");
                    bHql.append(" and r.roleId in(:roleId)");
                } else if (roleId instanceof Object[]) {
                    map.put("roleId", roleId);
                    cHql.append(" and r.roleId in(:roleId)");
                    bHql.append(" and r.roleId in(:roleId)");
                }
            }
            String createUser = (String) param.get("createUser");
            if (createUser != null && !"".equals(createUser)) {
                map.put("createUser", createUser);
                cHql.append(" and r.createUser =:createUser");
                bHql.append(" and r.createUser =:createUser");
            }
        }
        bHql.append(" order by roleOrderNo ");
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), map, false);
    }
}
