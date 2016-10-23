<%@page import="com.fcc.web.sys.model.*" %>
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
			url : '<%=basePath%>manage/sys/sysLock/report/datagrid.do?random=' + Math.random(),
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
				lockStatus : $('#toolbar input[name=lockStatus]').val(),
				createTimeBegin : $('#toolbar input[name=createTimeBegin]').val(),
				createTimeEnd : $('#toolbar input[name=createTimeEnd]').val(),
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
		window.location.href = "<%=basePath%>manage/sys/sysLock/view.do";
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
			<th><%=SysLock.ALIAS_LOCK_STATUS%></th>	
			<td colspan="2">
				<input id="lockStatus" name="lockStatus" maxlength="10"  style="width: 120px;"/>
			</td>
			<th><%=SysLock.ALIAS_CREATE_TIME%></th>	
			<td colspan="2">
				<input value="" class="easyui-datebox" style="width: 120px;" id="createTimeBegin" name="createTimeBegin"   />
				<input value="" class="easyui-datebox" style="width: 120px;" id="createTimeEnd" name="createTimeEnd"   />
			</td>
		</tr>	
		<tr>
			<th>分组</th>	
			<td colspan="2">
				<select name="reportGroupName" id="reportGroupName" style="width: 130px;">
					<option value="">---请选择---</option>
					<option value="lockStatus"><%=SysLock.ALIAS_LOCK_STATUS%></option>
					<option value="createTime"><%=SysLock.ALIAS_CREATE_TIME%></option>
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
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="lineCharts('<%=SysLock.TABLE_ALIAS%>', '<%=basePath %>');" plain="true" href="javascript:void(0);">折线图</a>
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="columnCharts('<%=SysLock.TABLE_ALIAS%>', '<%=basePath %>');" plain="true" href="javascript:void(0);">柱形图</a>
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="pieCharts('<%=SysLock.TABLE_ALIAS%>', '<%=basePath %>');" plain="true" href="javascript:void(0);">饼图</a> |
    </div>
  </div>
  <div id="container" style="width: 800px; height: 360px; margin: 0 auto; display: none;" ></div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
