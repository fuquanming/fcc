/*
 * @(#)SysUserDao.java
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
import com.fcc.web.sys.model.SysUser;

/**
 * 系统用户
 * @version v1.0
 * @author 傅泉明
 */
public interface SysUserDao {
    /**
     * 更新系统用户
     * @param sysUser
     */
    public Integer update(SysUser data);
    
    /**
     * 更新用户状态
     * @param userIds
     * @param userStatus
     * @return
     */
    public Integer updateStatus(String[] userIds, String userStatus);
    
    /**
     * 更新密码
     * @param userId
     * @param newPassword
     * @return
     */
    public Integer updatePassword(String userId, String newPassword);
    
    /**
     * 重置用户密码
     * @param userIds
     * @param userPass
     * @param createUser
     */
    public Integer resetPassword(String[] userIds, String userPass);
    /**
     * 重置用户密码
     * @param userId
     * @param userPass
     * @param createUser
     */
    public Integer resetPassword(String userId, String userPass);
    
    /**
     * 删除用户
     * @param userIds
     * @return
     */
    public Integer deleteUser(String[] userIds);
    
    /**
     * 通过用户角色ID取得用户
     * @param roleIdList
     * @return
     */
    public List<SysUser> getUserByRoleIds(List<String> roleIdList);
    
    /**
     * 
     * @param userName
     * @return
     */
    public SysUser findByUsername(String userName);
    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param param
     * @return
     */
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);
    
}
