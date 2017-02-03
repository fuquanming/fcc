package com.fcc.web.sys.dao;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.model.SysAnnex;

/**
 * <p>Description:SysAnnex</p>
 */
public interface SysAnnexDao {
    /**
     * 查询附件
     * @param linkId
     * @param linkType
     * @param annexType
     * @return
     */
    List<SysAnnex> query(String linkId, String linkType, String annexType);
    /**
     * 分页查询
     * @return
     */
    ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
    /**
     * 分页查询
     * @return
     */
    List<SysAnnex> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
    /**
     * 报表
     * @return
     */
    ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
}
