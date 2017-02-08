package com.fcc.commons.workflow.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
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
import com.fcc.commons.workflow.common.WorkflowTaskQueryEnum;
import com.fcc.commons.workflow.service.ProcessTaskService;
import com.fcc.commons.workflow.util.ProcessDefinitionCache;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.commons.workflow.view.ProcessTaskSequenceFlowInfo;

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
	public void complete(String taskId, String processInstanceId, Map<String, Object> variables, String message) {
		if (StringUtils.isNotEmpty(message)) {
			taskService.addComment(taskId, processInstanceId, message);
		}
		taskService.complete(taskId, variables);
	}
	
	@Transactional(readOnly = true) //只查事务申明
	public List<ProcessTaskInfo> queryList(Map<String, Object> param) {
		TaskQuery query = taskService.createTaskQuery();
		if (param != null) {
			createQueryParam(query, param);
		}
		List<Task> processTaskList = query.active().orderByTaskId().desc().orderByTaskCreateTime().desc().list();
		return buildProcessTaskInfo(processTaskList);
	}
	
	@Transactional(readOnly = true) //只查事务申明
	public Long queryPageCount(Map<String, Object> param) {
		TaskQuery query = taskService.createTaskQuery();
		if (param != null) {
			createQueryParam(query, param);
		}
		return query.active().count();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true) //只查事务申明
	public ListPage queryPage(int pageNo, int pageSize,
			Map<String, Object> param) {
		// 根据当前人的ID查询
        TaskQuery query = taskService.createTaskQuery();
		if (param != null) {
			createQueryParam(query, param);
		}
		query.active().orderByTaskId().desc().orderByTaskCreateTime().desc();
		ListPage listPage = processBaseService.queryPage(pageNo, pageSize, query);
		List<Task> processTaskList = listPage.getDataList();
		listPage.setDataList(buildProcessTaskInfo(processTaskList));
		return listPage;
	}
	
	private void createQueryParam(TaskQuery query, Map<String, Object> param) {
		if (param != null) {
			String taskId = (String) param.get(WorkflowTaskQueryEnum.taskId.toString());// 流程业务ID
            if (taskId != null) {
            	query.taskId(taskId);
            }
            String businessKey = (String) param.get(WorkflowTaskQueryEnum.businessKey.toString());// 流程业务ID
            if (businessKey != null) {
            	query.processInstanceBusinessKey(businessKey);
            }
            String definitionKey = (String) param.get(WorkflowTaskQueryEnum.definitionKey.toString());// 流程定义KEY
            if (definitionKey != null) {
            	query.processDefinitionKey(definitionKey);
            }
            String processDefinitionId = (String) param.get(WorkflowTaskQueryEnum.processDefinitionId.toString());// 流程定义ID
            if (processDefinitionId != null) {
            	query.processDefinitionId(processDefinitionId);
            }
            String processInstanceId = (String) param.get(WorkflowTaskQueryEnum.processInstanceId.toString());// 流程实例ID
            if (processInstanceId != null) {
            	query.processInstanceId(processInstanceId);
            }
            String taskAssignee = (String) param.get(WorkflowTaskQueryEnum.taskAssignee.toString());// 想要办理的任务
            String taskCandidateUser = (String) param.get(WorkflowTaskQueryEnum.taskCandidateUser.toString());// 想要签收的任务
            String userId = (String) param.get(WorkflowTaskQueryEnum.userId.toString());// 待签收、待办 任务 sql语句有问题，被签收的任务会显示出来
            if (taskAssignee != null) {
            	query.taskAssignee(taskAssignee);
            } else if (taskCandidateUser != null) {
            	query.taskCandidateUser(taskCandidateUser);
            } else if (userId != null) {
            	query.taskCandidateOrAssigned(userId);
            }
		}
	}
	
	private List<ProcessTaskInfo> buildProcessTaskInfo(List<Task> processTaskList) {
		List<ProcessTaskInfo> dataList = new ArrayList<ProcessTaskInfo>();
		ProcessDefinitionCache.setRepositoryService(repositoryService);
		for (Task task : processTaskList) {
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
			info.setProcessInstanceId(task.getProcessInstanceId());
			info.setTaskDefinitionKey(task.getTaskDefinitionKey());
			info.setExecutionId(task.getExecutionId());
			dataList.add(info);
		}
		return dataList;
	}
	
}
