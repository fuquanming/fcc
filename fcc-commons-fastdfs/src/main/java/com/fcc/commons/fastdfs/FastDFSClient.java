/*
 * @(#)FastDFSClientFactory.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-fastdfs
 * 创建日期 : 2017年12月22日
 * 修改历史 : 
 *     1. [2017年12月22日]创建文件 by 傅泉明
 */
package com.fcc.commons.fastdfs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;

import com.fcc.commons.fastdfs.pool.ConnectionFactory;
import com.fcc.commons.fastdfs.pool.ConnectionManager;


/**
 * FastDFS 客户端
 * @version 
 * @author 傅泉明
 */
public class FastDFSClient {
    
    private static final Logger logger = Logger.getLogger(FastDFSClient.class);

    public static StorageServer getStorageServer(TrackerServer trackerServer) {
        StorageServer storageServer = null;
        try {
            storageServer = ConnectionFactory.getTrackerClient().getStoreStorage(trackerServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return storageServer;
    }
    
    public static StorageClient1 getStorageClient1(TrackerServer trackerServer) {
        StorageServer storageServer = getStorageServer(trackerServer);
        StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
        return storageClient1;
    }
    
    public static StorageClient1 getStorageClient1(TrackerServer trackerServer, StorageServer storageServer) {
        StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
        return storageClient1;
    }
    
    public static void closeStorageServer(StorageServer storageServer) {
        try {
            if (storageServer != null) storageServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取文件后缀名（不带点）.
     * 
     * @return 如："jpg" or "".
     */
    private static String getFileExt(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        } else {
            return fileName.substring(fileName.lastIndexOf(".") + 1); // 不带最后的点
        }
    }
    /**
     * 上传文件
     * @param file      文件
     * @param fileName  文件名
     * @return
     */
    public static String uploadFile(File file, String fileName) {
        FileInputStream fis = null;
        String fileid = null;
        
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            NameValuePair[] meta_list = null; // new NameValuePair[0];
            fis = new FileInputStream(file);
            byte[] file_buff = IOUtils.toByteArray(fis);

            trackerServer = ConnectionManager.getTrackerServer();
            storageServer = getStorageServer(trackerServer);
            fileid = getStorageClient1(trackerServer, storageServer).upload_file1(file_buff, getFileExt(fileName), meta_list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        } finally {
            IOUtils.closeQuietly(fis);
            closeStorageServer(storageServer);
            ConnectionManager.returnTrackerServer(trackerServer);
        }
        return fileid;
    }
    /**
     * 上传文件
     * @param fileBuff  文件字节数组
     * @param fileName  文件名
     * @return
     */
    public static String uploadFile(byte[] fileBuff, String fileName) {
        String fileid = null;
        
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            NameValuePair[] meta_list = null; // new NameValuePair[0];
            trackerServer = ConnectionManager.getTrackerServer();
            storageServer = getStorageServer(trackerServer);
            fileid = getStorageClient1(trackerServer, storageServer).upload_file1(fileBuff, getFileExt(fileName), meta_list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        } finally {
            closeStorageServer(storageServer);
            ConnectionManager.returnTrackerServer(trackerServer);
        }
        return fileid;
    }
    /**
     * 上传文件
     * @param file          文件
     * @param fileName      文件名
     * @param masterFileId  父文件ID   如：group1/M00/00/00/wKjTgFo7cNGAI8TvAALa-N2N974394.jpg
     * @param prefixName    追加文件名       如：_181x161
     * @return  如：group1/M00/00/00/wKjTgFo7cNGAI8TvAALa-N2N974394_181x161.jpg
     */
    public static String uploadFile(File file, String fileName, String masterFileId, String prefixName) {
        FileInputStream fis = null;
        String fileid = null;
        
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            NameValuePair[] meta_list = null; // new NameValuePair[0];
//            NameValuePair[] meta_list = new NameValuePair[]{ 
//                    new NameValuePair("age", "18"), 
//                    new NameValuePair("sex", "male") 
//            }; 
            fis = new FileInputStream(file);
            byte[] file_buff = IOUtils.toByteArray(fis);

            trackerServer = ConnectionManager.getTrackerServer();
            storageServer = getStorageServer(trackerServer);
            fileid = getStorageClient1(trackerServer, storageServer).upload_file1(masterFileId, prefixName, file_buff, getFileExt(fileName), meta_list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        } finally {
            IOUtils.closeQuietly(fis);
            closeStorageServer(storageServer);
            ConnectionManager.returnTrackerServer(trackerServer);
        }
        return fileid;
    }
    
    /**
     * 上传文件
     * @param fileByte      文件字节数组
     * @param fileName      文件名
     * @param masterFileId  父文件ID   如：group1/M00/00/00/wKjTgFo7cNGAI8TvAALa-N2N974394.jpg
     * @param prefixName    追加文件名       如：_181x161
     * @return  如：group1/M00/00/00/wKjTgFo7cNGAI8TvAALa-N2N974394_181x161.jpg
     */
    public static String uploadFile(byte[] fileByte, String fileName, String masterFileId, String prefixName) {
        String fileid = null;
        
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            NameValuePair[] meta_list = null; // new NameValuePair[0];
            trackerServer = ConnectionManager.getTrackerServer();
            storageServer = getStorageServer(trackerServer);
            fileid = getStorageClient1(trackerServer, storageServer).upload_file1(masterFileId, prefixName, fileByte, getFileExt(fileName), meta_list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        } finally {
            closeStorageServer(storageServer);
            ConnectionManager.returnTrackerServer(trackerServer);
        }
        return fileid;
    }
    
    /**
     * 删除文件 根据组名和远程文件名来删除一个文件
     * @param groupName 例如 "group1" 如果不指定该值，默认为group1
     * @param fileName 例如"M00/00/00/wKjTgFo7cNGAI8TvAALa-N2N974394_181x161.jpg"
     * @return 0为成功，非0为失败，具体为错误代码
     */
    public static int deleteFile(String groupName, String fileName) {
        int delFlag = -1;// 0 表示成功
        
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            trackerServer = ConnectionManager.getTrackerServer();
            storageServer = getStorageServer(trackerServer);
            delFlag = getStorageClient1(trackerServer, storageServer).delete_file(groupName == null ? "group1" : groupName, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        } finally {
            closeStorageServer(storageServer);
            ConnectionManager.returnTrackerServer(trackerServer);
        }
        return delFlag;
    }
    /**
     * 删除文件 根据fileId来删除一个文件（上传文件时直接将fileId保存在了数据库中）
     * @param fileId 如：group1/M00/00/00/wKjTgFo7cNGAI8TvAALa-N2N974394_181x161.jpg
     * @return 0为成功，非0为失败，具体为错误代码
     */
    public static int deleteFile(String fileId) {
        int delFlag = -1;// 0 表示成功
        
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            trackerServer = ConnectionManager.getTrackerServer();
            storageServer = getStorageServer(trackerServer);
            delFlag = getStorageClient1(trackerServer, storageServer).delete_file1(fileId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        } finally {
            closeStorageServer(storageServer);
            ConnectionManager.returnTrackerServer(trackerServer);
        }
        return delFlag;
    }
    
    /**
     * 文件下载
     * 
     * @param fileId    如：group1/M00/00/00/wKjTgFo7cNGAI8TvAALa-N2N974394_181x161.jpg
     * @return 返回一个流
     */
    public static InputStream downloadFile(String fileId) {
        InputStream is = null;
        
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            trackerServer = ConnectionManager.getTrackerServer();
            storageServer = getStorageServer(trackerServer);
            byte[] bytes = getStorageClient1(trackerServer, storageServer).download_file1(fileId);
            is = new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        } finally {
            closeStorageServer(storageServer);
            ConnectionManager.returnTrackerServer(trackerServer);
        }
        return is;
    }
    /**
     * 获取文件信息
     * @param fileId    如：group1/M00/00/00/wKjTgFo7cNGAI8TvAALa-N2N974394_181x161.jpg
     * @return
     */
    public static FileInfo getFileInfo(String fileId) {
        FileInfo fileInfo = null;
        
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            trackerServer = ConnectionManager.getTrackerServer();
            storageServer = getStorageServer(trackerServer);
            fileInfo = getStorageClient1(trackerServer, storageServer).get_file_info1(fileId);
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            closeStorageServer(storageServer);
            ConnectionManager.returnTrackerServer(trackerServer);
        }
        return fileInfo;
    }
    
}
