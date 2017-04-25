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
 * <p>Description:审批成功</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Component
public class LeaveApplySuccessStatusService implements JavaDelegate {
	
	private static Logger logger = Logger.getLogger(LeaveApplySuccessStatusService.class);
	@Resource
	private LeaveService leaveService;

	public void execute(DelegateExecution execution) throws Exception {
		// 更新状态
		try {
		    String leaveId = execution.getProcessBusinessKey();
		    leaveService.updateStatus(leaveId, WorkflowStatus.success.name());
			
			// 获取设置的金额
			
			// 入库统计金额
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		logger.info("Leave审批成功");
	}

}
