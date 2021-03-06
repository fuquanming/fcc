package com.fcc.commons.workflow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.workflow.query.WorkflowHistoryQuery;
import com.fcc.commons.workflow.service.ProcessHistoryService;
import com.fcc.commons.workflow.service.ProcessInstanceService;
import com.fcc.commons.workflow.service.ProcessTaskService;
import com.fcc.commons.workflow.util.ProcessDefinitionCache;
import com.fcc.commons.workflow.view.ProcessHistoryInfo;
import com.fcc.commons.workflow.view.ProcessTaskAttachmentInfo;
import com.fcc.commons.workflow.view.ProcessTaskCommentInfo;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
/**
 * <p>Description:流程历史</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Service
public class ProcessHistoryServiceImpl implements ProcessHistoryService {

    @Resource
	private RepositoryService repositoryService;
    @Resource
	private HistoryService historyService;
//    @Resource
//    private TaskService taskService;
    @Resource
    private ProcessInstanceService processInstanceService;
    @Resource
    private ProcessTaskService processTaskService;
    @Resource
	private ProcessBaseService processBaseService;
	
    private ProcessHistoryInfo buildProcessHistoryInfo(HistoricProcessInstance historicProcessInstance) {
        if (historicProcessInstance == null) return null;
        ProcessHistoryInfo info = new ProcessHistoryInfo();
        info.setId(historicProcessInstance.getId());
        info.setBusinessKey(historicProcessInstance.getBusinessKey());
        info.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
        info.setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey());
        info.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
        info.setProcessDefinitionVersion(historicProcessInstance.getProcessDefinitionVersion());
        info.setStartTime(historicProcessInstance.getStartTime());
        info.setEndTime(historicProcessInstance.getEndTime());
        info.setDurationInMillis(historicProcessInstance.getDurationInMillis());
        info.setDeleteReason(historicProcessInstance.getDeleteReason());
        info.setStartUserId(historicProcessInstance.getStartUserId());
        HistoricProcessInstanceEntity entiy = (HistoricProcessInstanceEntity) historicProcessInstance;
        info.setProcessInstanceId(entiy.getProcessInstanceId());
        return info;
    }
    
    @Transactional(readOnly = true)//只查事务申明
    @Override
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
        Map<String, Object> variables = new HashMap<String, Object>(historicVariableInstances.size());
        for (HistoricVariableInstance historicProcessInstance : historicVariableInstances) {
            variables.put(historicProcessInstance.getVariableName(), historicProcessInstance.getValue());
        }
        return variables;
    }
    
    @Transactional(readOnly = true)//只查事务申明
    @Override
    public Object getProcessVariable(String processInstanceId, String variableName) {
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).variableName(variableName).singleResult();
        return historicVariableInstance.getValue();
    }
    
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void deleteHistoryProcessInstance(String processInstanceId) {
		historyService.deleteHistoricProcessInstance(processInstanceId);
	}
	
	@Transactional(readOnly = true)//只查事务申明
	@Override
	public ProcessHistoryInfo getProcessInstace(String processInstanceId) {
	    HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
	    return buildProcessHistoryInfo(query.processInstanceId(processInstanceId).singleResult());
	}

	@SuppressWarnings("unchecked")
    @Transactional(readOnly = true)//只查事务申明
	public ListPage queryPage(int pageNo, int pageSize,
	        WorkflowHistoryQuery workflowHistoryQuery) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		if (workflowHistoryQuery != null) {
		    String businessKey = workflowHistoryQuery.processInstanceBusinessKey(null);
	        if (businessKey != null) {
	            query.processInstanceBusinessKey(businessKey);
	        }
	        String definitionKey = workflowHistoryQuery.processDefinitionKey(null);
	        if (definitionKey != null) {
	            query.processDefinitionKey(definitionKey);
	        }
	        String processInstanceId = workflowHistoryQuery.processInstanceId(null);
	        if (processInstanceId != null) {
	            query.processInstanceId(processInstanceId);
	        }
	        String startedBy = workflowHistoryQuery.startedBy(null);
            if (startedBy != null) {
                query.startedBy(startedBy);
            }
            String involvedUser = workflowHistoryQuery.involvedUser(null);
            if (involvedUser != null) {
                query.involvedUser(involvedUser);
            }
		}
		query.finished().orderByProcessInstanceEndTime().desc();
		ListPage listPage = processBaseService.queryPage(pageNo, pageSize, query);
		List<HistoricProcessInstance> historicProcessInstanceList = listPage.getDataList();
		List<ProcessHistoryInfo> dataList = new ArrayList<ProcessHistoryInfo>(historicProcessInstanceList.size());
		for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
			dataList.add(buildProcessHistoryInfo(historicProcessInstance));
        }
		listPage.setDataList(dataList);
		return listPage;
	}
	
	@Transactional(readOnly = true)//只查事务申明
	public List<ProcessTaskInfo> query(String processInstanceId) {
	    HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
	    List<HistoricTaskInstance> list = query.processInstanceId(processInstanceId)
	                                            .orderByTaskId().desc().list();
	    List<ProcessTaskInfo> taskList = new ArrayList<ProcessTaskInfo>(list.size());
	    ProcessDefinitionCache.setRepositoryService(repositoryService);
	    // 获取评论
	    List<ProcessTaskCommentInfo> commentList = processInstanceService.getProcessInstanceComments(processInstanceId);
	    // 任务附件
	    List<ProcessTaskAttachmentInfo> attachmentList = processInstanceService.getProcessInstanceAttachments(processInstanceId);
	    for (HistoricTaskInstance task : list) {
	        ProcessTaskInfo info = new ProcessTaskInfo();
	        // 流程定义KEY definitionKey, 流程定义Name
	        ProcessDefinition processDefinition = ProcessDefinitionCache.get(task.getProcessDefinitionId());
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
	        
	        info.setStartTime(task.getStartTime());
	        info.setEndTime(task.getEndTime());
	        info.setDurationInMillis(task.getDurationInMillis());
	        
	        for (ProcessTaskCommentInfo comment : commentList) {
	            if (comment.getTaskId().equals(info.getId())) {
	                info.setComment(comment.getComment());
	                info.setCommentTime(comment.getTime());
	            }
	        }
	        
	        for (ProcessTaskAttachmentInfo attachment : attachmentList) {
                if (attachment.getTaskId().equals(info.getId())) {
                    List<ProcessTaskAttachmentInfo> taskAttachmentInfoList = info.getAttachmentList();
                    if (taskAttachmentInfoList == null) taskAttachmentInfoList = new ArrayList<ProcessTaskAttachmentInfo>();
                    ProcessTaskAttachmentInfo attachmentInfo = new ProcessTaskAttachmentInfo();
                    
                    attachmentInfo.setAttachmentDescription(attachment.getAttachmentDescription());
                    attachmentInfo.setAttachmentName(attachment.getAttachmentName());
                    attachmentInfo.setAttachmentType(attachment.getAttachmentType());
                    attachmentInfo.setTaskId(info.getId());
                    attachmentInfo.setUrl(attachment.getUrl());
                    attachmentInfo.setProcessInstanceId(processInstanceId);
                    
                    taskAttachmentInfoList.add(attachmentInfo);
                    info.setAttachmentList(taskAttachmentInfoList);
                }
            }
	        
	        taskList.add(info);
	    }
	    return taskList;
	}
}
