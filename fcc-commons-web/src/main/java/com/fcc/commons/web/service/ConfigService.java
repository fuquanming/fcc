/*
 * @(#)ThreadPoolService.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-web
 * 创建日期 : 2016年12月29日
 * 修改历史 : 
 *     1. [2016年12月29日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

/**
 * 线程池
 * @version 
 * @author 傅泉明
 */
@Service
public class ConfigService {
    
    /** 导出数据大小 */
    private int exportDataSize = 500000;
    /** 线程池大小 */
    private int threadPoolSize = 10;
    /** 应用名称 */
    private String appName;
    
    private ThreadPoolExecutor pool;
    
    public void init() {
        pool = new ThreadPoolExecutor(threadPoolSize, threadPoolSize * 2, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }
    
    public void destroy() {
        pool.shutdown();
    }
    
    public ThreadPoolExecutor getThreadPool() {
        return pool;
    }
    
    public int getThreadPoolSize() {
        return threadPoolSize;
    }
    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
    public int getExportDataSize() {
        return exportDataSize;
    }
    public void setExportDataSize(int exportDataSize) {
        this.exportDataSize = exportDataSize;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
}
