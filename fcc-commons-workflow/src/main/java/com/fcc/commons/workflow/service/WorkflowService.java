package com.fcc.commons.workflow.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.workflow.filter.WorkflowTaskBusinessDataFilter;
import com.fcc.commons.workflow.filter.WorkflowTaskEditDataFilter;
import com.fcc.commons.workflow.model.WorkflowBean;
import com.fcc.commons.workflow.query.WorkflowDefinitionQuery;
import com.fcc.commons.workflow.query.WorkflowHistoryQuery;
import com.fcc.commons.workflow.query.WorkflowInstanceQuery;
import com.fcc.commons.workflow.query.WorkflowModelQuery;
import com.fcc.commons.workflow.query.WorkflowTaskQuery;
import com.fcc.commons.workflow.view.ProcessHistoryInfo;
import com.fcc.commons.workflow.view.ProcessInstanceInfo;
import com.fcc.commons.workflow.view.ProcessTaskAttachmentInfo;
import com.fcc.commons.workflow.view.ProcessTaskCommentInfo;
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
     * 创建流程任务查询参数
     * @return ProcessTaskQuery
     */
    WorkflowTaskQuery createTaskQuery();
    /**
     * 创建流程实例查询参数
     * @return
     */
    WorkflowInstanceQuery createInstanceQuery();
    /**
     * 创建流程定义查询参数
     * @return
     */
    WorkflowDefinitionQuery createDefinitionQuery();
    /**
     * 创建流程历史查询参数
     * @return
     */
    WorkflowHistoryQuery createHistoryQuery();
    /**
     * 创建流程模型查询参数
     * @return
     */
    WorkflowModelQuery createModelQuery();
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
	 * 流程当前任务
	 * @param processInstanceId
	 * @return
	 */
	ProcessTaskInfo getCurrentTask(String processInstanceId);
	
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
	void taskComplete(String userId, String taskId, String processInstanceId, Map<String, Object> variables, String message);
	
	/**
     * 完成任务
	 * @param taskId
	 * @param variables
	 * @param attachmentList TODO
     */
    void taskComplete(String userId, String taskId, String processInstanceId, Map<String, Object> variables, String message,
            List<ProcessTaskAttachmentInfo> attachmentList, HttpServletRequest request);
    
	/**
	 * 流程跟踪
	 * @param processInstanceId ：流程实例id
	 * @param businessKey：流程业务数据id
	 * @param definitionKey：流程定义id
	 * @return 流程跟踪图的流数据
	 */
	InputStream trace(String processInstanceId, String businessKey, String definitionKey);
	
	/**
	 * 获取任务出去的线
	 * @param processTaskInfo
	 * @return
	 */
	List<ProcessTaskSequenceFlowInfo> getTaskOutSequenceFlow(ProcessTaskInfo processTaskInfo);

	/**
	 * 流程实例任务、评论、附件
	 * @param processInstanceId
	 * @return
	 */
	List<ProcessTaskInfo> getTasks(String processInstanceId);
	
	/**
	 * 流程实例评论
	 * @param processInstanceId
	 * @return
	 */
	List<ProcessTaskCommentInfo> getComments(String processInstanceId);
	
	/**
     * 获取流程任务
     * @param param
     * @return
     */
    ProcessTaskInfo getProcessTask(String taskId);
    
    /**
     * 获取流程实例
     * @param instaceId
     * @return
     */
    ProcessInstanceInfo getProcessInstace(String instaceId);
    
    /**
     * 获取流程实例
     * @param instaceId
     * @return
     */
    ProcessHistoryInfo getHistoricProcessInstance(String instaceId);
	
    /**
     * 获取流程任务绑定的数据，实现
     * @param taskInfo     流程任务
     * @param businessKey  流程业务ID
     * @return Map，需要显示的数据
     */
    Map<String, Object> getTaskBusinessData(ProcessTaskInfo taskInfo, String businessKey);
    
	/**
	 * 查询-流程实例
	 * @param pageNo
	 * @param pageSize
	 * @param param ProcessInstanceInfo
	 * @return 
	 */
	public ListPage queryPageProcessInstance(int pageNo, int pageSize, WorkflowInstanceQuery workflowInstanceQuery);
	
	/**
	 * 查询-流程历史
	 * @param pageNo
	 * @param pageSize
	 * @param param ProcessHistoryInfo
	 * @return 
	 */
	public ListPage queryPageProcessHistory(int pageNo, int pageSize, WorkflowHistoryQuery workflowHistoryQuery);
	
	/**
	 * 查询-流程任务
	 * @param param
	 * @return
	 */
	public List<ProcessTaskInfo> queryProcessTask(WorkflowTaskQuery processTaskQuery);
	
	/**
     * 查询-流程任务
     * @param pageNo
     * @param pageSize
     * @param processTaskQuery 
     * @return ProcessTaskInfo
     */
    public ListPage queryPageProcessTask(int pageNo, int pageSize, WorkflowTaskQuery processTaskQuery);
    
	/**
	 * 查询任务 总数
	 * @return Long
	 */
	Long queryProcessTaskCount(WorkflowTaskQuery processTaskQuery);
	
	/**
	 * 办理任务-绑定数据
	 * @return
	 */
	public Set<WorkflowTaskBusinessDataFilter> getTaskBusinessDataSet();

	/**
	 * 任务-修改数据
	 * @return
	 */
    public Set<WorkflowTaskEditDataFilter> getTaskEditDataSet();
	
}
