package com.fcc.commons.workflow.service;

import java.util.List;

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
