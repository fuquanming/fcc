<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<input name="readonly" type="hidden" value="<@jspEl "readonly" />" />
<#list table.pkColumns as column>
<input name="dataId" type="hidden" value="<@jspEl "data." + column.columnNameLower />" />
</#list>
<#list table.editJspShowColumns as column>
<#if !column.htmlHidden>  
<#if column.columnNameLower != "createTime" && column.columnNameLower != "createUser" && column.columnNameLower != "updateTime" && column.columnNameLower != "updateUser" && column.columnNameLower != "status" && column.columnNameLower != "processInstanceId" && column.columnNameLower != "processDefinitionId" && column.columnNameLower != "processNodeName">  
<tr>    
    <th><#if !column.nullable><span class="required">*</span></#if>${column.columnAlias}：</th>
    <td>
    <c:choose>
    <c:when test="<@jspEl "readonly == true" />">
    <#if column.isDateTimeColumn>
    <fmt:formatDate value="<@jspEl "data."+column.columnNameLower/>" pattern="yyyy-MM-dd HH:mm:ss"/>
    <#else>
         <#if column.size gte minLine>
    <@jspEl "data."+column.columnNameLower/>
         <#else>
    <@jspEl "data."+column.columnNameLower/>
         </#if>
    </#if>
    </c:when>
    <c:otherwise>
    <#if column.isDateTimeColumn>
    <input id="${column.columnNameLower}String" name="${column.columnNameLower}String" class="easyui-datetimebox" <#if !column.nullable>required="true"</#if> value="<fmt:formatDate value="<@jspEl "data."+column.columnNameLower/>" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
    <#else>
         <#if column.size gte minLine>
    <textarea rows="5" cols="40" id="${column.columnNameLower}" name="${column.columnNameLower}" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, ${column.size}]"><@jspEl "data."+column.columnNameLower/></textarea>
         <#else>
    <input id="${column.columnNameLower}" name="${column.columnNameLower}" type="text" value="<@jspEl "data."+column.columnNameLower/>" class="easyui-validatebox easyui-textbox" maxlength="${column.size}" <#if !column.nullable>required="true" data-options="prompt:'请输入${column.columnAlias}...'"</#if>/>
         </#if>
    </#if>
    </c:otherwise>
    </c:choose>     
    </td>
</tr>   
</#if>
</#if>
</#list>