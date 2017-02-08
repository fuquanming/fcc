package com.fcc.commons.workflow.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.workflow.common.WorkflowHistoryQueryEnum;
import com.fcc.commons.workflow.service.ProcessHistoryService;
import com.fcc.commons.workflow.util.ProcessDefinitionCache;
import com.fcc.commons.workflow.view.ProcessHistoryInfo;
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
    @Resource
	private ProcessBaseService processBaseService;
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void deleteHistoryProcessInstance(String id) {
		historyService.deleteHistoricProcessInstance(id);
	}

	@SuppressWarnings("unchecked")
    @Transactional(readOnly = true)//只查事务申明
	public ListPage queryPage(int pageNo, int pageSize,
			Map<String, Object> param) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		if (param != null) {
            String businessKey = (String) param.get(WorkflowHistoryQueryEnum.businessKey.toString());
            if (businessKey != null) {
            	query.processInstanceBusinessKey(businessKey);
            }
            String definitionKey = (String) param.get(WorkflowHistoryQueryEnum.definitionKey.toString());
            if (definitionKey != null) {
            	query.processDefinitionKey(definitionKey);
            }
		}
		query.finished().orderByProcessInstanceEndTime().desc();
		ListPage listPage = processBaseService.queryPage(pageNo, pageSize, query);
		List<HistoricProcessInstance> historicProcessInstanceList = listPage.getDataList();
		List<ProcessHistoryInfo> dataList = new ArrayList<ProcessHistoryInfo>();
		ProcessDefinitionCache.setRepositoryService(repositoryService);
		for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
			ProcessHistoryInfo info = new ProcessHistoryInfo();
			info.setId(historicProcessInstance.getId());
			info.setBusinessKey(historicProcessInstance.getBusinessKey());
			info.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
			info.setProcessDefinitionVersion(ProcessDefinitionCache.get(historicProcessInstance.getProcessDefinitionId()).getVersion());
			info.setStartTime(historicProcessInstance.getStartTime());
			info.setEndTime(historicProcessInstance.getEndTime());
			info.setDurationInMillis(historicProcessInstance.getDurationInMillis());
			info.setDeleteReason(historicProcessInstance.getDeleteReason());
			dataList.add(info);
        }
		listPage.setDataList(dataList);
		return listPage;
	}

}
