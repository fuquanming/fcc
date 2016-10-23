/*
 * @(#)TaskServiceImpl.java
 * 
 * Copyright (c) 2015 www.ssit-xm.com.cn, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年10月19日
 * 修改历史 : 
 *     1. [2016年10月19日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fcc.commons.core.service.BaseService;
import com.fcc.web.sys.cache.QueueUtil;
import com.fcc.web.sys.service.TaskService;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Service
public class TaskServiceImpl implements TaskService {

    private Logger logger = Logger.getLogger(TaskServiceImpl.class);
    @Resource
    private BaseService baseService;
    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.TaskService#executeQueue()
     **/
    @Override
    public void executeQueue() {
        Object obj = null;
        while ((obj = QueueUtil.getCreateQueue().poll()) != null) {
            try {
                baseService.create(obj);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("保持失败！", e);
            }
        }
    }

}
