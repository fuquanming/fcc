package com.fcc.web.workflow.service;

/**
 * <p>Description:Leave</p>
 */

public interface LeaveWorkflowService {
    /**
     * 启动流程
     * @param leaveIds
     */
    void startWorkflow(String leaveId);
}