/*
 * @(#)FileLocalServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web-manage
 * 创建日期 : 2018年1月23日
 * 修改历史 : 
 *     1. [2018年1月23日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import com.fcc.commons.file.BaseFileService;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.config.ConfigUtil;
import com.fcc.web.sys.model.SysAnnex;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Service
public class FileServiceImpl extends BaseFileService {
    /**
     * 获取文件保存的父文件
     * @return
     */
    private File getParentFile(SysAnnex sysAnnex) {
        Date now = new Date();
        String timePath = DateFormatUtils.format(now, "yyyyMMdd");
        String linkType = sysAnnex.getLinkType();
        String annexType = sysAnnex.getAnnexType();
        StringBuilder sb = new StringBuilder();
        sb.append(ConfigUtil.getFileUploadPath())
        .append(Constants.uploadFilePath)
        .append(File.separatorChar).append(linkType)
        .append(File.separatorChar).append(annexType)
        .append(File.separatorChar).append(timePath);
        File parentFile = new File(sb.toString());
        if (parentFile.exists() == false) parentFile.mkdirs();
        return parentFile;
    }
    /**
     * @see com.fcc.commons.file.FileService#uploadFile(java.io.File, java.lang.String)
     **/
    @Override
    public String uploadFile(File file, String fileName) {
        Object obj = getObject();
        if (obj != null && obj instanceof SysAnnex) {
            SysAnnex sysAnnex = (SysAnnex) obj;
            Date now = new Date();
            String timePath = DateFormatUtils.format(now, "yyyyMMdd");
            String linkType = sysAnnex.getLinkType();
            String annexType = sysAnnex.getAnnexType();
            File parentFile = getParentFile(sysAnnex);
            try {
                FileUtils.moveFileToDirectory(file, parentFile, true);
                StringBuilder fileUrlSb = new StringBuilder();
                fileUrlSb.append(Constants.uploadFilePath)
                .append("/").append(linkType).append("/").append(annexType)
                .append("/").append(timePath).append("/").append(sysAnnex.getFileName());
                return fileUrlSb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @see com.fcc.commons.file.FileService#uploadFile(byte[], java.lang.String)
     **/
    @Override
    public String uploadFile(byte[] fileByte, String fileName) {
        Object obj = getObject();
        if (obj != null && obj instanceof SysAnnex) {
            SysAnnex sysAnnex = (SysAnnex) obj;
            Date now = new Date();
            String timePath = DateFormatUtils.format(now, "yyyyMMdd");
            String linkType = sysAnnex.getLinkType();
            String annexType = sysAnnex.getAnnexType();
            File parentFile = getParentFile(sysAnnex);
            try {
                File file = new File(parentFile, sysAnnex.getFileName());
                FileUtils.writeByteArrayToFile(file, fileByte);
                StringBuilder fileUrlSb = new StringBuilder();
                fileUrlSb.append(Constants.uploadFilePath)
                .append("/").append(linkType).append("/").append(annexType)
                .append("/").append(timePath).append("/").append(sysAnnex.getFileName());
                return fileUrlSb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @see com.fcc.commons.file.FileService#uploadFile(java.io.File, java.lang.String, java.lang.String, java.lang.String)
     **/
    @Override
    public String uploadFile(File file, String fileName, String masterFileId, String prefixName) {
        return null;
    }

    /**
     * @see com.fcc.commons.file.FileService#uploadFile(byte[], java.lang.String, java.lang.String, java.lang.String)
     **/
    @Override
    public String uploadFile(byte[] fileByte, String fileName, String masterFileId, String prefixName) {
        return null;
    }

    /**
     * @see com.fcc.commons.file.FileService#downloadFile(java.lang.String)
     **/
    @Override
    public InputStream downloadFile(String fileId) {
        StringBuilder sb = new StringBuilder();
        sb.append(ConfigUtil.getFileUploadPath()).append(fileId);
        File file = new File(sb.toString());
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @see com.fcc.commons.file.FileService#deleteFile(java.lang.String)
     **/
    @Override
    public int deleteFile(String fileId) {
        StringBuilder sb = new StringBuilder();
        sb.append(ConfigUtil.getFileUploadPath()).append(fileId);
        File file = new File(sb.toString());
        if (file.exists()) {
            return file.delete() ? 0 : -1;
        }
        return -1;
    }

}
