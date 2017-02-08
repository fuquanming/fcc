package com.fcc.commons.workflow.service;

import java.util.Map;

import com.fcc.commons.data.ListPage;

/**
 * <p>Description:流程模型</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface ProcessModelService {
	
	/**
	 * 
	 * @param key		模型KEY
	 * @param name		模型名称
	 * @return			模型ID
	 */
	String addModel(String modelKey, String modelName, String modelDescription);
	
	/**
	 * 删除模型
	 * @param modelId	模型ID
	 */
	void deleteModel(String modelId);
	
	/**
	 * 部署模型
	 * @param modelId
	 */
	String deploy(String modelId);
	
	/**
	 * 导出XML
	 * @param modelId	
	 * @return
	 */
	Map<String, Object> exportXML(String modelId);
	
	/**
	 * 分页查询
	 * @return
	 */
	ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);
	
}
