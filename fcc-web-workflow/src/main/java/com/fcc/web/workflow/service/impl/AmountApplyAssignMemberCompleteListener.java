package com.fcc.web.workflow.service.impl;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;
/**
 * 
 * <p>Description:风控人员完成任务</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Component
public class AmountApplyAssignMemberCompleteListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4165708855611629332L;
	
	public void notify(DelegateTask delegateTask) {
		String memberAmountKey = delegateTask.getTaskDefinitionKey() + "_amount";// 设置金额
		String memberAmount = delegateTask.getVariable(memberAmountKey).toString();
		System.err.println("memberAmount=" + memberAmount);
		// 入库设置金额
	}
	
}
