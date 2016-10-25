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
            /** 成功 sys_000 */
            String success = "sys_000";
            /** 失败 sys_001 */
            String fail = "sys_001";
            /** 修改记录时，ID为空 sys_002 */
            String emptyUpdateId = "sys_002";
            /** 删除记录时，ID为空 sys_003 */
            String emptyDeleteId = "sys_003";
            /** session过期 */
            String sessionTimeout = "sys_004";
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
