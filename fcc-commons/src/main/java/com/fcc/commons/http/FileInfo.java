/*
 * @(#)FileInfo.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : wangji-file-client
 * 创建日期 : 2017年12月26日
 * 修改历史 : 
 *     1. [2017年12月26日]创建文件 by 傅泉明
 */
package com.fcc.commons.http;

import java.io.File;

/**
 * 文件信息
 * @version 
 * @author 傅泉明
 */
public class FileInfo {
    /** form表单上传的name */
    private String formName;
    /** 文件名 */
    private String fileName;
    /** 文件字节数组 */
    private byte[] fileByte;
    /** 本地文件 */
    private File file;
    
    public String getFormName() {
        return formName;
    }
    public void setFormName(String formName) {
        this.formName = formName;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public byte[] getFileByte() {
        return fileByte;
    }
    public void setFileByte(byte[] fileByte) {
        this.fileByte = fileByte;
    }
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
}
