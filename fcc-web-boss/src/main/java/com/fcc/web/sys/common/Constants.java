/*
 * @(#)Constants.java
 * 
 * Copyright (c) 2015 , All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年10月21日
 * 修改历史 : 
 *     1. [2016年10月21日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public interface Constants extends com.fcc.commons.web.common.Constants {
    
    /** 用户默认密码 */
    String defaultUserPass = "888888";
    
    /**
     * 当前登录的用户
     */
    String currentUser = "user";
    String currentUserName = "username";
    
    /** 系统用户session中的变量 */
    public interface SysUserSession {
        /** 登陆系统的用户 */
        String loginUser = "USER_LOGIN_SYS";
        /** 当前调用的action **/
        String action = "USER_ACTION_SYS";
        /** 当前用户选择的语言  **/
        String language = "USER_LANGUAGE";
        
        String menu = "USER_MENU";
        
        String menuUi = "USER_MENU_UI";
        /** 查看、修改角色信息 */
        String sessionRole = "sessionRole";
        /** 查看、修改角色权值 */
        String sessionRoleRightMap = "sessionRoleRightMap";
    }
    
//    public interface UserStatus {
//        /** 注册用户状态 未激活 0 */
//        String inactive = "0";
//        /** 注册用户状态 激活 1 */
//        String activation = "1";
//        /** 注册用户状态 注销 2 */
//        String off = "2";
//        /** 注册用户状态 锁定 3 */
//        String lock = "3";
//    }
    /** 请求对象 */
    public interface Request {
        /** 请求的模块 */
        public String module = "reuestModule";
        /** 请求的操作 */
        public String operate = "requestOperate";
        /** 请求的处理结果 */
        public String message = "requestMessage";
    }
    
    /** 自定义访问模块 */
    public interface Module {
        /** 访问系统 */
        String requestApp = "access";
        
        public static class Text {
            public static Map<String, String> TEXT_MAP = new HashMap<String, String>();
            static {
                TEXT_MAP.put(requestApp, "访问系统");
            }
        }
    }
    
    /** 自定义访问操作 */
    public interface Operate {
        /** 登陆 */
        public String login = "login";
        /** 退出 */
        public String logout = "logout";
        
        public class Text {
            public static Map<String, String> TEXT_MAP = new HashMap<String, String>();
            static {
                TEXT_MAP.put(login, "登录");
                TEXT_MAP.put(logout, "退出");
            }
        }
    }
}
