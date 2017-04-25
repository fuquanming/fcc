/*
 * @(#)WorkflowHistoryQuery.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年3月24日
 * 修改历史 : 
 *     1. [2017年3月24日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.query;

/**
 * 流程历史查询
 * @version 
 * @author 傅泉明
 */
public interface WorkflowHistoryQuery {
    /**
     * 流程实例ID
     * @param processInstanceId
     * @return
     */
    String processInstanceId(String processInstanceId);
    /**
     * 流程业务ID
     * @param processInstanceBusinessKey
     * @return
     */
    String processInstanceBusinessKey(String processInstanceBusinessKey);
    /**
     * 流程定义KEY
     * @param processDefinitionKey
     * @return
     */
    String processDefinitionKey(String processDefinitionKey);
    /**
     * 流程-用户发起
     * @param startedBy
     * @return
     */
    String startedBy(String startedBy);
    /**
     * 流程-用户参与
     * @param involvedUser
     * @return
     */
    String involvedUser(String involvedUser);
}
