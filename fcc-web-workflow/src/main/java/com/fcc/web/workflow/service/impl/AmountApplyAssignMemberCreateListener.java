package com.fcc.web.workflow.service.impl;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import com.fcc.web.workflow.common.AmountApplyEnum;
/**
 * 
 * <p>Description:风控人员分配任务</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Component
public class AmountApplyAssignMemberCreateListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4165708855611629332L;

	public void notify(DelegateTask delegateTask) {
		String taskId = delegateTask.getTaskDefinitionKey();// 流程图taskID
		String index = taskId.substring(taskId.length() - 1);
		String userId = (String) delegateTask.getVariable(AmountApplyEnum.randomMember.toString() + index);
		delegateTask.setAssignee(userId);
	}
	
}
