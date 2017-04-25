package com.fcc.commons.workflow.common;

/**
 * <p>Description:工作流-流程变量</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public enum WorkflowVariableEnum {
    /** 流程中存的业务数据变量名 */
	model,
	/** 流程中存的申请人ID变量 */
	requestUserId,
	/** 流程中存的申请人名称变量 */
	requestUserName,
	/** 用于一个任务多个出去的线，执行那个线的条件 */
	transition,
	/** 办理任务显示绑定数据的页面 */
	editTaskPage,
	;
}
