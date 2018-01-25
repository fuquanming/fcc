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

import java.io.File;

import com.fcc.commons.web.common.Constants;
import com.fcc.commons.web.config.Resources;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public class ConfigUtil {

    /** webRealPath / */
    public static String WEB_REAL_PATH = "";
    /** 文件上传临时路径 */
    private static String uploadFileTempPath = null;
    /** excel导入文件夹 */
    private static String excelImportFilePath = null;
    /** excel导出文件夹 */
    private static String execlExportFilePath = null;
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
    /** 临时文件访问的路径 */
    public static String getTempFileAccessPath() {
        return Resources.CONFIG.getString("temp.file.access.path");
    }
    /** 文件访问的路径 */
    public static String getFileAccessPath() {
        return Resources.CONFIG.getString("file.access.path");
    }
    /** 文件上传临时路径 */
    public static String getUploadFileTempPath() {
        if (uploadFileTempPath == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(ConfigUtil.getFileUploadPath())
                    .append(File.separatorChar).append(Constants.uploadFileTempPath).append(File.separatorChar);
            uploadFileTempPath  = sb.toString();
        }
        return uploadFileTempPath;
    }
    /** excel导入文件夹 */
    public static String getExcelImportFilePath() {
        if (excelImportFilePath == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(WEB_REAL_PATH).append(Constants.importDataFileName);
            excelImportFilePath  = sb.toString();
        }
        return excelImportFilePath;
    }
    /** excel导出文件夹 */
    public static String getExcelExportFilePath() {
        if (execlExportFilePath == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(WEB_REAL_PATH).append(Constants.exportDataFileName);
            execlExportFilePath  = sb.toString();
        }
        return execlExportFilePath;
    }
}
