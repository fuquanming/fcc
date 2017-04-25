/*
 * @(#)WorkflowInstanceQueryImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年3月24日
 * 修改历史 : 
 *     1. [2017年3月24日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.query.impl;

import com.fcc.commons.workflow.query.WorkflowInstanceQuery;

/**
 * 流程实例查询
 * @version 
 * @author 傅泉明
 */
public class WorkflowInstanceQueryImpl implements WorkflowInstanceQuery {

    protected String processInstanceId;
    protected String processInstanceBusinessKey;
    protected String processDefinitionKey;
    protected String active;
    
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
    public String active(String active) {
        if (active != null) this.active = active;
        return this.active;
    }

}
