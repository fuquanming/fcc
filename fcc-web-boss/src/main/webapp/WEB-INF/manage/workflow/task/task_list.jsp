<%@page import="com.fcc.commons.workflow.amountApply.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
<%@ include file="/WEB-INF/head/upload_js.jsp" %>
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
			<th>流程名称</th>	
			<td colspan="2">
				<input id="definitionKey" name="definitionKey" style="width: 155px;" class="easyui-combobox"/>
			</td>
			<th>任务类型</th>    
            <td colspan="2">
                <input id="taskType" name="taskType" style="width: 155px;" class="easyui-combobox"/>
            </td>
		</tr>	
		<tr>
	        <td colspan="3" align="left">
	        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
	        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
	        <span id="importDataSizeSpan" style="color: red; font-weight: bolder;"></span> 
        	</td>
      	</tr>
	</table>
	</form>
    </fieldset>
    <div id="operateDiv"></div>
  </div>
  <table id="datagrid">
  </table>
  <form id="taskForm" name="taskForm" method="post" >
  <input name="taskId" value="" type="hidden">
  <input name="type" value="" type="hidden">
  </form>
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
	var definitionKey = getComboBoxByData({
        id : 'definitionKey',
        valueField : 'id',
        textField : 'text',
        data : [
        	{text : '--请选择--', id : ''},
            <c:forEach items="${definitionKeyMap }" var="key" varStatus="stat">
            {text : '${key.value }', id : '${key.key }'}<c:if test="${!stat.last}" >,</c:if>
	        </c:forEach>
        ],
        editable : false,
        selectValue : '${data.definitionKey }'
    })
    var taskType = getComboBoxByData({
        id : 'taskType',
        valueField : 'id',
        textField : 'text',
        data : [
        	{text : '--请选择--', id : ''},
            {text : '签收', id : 'taskClaim'},
            {text : '办理', id : 'taskExecute'}
        ],
        editable : false,
        selectValue : '${data.definitionKey }'
    })
})

datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = 'manage/workflow/task/datagrid.do';// 数据源url
datagridParam_idField = 'id';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
    {
        field : 'processDefinitionName',
        title : '流程名称',
        width : 100
    } ,
    {
        field : 'name',
        title : '当前节点',
        width : 100,
        formatter : function(value, rowData, rowIndex) {
            return '<a class="trace" href="javascript:void(0)" onclick="showTraceImg(\'' + rowData.processInstanceId + '\', \'' + rowData.processDefinitionId + '\')" title="点击查看流程图">' + rowData.name + '</a>';
        }
    } ,
    {
        field : 'assignee',
        title : '当前处理人',
        width : 100
    } ,
    {
        field : 'requestUserName',
        title : '申请人',
        width : 100,
        formatter : function(value, rowData, rowIndex) {
        	// rowData.processVariables.requestUserId + ":" + 
        	return rowData.processVariables.requestUserName;
        }
    } ,
    {
        field : 'createTime',
        title : '任务创建时间',
        width : 100,
        formatter : function(value, rowData, rowIndex) {
            value = rowData.createTime;
            return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
        }
    } ,
    {
        field : 'processDefinitionVersion',
        title : '流程版本',
        width : 50,
        formatter : function(value, rowData, rowIndex) {
            value = rowData.processDefinitionVersion;
            return '<b title="流程版本号">V: ' + value + '</b>';;
        }
    } ,
    {
        field : 'operate',
        title : '操作',
        sortable:true,
        width : 50,
        formatter : function(value, rowData, rowIndex) {
            var assignee = rowData.assignee;
            var msg = '';
            <fcc:permission operateId="edit">
            if (assignee == null || "" == assignee) {
                msg = '<a href="javascript:void(0)" onclick="taskClaim(\'taskClaim\', \'' + rowData.id + '\')">签收</a>';
            } else {
                msg = '<a href="javascript:void(0)" onclick="toEdit(\'' + rowData.id + '\')">办理</a>'
            }
            </fcc:permission>   
            return msg;
        }
    } 
] ];// 表格的列
datagridParam_queryParamName = [
        'definitionKey', 'taskType'
];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'id';
operateParam_dataName = 'id';
operateParam_viewUrl = 'manage/workflow/task/toView.do';

operateParam_view_beforeCallback = function(row) {
	Tool.goNewPage(Tool.urlAddParam(operateParam_viewUrl, "id=" + row.processInstanceId));
	return false;
}

function taskClaim(type, taskId) {
    var taskForm = $('#taskForm').form();
    taskForm.find('[name=taskId]').val(taskId);
    taskForm.find('[name=type]').val(type);
    
    saveParam_form = "taskForm";// 提交的Form
    saveParam_saveUrl = "manage/workflow/task/edit.do";// 保存URL地址
    saveParam_afterCallback = function(data, success) {
    	if (success == true) {
    		searchFun();
    	}
    };// save后执行的方法，返回false 将不执行backUrl；传递参数（data,success）
    save();
}

function toEdit(taskId) {
	Tool.goNewPage("${basePath}manage/workflow/task/toEdit.do?id=" + taskId);
}
</script>