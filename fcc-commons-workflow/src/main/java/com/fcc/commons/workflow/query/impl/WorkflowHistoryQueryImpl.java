/*
 * @(#)WorkflowHistoryQueryImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年3月24日
 * 修改历史 : 
 *     1. [2017年3月24日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.query.impl;

import com.fcc.commons.workflow.query.WorkflowHistoryQuery;

/**
 * 流程历史查询
 * @version 
 * @author 傅泉明
 */
public class WorkflowHistoryQueryImpl implements WorkflowHistoryQuery {

    protected String processInstanceId;
    protected String processInstanceBusinessKey;
    protected String processDefinitionKey;
    protected String startedBy;
    protected String involvedUser;
    
    @Override
    public String processInstanceId(String processInstanceId) {
        if (processInstanceId != null) this.processInstanceId = processInstanceId;
        return this.processInstanceId;
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
    public String startedBy(String startedBy) {
        if (startedBy != null) this.startedBy = startedBy;
        return this.startedBy;
    }

    @Override
    public String involvedUser(String involvedUser) {
        if (involvedUser != null) this.involvedUser = involvedUser;
        return this.involvedUser;
    }
}
