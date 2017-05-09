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
    <legend>修改${table.tableAlias}</legend>
    <form id="userForm" name="userForm" method="post">
      <input type="hidden" id="operate" name="operate" value="save"/>
      <#list table.columns as column>
	  <#if column.pk>
	  <input name="${column.columnNameLower}" type="hidden" value="<@jspEl classNameLower+"."+column.columnNameLower/>" />
	  </#if>
	  </#list>
      <table class="tableForm" align="center">
        <#list table.editJspShowColumns as column>
		<#if !column.htmlHidden>
		<#if column.columnNameLower != "createTime" && column.columnNameLower != "createUser" && column.columnNameLower != "updateTime" && column.columnNameLower != "updateUser" && column.columnNameLower != "status" && column.columnNameLower != "processInstanceId" && column.columnNameLower != "processDefinitionId" && column.columnNameLower != "processNodeName">  
        <tr>	
			<th><#if !column.nullable><span class="required">*</span></#if>${column.columnAlias}：</th>		
			<td>
			<#if column.isDateTimeColumn>
			<input id="${column.columnNameLower}String" name="${column.columnNameLower}String" class="easyui-datetimebox" <#if !column.nullable>required="true"</#if> value="<fmt:formatDate value="<@jspEl classNameLower+"."+column.columnNameLower/>" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<#else>
			     <#if column.size gte minLine>
            <textarea rows="5" cols="40" id="${column.columnNameLower}" name="${column.columnNameLower}" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, ${column.size}]"><@jspEl classNameLower+"."+column.columnNameLower/></textarea>
                 <#else>
            <input id="${column.columnNameLower}" name="${column.columnNameLower}" type="text" value="<@jspEl classNameLower+"."+column.columnNameLower/>" class="easyui-validatebox easyui-textbox" maxlength="${column.size}" <#if !column.nullable>required="true" data-options="prompt:'请输入${column.columnAlias}...'"</#if>/>
                 </#if>
			</#if>
			</td>
		</tr>	
		</#if>
		</#if>
		</#list>
        <tr>
          <td colspan="2" align="center">
          <a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="start('save');save();" href="javascript:void(0);">保存</a>
          <a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="start('start');save();" href="javascript:void(0);">保存并提交</a> 
          <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a>
          </td>
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
saveParam_saveUrl = '${jspFileBasePath}/edit.do';// 保存URL地址
saveParam_backUrl = '${jspFileBasePath}/view.do';// 跳转地址
saveParam_afterCallback = function(data, success) {
    if (success == false) return false;// 失败，不执行自动跳转
}
function start(start) {
    $('#operate').val(start);
}
</script>