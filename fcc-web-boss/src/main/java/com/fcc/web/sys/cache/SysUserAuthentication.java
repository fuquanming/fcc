/*
 * @(#)Authentication.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2017年4月1日
 * 修改历史 : 
 *     1. [2017年4月1日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.cache;

import com.fcc.web.sys.model.SysUser;

/**
 * 缓存当前线程的用户信息
 * @version 
 * @author 傅泉明
 */
public class SysUserAuthentication {
    
    static ThreadLocal<SysUser> sysUserThreadLocal = new ThreadLocal<SysUser>();

    public static void setSysUser(SysUser sysUser) {
        sysUserThreadLocal.set(sysUser);
    }

    public static SysUser getSysUser() {
        return sysUserThreadLocal.get();
    }
}
