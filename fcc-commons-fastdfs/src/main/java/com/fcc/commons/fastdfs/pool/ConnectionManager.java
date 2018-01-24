/*
 * @(#)ConnectionManager.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-fastdfs
 * 创建日期 : 2017年12月22日
 * 修改历史 : 
 *     1. [2017年12月22日]创建文件 by 傅泉明
 */
package com.fcc.commons.fastdfs.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import org.csource.fastdfs.TrackerServer;

/**
 * FastDFS TrackerServer 连接池
 * @version 
 * @author 傅泉明
 */
public class ConnectionManager {
    
    private static Logger logger = Logger.getLogger(ConnectionManager.class);
    
    private static GenericObjectPool<TrackerServer> pool;
    
    private static boolean initFlag = false;
    
    private static Boolean lockFlag = Boolean.TRUE;
    
    public static void init() {
        synchronized (lockFlag) {
            if (initFlag == false) {
                logger.info("FastDFS ConnectionManager init");
                ConnectionFactory connectionFactory = new ConnectionFactory();
                GenericObjectPoolConfig config = new GenericObjectPoolConfig();
                config.setMaxIdle(10);   //最大空闲数量
                config.setMaxTotal(20);  //连接池最大数量
                config.setMinIdle(3);    //最小空闲数量
                config.setTestOnBorrow(true);  //在从pool中去对象时进行有效性检查，会调用工厂中的validateObject
                config.setMaxWaitMillis(1000); //提取对象时最大等待时间，超时会抛出异常
                config.setMinEvictableIdleTimeMillis(600000); // 最小的空闲对象驱除时间间隔，空闲超过指定的时间的对象，会被清除掉
                config.setTimeBetweenEvictionRunsMillis(30000);//后台驱逐线程休眠时间
                config.setNumTestsPerEvictionRun(3); //设置驱逐线程每次检测对象的数量
                config.setTestWhileIdle(true);  //是否对空闲对象使用PoolableObjectFactory的validateObject校验，
                pool = new GenericObjectPool<TrackerServer>(connectionFactory, config);
                initFlag = true;
            }
        }
    }
    
    public static TrackerServer getTrackerServer() {
        try {
            if (initFlag == false) init();
            return pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void returnTrackerServer(TrackerServer trackerServer) {
        pool.returnObject(trackerServer);
    }
    
    public static void destroy() {
        pool.clear();
        pool.close();
        logger.info("FastDFS ConnectionManager destory");
    }
}
