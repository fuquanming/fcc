package com.fcc.commons.workflow.common;
/**
 * <p>Description:任务查询条件</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public enum WorkflowTaskQueryEnum {
	/** 流程业务ID */
	taskId,
	/** 流程业务ID */
	businessKey,
	/** 流程定义KEY */
	definitionKey,
	/** 流程定义ID */
	processDefinitionId,
	/** 流程实例ID */
	processInstanceId,
	/** 用户ID，待办理任务 */
	taskAssignee,
	/** 用户ID，待签收任务 */
	taskCandidateUser,
	/** 用户Id，待签收，办理任务 */
	userId;
}

