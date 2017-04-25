<%@page import="com.fcc.app.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%-- 流程实例管理 --%>
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
			<th>流程定义KEY</th>	
			<td colspan="2">
				<input id="definitionKey" name="definitionKey" maxlength="20"  style="width: 150px;" class="easyui-textbox"  data-options="prompt:'请输入流程定义KEY...'"/>
			</td>
			<th>流程业务ID</th>	
			<td colspan="2">
				<input id="businessKey" name="businessKey" maxlength="20"  style="width: 150px;" class="easyui-textbox"  data-options="prompt:'请输入流程业务ID...'"/>
			</td>
		</tr>	
		<tr>
	        <td colspan="3" align="center">
	        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
	        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
        	</td>
      	</tr>
	</table>
	</form>
    </fieldset>
    <div id="operateDiv"></div>
  </div>
  <table id="datagrid">
  </table>
  <form id="tempForm" name="tempForm" method="post" >
  <input name="processDefinitionId" value="" type="hidden">
  </form>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_operate.jsp" %>
<%@ include file="/WEB-INF/head/init_import.jsp" %>
<script type="text/javascript">
datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${basePath}manage/sys/workflow/processInstance/datagrid.do';// 数据源url
datagridParam_idField = 'id';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
    /* {
        field : 'processInstanceId',
        title : '流程实例ID',
        width : 40
    } , */
    {
        field : 'processDefinitionId',
        title : '流程定义ID',
        width : 80
    } ,
    {
        field : 'businessKey',
        title : '流程业务ID',
        width : 50
    } ,
    {
        field : 'definitionKey',
        title : '流程定义KEY',
        width : 50
    } ,
    {
        field : 'definitionName',
        title : '流程定义名称',
        width : 50
    } ,
    {
        field : 'currentNodeName',
        title : '当前节点',
        width : 50,
        formatter : function(value, rowData, rowIndex) {
            return '<a class="trace" href="javascript:void(0)" onclick="showTraceImg(\'' + rowData.id + '\', \'' + rowData.processDefinitionId + '\')" title="点击查看流程图">' + value + '</a>';
        }
    } ,
    {
        field : 'taskCreateTime',
        title : '任务创建时间',
        width : 70,
        formatter : function(value, rowData, rowIndex) {
            return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
        }
    } ,
    {
        field : 'taskAssignee',
        title : '当前处理人',
        width : 50,
        formatter : function(value, rowData, rowIndex) {
            return value;
        }
    } ,
    {
        field : 'processDefinitionVersion',
        title : '流程定义版本号',
        width : 50,
        formatter : function(value, rowData, rowIndex) {
            return value;
        }
    } ,
    {
        field : 'suspended',
        title : '是否挂起',
        width : 40,
        formatter : function(value, rowData, rowIndex) {
            var msg = '否'
            if (value == true) {
                msg = '是';
            }
            return msg;
        }
    }  ,
    {
        field : 'processSuspended1',
        title : '操作',
        width : 30,
        formatter : function(value, rowData, rowIndex) {
            var processInstanceId = rowData.processInstanceId;
            var msg = '';
            <fcc:permission operateId="edit">
            msg = '<a href="javascript:void(0)" onclick="edit(\'suspend\', \'' + processInstanceId + '\')">挂起</a>';
            if (rowData.suspended == true) {
                msg = '<a href="javascript:void(0)" onclick="edit(\'activate\', \'' + processInstanceId + '\')">激活</a>'
            }
            </fcc:permission>                       
            return msg;
        }
    } 
] ];// 表格的列
datagridParam_queryParamName = ['definitionKey', 'businessKey'];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'id';
operateParam_dataName = 'definitionName';
operateParam_viewUrl = '${basePath}manage/sys/workflow/processInstance/toView.do';
operateParam_delUrl = '${basePath}manage/sys/workflow/processInstance/delete.do';

operateParam_view_beforeCallback = function(row) {
	Tool.goNewPage(Tool.urlAddParam(operateParam_viewUrl, "id=" + row.processInstanceId));
	return false;
}

function edit(status, id) {
    Tool.message.confirm(Lang.confirm, Lang.confirmOperate, function(r) {
        if (r) {
            saveParam_form = 'userForm';
            saveParam_saveUrl = 'manage/sys/workflow/processInstance/edit.do?processInstanceId=' + id + "&status=" + status;
            saveParam_afterCallback = function(data, success) {
                gridUnselectAll();
                searchFun();
            };
            save();
        }
    });
}
</script>