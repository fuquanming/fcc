/*
 * @(#)ProcessTaskQueryImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年3月23日
 * 修改历史 : 
 *     1. [2017年3月23日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.query.impl;

import com.fcc.commons.workflow.query.WorkflowTaskQuery;

/**
 * 流程任务查询
 * @version 
 * @author 傅泉明
 */
public class WorkflowTaskQueryImpl implements WorkflowTaskQuery {

    protected String taskId;
    protected String processInstanceBusinessKey;
    protected String processDefinitionKey;
    protected String processDefinitionId;
    protected String processInstanceId;
    protected String taskAssignee;
    protected String taskCandidateUser;
    protected String taskCandidateOrAssigned;
    
    @Override
    public String taskId(String taskId) {
        if (taskId != null) this.taskId = taskId;
        return this.taskId;
    }

    @Override
    public String processInstanceBusinessKey(String processInstanceBusinessKey) {
        if (processInstanceBusinessKey != null) this.processInstanceBusinessKey = processInstanceBusinessKey;
        return this.processInstanceBusinessKey;
    }

    @Override
    public String processDefinitionKey(String processDefinitionKey) {
        if (processDefinitionKey != null) this.processDefinitionKey = processDefinitionKey;
        return this.processDefinitionKey;
    }

    @Override
    public String processDefinitionId(String processDefinitionId) {
        if (processDefinitionId != null) this.processDefinitionId = processDefinitionId;
        return this.processDefinitionId;
    }

    @Override
    public String processInstanceId(String processInstanceId) {
        if (processInstanceId != null) this.processInstanceId = processInstanceId;
        return this.processInstanceId;
    }

    @Override
    public String taskAssignee(String taskAssignee) {
        if (taskAssignee != null) this.taskAssignee = taskAssignee; 
        return this.taskAssignee;
    }

    @Override
    public String taskCandidateUser(String taskCandidateUser) {
        if (taskCandidateUser != null) this.taskCandidateUser = taskCandidateUser;
        return this.taskCandidateUser;
    }

    @Override
    public String taskCandidateOrAssigned(String taskCandidateOrAssigned) {
        if (taskCandidateOrAssigned != null) this.taskCandidateOrAssigned = taskCandidateOrAssigned;
        return this.taskCandidateOrAssigned;
    }
    
}
