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
<%@ include file="/WEB-INF/head/highcharts.jsp" %>
<%@ include file="/WEB-INF/head/fusionCharts.jsp" %>
<script src="js/support/report.js" charset="UTF-8" type="text/javascript"></script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;display: none;">
    <fieldset>
    <legend>筛选</legend>
    <form id="userForm" name="userForm" method="post">
  	<input name="ids" type="hidden" value=""/>
    <table class="tableForm">
		<#list table.reportJspQueryColumns?chunk(5) as row>
		<tr>	
			<#list row as column>
			<#if !column.htmlHidden>	
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
			</#list>
		</tr>	
	    </#list>	
	    <tr>
	       <th>分组</th>  
           <td colspan="2">
               <input id="reportGroupName" name="reportGroupName" style="width: 130px;" class="easyui-combobox"/>
           </td>
	    </tr>
		<tr>
	        <td colspan="3" align="center">
	        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
	        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
	        <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a>
        	</td>
      	</tr>
	</table>
	</form>
    </fieldset>
    <div> 
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="lineCharts('${table.tableAlias}', '<%=basePath %>');" plain="true" href="javascript:void(0);">折线图</a>
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="columnCharts('${table.tableAlias}', '<%=basePath %>');" plain="true" href="javascript:void(0);">柱形图</a>
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="pieCharts('${table.tableAlias}', '<%=basePath %>');" plain="true" href="javascript:void(0);">饼图</a> |
    </div>
  </div>
  <div id="container" style="width: 800px; height: 360px; margin: 0 auto; display: none;" ></div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_combotree.jsp" %>
<%@ include file="/WEB-INF/head/init_combobox.jsp" %>
<script type="text/javascript" charset="UTF-8">
// 报表数据缓存    
var dataList;   
var dataTitle;
$(function() {
    getComboBoxByData({
        id : 'reportGroupName',
        valueField : 'id',
        textField : 'text',
        data : [
            <#list table.columns as column>
            <#if !column.htmlHidden>    
            {text : '${column.columnAlias}', id : '${column.columnNameLower}'},
            </#if>
            </#list>
        ]
    })
})   
function toBack() {
    window.location.href = "${jspFileBasePath}/view.do";
} 
datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${jspFileBasePath}/report/datagrid.do';// 数据源url
datagridParam_idField = 'groupName';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
    {
        field : 'groupName',
        title : '',
        sortable:true,
        width : 100
    } ,
    {
        field : 'count',
        title : '总数',
        sortable:true,
        width : 100
    } 
] ];// 表格的列
datagridParam_queryParamName = [
	<#list table.reportJspQueryColumns as column>
    <#if !column.htmlHidden>
        <#if column.isDateTimeColumn>
        '${column.columnNameLower}Begin','${column.columnNameLower}End'<#if column_has_next>,</#if>
        <#else>
        '${column.columnNameLower}'<#if column_has_next>,</#if>
        </#if>
    </#if>
    </#list> 
, 'reportGroupName'];
datagridParam_load_beforeCallback = function(data) {
    dataTitle = $('.datagrid-header-row td[field="groupName"] div span:first-child');
    var groupName = $('#toolbar input[name="reportGroupName"]').val();
    if (groupName != '') {
        dataTitle.html($('#reportGroupName').combobox('getText'));
    }
    dataList = data.rows;
}
</script>