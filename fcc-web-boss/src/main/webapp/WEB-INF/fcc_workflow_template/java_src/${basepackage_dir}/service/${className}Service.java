<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;

import ${basepackage}.model.${className};

/**
 * <p>Description:${table.tableAlias}</p>
 */

public interface ${className}Service {
    /**
     * 新增,启动流程
     * @param ${classNameLower}
     */
    void add(${className} ${classNameLower});
    /**
     * 修改，启动流程
     * @param ${classNameLower}
     */
    void edit(${className} ${classNameLower});
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
	List<${className}> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
	/**
	 * 报表
	 * @return
	 */
	ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
}