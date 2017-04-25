package com.fcc.web.workflow.service;

/**
 * 额度申请-流程
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface LeaveApplyWorkflowService {

	/**
	 * 启动流程
	 * @param leaveIds
	 */
	void startWorkflow(String leaveId);
	
}
