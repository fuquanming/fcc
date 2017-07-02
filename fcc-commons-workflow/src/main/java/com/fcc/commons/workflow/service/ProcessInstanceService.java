package com.fcc.commons.workflow.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.workflow.query.WorkflowInstanceQuery;
import com.fcc.commons.workflow.view.ProcessInstanceInfo;
import com.fcc.commons.workflow.view.ProcessTaskAttachmentInfo;
import com.fcc.commons.workflow.view.ProcessTaskCommentInfo;

/**
 * <p>Description:流程实例</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface ProcessInstanceService {
    
    /**
     * 设置流程实例变量
     * @param processInstanceId         流程实例ID
     * @param variableName              变量名
     * @param value                     变量值
     * @return
     */
    void setProcessVariable(String processInstanceId, String variableName, Object value);
    
    /**
     * 设置流程实例变量
     * @param processInstanceId         流程实例ID
     * @param variables                 变量
     * @return
     */
    void setProcessVariables(String processInstanceId, Map<String, ? extends Object> variables);
    
    /**
     * 获取流程实例变量
     * @param processInstanceId         流程实例ID
     * @param variableName              变量名
     * @return
     */
    Object getProcessVariable(String processInstanceId, String variableName);
    /**
     * 获取流程实例变量
     * @param processInstanceId     流程实例ID
     * @return
     */
    Map<String, Object> getProcessVariables(String processInstanceId);
    /**
     * 获取流程实例变量
     * @param processInstanceId     流程实例ID
     * @param variableNames         变量集合
     * @return
     */
    Map<String, Object> getVariables(String processInstanceId, Collection<String> variableNames);
	
	/**
	 * 激活指定id的流程实例
	 * @param processInstanceId
	 */
	void activateProcessInstanceById(String processInstanceId);
	
	/**
	 * 挂起制定id的流程实例
	 * @param processInstanceId
	 */
	void suspendProcessInstanceById(String processInstanceId);
	/**
	 * 删除流程实例
	 * @param id
	 * @param info	删除信息
	 */
	void deleteProcessInstance(String id, String info);
	
	/**
	 * 流程实例评论
	 * @param processInstanceId
	 * @return
	 */
	List<ProcessTaskCommentInfo> getProcessInstanceComments(String processInstanceId);
	
	/**
	 * 流程实例附件
	 * @return
	 */
	List<ProcessTaskAttachmentInfo> getProcessInstanceAttachments(String processInstanceId);
	/**
	 * 获取流程实例
	 * @param instaceId
	 * @return
	 */
	ProcessInstanceInfo getProcessInstace(String instaceId);
	
	/**
	 * 分页查询
	 * @return
	 */
	ListPage queryPage(int pageNo, int pageSize, WorkflowInstanceQuery workflowInstanceQuery);
	
}
