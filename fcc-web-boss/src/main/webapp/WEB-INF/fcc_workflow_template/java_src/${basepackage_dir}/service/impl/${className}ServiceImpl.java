<#include "/custom.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.DataFormater;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.web.service.ExportService;
import com.fcc.commons.web.service.ImportService;
import com.fcc.commons.workflow.service.WorkflowService;

import ${basepackage}.model.${className};
import ${basepackage}.dao.${className}Dao;
import ${basepackage}.service.${className}Service;
import ${basepackage}.service.${className}WorkflowService;
/**
 * <p>Description:${table.tableAlias}</p>
 */

@Service
public class ${className}ServiceImpl implements ${className}Service, ExportService, ImportService {
    @Resource
    private ${className}Dao ${classNameLower}Dao;
    @Resource
    private BaseService baseService;
    @Resource
    private ${className}WorkflowService ${classNameLower}WorkflowService;
    @Resource
    private WorkflowService workflowService;
    
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
    
    public Object converObject(Object src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Row row = (Row) src;
        Cell cell = row.getCell(0);
        if (cell == null) return null;
        ${className} ${classNameLower} = new ${className}();
        try {
            <#list table.columns as column>
            <#if column.pk>
                <#if column.isStringColumn>
            ${classNameLower}.set${column.columnName}(UUID.randomUUID().toString().replaceAll("-", ""));
                </#if>
            </#if>
            <#if !column.pk>
            Cell ${column.columnNameLower}Cell = row.getCell(${column_index - 1});
            ${column.columnNameLower}Cell.setCellType(Cell.CELL_TYPE_STRING);
            String ${column.columnNameLower}Value = ${column.columnNameLower}Cell.getStringCellValue();
                <#if column.isDateTimeColumn>
            ${classNameLower}.set${column.columnName}(format.parse(${column.columnNameLower}Value));
                <#else>
            ${classNameLower}.set${column.columnName}(${column.javaType}.valueOf(${column.columnNameLower}Value));
                </#if>
            </#if>
            </#list>
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ${classNameLower};
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addData(List<Object> dataList) {
        this.baseService.addListBatch(dataList);
    }
    
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void add(${className} ${classNameLower}) {
        baseService.add(${classNameLower});
        ${classNameLower}WorkflowService.startWorkflow(${classNameLower}.<#list table.pkColumns as column>get${column.columnName}()</#list>);
    }
    
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void edit(${className} ${classNameLower}) {
        baseService.edit(${classNameLower});
        ${classNameLower}WorkflowService.startWorkflow(${classNameLower}.<#list table.pkColumns as column>get${column.columnName}()</#list>);
    }
    
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void updateStatus(String dataId, String status) {
        Map<String, Object> param = new HashMap<String, Object>(2);
        param.put("status", status);
        param.put("dataId", dataId);
        baseService.executeHql("update ${className} set status=:status where <#list table.pkColumns as column>${column.columnNameLower}</#list>=:dataId", param);
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