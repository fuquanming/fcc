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
		<tr>	
			<th>开始时间</th>	
			<td colspan="2">
				<input value="" class="easyui-datebox" style="width: 120px;" id="startTimeBegin" name="startTimeBegin"   />
				<input value="" class="easyui-datebox" style="width: 120px;" id="startTimeEnd" name="startTimeEnd"   />
			</td>
			<th>结束时间</th>	
			<td colspan="2">
				<input value="" class="easyui-datebox" style="width: 120px;" id="endTimeBegin" name="endTimeBegin"   />
				<input value="" class="easyui-datebox" style="width: 120px;" id="endTimeEnd" name="endTimeEnd"   />
			</td>
		</tr>	
		<tr>	
			<th>状态</th>	
			<td colspan="2">
				<input id="status" name="status" maxlength="16"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入状态...'"/>
			</td>
		</tr>	
		<tr>	
		</tr>	
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
datagridParam_url = 'manage/workflow/leave/datagrid.do';// 数据源url
datagridParam_idField = 'leaveId';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
        {
            field : 'startTime',
            title : '开始时间',
            sortable:true,
            width : 100,
            formatter : function(value, rowData, rowIndex) {
            	return Tool.dateFormat({'value':value, 'format':'yyyy-MM-dd HH:mm:ss'});
            }
        } ,
        {
            field : 'endTime',
            title : '结束时间',
            sortable:true,
            width : 100,
            formatter : function(value, rowData, rowIndex) {
            	return Tool.dateFormat({'value':value, 'format':'yyyy-MM-dd HH:mm:ss'});
            }
        } ,
        {
            field : 'content',
            title : '内容',
            sortable:true,
            width : 100
        } ,
    {
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
        'startTimeBegin','startTimeEnd',
        'endTimeBegin','endTimeEnd',
        'content',
        'processInstanceId',
        'processDefinitionId',
        'processNodeName',
        'status',
        'createUser',
        'createTimeBegin','createTimeEnd',
        'updateUser',
        'updateTimeBegin','updateTimeEnd'
];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'leaveId';
operateParam_dataName = 'startTime';
operateParam_viewUrl = 'manage/workflow/leave/toView.do';
operateParam_addUrl = 'manage/workflow/leave/toAdd.do';
operateParam_editUrl = 'manage/workflow/leave/toEdit.do';
operateParam_delUrl = 'manage/workflow/leave/delete.do';
operateParam_reportUrl = 'manage/workflow/leave/report/view.do';
operateParam_edit_beforeCallback = function(row) {
    if (row.status == 'unstart') {
        return true;
    } else {
        Tool.message.alert(Lang.tip, StatusCode.msg('workflow_000'), Tool.icon.error);
        return false;
    }
}

exportParam_form = 'userForm';
exportParam_exportUrl = "manage/workflow/leave/export.do";// 导出数据URL
exportParam_queryExportUrl = "manage/workflow/leave/queryExport.do";// 查询导出数据URL
exportParam_model = "leave";// 模块

importParam_importUrl = "manage/workflow/leave/import.do";// 导入数据URL
importParam_queryImportUrl = "manage/workflow/leave/queryImport.do";// 查询导入数据URL
</script>