/*
 * @(#)StatusCode.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年12月23日
 * 修改历史 : 
 *     1. [2016年12月23日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.common;

/**
 * 应用状态码
 * @version 
 * @author 傅泉明
 */
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
        /** 无权限 sys_005 */
        String noPermissions = "sys_005";
        /** 堆空间不足 sys_006 */
        String heapSpace = "sys_006";
    }
}