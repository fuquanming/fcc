/*
 * @(#)Constants.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-web
 * 创建日期 : 2017年2月4日
 * 修改历史 : 
 *     1. [2017年2月4日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.common;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public interface Constants {
    /** 字符编码 */
    String encode = "UTF-8";
    /** 系统随机验证码 */
    String randCodeKey = "randCode";
    /** 上传附件临时文件目录 */
    String uploadFileTempPath = "temp";
    /** 上传附件文件目录 */
    String uploadFilePath = "fcc";
    /** 上传附件文件名分割符号 */
    String uploadFileNameSplit = "-";
    /** 系统提供导出数据下载文件夹 */
    String exportDataFileName = "exportData";
    /** 系统提供导入数据文件夹 */
    String importDataFileName = "importData";
    /** 系统提供导出数据每次查询数据量 */
    int exportDataPageSize = 5000;
}
