package com.fcc.commons.workflow.service;

import java.util.Date;
import java.util.Map;

import com.fcc.commons.data.ListPage;

/**
 * <p>Description:流程定义</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface ProcessDefinitionService {
	
	/**
	 * 激活指定id的流程定义
	 * @param processDefinitionId		流程定义ID
	 * @param activateProcessInstances	激活流程实例
	 * @param activationDate			激活日期
	 */
	void activateProcessDefinitionById(String processDefinitionId,
            boolean activateProcessInstances,
            Date activationDate);
	/**
	 * 挂起指定id的流程定义
	 * @param processDefinitionId		流程定义ID
	 * @param suspendProcessInstances	挂起流程实例
	 * @param suspensionDate			挂起日期
	 */
	void suspendProcessDefinitionById(String processDefinitionId,
            boolean suspendProcessInstances,
            Date suspensionDate);
	
	/**
	 * 删除部署的流程
	 * @param deploymentId	部署ID
	 * @param cascade		是否级联删除流程实例
	 */
	void deleteDeployment(String deploymentId, boolean cascade);
	
	/**
	 * 流程定义转换模型
	 * @param processDefinitionId	流程定义ID
	 */
	void convertToModel(String processDefinitionId);

	/**
	 * 分页查询
	 * @return
	 */
	ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);
	
}
