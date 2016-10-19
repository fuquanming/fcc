<%@page import="com.fcc.app.lucene.model.*" %>
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
			url : '<%=basePath%>manage/sys/indexInfo/report/datagrid.do?random=' + Math.random(),
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
				infoKey : $('#toolbar input[name=infoKey]').val(),
				indexDir : $('#toolbar input[name=indexDir]').val(),
				documentType : $('#toolbar input[name=documentType]').val(),
				className : $('#toolbar input[name=className]').val(),
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
		window.location.href = "<%=basePath%>manage/sys/indexInfo/view.do";
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
			<th><%=IndexInfo.ALIAS_INFO_KEY%></th>	
			<td colspan="2">
				<input id="infoKey" name="infoKey" maxlength="100"  style="width: 120px;"/>
			</td>
			<th><%=IndexInfo.ALIAS_INDEX_DIR%></th>	
			<td colspan="2">
				<input id="indexDir" name="indexDir" maxlength="255"  style="width: 120px;"/>
			</td>
			<th><%=IndexInfo.ALIAS_CREATE_TIME%></th>	
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
					<option value="infoKey"><%=IndexInfo.ALIAS_INFO_KEY%></option>
					<option value="indexDir"><%=IndexInfo.ALIAS_INDEX_DIR%></option>
					<option value="documentType"><%=IndexInfo.ALIAS_DOCUMENT_TYPE%></option>
					<option value="className"><%=IndexInfo.ALIAS_CLASS_NAME%></option>
					<option value="idType"><%=IndexInfo.ALIAS_ID_TYPE%></option>
					<option value="methodNames"><%=IndexInfo.ALIAS_METHOD_NAMES%></option>
					<option value="isTokenizeds"><%=IndexInfo.ALIAS_IS_TOKENIZEDS%></option>
					<option value="isBackupFields"><%=IndexInfo.ALIAS_IS_BACKUP_FIELDS%></option>
					<option value="fieldNames"><%=IndexInfo.ALIAS_FIELD_NAMES%></option>
					<option value="delFieldName"><%=IndexInfo.ALIAS_DEL_FIELD_NAME%></option>
					<option value="analyzerName"><%=IndexInfo.ALIAS_ANALYZER_NAME%></option>
					<option value="createTime"><%=IndexInfo.ALIAS_CREATE_TIME%></option>
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
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="lineCharts('<%=IndexInfo.TABLE_ALIAS%>', '<%=basePath %>');" plain="true" href="javascript:void(0);">折线图</a>
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="columnCharts('<%=IndexInfo.TABLE_ALIAS%>', '<%=basePath %>');" plain="true" href="javascript:void(0);">柱形图</a>
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="pieCharts('<%=IndexInfo.TABLE_ALIAS%>', '<%=basePath %>');" plain="true" href="javascript:void(0);">饼图</a> |
    </div>
  </div>
  <div id="container" style="width: 800px; height: 360px; margin: 0 auto; display: none;" ></div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
