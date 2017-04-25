/*
 * @(#)WorkflowViewEditTaskFilter.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年3月25日
 * 修改历史 : 
 *     1. [2017年3月25日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.filter;

import java.util.Map;

import com.fcc.commons.workflow.view.ProcessTaskInfo;

/**
 * 办理任务的过滤器
 * @version 
 * @author 傅泉明
 */
public interface WorkflowTaskBusinessDataFilter {
    /**
     * 显示修改任务过滤器 
     * @param taskInfo      流程任务信息
     * @param businessKey   流程业务ID
     * @return null 未过滤，用于request设置setAttribute
     */
    Map<String, Object> filter(ProcessTaskInfo taskInfo, String businessKey);
    
}
