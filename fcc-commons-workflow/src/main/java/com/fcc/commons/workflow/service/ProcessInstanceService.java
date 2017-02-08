package com.fcc.commons.workflow.service;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.workflow.view.ProcessTaskInfo;

/**
 * <p>Description:流程实例</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface ProcessInstanceService {
	
	/**
	 * 激活指定id的流程实例
	 * @param processInstanceId
	 */
	void activateProcessInstanceById(String processInstanceId);
	
	/**
	 * 挂起制定id的流程实例
	 * @param processInstanceId
	 */
	void suspendProcessInstanceById(String processInstanceId);
	/**
	 * 删除流程实例
	 * @param id
	 * @param info	删除信息
	 */
	void deleteProcessInstance(String id, String info);
	
	/**
	 * 流程实例评论
	 * @param processInstanceId
	 * @return
	 */
	List<ProcessTaskInfo> getProcessInstanceComments(String processInstanceId);
	
	/**
	 * 分页查询
	 * @return
	 */
	ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);
	
}
