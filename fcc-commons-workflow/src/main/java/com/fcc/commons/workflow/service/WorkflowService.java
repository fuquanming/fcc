package com.fcc.commons.workflow.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.workflow.model.WorkflowBean;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.commons.workflow.view.ProcessTaskSequenceFlowInfo;

/**
 * <p>Description:工作流</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface WorkflowService {

	/**
	 * 启动工作流
	 * @param workflowBean	
	 * @param userId
	 * @param userName
	 * @param variables		流程绑定参数	
	 * @return	流程实例ID		
	 */
	String startWorkflow(WorkflowBean workflowBean, String userId, String userName, Map<String, Object> variables);
	
	/**
	 * 签收任务
	 * @param taskId
	 * @param userId
	 */
	void taskClaim(String taskId, String userId);
	
	/**
	 * 完成任务
	 * @param taskId
	 * @param variables
	 */
	void taskComplete(String taskId, String processInstanceId, Map<String, Object> variables, String message);
	
	/**
	 * 流程跟踪
	 * @param processInstanceId ：流程实例id
	 * @param businessKey：流程业务数据id
	 * @param definitionKey：流程定义id
	 * @return 流程跟踪图的流数据
	 */
	public InputStream trace(String processInstanceId, String businessKey, String definitionKey);
	
	/**
	 * 获取任务出去的线
	 * @param processTaskInfo
	 * @return
	 */
	public List<ProcessTaskSequenceFlowInfo> getTaskOutSequenceFlow(ProcessTaskInfo processTaskInfo);

	/**
	 * 流程实例评论
	 * @param processInstanceId
	 * @return
	 */
	List<ProcessTaskInfo> getComments(String processInstanceId);
	
	/**
	 * 查询-流程实例
	 * @param pageNo
	 * @param pageSize
	 * @param param ProcessInstanceInfo
	 * @return 
	 */
	public ListPage queryPageProcessInstance(int pageNo, int pageSize, Map<String, Object> param);
	
	/**
	 * 查询-流程历史
	 * @param pageNo
	 * @param pageSize
	 * @param param ProcessHistoryInfo
	 * @return 
	 */
	public ListPage queryPageProcessHistory(int pageNo, int pageSize, Map<String, Object> param);
	
	/**
	 * 查询-流程任务
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
	 * 查询-流程任务
	 * @param pageNo
	 * @param pageSize
	 * @param param 
	 * @return ProcessTaskInfo
	 */
	public ListPage queryPageProcessTask(int pageNo, int pageSize, Map<String, Object> param);
	
}
