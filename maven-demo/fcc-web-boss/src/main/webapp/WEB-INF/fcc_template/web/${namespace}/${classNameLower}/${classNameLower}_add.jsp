<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <legend>新增${table.tableAlias}</legend>
    <form id="userForm" name="userForm" method="post">
      <table class="tableForm" align="center">
      	<#list table.addJspShowColumns as column>
		<#if !column.htmlHidden>	
		<tr>	
			<th><#if !column.nullable><span class="required">*</span></#if>${column.columnAlias}：</th>		
			<td>
			<#if column.isDateTimeColumn>
			<input id="${column.columnNameLower}String" name="${column.columnNameLower}String" class="easyui-datetimebox" <#if !column.nullable>required="true"</#if>/>
			<#else>
			<input id="${column.columnNameLower}" name="${column.columnNameLower}" type="text" class="easyui-validatebox easyui-textbox" maxlength="${column.size}" <#if !column.nullable>required="true" data-options="prompt:'请输入${column.columnAlias}...'"</#if>/>
			</#if>
			</td>
		</tr>	
		</#if>
		</#list>
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="save();" href="javascript:void(0);">保存</a> <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<script type="text/javascript" charset="UTF-8">
saveParam_form = 'userForm';// 提交的Form
saveParam_saveUrl = '${jspFileBasePath}/add.do';// 保存URL地址
saveParam_backUrl = '${jspFileBasePath}/view.do';// 跳转地址
saveParam_afterCallback = function(data, success) {
    return false;// 不执行自动跳转
}
</script>