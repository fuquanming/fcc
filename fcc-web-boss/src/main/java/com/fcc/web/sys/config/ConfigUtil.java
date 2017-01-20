/*
 * @(#)ConfigUtil.java
 * 
 * Copyright (c) 2015 , All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年10月19日
 * 修改历史 : 
 *     1. [2016年10月19日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.config;

import com.fcc.commons.web.config.Resources;

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
    /** 是否是组模式 */
    public static boolean isGroup() {
        String mode = Resources.CONFIG.getString("group.mode");
        if ("group".equals(mode)) {
            return true;
        }
        return false;
    }
    /** 文件上传的物理路径 */
    public static String getFileUploadPath() {
        return Resources.CONFIG.getString("file.upload.path");
    }
    /** 文件访问的路径 */
    public static String getFileAccessPath() {
        return Resources.CONFIG.getString("file.access.path");
    }
    
}
