<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>查看${table.tableAlias}</legend>
    <form id="userForm" name="userForm" method="post">
      <#list table.columns as column>
	  <#if column.pk>
	  <input name="${column.columnNameLower}" type="hidden" value="<@jspEl classNameLower+"."+column.columnNameLower/>" />
	  </#if>
	  </#list>
      <table class="tableForm" align="center">
        <#list table.viewJspShowColumns as column>
		<#if !column.htmlHidden>	
		<tr>	
			<th>${column.columnAlias}：</th>		
			<td><#if column.isDateTimeColumn><fmt:formatDate value="<@jspEl classNameLower+"."+column.columnNameLower/>" pattern="yyyy-MM-dd HH:mm:ss"/>
			<#else><@jspEl classNameLower+"."+column.columnNameLower/></#if></td>
		</tr>	
		</#if>
		</#list>
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
<script type="text/javascript" charset="UTF-8">
function toBack() {
    window.location.href = '${jspFileBasePath}/view.do';
}
</script>