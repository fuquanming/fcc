<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

/**
 * <p>Description:${table.tableAlias}</p>
 */

public interface ${className}WorkflowService {
    /**
     * 启动流程
     * @param leaveIds
     */
    void startWorkflow(String <#list table.columns as column><#if column.pk>${column.columnNameLower}</#if></#list>);
}