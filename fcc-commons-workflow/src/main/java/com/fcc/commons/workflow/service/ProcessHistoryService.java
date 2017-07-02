package com.fcc.commons.workflow.service;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.workflow.query.WorkflowHistoryQuery;
import com.fcc.commons.workflow.view.ProcessHistoryInfo;
import com.fcc.commons.workflow.view.ProcessTaskInfo;

/**
 * <p>Description:流程历史</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface ProcessHistoryService {
	
    /**
     * 获取流程变量
     * @param processInstanceId         流程实例ID
     * @param variableName              变量名
     * @return
     */
    Object getProcessVariable(String processInstanceId, String variableName);
    /**
     * 获取流程变量
     * @param processInstanceId     流程实例ID
     * @return
     */
    Map<String, Object> getProcessVariables(String processInstanceId);
    
	/**
	 * 删除流程历史
	 * @param id	
	 */
	void deleteHistoryProcessInstance(String processInstanceId);
	
	/**
     * 获取流程实例
     * @param instaceId
     * @return
     */
	ProcessHistoryInfo getProcessInstace(String processInstanceId);
	
	/**
	 * 分页查询
	 * @return  ProcessHistoryInfo
	 */
	ListPage queryPage(int pageNo, int pageSize, WorkflowHistoryQuery workflowHistoryQuery);
	
	/**
	 * 查询流程历史任务及评论
	 * @param processInstanceId
	 * @return
	 */
	public List<ProcessTaskInfo> query(String processInstanceId);
}
