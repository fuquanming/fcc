package com.fcc.commons.workflow.service;

import java.util.Map;

import com.fcc.commons.data.ListPage;

/**
 * <p>Description:流程历史</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface ProcessHistoryService {
	
	/**
	 * 删除流程历史
	 * @param id	
	 */
	void deleteHistoryProcessInstance(String id);
	
	/**
	 * 分页查询
	 * @return
	 */
	ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);
	
}
