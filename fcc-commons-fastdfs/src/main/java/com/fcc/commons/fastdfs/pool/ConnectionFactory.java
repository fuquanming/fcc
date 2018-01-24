/*
 * @(#)ConnectionFactory.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-fastdfs
 * 创建日期 : 2017年12月22日
 * 修改历史 : 
 *     1. [2017年12月22日]创建文件 by 傅泉明
 */
package com.fcc.commons.fastdfs.pool;

import java.io.IOException;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/**
 * FastDFS TrackerServer 获取
 * @version 
 * @author 傅泉明
 */
public class ConnectionFactory extends BasePooledObjectFactory<TrackerServer> {

//    private static final String CONF_FILENAME = "src/main/resources/fdfs/fdfs_client.conf";
//    private static Logger logger = Logger.getLogger(ConnectionFactory.class);
    
    private static TrackerClient trackerClient;
    
    static {
        try {
//            StringBuilder sb = new StringBuilder();
//            sb.append(ClassUtil.getClassRootPath()).append("/fdfs/fdfs_client.conf");
            ClientGlobal.init("fdfs/fdfs_client.conf");
            trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static TrackerClient getTrackerClient() {
        return trackerClient;
    }
    
    @Override
    public TrackerServer create() throws Exception {
//        logger.info("---create---");
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trackerServer;
    }

    @Override
    public PooledObject<TrackerServer> wrap(TrackerServer obj) { //创建包装对象，包装对象是真正放在pool中的对象
//        logger.info("---wrap---");
        return new DefaultPooledObject<TrackerServer>(obj);
    }
    
    @Override
    public boolean validateObject(PooledObject<TrackerServer> p) { //校验对象有效性
        boolean flag = false;
        try {
            flag = ProtoCommon.activeTest(p.getObject().getSocket());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        logger.info("---validateObject---" + flag);
        return flag;
    }
    
    @Override
    public void destroyObject(PooledObject<TrackerServer> p) throws Exception {
        p.getObject().close();
//        logger.info("---destroyObject---");
    }
}
