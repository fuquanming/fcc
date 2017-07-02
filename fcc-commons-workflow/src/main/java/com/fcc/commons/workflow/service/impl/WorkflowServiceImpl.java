package com.fcc.commons.workflow.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
//import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.workflow.common.WorkflowVariableEnum;
import com.fcc.commons.workflow.listener.TaskListener;
import com.fcc.commons.workflow.model.WorkflowBean;
import com.fcc.commons.workflow.query.WorkflowDefinitionQuery;
import com.fcc.commons.workflow.query.WorkflowHistoryQuery;
import com.fcc.commons.workflow.query.WorkflowInstanceQuery;
import com.fcc.commons.workflow.query.WorkflowModelQuery;
import com.fcc.commons.workflow.query.WorkflowTaskQuery;
import com.fcc.commons.workflow.query.impl.WorkflowDefinitionQueryImpl;
import com.fcc.commons.workflow.query.impl.WorkflowHistoryQueryImpl;
import com.fcc.commons.workflow.query.impl.WorkflowInstanceQueryImpl;
import com.fcc.commons.workflow.query.impl.WorkflowModelQueryImpl;
import com.fcc.commons.workflow.query.impl.WorkflowTaskQueryImpl;
import com.fcc.commons.workflow.service.ProcessDefinitionService;
import com.fcc.commons.workflow.service.ProcessHistoryService;
import com.fcc.commons.workflow.service.ProcessInstanceService;
import com.fcc.commons.workflow.service.ProcessTaskService;
import com.fcc.commons.workflow.service.WorkflowService;
import com.fcc.commons.workflow.view.ProcessHistoryInfo;
import com.fcc.commons.workflow.view.ProcessInstanceInfo;
import com.fcc.commons.workflow.view.ProcessTaskAttachmentInfo;
import com.fcc.commons.workflow.view.ProcessTaskCommentInfo;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.commons.workflow.view.ProcessTaskSequenceFlowInfo;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Service
@SuppressWarnings("unchecked")
public class WorkflowServiceImpl implements WorkflowService {

    @Resource
	private IdentityService identityService;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private RepositoryService repositoryService;
	@Autowired
	private ProcessEngineFactoryBean processEngine;
	@Resource
	private ProcessDefinitionService processDefinitionService;
	@Resource
	private ProcessInstanceService processInstanceService;
	@Resource
	private ProcessHistoryService processHistoryService;
	@Resource
	private ProcessTaskService processTaskService;
	@Resource
	private TaskService taskService;
//	@Resource
//	private ProcessDiagramGenerator processDiagramGenerator;
	
	private Set<TaskListener> taskListeners = new HashSet<TaskListener>();

    @Override
    public WorkflowTaskQuery createTaskQuery() {
        return new WorkflowTaskQueryImpl();
    }
	
	@Override
    public WorkflowDefinitionQuery createDefinitionQuery() {
        return new WorkflowDefinitionQueryImpl();
    }
	
	@Override
    public WorkflowInstanceQuery createInstanceQuery() {
        return new WorkflowInstanceQueryImpl();
    }
	
	@Override
    public WorkflowHistoryQuery createHistoryQuery() {
        return new WorkflowHistoryQueryImpl();
    }
	
	@Override
    public WorkflowModelQuery createModelQuery() {
        return new WorkflowModelQueryImpl();
    }
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public String startWorkflow(WorkflowBean workflowBean, String userId, String userName, Map<String, Object> variables) {
		//流程变量requestUser=当前登录用户,model=业务数据
		if (variables == null) variables = new HashMap<String, Object>(2);
		variables.put(WorkflowVariableEnum.requestUserId.toString(), userId);
		variables.put(WorkflowVariableEnum.requestUserName.toString(), userName);
		//definitionKey：流程定义的id
		//businessKey：流程业务数据id,存放请假单Id,业务数据ID,在进行流程跟踪时，需要通过请假单id,查询流程实例，进行请假单流程跟踪
		ProcessInstance processInstance = null;
		try {
//			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(userId);
			processInstance = runtimeService.startProcessInstanceByKey(workflowBean.getDefinitionKey(), workflowBean.getBusinessKey(), variables);
		} finally {
//			identityService.setAuthenticatedUserId(null);
		}
		workflowBean.setProcessDefinitionId(processInstance.getProcessDefinitionId());
		workflowBean.setProcessInstanceId(processInstance.getProcessInstanceId());
		return processInstance.getId();
	}
	
