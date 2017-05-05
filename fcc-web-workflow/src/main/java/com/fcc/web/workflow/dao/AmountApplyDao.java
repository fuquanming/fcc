package com.fcc.web.workflow.dao;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.web.workflow.model.AmountApply;

/**
 * <p>Description:AmountApply</p>
 */
public interface AmountApplyDao {
    /**
     * 分页查询
     * @return
     */
    ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
    /**
     * 分页查询
     * @return
     */
    List<AmountApply> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
    /**
     * 报表
     * @return
     */
    ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
}
