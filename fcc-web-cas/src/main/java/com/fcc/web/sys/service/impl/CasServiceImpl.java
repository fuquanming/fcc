/*
 * @(#)BaseCasService.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web-cas
 * 创建日期 : 2018年3月8日
 * 修改历史 : 
 *     1. [2018年3月8日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.stereotype.Service;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.enums.UserStatus;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CasService;
import com.fcc.web.sys.service.SysUserService;
import com.fcc.web.sys.view.CasUser;

/**
 * Cas登录基类-其他类可以重写如定义CasServer，应用该类
 * @version 
 * @author 傅泉明
 */
@Service
public class CasServiceImpl implements CasService {

    private Logger logger = Logger.getLogger(CasServiceImpl.class);
    
    @Resource
    private SysUserService sysUserService;
    @Resource
    private BaseService baseService;
    
    @Override
    public SysUser login(Assertion assertion) throws RefusedException, Exception {
//      AttributePrincipal p= assertion.getPrincipal();
        String loginName = assertion.getPrincipal().getName();
        SysUser user = (SysUser) baseService.get(SysUser.class, loginName);
        if (user == null) {// cas 的账号，新建该账号
            user = new SysUser();
            user.setUserId(loginName);
            user.setUserName(loginName);
            user.setUserType(CasUser.userTypeKey);// 用户类型决定角色
            
            user.setUserStatus(UserStatus.normal.name());
            user.setDept(CasUser.userTypeKey);
            user.setRegDate(new Date());
            user.setCreateTime(user.getRegDate());
            user.setCreateUser(CasUser.userTypeKey);
            sysUserService.add(user, null);
            logger.info("create cas user=" + loginName);
        }
        user = sysUserService.getLoginUser(loginName);
        return user;
    }
    
}
