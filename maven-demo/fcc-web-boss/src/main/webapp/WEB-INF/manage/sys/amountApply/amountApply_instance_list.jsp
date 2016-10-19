<%@page import="com.fcc.app.workflow.amountApply.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<%@ include file="/head/upload_js.jsp" %>
<%@ include file="/head/workflow.jsp" %>
<script type="text/javascript" charset="UTF-8">
	var datagrid;
	var userForm;
	var importDataFlag = false;
	$(function() {

		userForm = $('#userForm').form();

		datagrid = $('#datagrid').datagrid({
			<c:if test="${empty param.lazy}">url : '<%=basePath%>manage/sys/amountApply/workflow/processInstance/datagrid.do?random=' + Math.random(),</c:if>
			toolbar : '#toolbar',
			title : '',
			iconCls : 'icon-save',
			pagination : true,
			rownumbers:true,
			striped:true,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 ],
			fit : true,
			fitColumns : true,
			nowrap : false,
			border : false,
			idField : 'amountApplyId',
			frozenColumns : [ [ 
			{
				field : 'id',
				width : 50,
				checkbox : true
			}, {
					field : 'amountApplyId',
					hidden : true
			} 
			 ] ],
			columns : [ [ 
				{
					field : 'userName',
					title : '<%=AmountApply.ALIAS_USER_NAME%>',
					sortable:true,
					width : 100
				} ,
				{
					field : 'initiatorUserId',
					title : '<%=AmountApply.ALIAS_INITIATOR_USER_ID%>',
					sortable:true,
					width : 100
				} ,
				{
					field : 'primaryAmount',
					title : '<%=AmountApply.ALIAS_PRIMARY_AMOUNT%>',
					sortable:true,
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						return value.toFixed(2);						
					}
				} ,
				{
					field : 'status',
					title : '<%=AmountApply.ALIAS_STATUS%>',
					sortable:true,
					width : 100
				} ,
				{
					field : 'applyTime',
					title : '<%=AmountApply.ALIAS_APPLY_TIME%>',
					sortable:true,
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
					}
				} ,
				{
					field : 'applyRemark',
					title : '<%=AmountApply.ALIAS_APPLY_REMARK%>',
					sortable:true,
					width : 100
				} ,
				{
					field : 'processInstanceInfo.currentNodeName',
					title : '当前节点',
					sortable:true,
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						return '<a class="trace" href="javascript:void(0)" onclick="showTraceImg(\'' + rowData.processInstanceInfo.id + '\', \'' + rowData.processInstanceInfo.processDefinitionId + '\')" title="点击查看流程图">' + rowData.processInstanceInfo.currentNodeName + '</a>';
					}
				} ,
				{
					field : 'processInstanceInfo.taskCreateTime',
					title : '任务创建时间',
					sortable:true,
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						value = rowData.processInstanceInfo.taskCreateTime;
						return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
					}
				} ,
				{
					field : 'processInstanceInfo.suspended',
					title : '流程状态',
					sortable:true,
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						var suspended = rowData.processInstanceInfo.suspended;
						var msg = '';
						if (suspended == true) {
							msg = '已挂起；';
						} else {
							msg = '正常；';
						}
						msg += '<b title="流程版本号">V: ' + rowData.processInstanceInfo.processDefinitionVersion + '</b>';
						return msg;
					}
				} ,
				{
					field : 'processInstanceInfo.taskAssignee',
					title : '当前处理人',
					sortable:true,
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						return rowData.processInstanceInfo.taskAssignee;
					}
				} 
			] ],
			onRowContextMenu : function(e, rowIndex, rowData) {
				e.preventDefault();
				$(this).datagrid('unselectAll');
				$(this).datagrid('selectRow', rowIndex);
				$('#menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			},
			onLoadError : function() {
				window.location.href = overUrl;
			},
			onLoadSuccess : function(data) {
				if (data.msg && data.msg != '') {
					$.messager.show({
						msg : '数据库异常！',
						title : '提示'
					});
				}
			}
		});
		
	});
	
	function view() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length != 1 && rows.length != 0) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].amountApplyId);
			}
			$.messager.show({
				msg : '只能择一条记录进行查看！您已经选择了【' + names.join(',') + '】' + rows.length + '条记录',
				title : '提示'
			});
		} else if (rows.length == 1) {
			window.location.href = "<%=basePath%>manage/sys/amountApply/toView.do?id=" + rows[0].amountApplyId;
		}
	}
	
	function searchFun() {
		<c:if test="${not empty param.lazy}">datagrid = $('#datagrid').datagrid({url : '<%=basePath%>manage/sys/amountApply/datagrid.do?random=' + Math.random()});</c:if>
		datagrid.datagrid('load', {
				userId : $('#toolbar input[name=userId]').val(),
				userName : $('#toolbar input[name=userName]').val(),
				initiatorUserId : $('#toolbar input[name=initiatorUserId]').val(),
				primaryAmount : $('#toolbar input[name=primaryAmount]').val(),
				applyRemark : $('#toolbar input[name=applyRemark]').val(),
				applyVarifyRemark : $('#toolbar input[name=applyVarifyRemark]').val(),
				applyStatus : $('#toolbar input[name=applyStatus]').val(),
				applyTimeBegin : $('#toolbar input[name=applyTimeBegin]').val(),
				applyTimeEnd : $('#toolbar input[name=applyTimeEnd]').val()
		});
	}
	function clearFun() {
		$('#toolbar input').val('');
		datagrid.datagrid('load', {});
	}
	
