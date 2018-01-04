/*
 * @(#)BaseExportTask.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : wangji-file-web
 * 创建日期 : 2018年1月4日
 * 修改历史 : 
 *     1. [2018年1月4日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.task;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.fcc.commons.web.view.ExportMessage;
import com.fcc.commons.zip.Zip;

/**
 * 数据导出
 * @version 
 * @author 傅泉明
 */
public abstract class BaseExportTask implements Runnable {

    protected static Logger logger = Logger.getLogger(BaseExportTask.class);
    
    protected ExportMessage exportMessage = new ExportMessage();
    
    protected String runningKey;
    
    protected boolean exportDataFlag;
    /** 导出数据保存的文件夹 */
    protected String exportDataPath;
    /** 查询接口 */
    protected Object queryService;
    /** 查询接口方法名 */
    protected String queryServiceMethodName;
    /** 页码在参数的下标位置 */
    protected int pageNoSub;
    /** 查询参数 */
    protected Object[] queryParams;
    /** 数据查询周期：生成一个文件的总数据量 */
    protected int dataQueryCycle = 10;
    /** 导出数据总大小 */
    protected int exportTotalSize = 500000;
    
    /** 生成临时文件路径 */
    protected List<String> filePaths;
    /** 生成文件的前缀20180104140504_product */
    protected String fileNo;
    /** 单个文件后缀名如：jpg */
    protected String fileExt;
    
    public void init() {
        exportMessage.setEmpty(false);
        exportMessage.setCurrentSize(0);
        exportMessage.setFileName(null);
        exportMessage.setError(false);
        exportDataFlag = true;
    }
    
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            exportDataFlag = true;
            exportData();
        } catch (Exception e) {
            exportMessage.setError(true);
            logger.error("导出数据错误：" + runningKey, e);
            e.printStackTrace();
        } finally {
            exportDataFlag = false;
            long endTime = System.currentTimeMillis();
            String info = "export end:%d,time=%d,totalSize=%d,fileName=%s";
            logger.info(String.format(info, Thread.currentThread().getId(), (endTime - startTime), exportMessage.getCurrentSize(), exportMessage.getFileName()));
        }
    }
    
    @SuppressWarnings("unchecked")
    private void exportData() throws Exception {
        File exportDataPathFile = new File(exportDataPath);
        if (exportDataPathFile.exists() == false) exportDataPathFile.mkdirs();
        filePaths = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        fileNo = new StringBuilder().append(format.format(new Date())).append("_").append(runningKey).toString();
        
        int fileFlag = 1;
        int xmlSize = 0;
        int totalSize = 0;
        
        List<Object> list = null;
        int pageNo = 1;

        Method method = null;
        for (Method m : queryService.getClass().getMethods()) {
            if (m.getName().equals(queryServiceMethodName)) {
                method = m;
                break;
            }
        }
        
        while (true) {
            initData();
            
            xmlSize = 0;
            boolean endFlag = false;
            int flag = dataQueryCycle;// 轮询10次,一个xml总数1000数据量
            String saveFile = new StringBuilder().append(exportDataPath).append(fileNo)
            .append(fileFlag).append(".").append(fileExt).toString();
            
            for (int i = 0; i < flag; i++) {
                Object returnObj = method.invoke(queryService, queryParams);
                list = (List<Object>) returnObj;
                pageNo++;
                queryParams[pageNoSub] = pageNo;
                int dataSize = (list == null) ? 0 : list.size();
                totalSize += dataSize;
                xmlSize += dataSize;
                exportMessage.setCurrentSize(totalSize);
                if (totalSize == 0) {
                    exportMessage.setEmpty(true);
                    File tempFile = new File(saveFile);
                    if (tempFile.exists()) tempFile.delete();
                    return;
                } else if (dataSize == 0) {
                    endFlag = true;
                    break;
                } else {
                    for (Object converObj : list) {
                        converData(converObj);
                    }
                }
                list.clear();
                list = null;
            }
            if (xmlSize > 0) {
                File file = new File(saveFile);
                
                saveFile(file);
                
                filePaths.add(saveFile);
                fileFlag++;
            }
            if (endFlag || totalSize >= exportTotalSize) {
                break;
            }
        }
        String fileName = fileNo + ".zip";
        new Zip().zipFile(fileName, exportDataPath, filePaths);
        for(String filePath : filePaths){//删除文件
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
        exportMessage.setFileName(fileName);
    }
    
    /** 初始化数据信息 */
    protected abstract void initData();
    /** 转化数据 */
    protected abstract void converData(Object obj) throws Exception;
    /** 保存文件 */
    protected abstract void saveFile(File file);
    
    public ExportMessage getExportMessage() {
        return exportMessage;
    }

    public void setExportMessage(ExportMessage exportMessage) {
        this.exportMessage = exportMessage;
    }

    public String getRunningKey() {
        return runningKey;
    }

    public void setRunningKey(String runningKey) {
        this.runningKey = runningKey;
    }

    public String getExportDataPath() {
        return exportDataPath;
    }

    public void setExportDataPath(String exportDataPath) {
        this.exportDataPath = exportDataPath;
    }

    public Object getQueryService() {
        return queryService;
    }

    public void setQueryService(Object queryService) {
        this.queryService = queryService;
    }

    public String getQueryServiceMethodName() {
        return queryServiceMethodName;
    }

    public void setQueryServiceMethodName(String queryServiceMethodName) {
        this.queryServiceMethodName = queryServiceMethodName;
    }

    public int getPageNoSub() {
        return pageNoSub;
    }

    public void setPageNoSub(int pageNoSub) {
        this.pageNoSub = pageNoSub;
    }

    public Object[] getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Object[] queryParams) {
        this.queryParams = queryParams;
    }

    public boolean isExportDataFlag() {
        return exportDataFlag;
    }

    public void setExportDataFlag(boolean exportDataFlag) {
        this.exportDataFlag = exportDataFlag;
    }
    
    public int getExportTotalSize() {
        return exportTotalSize;
    }

    public void setExportTotalSize(int exportTotalSize) {
        this.exportTotalSize = exportTotalSize;
    }

    public int getDataQueryCycle() {
        return dataQueryCycle;
    }

    public void setDataQueryCycle(int dataQueryCycle) {
        this.dataQueryCycle = dataQueryCycle;
    }
}
