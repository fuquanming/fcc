package com.fcc.commons.workflow.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.commons.workflow.view.ProcessTaskSequenceFlowInfo;

/**
 * 
 * <p>Description:流程任务</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface ProcessTaskService {

	/**
	 * 签收任务
	 * @param taskId
	 * @param userId
	 */
	void claim(String taskId, String userId);
	
	/**
	 * 获取任务变量
	 * @param taskId		任务ID
	 * @param variableName	变量名
	 * @return
	 */
	Object getVariable(String taskId, String variableName);
	
	/**
	 * 获取任务变量
	 * @param taskId		任务ID
	 * @return
	 */
	Map<String,Object> getVariables(String taskId);
	
	/**
	 * 获取任务变量
	 * @param taskId		任务ID
	 * @param variableNames	变量集合
	 * @return
	 */
	Map<String,Object> getVariables(String taskId, Collection<String> variableNames);
	
	/**
	 * 获取任务出去的线
	 * @param taskInfo	
	 * @return
	 */
	List<ProcessTaskSequenceFlowInfo> getTaskOutSequenceFlow(ProcessTaskInfo processTaskInfo);
	
	/**
	 * 完成任务
	 * @param taskId
	 * @param variables
	 */
	void complete(String taskId, String processInstanceId, Map<String, Object> variables, String message);
	
	/**
	 * 查询所有任务
	 * @param param
	 * @return
	 */
	public List<ProcessTaskInfo> queryList(Map<String, Object> param);
	
	/**
	 * 查询 总数
	 * @return Long
	 */
	Long queryPageCount(Map<String, Object> param);
	
	/**
	 * 分页查询
	 * @return ProcessTaskInfo
	 */
	ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);
	
}
