<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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