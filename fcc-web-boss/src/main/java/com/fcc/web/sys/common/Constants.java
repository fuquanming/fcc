/*
 * @(#)Constants.java
 * 
 * Copyright (c) 2015 www.ssit-xm.com.cn, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年10月21日
 * 修改历史 : 
 *     1. [2016年10月21日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.common;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public interface Constants {
    /**
     * 当前登录的用户
     */
    String CURRENT_USER = "user";
    String CURRENT_USERNAME = "username";
    
    public interface StatusCode {
        /** 系统 */
        public interface Sys {
            /** 成功 */
            String success = "sys_000";
            /** 失败 */
            String fail = "sys_001";
        }
        /** 用户登录 */
        public interface Login {
            /** 用户名为空 login_000 */
            String emptyUserName = "login_000";
            /** 密码为空 login_001 */
            String emptyPassword = "login_001";
            /** 验证码为空 login_002 */
            String emptyRandCode = "login_002";
            /** 验证码错误 login_003 */
            String errorRandCode = "login_003";
            /** 用户名不存在 login_004 */
            String errorUserName = "login_004";
            /** 密码错误 login_005 */
            String errorPassword = "login_005";
            /** 用户锁定 login_006 */
            String lockUserName = "login_006";
        }
    }
}
