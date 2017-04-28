package com.fcc.web.sys.service;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.model.SysAnnex;

/**
 * <p>Description:SysAnnex</p>
 */

public interface SysAnnexService {
    /**
     * 保存附件
     * @param linkType              关联类型
     * @param linkId                关联ID
     * @param annexType             附件类型
     * @param uploadFileNames       上传附件的名称
     * @param uploadFileRealNames   上传附件的真实名称
     */
    List<SysAnnex> add(String linkType, String linkId, String annexType, String[] uploadFileNames, String[] uploadFileRealNames);
    /**
     * 删除附件
     * @param annexIds
     */
    void delete(String[] annexIds);
    /**
     * 删除附件
     * @param sysAnnex
     */
    void delete(SysAnnex sysAnnex);
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