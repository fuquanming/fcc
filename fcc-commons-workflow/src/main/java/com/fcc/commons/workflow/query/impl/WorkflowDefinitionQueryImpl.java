/*
 * @(#)WorkflowDefinitionQueryImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年3月24日
 * 修改历史 : 
 *     1. [2017年3月24日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.query.impl;

import com.fcc.commons.workflow.query.WorkflowDefinitionQuery;

/**
 * 流程定义查询
 * @version 
 * @author 傅泉明
 */
public class WorkflowDefinitionQueryImpl implements WorkflowDefinitionQuery {

    protected String processDefinitionNameLike;
    protected String processDefinitionKey;
    
    @Override
    public String processDefinitionNameLike(String processDefinitionNameLike) {
        if (processDefinitionNameLike != null) this.processDefinitionNameLike = processDefinitionNameLike;
        return this.processDefinitionNameLike;
    }

    @Override
    public String processDefinitionKey(String processDefinitionKey) {
        if (processDefinitionKey != null) this.processDefinitionKey = processDefinitionKey;
        return this.processDefinitionKey;
    }

}
