package com.fcc.web.workflow.service.impl;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import com.fcc.commons.core.service.BaseService;
import com.fcc.web.sys.model.Organization;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.SysUserService;
/**
 * 
 * <p>Description:部门领导审核-创建</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Component
public class DeptLeaderCreateListener implements TaskListener {

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
		
		// 设置部门领导组织机构ID
	    String userId = (String) delegateTask.getVariable("applyUserId");
	    if (userId != null) {
	        // 用户所属上级机构
	        SysUser sysUser = (SysUser) baseService.get(SysUser.class, userId);
	        String dept = sysUser.getDept();
	        Organization organ = (Organization) baseService.get(Organization.class, dept);
//	        System.out.println("deptLeader=" + organ.getParentId());
	        delegateTask.addCandidateGroup(organ.getParentId());
	    }
	}
	
}
