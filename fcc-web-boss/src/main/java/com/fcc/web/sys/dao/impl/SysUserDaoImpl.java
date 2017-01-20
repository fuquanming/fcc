/*
 * @(#)SysUserDaoImpl.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-app-web Maven Webapp
 * 创建日期 : 2016年10月17日
 * 修改历史 : 
 *     1. [2016年10月17日]创建文件 by fqm
 */
package com.fcc.web.sys.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.dao.SysUserDao;
import com.fcc.web.sys.model.SysUser;

/**
 * //TODO 添加类/接口功能描述
 *
 * @version v1.0
 * @author 傅泉明
 */
@Repository
public class SysUserDaoImpl implements SysUserDao {

    @Resource
    private BaseDao baseDao;
    
    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.dao.SysUserDao#update(com.fcc.web.sys.model.SysUser)
     **/
    @Override
    public Integer update(SysUser data) {
        Map<String, Object> param = new HashMap<String, Object>(7);
        param.put("userId", data.getUserId());
        param.put("userName", data.getUserName());
        param.put("email", data.getEmail());
        param.put("mobile", data.getMobile());
        param.put("tel", data.getTel());
        param.put("remark", data.getRemark());
        param.put("dept", data.getDept());
        return baseDao.executeHql("update SysUser set userName=:userName, email=:email, mobile=:mobile, tel=:tel, remark=:remark, dept=:dept where userId=:userId", param);
    }

    @Override
    public Integer updateStatus(String[] userIds, String userStatus) {
        Map<String, Object> param = new HashMap<String, Object>(2);
        param.put("userStatus", userStatus);
        param.put("userId", userIds);
        return baseDao.executeHql("update from SysUser set userStatus=:userStatus where userId in(:userId)", param);
    }
    
    @Override
    public Integer updatePassword(String userId, String newPassword) {
        return baseDao.executeHql("update SysUser set password =? where userId=?", newPassword, userId);
    }
    
    @Override
    public Integer resetPassword(String[] userIds, String userPass) {
        Map<String, Object> param = new HashMap<String, Object>(2);
        param.put("userId", userIds);
        param.put("userPass", userPass);
        return baseDao.executeHql("update SysUser set password=:userPass where userId in(:userId)", param);
    }
    
    @Override
    public Integer resetPassword(String userId, String userPass) {
        Map<String, Object> param = new HashMap<String, Object>(2);
        param.put("userId", userId);
        param.put("userPass", userPass);
        return baseDao.executeHql("update SysUser set password=:userPass where userId =:userId", param);
    }
    
    @Override
    public Integer deleteUser(String[] userIds) {
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("userId", userIds);
        return baseDao.executeHql("delete from SysUser where userId in(:userId)", param);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public List<SysUser> getUserByRoleIds(List<String> roleIdList) {
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("roleId", roleIdList);
        List list = baseDao.findSQL("select u.user_id, u.user_name from sys_rbac_user u join SYS_RBAC_USERTOROLE r on u.user_id=r.user_id  where r.role_Id in(:roleId)", param);
        List<SysUser> userList = new ArrayList<SysUser>();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Object[] obj = (Object[])list.get(i);
            Object userIdObj = obj[0];
            Object userNameObj = obj[1];
            if (userIdObj == null) continue;
            if (userNameObj == null) continue;
            SysUser user = new SysUser();
            user.setUserId(userIdObj.toString());
            user.setUserName(userNameObj.toString());
            userList.add(user);
        }
        return userList;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public SysUser findByUsername(String userName) {
        List<SysUser> list = baseDao.find("from SysUser where userId=?", userName);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param) {
        StringBuilder cHql = new StringBuilder();
        StringBuilder bHql = new StringBuilder();
        HashMap<String, Object> map = new HashMap<String, Object>();
        cHql.append("select count(r) from SysUser r where 1=1 ");
        bHql.append("from SysUser r where 1=1 ");
        if (param != null) {
            String userId = (String) param.get("userId");
            if (userId != null && !"".equals(userId)) {
                map.put("userId", "%" + userId + "%");
                cHql.append(" and r.userId like:userId");
                bHql.append(" and r.userId like:userId");
            }
            String userName = (String) param.get("userName");
            if (userName != null && !"".equals(userName)) {
                map.put("userName", "%" + userName + "%");
                cHql.append(" and r.userName like:userName");
                bHql.append(" and r.userName like:userName");
            }
            String dept = (String) param.get("dept");
            if (dept != null && !"".equals(dept)) {
                map.put("dept", dept + "%");
                cHql.append(" and r.dept like:dept");
                bHql.append(" and r.dept like:dept");
            }
            String createUser = (String) param.get("createUser");
            if (createUser != null && !"".equals(createUser)) {
                map.put("createUser", createUser);
                cHql.append(" and r.createUser =:createUser");
                bHql.append(" and r.createUser =:createUser");
            }
//            String isAdmin = (String) param.get("isAdmin");
//            if (isAdmin != null && !"".equals(isAdmin) && createUser != null && !"".equals(createUser)) {
//                map.put("userIds", createUser);
//                cHql.append(" and r.userId <>:userIds");
//                bHql.append(" and r.userId <>:userIds");
//                map.remove("isAdmin");
//            }
        }
        bHql.append(" order by userId desc");
        ListPage listPage = baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), map, false);
        List<SysUser> dataList = listPage.getDataList();
        for (SysUser data : dataList) {
            data.getRoles().size();
        }
        return listPage;
    }
}
