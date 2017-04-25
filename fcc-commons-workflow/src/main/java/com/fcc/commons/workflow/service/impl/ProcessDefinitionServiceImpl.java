package com.fcc.commons.workflow.service.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.web.common.Constants;
import com.fcc.commons.workflow.query.WorkflowDefinitionQuery;
import com.fcc.commons.workflow.service.ProcessDefinitionService;
import com.fcc.commons.workflow.view.ProcessDefinitionInfo;

/**
 * <p>Description:流程定义</p>
 * <p>Copyright:Copyright (c) 2009</p>
 * 
 * @author 傅泉明
 * @version v1.0
 */
@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    @Resource
	private RepositoryService repositoryService;
    @Resource
	private ProcessBaseService processBaseService;
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void activateProcessDefinitionById(String processDefinitionId, boolean activateProcessInstances, java.util.Date activationDate) {
		repositoryService.activateProcessDefinitionById(processDefinitionId, activateProcessInstances, activationDate);
	};
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void suspendProcessDefinitionById(String processDefinitionId, boolean suspendProcessInstances, java.util.Date suspensionDate) {
		repositoryService.suspendProcessDefinitionById(processDefinitionId, suspendProcessInstances, suspensionDate);
	};
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void deleteDeployment(String deploymentId, boolean cascade) {
		repositoryService.deleteDeployment(deploymentId, cascade);
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void convertToModel(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
		        .processDefinitionId(processDefinitionId).singleResult();
		InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
		        processDefinition.getResourceName());
		XMLInputFactory xif = XMLInputFactory.newInstance();
		InputStreamReader in = null;
		XMLStreamReader xtr = null;
		try {
			in = new InputStreamReader(bpmnStream, Constants.encode);
			xtr = xif.createXMLStreamReader(in);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
		
		BpmnJsonConverter converter = new BpmnJsonConverter();
		ObjectNode modelNode = converter.convertToJson(bpmnModel);
		Model modelData = repositoryService.newModel();
		modelData.setKey(processDefinition.getKey());
		modelData.setName(processDefinition.getResourceName());
		modelData.setCategory(processDefinition.getDeploymentId());
		
		ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
		modelData.setMetaInfo(modelObjectNode.toString());
		
		repositoryService.saveModel(modelData);
		
		try {
			repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes(Constants.encode));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)//只查事务申明
	public ListPage queryPage(int pageNo, int pageSize, WorkflowDefinitionQuery workflowDefinitionQuery) {
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		if (workflowDefinitionQuery != null) {
		    String definitionName = workflowDefinitionQuery.processDefinitionNameLike(null);
	        if (definitionName != null) {
	            query.processDefinitionNameLike("%" + definitionName + "%");
	        }
	        String definitionKey = workflowDefinitionQuery.processDefinitionKey(null);
	        if (definitionKey != null) {
	            query.processDefinitionKey(definitionKey);
	        }
		}
		query.orderByDeploymentId().desc();
		ListPage listPage = processBaseService.queryPage(pageNo, pageSize, query);
		List<ProcessDefinition> processDefinitionList = listPage.getDataList();
		List<ProcessDefinitionInfo> dataList = new ArrayList<ProcessDefinitionInfo>();
		for (ProcessDefinition processDefinition : processDefinitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            ProcessDefinitionInfo info = new ProcessDefinitionInfo();
            info.setProcessDefinitionId(processDefinition.getId());
            info.setProcessDeploymentId(processDefinition.getDeploymentId());
            info.setProcessName(processDefinition.getName());
            info.setProcessKey(processDefinition.getKey());
            info.setProcessVersion(processDefinition.getVersion());
            info.setProcessResourceName(processDefinition.getResourceName());
            info.setProcessDiagramResourceName(processDefinition.getDiagramResourceName());
            info.setDeploymentTime(deployment.getDeploymentTime());
            info.setProcessSuspended(processDefinition.isSuspended());
            dataList.add(info);
        }
		listPage.setDataList(dataList);
		return listPage;
	}

}