</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;display: none;">
    <fieldset>
    <legend>筛选</legend>
    <form id="userForm" name="userForm" method="post">
  	<input name="ids" type="hidden" value=""/>
    <table class="tableForm">
    	<%--
		<tr>	
			<th><%=AmountApply.ALIAS_USER_NAME%></th>	
			<td colspan="2">
				<input id="userName" name="userName" maxlength="20"  style="width: 120px;"/>
			</td>
			<th><%=AmountApply.ALIAS_INITIATOR_USER_ID%></th>	
			<td colspan="2">
				<input id="initiatorUserId" name="initiatorUserId" maxlength="20"  style="width: 120px;"/>
			</td>
			<th><%=AmountApply.ALIAS_PRIMARY_AMOUNT%></th>	
			<td colspan="2">
				<input id="primaryAmount" name="primaryAmount" maxlength="11"  style="width: 120px;"/>
			</td>
			<th><%=AmountApply.ALIAS_APPLY_TIME%></th>	
			<td colspan="2">
				<input value="" class="easyui-datebox" style="width: 120px;" id="applyTimeBegin" name="applyTimeBegin"   />
				<input value="" class="easyui-datebox" style="width: 120px;" id="applyTimeEnd" name="applyTimeEnd"   />
			</td>
		</tr>	
		--%>
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
    <div> 
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="view();" plain="true" href="javascript:void(0);">查看</a> 
    <fcc:permission moduleId="${rightModuleId}" operateId="add">
    <a class="easyui-linkbutton" iconCls="icon-add" onClick="start();" plain="true" href="javascript:void(0);">启动</a>
    <a class="easyui-linkbutton" iconCls="icon-add" onClick="add();" plain="true" href="javascript:void(0);">新增</a> 
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="edit">
    <a class="easyui-linkbutton" iconCls="icon-edit" onClick="edit();" plain="true" href="javascript:void(0);">修改</a>
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="delete">
    <a class="easyui-linkbutton" iconCls="icon-remove" onClick="del();" plain="true" href="javascript:void(0);">删除</a>  
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="export">
    <a class="easyui-linkbutton" iconCls="icon-download" onClick="exportData();" plain="true" href="javascript:void(0);">导出</a>
    </fcc:permission> 
    <fcc:permission moduleId="${rightModuleId}" operateId="import">
    <a class="easyui-linkbutton" iconCls="icon-upload" onClick="importData();" plain="true" href="javascript:void(0);">导入</a>
    </fcc:permission> 
    <fcc:permission moduleId="${rightModuleId}" operateId="report">
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="report();" plain="true" href="javascript:void(0);">报表</a>
    </fcc:permission>
    <a class="easyui-linkbutton" iconCls="icon-undo" onClick="datagrid.datagrid('unselectAll');" plain="true" href="javascript:void(0);">取消选中</a> 
    <span id="expordDataSizeSpan" style="color: red; font-weight: bolder;"></span>
    </div>
  </div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