	@Transactional(readOnly = true) //只查事务申明
	@Override
    public ProcessTaskInfo getCurrentTask(String processInstanceId) {
        return processTaskService.getCurrentTask(processInstanceId);
    }
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void taskClaim(String taskId, String userId) {
		processTaskService.claim(taskId, userId);
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void taskComplete(String userId, String taskId, String processInstanceId, Map<String, Object> variables, String message) {
		processTaskService.complete(userId, taskId, processInstanceId, variables, null, message);
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	@Override
    public void taskComplete(String userId, String taskId, String processInstanceId, Map<String, Object> variables, String message, List<ProcessTaskAttachmentInfo> attachmentList, HttpServletRequest request) {
	    for (TaskListener task : taskListeners) {
	        task.beforeComplete(request, variables);
        }
	    processTaskService.complete(userId, taskId, processInstanceId, variables, attachmentList, message);
	    for (TaskListener task : taskListeners) {
	        task.afterComplete(request, variables);
        }
    }
	
	@Transactional(readOnly = true) //只查事务申明
	public InputStream trace(String processInstanceId, String businessKey, String definitionKey) {
		ProcessInstance processInstance = null;
		if (processInstanceId != null && !"".equals(processInstanceId)) { // 流程实例中进行流程跟踪
			processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();
		} else { // 业务数据，审批中进行流程跟踪
			processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceBusinessKey(businessKey)
					.processDefinitionKey(definitionKey).singleResult();
		}		
		
		//通过流程定义id得到bpmnModel
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
		//得到正在执行的环节id
		List<String> activeIds = runtimeService.getActiveActivityIds(processInstance.getId());
		//打印流程图
		Context.setProcessEngineConfiguration(processEngine.getProcessEngineConfiguration());
//		return processDiagramGenerator.generateDiagram(bpmnModel, "png", activeIds);
		
		// 使用spring注入引擎请使用下面的这行代码
		ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        return diagramGenerator.generateDiagram(bpmnModel, "png", activeIds);
	}	
	
	@Transactional(readOnly = true) //只查事务申明
	public List<ProcessTaskSequenceFlowInfo> getTaskOutSequenceFlow(ProcessTaskInfo processTaskInfo) {
		return processTaskService.getTaskOutSequenceFlow(processTaskInfo);
	}
	
	@Transactional(readOnly = true) //只查事务申明
    @Override
    public List<ProcessTaskInfo> getTasks(String processInstanceId) {
	    return processHistoryService.query(processInstanceId);
    }
	
	@Transactional(readOnly = true) //只查事务申明	
	public List<ProcessTaskCommentInfo> getComments(String processInstanceId) {
		return processInstanceService.getProcessInstanceComments(processInstanceId);
	}
	
	@Transactional(readOnly = true) //只查事务申明
    public ListPage queryPageProcessDefinition(int pageNo, int pageSize,
            WorkflowDefinitionQuery workflowDefinitionQuery) {
        return processDefinitionService.queryPage(pageNo, pageSize, workflowDefinitionQuery);
    }
	
	@Transactional(readOnly = true) //只查事务申明
	public ListPage queryPageProcessInstance(int pageNo, int pageSize,
			WorkflowInstanceQuery workflowInstanceQuery) {
		return processInstanceService.queryPage(pageNo, pageSize, workflowInstanceQuery);
	}
	
	@Transactional(readOnly = true) //只查事务申明	
	public ListPage queryPageProcessHistory(int pageNo, int pageSize,
			WorkflowHistoryQuery workflowHistoryQuery) {
		return processHistoryService.queryPage(pageNo, pageSize, workflowHistoryQuery);
	}
	
	@Transactional(readOnly = true) //只查事务申明
	@Override
    public ProcessTaskInfo getProcessTask(String taskId) {
        ProcessTaskInfo info = processTaskService.getProcessTask(taskId);
        info.setProcessVariables(processTaskService.getVariables(info.getId()));
	    return info;
    }
	
	@Transactional(readOnly = true) //只查事务申明
	@Override
    public ProcessInstanceInfo getProcessInstace(String instaceId) {
        return processInstanceService.getProcessInstace(instaceId);
    }
	
	@Transactional(readOnly = true) //只查事务申明
	@Override
	public ProcessHistoryInfo getHistoricProcessInstance(String instaceId) {
	    return processHistoryService.getProcessInstace(instaceId);
	}
	
	@Transactional(readOnly = true) //只查事务申明
	public List<ProcessTaskInfo> queryProcessTask(WorkflowTaskQuery processTaskQuery) {
		List<ProcessTaskInfo> infoList = processTaskService.queryList(processTaskQuery);
		// 设置变量
		for (ProcessTaskInfo info : infoList) {
			info.setProcessVariables(processTaskService.getVariables(info.getId()));
		}
		return infoList;
	}
	
	@Transactional(readOnly = true) //只查事务申明	
	public Long queryProcessTaskCount(WorkflowTaskQuery processTaskQuery) {
		return processTaskService.queryProcessTaskCount(processTaskQuery);
	}
	
	@Transactional(readOnly = true) //只查事务申明	
	public ListPage queryPageProcessTask(int pageNo, int pageSize, WorkflowTaskQuery processTaskQuery) {
		// 添加变量
		ListPage listPage = processTaskService.queryPage(pageNo, pageSize, processTaskQuery);
		List<ProcessTaskInfo> infoList = listPage.getDataList();
		
		List<String> processVariablesList = new ArrayList<String>(2);
		processVariablesList.add(WorkflowVariableEnum.requestUserId.toString());
		processVariablesList.add(WorkflowVariableEnum.requestUserName.toString());
		
		for (ProcessTaskInfo info : infoList) {
			info.setProcessVariables(processTaskService.getVariables(info.getId(), processVariablesList));
		}
		
		return listPage;
	}
	
	@Transactional(readOnly = true) //只查事务申明 
	@Override
    public Map<String, Object> getTaskBusinessData(ProcessTaskInfo taskInfo, String businessKey) {
	    for (TaskListener task : taskListeners) {
            Map<String, Object> map = task.getBusinessData(taskInfo, businessKey);
            if (map != null) {
                return map;
            }
        }
        return null;
    }
	
	@Override
    public Set<TaskListener> getTaskListeners() {
        return taskListeners;
    }

    public void setTaskListeners(Set<TaskListener> taskListeners) {
        this.taskListeners = taskListeners;
    }
}

