package com.fcc.commons.workflow.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.workflow.query.WorkflowInstanceQuery;
import com.fcc.commons.workflow.service.ProcessInstanceService;
import com.fcc.commons.workflow.util.ProcessDefinitionCache;
import com.fcc.commons.workflow.view.ProcessInstanceInfo;
import com.fcc.commons.workflow.view.ProcessTaskCommentInfo;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
/**
 * <p>Description:流程实例</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Service
public class ProcessInstanceServiceImpl implements ProcessInstanceService {

    @Resource
	private RepositoryService repositoryService;
    @Resource
	private RuntimeService runtimeService;
    @Resource
	private TaskService taskService;
    @Resource
	private ProcessBaseService processBaseService;
	@Resource
	private HistoryService historyService;
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void activateProcessInstanceById(String processInstanceId) {
		runtimeService.activateProcessInstanceById(processInstanceId);
	};
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void suspendProcessInstanceById(String processInstanceId) {
		runtimeService.suspendProcessInstanceById(processInstanceId);
	};
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void deleteProcessInstance(String id, String info) {
		runtimeService.deleteProcessInstance(id, info);
	}
	
	@Transactional(readOnly = true) //只查事务申明
	@Override
	public List<ProcessTaskInfo> getProcessInstanceCommentWithTasks(String processInstanceId) {
	    List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
        List<ProcessTaskInfo> list = new ArrayList<ProcessTaskInfo>(comments.size());
        for (Comment comment : comments) {
            HistoricTaskInstance task = historyService
              .createHistoricTaskInstanceQuery()
              .taskId(comment.getTaskId()).singleResult();
            
            ProcessTaskInfo info = new ProcessTaskInfo();
            info.setComment(comment.getFullMessage());
            info.setCommentTime(comment.getTime());
            info.setProcessInstanceId(comment.getProcessInstanceId());
            
            info.setId(task.getId());
            info.setAssignee(task.getAssignee());
            info.setCreateTime(task.getCreateTime());
            info.setDescription(task.getDescription());
            info.setDueDate(task.getDueDate());
            info.setName(task.getName());
            info.setOwner(task.getOwner());
            info.setPriority(task.getPriority());
            info.setProcessDefinitionId(task.getProcessDefinitionId());
            info.setTaskDefinitionKey(task.getTaskDefinitionKey());
            info.setExecutionId(task.getExecutionId());
            list.add(info);
        }
        return list;
	}
	
	@Transactional(readOnly = true) //只查事务申明
	public List<ProcessTaskCommentInfo> getProcessInstanceComments(String processInstanceId) {
	    // 流程附件
//		List<Attachment> attachments = taskService.getProcessInstanceAttachments(processInstanceId);
//		for (Attachment comment : attachments) {
//			Map<String, String> map = new HashMap<String, String>();
//			HistoricTaskInstance t = historyService
//					.createHistoricTaskInstanceQuery()
//					.taskId(comment.getTaskId()).singleResult();
//			String commentName = t.getName() + "(" + comment.getName() + ")";
//			map.put("taskName", commentName);
//			map.put("comment", comment.getDescription());
//			list.add(map);
//		}
		List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
		List<ProcessTaskCommentInfo> list = new ArrayList<ProcessTaskCommentInfo>(comments.size());
		for (Comment comment : comments) {
//			HistoricTaskInstance task = historyService
//			.createHistoricTaskInstanceQuery()
//			.taskId(comment.getTaskId()).singleResult();
			
		    ProcessTaskCommentInfo info = new ProcessTaskCommentInfo();
		    info.setComment(comment.getFullMessage());
		    info.setId(comment.getId());
		    info.setProcessInstanceId(comment.getProcessInstanceId());
		    info.setTaskId(comment.getTaskId());
		    info.setTime(comment.getTime());
		    info.setUserId(comment.getUserId());
			list.add(info);
		}
		return list;
	}
	
	@Transactional(readOnly = true)//只查事务申明
	@Override
	public ProcessInstanceInfo getProcessInstace(String instaceId) {
	    ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
	    query.processInstanceId(instaceId);// 流程实例ID
	    return buildProcessInstanceInfo(query.singleResult());
	}	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)//只查事务申明
	public ListPage queryPage(int pageNo, int pageSize,
			WorkflowInstanceQuery workflowInstanceQuery) {
		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
		if (workflowInstanceQuery != null) {
		    String processInstanceId = workflowInstanceQuery.processInstanceId(null);
	        if (processInstanceId != null) {
	            query.processInstanceId(processInstanceId);// 流程实例ID
	        }
	        String businessKey = workflowInstanceQuery.processInstanceBusinessKey(null);
	        if (businessKey != null) {
	            query.processInstanceBusinessKey(businessKey);// 关联业务ID
	        }
	        String definitionKey = workflowInstanceQuery.processDefinitionKey(null);// 关联业务KEY，如leave 请假
	        if (definitionKey != null) {
	            query.processDefinitionKey(definitionKey);
	        }
	        String active = workflowInstanceQuery.active(null);// 是否 运行 中的流程实例
	        if (active != null) {
	            query.active();
	        }
		}
		query.orderByProcessDefinitionKey().orderByProcessInstanceId().desc();
		ListPage listPage = processBaseService.queryPage(pageNo, pageSize, query);
		List<ProcessInstance> processInstanceList = listPage.getDataList();
		List<ProcessInstanceInfo> dataList = new ArrayList<ProcessInstanceInfo>(processInstanceList.size());
		for (ProcessInstance processInstance : processInstanceList) {
			dataList.add(buildProcessInstanceInfo(processInstance));
        }
		listPage.setDataList(dataList);
		return listPage;
	}

	private ProcessInstanceInfo buildProcessInstanceInfo(ProcessInstance processInstance) {
	    if (processInstance == null) return null;
	    ProcessDefinitionCache.setRepositoryService(repositoryService);
	    // 流程定义KEY definitionKey, 流程定义Name
        ProcessDefinition processDefinition = ProcessDefinitionCache.get(processInstance.getProcessDefinitionId());
        ProcessInstanceInfo info = new ProcessInstanceInfo();
        info.setId(processInstance.getId());
        info.setBusinessKey(processInstance.getBusinessKey());
        // 使用任务信息
        // info.setCurrentNodeName(ProcessDefinitionCache.getActivityName(processInstance.getProcessDefinitionId(), ObjectUtils.toString(processInstance.getActivityId())));
        // 设置当前任务信息
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByTaskCreateTime().desc().listPage(0, 20);
        Task task = taskList.get(0);
        info.setCurrentNodeName(task.getName());
        info.setTaskAssignee(task.getAssignee());
        info.setTaskCreateTime(task.getCreateTime());
        info.setProcessDefinitionVersion(ProcessDefinitionCache.get(processInstance.getProcessDefinitionId()).getVersion());
        
        info.setEnded(processInstance.isEnded());
        info.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        info.setProcessInstanceId(processInstance.getProcessInstanceId());
        info.setSuspended(processInstance.isSuspended());
        info.setDefinitionKey(processDefinition.getKey());
        info.setDefinitionName(processDefinition.getName());
        return info;
	}
}
