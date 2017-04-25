/*
 * @(#)WorkflowModelQueryImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年3月24日
 * 修改历史 : 
 *     1. [2017年3月24日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.query.impl;

import com.fcc.commons.workflow.query.WorkflowModelQuery;

/**
 * 流程模型查询参数
 * @version 
 * @author 傅泉明
 */
public class WorkflowModelQueryImpl implements WorkflowModelQuery {

    protected String modelNameLike;
    protected String modelKey;
    
    @Override
    public String modelNameLike(String modelNameLike) {
        if (modelNameLike != null) this.modelNameLike = modelNameLike;
        return this.modelNameLike;
    }

    @Override
    public String modelKey(String modelKey) {
        if (modelKey != null) this.modelKey = modelKey;
        return this.modelKey;
    }
    
   
}
