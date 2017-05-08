/*
 * @(#)TaskListener.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年5月6日
 * 修改历史 : 
 *     1. [2017年5月6日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.listener;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fcc.commons.workflow.view.ProcessTaskInfo;

/**
 * 任务监听器:显示任务、完成任务前、完成任务后
 * @version 
 * @author 傅泉明
 */
public interface TaskListener {
    /**
     * 显示办理任务
     * @param taskInfo
     * @param businessKey
     * @return
     */
    public Map<String, Object> getBusinessData(ProcessTaskInfo taskInfo, String businessKey);
    /**
     * 完成任务前执行
     * @param request
     * @param variables
     */
    public void beforeComplete(HttpServletRequest request, Map<String, Object> variables);
    /**
     * 完成任务后执行
     * @param request
     * @param variables
     */
    public void afterComplete(HttpServletRequest request, Map<String, Object> variables);
}
