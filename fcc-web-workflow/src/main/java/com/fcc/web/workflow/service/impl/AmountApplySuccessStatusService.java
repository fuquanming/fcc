package com.fcc.web.workflow.service.impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fcc.commons.workflow.common.WorkflowStatus;
import com.fcc.web.workflow.service.AmountApplyService;

/**
 * 
 * <p>Description:审批成功</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Component
public class AmountApplySuccessStatusService implements JavaDelegate {
	
	private static Logger logger = Logger.getLogger(AmountApplySuccessStatusService.class);
	@Autowired
	private AmountApplyService amountApplyService;

	public void execute(DelegateExecution execution) throws Exception {
		// 更新状态
		try {
			String amountApplyId = execution.getProcessBusinessKey();
			amountApplyService.updateStatus(amountApplyId, WorkflowStatus.success.name());
			
			// 获取设置的金额
			
			// 入库统计金额
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

}
