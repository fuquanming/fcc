/*
 * @(#)FileFastDFSServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-fastdfs
 * 创建日期 : 2018年1月23日
 * 修改历史 : 
 *     1. [2018年1月23日]创建文件 by 傅泉明
 */
package com.fcc.commons.fastdfs;

import java.io.File;
import java.io.InputStream;

import com.fcc.commons.file.BaseFileService;

/**
 * FastDFS 文件操作
 * @version 
 * @author 傅泉明
 */
public class FileServiceFastDFSImpl extends BaseFileService {

    /**
     * @see com.fcc.commons.file.FileService#uploadFile(java.io.File, java.lang.String)
     **/
    @Override
    public String uploadFile(File file, String fileName) {
        return FastDFSClient.uploadFile(file, fileName);
    }

    /**
     * @see com.fcc.commons.file.FileService#uploadFile(byte[], java.lang.String)
     **/
    @Override
    public String uploadFile(byte[] fileByte, String fileName) {
        return FastDFSClient.uploadFile(fileByte, fileName);
    }

    /**
     * @see com.fcc.commons.file.FileService#uploadFile(java.io.File, java.lang.String, java.lang.String, java.lang.String)
     **/
    @Override
    public String uploadFile(File file, String fileName, String masterFileId, String prefixName) {
        return FastDFSClient.uploadFile(file, fileName, masterFileId, prefixName);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.file.FileService#uploadFile(byte[], java.lang.String, java.lang.String, java.lang.String)
     **/
    @Override
    public String uploadFile(byte[] fileByte, String fileName, String masterFileId, String prefixName) {
        return FastDFSClient.uploadFile(fileByte, fileName, masterFileId, prefixName);
    }

    /**
     * @see com.fcc.commons.file.FileService#downloadFile(java.lang.String)
     **/
    @Override
    public InputStream downloadFile(String fileId) {
        return FastDFSClient.downloadFile(fileId);
    }

    /**
     * @see com.fcc.commons.file.FileService#deleteFile(java.lang.String)
     **/
    @Override
    public int deleteFile(String fileId) {
        return FastDFSClient.deleteFile(fileId);
    }

}
