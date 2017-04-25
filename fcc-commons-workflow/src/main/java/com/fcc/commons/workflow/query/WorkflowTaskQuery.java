/*
 * @(#)ProcessTaskQuery.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年3月23日
 * 修改历史 : 
 *     1. [2017年3月23日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.query;

/**
 * 流程任务查询
 * @version 
 * @author 傅泉明
 */
public interface WorkflowTaskQuery {
    /**
     * 流程业务ID
     * @param taskId
     * @return
     */
    String taskId(String taskId);
    /**
     * 流程绑定业务ID
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
     * 流程定义ID
     * @param processDefinitionId
     * @return
     */
    String processDefinitionId(String processDefinitionId);
    /**
     * 流程实例ID
     * @param processInstanceId
     * @return
     */
    String processInstanceId(String processInstanceId);
    /**
     * 待办理任务的人
     * @param taskAssignee
     * @return
     */
    String taskAssignee(String taskAssignee);
    /**
     * 待签收任务的人
     * @param taskCandidateUser
     * @return
     */
    String taskCandidateUser(String taskCandidateUser);
    /**
     * 待签收、待办理的人
     * @param taskCandidateOrAssigned
     * @return
     */
    String taskCandidateOrAssigned(String taskCandidateOrAssigned);
    
}
