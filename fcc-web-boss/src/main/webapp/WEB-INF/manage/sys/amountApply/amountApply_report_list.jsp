<%@page import="com.fcc.app.workflow.amountApply.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<%@ include file="/head/highcharts.jsp" %>
<%@ include file="/head/fusionCharts.jsp" %>
<script src="js/report.js" charset="UTF-8" type="text/javascript"></script>
<script type="text/javascript" charset="UTF-8">
	var datagrid;
	var userForm;
	var dataList;
	var dataTitle;
	$(function() {
		userForm = $('#userForm').form();
		
		datagrid = $('#datagrid').datagrid({
			url : '<%=basePath%>manage/sys/amountApply/report/datagrid.do?random=' + Math.random(),
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
			idField : 'groupName',
			frozenColumns : [ [ 
			 ] ],
			columns : [ [ 
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
			onLoadError : function(e) {
				window.location.href = overUrl;
			},
			onLoadSuccess : function(data) {
				if (data.msg && data.msg != '') {
					$.messager.show({
						msg : '数据库异常！',
						title : '提示'
					});
				} else {
					dataList = data.rows;
					dataTitle = $('.datagrid-view td[field="groupName"] div span:first-child');
				}
			}
		});

	});
	
	function searchFun() {
		datagrid.datagrid('load', {
				userId : $('#toolbar input[name=userId]').val(),
				userName : $('#toolbar input[name=userName]').val(),
				initiatorUserId : $('#toolbar input[name=initiatorUserId]').val(),
				primaryAmount : $('#toolbar input[name=primaryAmount]').val(),
				applyRemark : $('#toolbar input[name=applyRemark]').val(),
				applyVarifyRemark : $('#toolbar input[name=applyVarifyRemark]').val(),
				applyStatus : $('#toolbar input[name=applyStatus]').val(),
				applyTimeBegin : $('#toolbar input[name=applyTimeBegin]').val(),
				applyTimeEnd : $('#toolbar input[name=applyTimeEnd]').val(),
				reportGroupName : $('#reportGroupName').val()
		});
		var groupName = $('#reportGroupName').val();
		if (groupName != '') {
			dataTitle.html($('#reportGroupName').find("option:selected").text());	
		}
	}
	function clearFun() {
		$('#toolbar input').val('');
		$('#reportGroupName').val('');
		datagrid.datagrid('load', {});
	}
	function toBack() {
		window.location.href = "<%=basePath%>manage/sys/amountApply/view.do";
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
		<tr>	
			<th><%=AmountApply.ALIAS_USER_ID%></th>	
			<td colspan="2">
				<input id="userId" name="userId" maxlength="19"  style="width: 120px;"/>
			</td>
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
		</tr>	
		<tr>	
			<th><%=AmountApply.ALIAS_APPLY_REMARK%></th>	
			<td colspan="2">
				<input id="applyRemark" name="applyRemark" maxlength="4000"  style="width: 120px;"/>
			</td>
			<th><%=AmountApply.ALIAS_APPLY_VARIFY_REMARK%></th>	
			<td colspan="2">
				<input id="applyVarifyRemark" name="applyVarifyRemark" maxlength="4000"  style="width: 120px;"/>
			</td>
			<th><%=AmountApply.ALIAS_STATUS%></th>	
			<td colspan="2">
				<input id="status" name="status" maxlength="16"  style="width: 120px;"/>
			</td>
			<th><%=AmountApply.ALIAS_APPLY_TIME%></th>	
			<td colspan="2">
				<input value="" class="easyui-datebox" style="width: 120px;" id="applyTimeBegin" name="applyTimeBegin"   />
				<input value="" class="easyui-datebox" style="width: 120px;" id="applyTimeEnd" name="applyTimeEnd"   />
			</td>
		</tr>	
		<tr>
			<th>分组</th>	
			<td colspan="2">
				<select name="reportGroupName" id="reportGroupName" style="width: 130px;">
					<option value="">---请选择---</option>
					<option value="userId"><%=AmountApply.ALIAS_USER_ID%></option>
					<option value="userName"><%=AmountApply.ALIAS_USER_NAME%></option>
					<option value="initiatorUserId"><%=AmountApply.ALIAS_INITIATOR_USER_ID%></option>
					<option value="primaryAmount"><%=AmountApply.ALIAS_PRIMARY_AMOUNT%></option>
					<option value="applyRemark"><%=AmountApply.ALIAS_APPLY_REMARK%></option>
					<option value="applyVarifyRemark"><%=AmountApply.ALIAS_APPLY_VARIFY_REMARK%></option>
					<option value="applyStatus"><%=AmountApply%></option>
					<option value="applyTime"><%=AmountApply.ALIAS_APPLY_TIME%></option>
				</select>
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
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="lineCharts('<%=AmountApply.TABLE_ALIAS%>', '<%=basePath%>');" plain="true" href="javascript:void(0);">折线图</a>
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="columnCharts('<%=AmountApply.TABLE_ALIAS%>', '<%=basePath%>');" plain="true" href="javascript:void(0);">柱形图</a>
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="pieCharts('<%=AmountApply.TABLE_ALIAS%>', '<%=basePath %>');" plain="true" href="javascript:void(0);">饼图</a> |
    </div>
  </div>
  <div id="container" style="width: 800px; height: 360px; margin: 0 auto; display: none;" ></div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
