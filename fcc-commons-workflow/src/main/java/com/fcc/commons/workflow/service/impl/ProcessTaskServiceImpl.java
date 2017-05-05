package com.fcc.commons.workflow.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.workflow.query.WorkflowTaskQuery;
import com.fcc.commons.workflow.service.ProcessTaskService;
import com.fcc.commons.workflow.util.ProcessDefinitionCache;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.commons.workflow.view.ProcessTaskSequenceFlowInfo;
import com.fcc.commons.workflow.view.ProcessTaskAttachmentInfo;

/**
 * 
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Service
public class ProcessTaskServiceImpl implements ProcessTaskService {

    @Resource
	private RepositoryServiceImpl repositoryService;
    @Resource
	private TaskService taskService;
    @Resource
	private ProcessBaseService processBaseService;
    @Resource
	private RuntimeService runtimeService;
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void claim(String taskId, String userId) {
		taskService.claim(taskId, userId);
	}
	
	@Transactional(readOnly = true) //只查事务申明
	public Object getVariable(String taskId, String variableName) {
		return taskService.getVariable(taskId, variableName);
	}
	
	@Transactional(readOnly = true) //只查事务申明
	public Map<String, Object> getVariables(String taskId) {
		return taskService.getVariables(taskId);
	}
	
	@Transactional(readOnly = true) //只查事务申明
	public Map<String, Object> getVariables(String taskId,
			Collection<String> variableNames) {
		return taskService.getVariables(taskId, variableNames);
	}
	
	@Transactional(readOnly = true) //只查事务申明
	public List<ProcessTaskSequenceFlowInfo> getTaskOutSequenceFlow(ProcessTaskInfo processTaskInfo) {
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) repositoryService
				.getDeployedProcessDefinition(processTaskInfo.getProcessDefinitionId());
		ExecutionEntity exe = (ExecutionEntity) runtimeService
				.createExecutionQuery().executionId(processTaskInfo.getExecutionId())
				.singleResult();
		ActivityImpl activity = pde.findActivity(exe.getActivityId());
		List<PvmTransition> transitions = activity.getOutgoingTransitions();
		List<ProcessTaskSequenceFlowInfo> buttons = new ArrayList<ProcessTaskSequenceFlowInfo>(transitions.size());
		for (PvmTransition pvmTransition : transitions) {
			// conditionText 输入的表达式
			ProcessTaskSequenceFlowInfo info = new ProcessTaskSequenceFlowInfo();
			info.setName(pvmTransition.getProperty("name").toString());
			info.setConditionText(pvmTransition.getProperty("conditionText").toString());
			buttons.add(info);
//			{conditionText=${deptLeaderAudit == 'pass'}, condition=org.activiti.engine.impl.el.UelExpressionCondition@1e334af, name=通过, documentation=null}
		}
		return buttons;
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void complete(String userId, String taskId, String processInstanceId, Map<String, Object> variables, List<ProcessTaskAttachmentInfo> attachmentList, String message) {
	    // 设置当前用户，用户评论绑定userId
        Authentication.setAuthenticatedUserId(userId);
		if (StringUtils.isNotEmpty(message)) {
			taskService.addComment(taskId, processInstanceId, message);
		}
		if (attachmentList != null && attachmentList.size() > 0) {
		    for (ProcessTaskAttachmentInfo attachment : attachmentList) {
		        taskService.createAttachment(attachment.getAttachmentType(), taskId, processInstanceId, 
		                attachment.getAttachmentName(), attachment.getAttachmentDescription(), attachment.getUrl());    
		    }
		}
		taskService.complete(taskId, variables);
	}
	
	@Transactional(readOnly = true) //只查事务申明
	@Override
	public ProcessTaskInfo getProcessTask(String taskId) {
	    TaskQuery query = taskService.createTaskQuery();
	    query.taskId(taskId);
        return buildProcessTaskInfo(query.active().singleResult());
	}
	
	@Transactional(readOnly = true) //只查事务申明
	@Override
	public ProcessTaskInfo getCurrentTask(String processInstanceId) {
	    return buildProcessTaskInfo(taskService.createTaskQuery().processInstanceId(processInstanceId).active().orderByTaskCreateTime().desc().listPage(0, 1).get(0));
	}
	
	@Transactional(readOnly = true) //只查事务申明
	public List<ProcessTaskInfo> queryList(WorkflowTaskQuery processTaskQuery) {
		TaskQuery query = taskService.createTaskQuery();
		if (processTaskQuery != null) {
			createQueryParam(query, processTaskQuery);
		}
		List<Task> processTaskList = query.active().orderByTaskId().desc().orderByTaskCreateTime().desc().list();
		return buildProcessTaskInfo(processTaskList);
	}
	
	@Transactional(readOnly = true) //只查事务申明
	public Long queryProcessTaskCount(WorkflowTaskQuery workflowTaskQuery) {
		TaskQuery query = taskService.createTaskQuery();
		if (workflowTaskQuery != null) {
			createQueryParam(query, workflowTaskQuery);
		}
		return query.active().count();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true) //只查事务申明
	public ListPage queryPage(int pageNo, int pageSize, WorkflowTaskQuery workflowTaskQuery) {
		// 根据当前人的ID查询
        TaskQuery query = taskService.createTaskQuery();
		if (workflowTaskQuery != null) {
			createQueryParam(query, workflowTaskQuery);
		}
		query.active().orderByTaskId().desc().orderByTaskCreateTime().desc();
		ListPage listPage = processBaseService.queryPage(pageNo, pageSize, query);
		List<Task> processTaskList = listPage.getDataList();
		listPage.setDataList(buildProcessTaskInfo(processTaskList));
		return listPage;
	}
	
	private void createQueryParam(TaskQuery query, WorkflowTaskQuery processTaskQuery) {
		if (processTaskQuery != null) {
		    String taskId = processTaskQuery.taskId(null);
		    String processInstanceBusinessKey = processTaskQuery.processInstanceBusinessKey(null);
		    String processDefinitionKey = processTaskQuery.processDefinitionKey(null);
		    String processDefinitionId = processTaskQuery.processDefinitionId(null);
		    String processInstanceId = processTaskQuery.processInstanceId(null);
		    String taskAssignee = processTaskQuery.taskAssignee(null);
		    String taskCandidateUser = processTaskQuery.taskCandidateUser(null);
		    String taskCandidateOrAssigned = processTaskQuery.taskCandidateOrAssigned(null);
		    if (taskId != null ) query.taskId(taskId);
            if (processInstanceBusinessKey != null) query.processInstanceBusinessKey(processInstanceBusinessKey);
            if (processDefinitionKey != null) query.processDefinitionKey(processDefinitionKey);
            if (processDefinitionId != null) query.processDefinitionId(processDefinitionId);
            if (processInstanceId != null) query.processInstanceId(processInstanceId);
        	if (taskAssignee != null) query.taskAssignee(taskAssignee);
        	if (taskCandidateUser != null) query.taskCandidateUser(taskCandidateUser);
        	if (taskCandidateOrAssigned != null) query.taskCandidateOrAssigned(taskCandidateOrAssigned);
		}
	}
	
	private ProcessTaskInfo buildProcessTaskInfo(Task task) {
	    if (task == null) return null;
	    ProcessDefinitionCache.setRepositoryService(repositoryService);
        // 流程定义KEY definitionKey, 流程定义Name
        ProcessDefinition processDefinition = ProcessDefinitionCache.get(task.getProcessDefinitionId());
        ProcessTaskInfo info = new ProcessTaskInfo();
        info.setId(task.getId());
        info.setAssignee(task.getAssignee());
        info.setCreateTime(task.getCreateTime());
        info.setDescription(task.getDescription());
        info.setDueDate(task.getDueDate());
        info.setName(task.getName());
        info.setOwner(task.getOwner());
        info.setPriority(task.getPriority());
        info.setProcessDefinitionId(task.getProcessDefinitionId());
        info.setProcessDefinitionName(processDefinition.getName());
        info.setProcessDefinitionVersion(processDefinition.getVersion());
        info.setProcessDefinitionKey(processDefinition.getKey());// 定义的key 识别哪个流程
        info.setProcessInstanceId(task.getProcessInstanceId());
        info.setTaskDefinitionKey(task.getTaskDefinitionKey());
        info.setExecutionId(task.getExecutionId());
        return info;
	}
	
	private List<ProcessTaskInfo> buildProcessTaskInfo(List<Task> processTaskList) {
		List<ProcessTaskInfo> dataList = new ArrayList<ProcessTaskInfo>(processTaskList.size());
		ProcessDefinitionCache.setRepositoryService(repositoryService);
		for (Task task : processTaskList) {
			dataList.add(buildProcessTaskInfo(task));
		}
		return dataList;
	}
	
}
