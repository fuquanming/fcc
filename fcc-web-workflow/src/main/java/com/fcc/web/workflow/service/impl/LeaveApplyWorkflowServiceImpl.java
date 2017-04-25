/*
 * @(#)LeaveApplyWorkflowServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2017年3月21日
 * 修改历史 : 
 *     1. [2017年3月21日]创建文件 by 傅泉明
 */
package com.fcc.web.workflow.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.workflow.common.WorkflowStatus;
import com.fcc.commons.workflow.service.WorkflowService;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.workflow.model.Leave;
import com.fcc.web.workflow.service.LeaveApplyWorkflowService;

/**
 * 请假流程工作流
 * @version 
 * @author 傅泉明
 */
@Service
public class LeaveApplyWorkflowServiceImpl implements LeaveApplyWorkflowService {

    @Resource
    private BaseService baseService;
    @Resource
    private WorkflowService workflowService;
    
    /**
     * @see com.fcc.web.workflow.service.LeaveApplyWorkflowService#startWorkflow(java.lang.Long[])
     **/
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void startWorkflow(String leaveId) {
        Leave data = (Leave) baseService.get(Leave.class, leaveId);
        // 判断状态
        String status = data.getStatus();
        if (!WorkflowStatus.unstart.name().equals(status)) { // 未启动状态才可以启动
            return;
        }
        
        SysUser sysUser = (SysUser) baseService.get(SysUser.class, data.getInitiatorUserId());
        data.setStatus(WorkflowStatus.start.name());
        
        Map<String, Object> variables = new HashMap<String, Object>(1);
        variables.put("applyUserId", sysUser.getUserId());// 设置启动用户ID
        String processInstanceId = workflowService.startWorkflow(data, sysUser.getUserId(), sysUser.getUserName(), variables);
        ProcessTaskInfo task = workflowService.getCurrentTask(processInstanceId);
        // 开始下个流程
        variables.clear();
        variables.put("action", "apply");// 设置下个流转
        workflowService.taskComplete(sysUser.getUserId(), task.getId(), processInstanceId, variables, null);
        task = workflowService.getCurrentTask(processInstanceId);
        if (task != null) data.setProcessNodeName(task.getName());
        baseService.edit(data);
    }
}
