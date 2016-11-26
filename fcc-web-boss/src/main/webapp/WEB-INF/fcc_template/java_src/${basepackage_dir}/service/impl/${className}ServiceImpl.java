<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.DataFormater;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.web.service.ExportService;
import com.fcc.commons.web.service.ImportService;

import ${basepackage}.model.${className};
import ${basepackage}.dao.${className}Dao;
import ${basepackage}.service.${className}Service;
/**
 * <p>Description:${table.tableAlias}</p>
 */

@Service
public class ${className}ServiceImpl implements ${className}Service, ExportService, ImportService {
    @Resource
	private ${className}Dao ${classNameLower}Dao;
	@Resource
    private BaseService baseService;
    
	@Override
	public List<String> dataConver(Object converObj) {
        List<String> dataList = new ArrayList<String>();
        if (converObj instanceof ${className}) {
            ${className} data = (${className}) converObj;
            <#list table.columns as column>
            <#if column.isDateTimeColumn>
            dataList.add(DataFormater.noNullValue(data.get${column.columnName}(), "yyyy-MM-dd HH:mm:ss"));
            <#else>
            dataList.add(DataFormater.noNullValue(data.get${column.columnName}()));
            </#if>
            </#list>
        }
        return dataList;
    }
    
	@Override
    public Object dataConver(List<String> dataList) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ${className} ${classNameLower} = new ${className}();
        try {
            String value = null;
            <#list table.columns as column>
            <#if !column.pk>
            value = dataList.get(${column_index - 1});
                <#if column.isDateTimeColumn>
            ${classNameLower}.set${column.columnName}(format.parse(value));
                <#else>
            ${classNameLower}.set${column.columnName}(${column.javaType}.valueOf(value));
                </#if>
            </#if>
            </#list>
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ${classNameLower};
    }
	
	@Transactional(rollbackFor = Exception.class)
	@Override
    public void saveData(List<Object> dataList) {
        baseService.addList(dataList);
    }
	
	@Transactional(readOnly = true)
	@Override
	public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
	    return ${classNameLower}Dao.queryPage(pageNo, pageSize, param, isSQL);
    }
	
	@Transactional(readOnly = true)
	@Override
	public List<${className}> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
	    return ${classNameLower}Dao.query(pageNo, pageSize, param, isSQL);
    }
	
	@Transactional(readOnly = true)
	@Override
	public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
	    return ${classNameLower}Dao.report(pageNo, pageSize, param, isSQL);
	}
}
