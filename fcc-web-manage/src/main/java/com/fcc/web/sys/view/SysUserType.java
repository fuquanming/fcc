/*
 * @(#)SysUserType.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web-manage
 * 创建日期 : 2017年5月10日
 * 修改历史 : 
 *     1. [2017年5月10日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.view;

import java.util.Set;

import com.fcc.web.sys.model.Role;

/**
 * 系统用户类型
 * @version 
 * @author 傅泉明
 */
public class SysUserType {
    /** 系统用户类型 */
    private String userType;
    
    private String userTypeName;
    
    private String roleNames;
    /** 角色 */
    private Set<Role> roles;
    
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getUserTypeName() {
        return userTypeName;
    }
    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }
    public String getRoleNames() {
        return roleNames;
    }
    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
