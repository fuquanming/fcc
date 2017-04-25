<%@page import="com.fcc.app.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%-- 流程历史管理 --%>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
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
				<input id="definitionKey" name="definitionKey" maxlength="20"  style="width: 120px;" class="easyui-textbox"  data-options="prompt:'请输入流程定义KEY...'"/>
			</td>
			<th>流程业务ID</th>	
			<td colspan="2">
				<input id="businessKey" name="businessKey" maxlength="20"  style="width: 120px;" class="easyui-textbox"  data-options="prompt:'请输入流程业务ID...'"/>
			</td>
			<th>用户发起的流程</th> 
            <td colspan="2">
                <input id="startedBy" name="startedBy" maxlength="20"  style="width: 150px;" class="easyui-textbox"  data-options="prompt:'请输入用户名...'"/>
            </td>
            <th>用户参与的流程</th> 
            <td colspan="2">
                <input id="involvedUser" name="involvedUser" maxlength="20"  style="width: 150px;" class="easyui-textbox"  data-options="prompt:'请输入用户名...'"/>
            </td>
		</tr>	
		<tr>
	        <td colspan="3" align="center">
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
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_operate.jsp" %>
<script type="text/javascript">
datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${basePath}manage/sys/workflow/processHistory/datagrid.do';// 数据源url
datagridParam_idField = 'processInstanceId';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
    {
        field : 'businessKey',
        title : '流程业务ID',
        width : 80
    } ,
    {
        field : 'processDefinitionId',
        title : '流程定义ID',
        width : 60
    } ,
    {
        field : 'processDefinitionKey',
        title : '流程定义KEY',
        width : 60
    } ,
    {
        field : 'processDefinitionName',
        title : '流程定义名称',
        width : 60
    } ,
    {
        field : 'startUserId',
        title : '发起人',
        width : 60
    } ,
    {
        field : 'startTime',
        title : '开始时间',
        width : 75,
        formatter : function(value, rowData, rowIndex) {
            return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
        }
    } ,
    {
        field : 'endTime',
        title : '结束时间',
        width : 75,
        formatter : function(value, rowData, rowIndex) {
            return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
        }
    } ,
    {
        field : 'durationInMillis',
        title : '持续时间',
        width : 70,
        formatter : function(value, rowData, rowIndex) {
            return Tool.dateFormat({'value' : value, 'interval' : true});
        }
    }  ,
    {
        field : 'deleteReason',
        title : '结束原因',
        width : 50,
        formatter : function(value, rowData, rowIndex) {
            return (value == null || "" == value) ? '正常结束' : value;
        }
    } ,
    {
        field : 'processDefinitionVersion',
        title : '版本号',
        width : 30
    } 
] ];// 表格的列
datagridParam_queryParamName = ['definitionKey', 'businessKey', 'startedBy', 'involvedUser'];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'processInstanceId';
operateParam_dataName = 'processDefinitionId';
operateParam_viewUrl = '${basePath}manage/sys/workflow/processHistory/toView.do';
operateParam_delUrl = '${basePath}manage/sys/workflow/processHistory/delete.do';
</script>