/*
 * @(#)WorkflowDefinitionQuery.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年3月24日
 * 修改历史 : 
 *     1. [2017年3月24日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.query;

/**
 * 流程定义查询
 * @version 
 * @author 傅泉明
 */
public interface WorkflowDefinitionQuery {
    /**
     * 流程定义名称查询Like
     * @param processDefinitionNameLike
     * @return
     */
    String processDefinitionNameLike(String processDefinitionNameLike);
    /**
     * 流程定义KEY
     * @param processDefinitionKey
     * @return
     */
    String processDefinitionKey(String processDefinitionKey);
    
}
