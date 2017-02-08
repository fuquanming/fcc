package com.fcc.commons.workflow.common;
/**
 * <p>Description:流程实例查询条件</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public enum WorkflowInstanceQueryEnum {
	/** 流程实例ID */
	processInstanceId,
	/** 关联业务ID */
	businessKey,
	/** 关联业务KEY，如leave 请假 */
	definitionKey,
	/** 是否 运行 中的流程实例 */
	active;
	
}
