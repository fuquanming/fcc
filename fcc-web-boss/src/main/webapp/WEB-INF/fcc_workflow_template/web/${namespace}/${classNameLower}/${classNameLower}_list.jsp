<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
<%@ include file="/WEB-INF/head/workflow.jsp" %>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;display: none;">
    <fieldset>
    <legend>筛选</legend>
    <form id="userForm" name="userForm" method="post">
  	<input name="ids" type="hidden" value=""/>
    <table class="tableForm">
      <#list table.listJspQueryColumns?chunk(5) as row>
		<tr>	
			<#list row as column>
			<#if !column.htmlHidden>	
			<#if column.columnNameLower != "createTime" && column.columnNameLower != "createUser" && column.columnNameLower != "updateTime" && column.columnNameLower != "updateUser" && column.columnNameLower != "processInstanceId" && column.columnNameLower != "processDefinitionId" && column.columnNameLower != "processNodeName">  
			<th>${column.columnAlias}</th>	
			<td colspan="2">
				<#if column.isDateTimeColumn>
				<input value="" class="easyui-datebox" style="width: 120px;" id="${column.columnNameLower}Begin" name="${column.columnNameLower}Begin"   />
				<input value="" class="easyui-datebox" style="width: 120px;" id="${column.columnNameLower}End" name="${column.columnNameLower}End"   />
				<#else>
				<input id="${column.columnNameLower}" name="${column.columnNameLower}" maxlength="${column.size}"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入${column.columnAlias}...'"/>
				</#if>
			</td>
			</#if>
			</#if>
			</#list>
		</tr>	
	  </#list>
		<tr>
	        <td colspan="3" align="left">
	        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
	        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
        	</td>
      	</tr>
	</table>
	</form>
    </fieldset>
    <div id="operateDiv"></div>
    <a id="viewProcess_button" class="easyui-linkbutton operate_button" iconCls="icon-search" onClick="showProcess();" plain="true" href="javascript:void(0);" >显示流程</a>
  </div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_operate.jsp" %>
<%@ include file="/WEB-INF/head/init_export.jsp" %>
<%@ include file="/WEB-INF/head/init_import.jsp" %>
<%@ include file="/WEB-INF/head/init_combotree.jsp" %>
<%@ include file="/WEB-INF/head/init_combobox.jsp" %>
<script type="text/javascript">
$(function() {
    $('#refresh_button').after($('#viewProcess_button'));
})
datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${jspFileBasePath}/datagrid.do';// 数据源url
datagridParam_idField = '<#list table.columns as column><#if column.pk>${column.columnNameLower}</#if></#list>';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
	<#list table.listJspShowColumns as column>
    <#if !column.htmlHidden>
    <#if column.columnNameLower != "createTime" && column.columnNameLower != "createUser" && column.columnNameLower != "updateTime" && column.columnNameLower != "updateUser" && column.columnNameLower != "status" && column.columnNameLower != "processInstanceId" && column.columnNameLower != "processDefinitionId" && column.columnNameLower != "processNodeName">  
        <#if column.isDateTimeColumn>
        {
            field : '${column.columnNameLower}',
            title : '${column.columnAlias}',
            sortable:true,
            width : 100,
            formatter : function(value, rowData, rowIndex) {
            	return Tool.dateFormat({'value':value, 'format':'yyyy-MM-dd HH:mm:ss'});
            }
        } <#if column_has_next>,</#if>
        <#else>
        {
            field : '${column.columnNameLower}',
            title : '${column.columnAlias}',
            sortable:true,
            width : 100
        } <#if column_has_next>,</#if>
        </#if>
    </#if>
    </#if>
    </#list>
	    ,{
	        field : 'processNodeName',
	        title : '当前节点',
	        width : 100,
	        formatter : function(value, rowData, rowIndex) {
	            if (value == null || value == '') {
	                return '';      
	            }
	            return '<a class="trace" href="javascript:void(0)" onclick="showTraceImg(\'' + rowData.processInstanceId + '\', \'' + rowData.processDefinitionId + '\')" title="点击查看流程图">' + rowData.processNodeName + '</a>';
	        }
	    } ,
	    {
	        field : 'status',
	        title : '状态',
	        sortable:true,
	        width : 100,
	        formatter : function(value, rowData, rowIndex) {
	            return formartStatus(value);
	        }
	    } 
] ];// 表格的列
datagridParam_queryParamName = [
	<#list table.listJspQueryColumns as column>
    <#if !column.htmlHidden>
        <#if column.isDateTimeColumn>
        '${column.columnNameLower}Begin','${column.columnNameLower}End'<#if column_has_next>,</#if>
        <#else>
        '${column.columnNameLower}'<#if column_has_next>,</#if>
        </#if>
    </#if>
    </#list>
];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = '<#list table.columns as column><#if column.pk>${column.columnNameLower}</#if></#list>';
operateParam_dataName = '<#list table.columns as column><#if !column.htmlHidden><#if column_index == 1>${column.columnNameLower}<#break></#if></#if></#list>';
operateParam_viewUrl = '${jspFileBasePath}/toView.do';
operateParam_addUrl = '${jspFileBasePath}/toAdd.do';
operateParam_editUrl = '${jspFileBasePath}/toEdit.do';
operateParam_delUrl = '${jspFileBasePath}/delete.do';
operateParam_reportUrl = '${jspFileBasePath}/report/view.do';
operateParam_edit_beforeCallback = function(row) {
    if (row.status == 'unstart') {
        return true;
    } else {
        Tool.message.alert(Lang.tip, StatusCode.msg('workflow_000'), Tool.icon.error);
        return false;
    }
}

exportParam_form = 'userForm';
exportParam_exportUrl = "${jspFileBasePath}/export.do";// 导出数据URL
exportParam_queryExportUrl = "${jspFileBasePath}/queryExport.do";// 查询导出数据URL
exportParam_model = "${classNameLower}";// 模块

importParam_importUrl = "${jspFileBasePath}/import.do";// 导入数据URL
importParam_queryImportUrl = "${jspFileBasePath}/queryImport.do";// 查询导入数据URL
</script>