package com.fcc.web.workflow.service.impl;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import com.fcc.commons.core.service.BaseService;
import com.fcc.web.sys.service.SysUserService;
/**
 * 
 * <p>Description:申请请假-创建</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Component
public class LeaveApplyCreateListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4165708855611629332L;

	@Resource
	private BaseService baseService;
	@Resource
	private SysUserService sysUserService;
	
	public void notify(DelegateTask delegateTask) {
//		String taskId = delegateTask.getTaskDefinitionKey();// 流程图taskID
//		String index = taskId.substring(taskId.length() - 1);
//		String userId = (String) delegateTask.getVariable(AmountApplyEnum.randomMember.toString() + index);
//		delegateTask.setAssignee(userId);
		
	    // 标识变量-区别是否可以修改原申请的请假的数据
	    String taskId = delegateTask.getTaskDefinitionKey();// 流程图taskID
	    System.out.println("notify taskId=" + taskId);
	}
	
}
