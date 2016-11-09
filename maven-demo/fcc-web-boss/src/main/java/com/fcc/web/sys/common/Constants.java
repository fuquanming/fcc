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
public interface Constants {
    /**
     * 当前登录的用户
     */
    String CURRENT_USER = "user";
    String CURRENT_USERNAME = "username";
    /** 验证码 */
    String RAND_CODE_KEY = "rand_code_key";
    /** 系统提供导出数据下载文件夹 */
    String EXPORT_DATA_FILENAME = "exportData";
    /** 系统提供导入数据文件夹 */
    String IMPORT_DATA_FILENAME = "importData";
    /** 系统提供导出数据每次查询数据量 */
    int EXPORT_DATA_PAGE_SIZE = 5000;
    
    /** 系统用户session中的变量 */
    public interface SysUserSession {
        /** 登陆系统的用户 */
        String login = "USER_LOGIN_SYS";
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
                TEXT_MAP.put(login, "退出");
            }
        }
    }
    
    
    public interface StatusCode {
        /** 系统 */
        public interface Sys {
            /** 成功 sys_000 */
            String success = "sys_000";
            /** 失败 sys_001 */
            String fail = "sys_001";
            /** 修改记录时，ID为空 sys_002 */
            String emptyUpdateId = "sys_002";
            /** 删除记录时，ID为空 sys_003 */
            String emptyDeleteId = "sys_003";
            /** session过期 sys_004 */
            String sessionTimeout = "sys_004";
            
            
        }
        
        /** 上传文件 */
        public interface Import {
            /** 已上传成功！系统正在导入数据... import_000 */
            String importNow = "import_000";
            /** 系统正在执行上次导入数据，请稍后... import_001 */
            String importBusy = "import_001";
            /** 请选择上传文件 import_002 */
            String emptyFile = "import_002";
        }
        
        /** 导出文件 */
        public interface Export {
            /** 系统正在导出数据... export_000 */
            String exportNow = "export_000";
            /** 系统正在执行上次导出数据，请稍后... export_001 */
            String exportBusy = "export_001";
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
        
        public interface Module {
            /** 模块名称为空 module_000 */
            String emptyModuleName = "module_000";
            /** 父模块为空 module_001 */
            String emptyParentModule = "module_001";
            /** 不能修改根节点 module_002 */
            String errorRootModuleId = "module_002";
            /** 修改的模块不存在 module_003 */
            String errorModuleId = "module_003";
        }
        
        public interface Operate {
            /** 操作ID为空 operate_000 */
            String emptyOperateId = "operate_000";
            /** 操作名称为空 operate_001 */
            String emptyOperateName = "operate_001";
            /** 操作ID已存在 operate_002 */
            String exitsOperateId = "operate_002";
            /** 超过最大操作个数 operate_003 */
            String maxOperateValue = "operate_003";
        }
        
        public interface Role {
            /** 角色名称为空 role_000 */
            String emptyRoleName = "role_000";
        }
        
        public interface Organization {
            /** 机构名称为空 organization_000 */
            String emptyOrganizationName = "organization_000";
            /** 父机构为空 organization_001 */
            String emptyParentOrganization = "organization_001";
            /** 不能修改根节点 organization_002 */
            String errorRootOrganizationId = "organization_002";
            /** 修改的机构不存在 organization_003 */
            String errorOrganizationId = "organization_003";
            /** 该组织机构下有人员！ organization_004 */
            String hasUser = "organization_004";
        }
        
        public interface SysUser {
            /** 用户账号为空 sysUser_000 */
            String emptyUserId = "sysUser_000";
            /** 机构ID为空 sysUser_001 */
            String emptyOrganID = "sysUser_001";
            /** 用户账号已存在 sysUser_002 */
            String exitsUserId = "sysUser_002";
        }
        
        /** 用户密码 */
        public interface UserPassword {
            /** 旧密码为空 userPassword_000 */
            String emptyOldPassword = "userPassword_000";
            /** 新密码为空 userPassword_001 */
            String emptyNewPassword = "userPassword_001";
            /** 确认码为空 userPassword_002 */
            String emptyConfirmPassword = "userPassword_002";
            /** 旧密码错误 userPassword_003 */
            String errorOldPassword = "userPassword_003";
            /** 新密码和确认码不一致 userPassword_004 */
            String errorPasswordConsistent = "userPassword_004";
        }
    }
}
