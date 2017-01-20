package com.fcc.web.sys.dao;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.model.SysKey;

/**
 * <p>Description:SysKey</p>
 */
public interface SysKeyDao {
    /**
     * 删除
     * @param linkType
     * @param linkIds
     */
    void delete(String linkType, String[] linkIds);
    /**
     * 分页查询
     * @return
     */
    ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
    /**
     * 分页查询
     * @return
     */
    List<SysKey> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
    /**
     * 报表
     * @return
     */
    ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
}
