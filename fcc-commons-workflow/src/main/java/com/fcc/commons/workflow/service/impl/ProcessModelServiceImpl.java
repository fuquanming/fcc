package com.fcc.commons.workflow.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.web.common.Constants;
import com.fcc.commons.workflow.query.WorkflowModelQuery;
import com.fcc.commons.workflow.service.ProcessModelService;
/**
 * 
 * <p>Description:流程模型</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Service
public class ProcessModelServiceImpl implements ProcessModelService {

    @Resource
	private RepositoryService repositoryService;
    @Resource
	private ProcessBaseService processBaseService;
	
	@SuppressWarnings("deprecation")
    @Transactional(rollbackFor = Exception.class)//事务申明
	public String addModel(String modelKey, String modelName, String modelDescription) {
		String modelId = null;
		try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, modelName);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelDescription = StringUtils.defaultString(modelDescription);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, modelDescription);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(modelName);
            modelData.setKey(StringUtils.defaultString(modelKey));

            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes(Constants.encode));

            modelId = modelData.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelId;
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void deleteModel(String modelId) {
		repositoryService.deleteModel(modelId);
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public String deploy(String modelId) {
        String deppoymentId = null;
		try {
			Model modelData = repositoryService.getModel(modelId);
			ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
			byte[] bpmnBytes = null;

			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			bpmnBytes = new BpmnXMLConverter().convertToXML(model);

			String processName = modelData.getName() + ".bpmn20.xml";
			// 字符集编码
			Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes, Constants.encode)).deploy();
			deppoymentId = deployment.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return deppoymentId;
	}
	
	@Transactional(readOnly = true)//只查事务申明
	public Map<String, Object> exportXML(String modelId) {
		Map<String, Object> map = new HashMap<String, Object>();
		InputStream is = null;
        try {
			Model modelData = repositoryService.getModel(modelId);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
			is = new ByteArrayInputStream(bpmnBytes);
			map.put("is", is);
			map.put("id", bpmnModel.getMainProcess().getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
        return map;
	}
	
	@Transactional(readOnly = true)//只查事务申明
	public ListPage queryPage(int pageNo, int pageSize,
	        WorkflowModelQuery workflowModelQuery) {
		ModelQuery query = repositoryService.createModelQuery();
		if (workflowModelQuery != null) {
			String modelName = workflowModelQuery.modelNameLike(null);
            if (modelName != null) {
            	query.modelNameLike("%" + modelName + "%");
            }
            String modelKey = workflowModelQuery.modelKey(null);
            if (modelKey != null) {
            	query.modelKey(modelKey);
            }
		}
		query.orderByLastUpdateTime().desc();
		return processBaseService.queryPage(pageNo, pageSize, query);
	}

}
