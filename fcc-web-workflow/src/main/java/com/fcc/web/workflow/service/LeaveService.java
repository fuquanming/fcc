package com.fcc.web.workflow.service;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;

import com.fcc.web.workflow.model.Leave;

/**
 * <p>Description:Leave</p>
 */

public interface LeaveService {
    /**
     * 新增,启动流程
     * @param leave
     */
    void add(Leave leave);
    /**
     * 修改，启动流程
     * @param leave
     */
    void edit(Leave leave);
    /**
     * 更新状态
     * @param dataId
     * @param status
     */
    void updateStatus(String dataId, String status);
	/**
	 * 分页查询
	 * @return
	 */
	ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
	/**
	 * 分页查询
	 * @return
	 */
	List<Leave> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
	/**
	 * 报表
	 * @return
	 */
	ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
}