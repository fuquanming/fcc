<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.data.ListPage;
import ${basepackage}.dao.${className}Dao;
import ${basepackage}.model.${className};

/**
 * <p>Description:${table.tableAlias}</p>
 */
@Repository
public class ${className}DaoImpl implements ${className}Dao {
    
    @Resource
    private BaseDao baseDao;
    
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        StringBuilder cHql = new StringBuilder("select count(t) from ${className} t where 1=1 ");
        StringBuilder bHql = new StringBuilder("select t from ${className} t where 1=1 ");
        if (param != null) {
            <#list table.columns as column>
            <#if column.isDateTimeColumn>
            if(param.get("${column.columnNameLower}Begin") != null) {
                bHql.append(" and  t.${column.columnNameLower} >= :${column.columnNameLower}Begin ");
                cHql.append(" and  t.${column.columnNameLower} >= :${column.columnNameLower}Begin ");
            }
            if(param.get("${column.columnNameLower}End") != null) {
                bHql.append(" and  t.${column.columnNameLower} <= :${column.columnNameLower}End ");
                cHql.append(" and  t.${column.columnNameLower} <= :${column.columnNameLower}End ");
            }
            <#else>
            if(param.get("${column.columnNameLower}") != null) {
                bHql.append(" and  t.${column.columnNameLower} = :${column.columnNameLower} ");
                cHql.append(" and  t.${column.columnNameLower} = :${column.columnNameLower} ");
            }
            </#if>
            </#list>
            if(param.get("sortColumns") != null) {
                bHql.append(" order by t.").append(param.get("sortColumns"));
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    bHql.append(" ").append(param.get("orderType"));
                    param.remove("orderType");
                }
                <#list table.columns as column>
                <#if column.pk>
                bHql.append(", t.${column.columnNameLower} desc");
                </#if>
                </#list>
            }
        }
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, false);
    }
    
    @SuppressWarnings("unchecked")
    public List<${className}> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        StringBuilder bHql = new StringBuilder("select t from ${className} t where 1=1 ");
        if (param != null) {
            <#list table.columns as column>
            <#if column.isDateTimeColumn>
            if(param.get("${column.columnNameLower}Begin") != null) {
                bHql.append(" and  t.${column.columnNameLower} >= :${column.columnNameLower}Begin ");
            }
            if(param.get("${column.columnNameLower}End") != null) {
                bHql.append(" and  t.${column.columnNameLower} <= :${column.columnNameLower}End ");
            }
            <#else>
            if(param.get("${column.columnNameLower}") != null) {
                bHql.append(" and  t.${column.columnNameLower} = :${column.columnNameLower} ");
            }
            </#if>
            </#list>
            if(param.get("sortColumns") != null) {
                bHql.append(" order by t.").append(param.get("sortColumns"));
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    bHql.append(" ").append(param.get("orderType"));
                    param.remove("orderType");
                }
                <#list table.columns as column>
                <#if column.pk>
                bHql.append(", t.${column.columnNameLower} desc");
                </#if>
                </#list>
            }
        }
        return baseDao.queryPage(pageNo, pageSize, bHql.toString(), param, false);
    }
    
    public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        <#list table.columns as column>
        <#if column.pk>
        String groupBy = "t.${column.columnNameLower}";
        </#if>
        </#list>
        StringBuilder cHql = new StringBuilder("select count(t) from ${className} t where 1=1  ");
        StringBuilder bHql = new StringBuilder("select new com.fcc.commons.web.view.ReportInfo(count(t), ");
        if (param != null) {
            if(param.get("reportGroupName") != null) {
                groupBy = (String) param.get("reportGroupName");
                param.remove("reportGroupName");
            }
            bHql.append(groupBy).append(") from ${className} t where 1=1 ");
            <#list table.columns as column>
            <#if column.isDateTimeColumn>
            if(param.get("${column.columnNameLower}Begin") != null) {
                bHql.append(" and  t.${column.columnNameLower} >= :${column.columnNameLower}Begin ");
                cHql.append(" and  t.${column.columnNameLower} >= :${column.columnNameLower}Begin ");
            }
            if(param.get("${column.columnNameLower}End") != null) {
                bHql.append(" and  t.${column.columnNameLower} <= :${column.columnNameLower}End ");
                cHql.append(" and  t.${column.columnNameLower} <= :${column.columnNameLower}End ");
            }
            <#else>
            if(param.get("${column.columnNameLower}") != null) {
                bHql.append(" and  t.${column.columnNameLower} = :${column.columnNameLower} ");
                cHql.append(" and  t.${column.columnNameLower} = :${column.columnNameLower} ");
            }
            </#if>
            </#list>
            if(param.get("sortColumns") != null) {
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    param.remove("orderType");
                }
            }
        } else {
            bHql.append(groupBy).append(") from ${className} t where 1=1 ");
        }
        bHql.append(" group by ").append(groupBy).append(" order by ").append(groupBy).append(" desc");
        cHql.append(" group by ").append(groupBy);
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, false);
    }
}
