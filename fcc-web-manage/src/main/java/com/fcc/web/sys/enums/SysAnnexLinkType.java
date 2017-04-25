/*
 * @(#)SysAnnexLinkType.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2017年1月26日
 * 修改历史 : 
 *     1. [2017年1月26日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.enums;

/**
 * 系统附件关联类型
 * @version 
 * @author 傅泉明
 */
public enum SysAnnexLinkType {
    /** 系统用户 */
    sysUser("系统用户");
    
    private final String info;

    private SysAnnexLinkType(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
    
}
