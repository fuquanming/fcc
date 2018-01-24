/*
 * @(#)FileService.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web-file
 * 创建日期 : 2017年12月22日
 * 修改历史 : 
 *     1. [2017年12月22日]创建文件 by 傅泉明
 */
package com.fcc.commons.file;

import java.io.File;
import java.io.InputStream;

/**
 * 文件服务器接口
 * @version 
 * @author 傅泉明
 */
public interface FileService {
    /**
     * 获取变量参数
     * @return
     */
    public Object getObject();
    /**
     * 设置变量参数
     * @param obj
     */
    public void setObject(Object obj);
    /**
     * 上传文件
     * @param file      文件
     * @param fileName  文件名
     * @return
     */
    public String uploadFile(File file, String fileName);
    /**
     * 上传文件
     * @param fileByte  文件字节数组
     * @param fileName  文件名
     * @return
     */
    public String uploadFile(byte[] fileByte, String fileName);
    /**
     * 上传文件
     * @param file          文件
     * @param fileName      文件后缀名   如：a.jpg
     * @param masterFileId  主文件ID   如：group1/M00/00/00/wKgEfVUYPieAd6a0AAP3btxj__E335.jpg
     * @param prefixName    主文件后缀       如：_120x120
     * @return fileId   如：group1/M00/00/00/wKgEfVUYPieAd6a0AAP3btxj__E335_120x120.jpg
     */
    public String uploadFile(File file, String fileName, String masterFileId, String prefixName);
    /**
     * 上传文件
     * @param fileByte      文件字节数组 
     * @param fileName      文件后缀名   如：a.jpg
     * @param masterFileId  主文件ID   如：group1/M00/00/00/wKgEfVUYPieAd6a0AAP3btxj__E335.jpg
     * @param prefixName    主文件后缀       如：_120x120
     * @return fileId   如：group1/M00/00/00/wKgEfVUYPieAd6a0AAP3btxj__E335_120x120.jpg
     */
    public String uploadFile(byte[] fileByte, String fileName, String masterFileId, String prefixName);
    /**
     * 下载文件 
     * @param fileId 如：group1/M00/00/00/wKgEfVUYPieAd6a0AAP3btxj__E335.jpg
     * @return
     */
    public InputStream downloadFile(String fileId);
    
    /**
     * 删除文件
     * @param fileId 如：group1/M00/00/00/wKgEfVUYPieAd6a0AAP3btxj__E335.jpg     * @return  0:成功，非0：失败
     */
    public int deleteFile(String fileId);
}
