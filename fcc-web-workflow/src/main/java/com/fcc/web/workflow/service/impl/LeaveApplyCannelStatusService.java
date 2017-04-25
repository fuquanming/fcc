package com.fcc.web.workflow.service.impl;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fcc.commons.workflow.common.WorkflowStatus;
import com.fcc.web.workflow.service.LeaveService;

/**
 * 
 * <p>Description:取消申请</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Component
public class LeaveApplyCannelStatusService implements JavaDelegate {

	private static Logger logger = Logger.getLogger(LeaveApplyCannelStatusService.class);
	@Resource
	private LeaveService leaveService;

	public void execute(DelegateExecution execution) throws Exception {
		// 更新状态
		try {
			String leaveId = execution.getProcessBusinessKey();
			leaveService.updateStatus(leaveId, WorkflowStatus.cannel.name());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	    logger.info("Leave取消申请");
	}

}
