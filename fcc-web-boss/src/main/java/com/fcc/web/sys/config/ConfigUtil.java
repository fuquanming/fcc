/*
 * @(#)ConfigUtil.java
 * 
 * Copyright (c) 2015 www.ssit-xm.com.cn, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年10月19日
 * 修改历史 : 
 *     1. [2016年10月19日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.config;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public class ConfigUtil {

    /**
     * 是否是开发模式
     * @return
     */
    public static boolean isDev() {
        String mode = Resources.CONFIG.getString("app.mode");
        if ("dev".equals(mode)) {
            return true;
        }
        return false;
    }
    
}
