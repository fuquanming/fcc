package com.fcc.web.workflow.service;

/**
 * <p>Description:AmountApply</p>
 */

public interface AmountApplyWorkflowService {
    /**
     * 启动流程
     * @param leaveIds
     */
    void startWorkflow(String amountApplyId);
}