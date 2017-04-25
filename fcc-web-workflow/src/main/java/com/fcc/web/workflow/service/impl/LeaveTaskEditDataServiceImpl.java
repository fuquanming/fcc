/*
 * @(#)LeaveTaskEditDataServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2017年3月28日
 * 修改历史 : 
 *     1. [2017年3月28日]创建文件 by 傅泉明
 */
package com.fcc.web.workflow.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.workflow.filter.WorkflowTaskEditDataFilter;
import com.fcc.commons.workflow.service.WorkflowService;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.web.workflow.model.Leave;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Service
public class LeaveTaskEditDataServiceImpl implements WorkflowTaskEditDataFilter {
    
    @Resource
    private BaseService baseService;
    @Resource
    private WorkflowService workflowService;
    
    @Override
    public void edit(HttpServletRequest request, Map<String, Object> variables) {
        String processDefinitionKey = request.getParameter("processDefinitionKey");// 流程定义KEY
        String readonly = request.getParameter("readonly");// 是否只读
        if (Leave.processDefinitionKey.equals(processDefinitionKey)) {
            String leaveId = request.getParameter("dataId");
            String startTimeString = request.getParameter("startTimeString");
            String endTimeString = request.getParameter("endTimeString");
            String content = request.getParameter("content");
            // 保存数据
            Leave leave = (Leave) baseService.get(Leave.class, leaveId);
            if ("false".equalsIgnoreCase(readonly)) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    leave.setStartTime(format.parse(startTimeString));
                    leave.setEndTime(format.parse(endTimeString));
                    leave.setContent(content);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            ProcessTaskInfo task = workflowService.getCurrentTask(request.getParameter("processInstanceId"));
            String taskName = "";
            if (task != null) taskName = task.getName();
            leave.setProcessNodeName(taskName);
            baseService.edit(leave);
        }
    }
}
