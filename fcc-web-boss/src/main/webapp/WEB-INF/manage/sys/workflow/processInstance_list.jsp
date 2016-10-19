<%@page import="com.fcc.app.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%-- 流程定义及部署管理 --%>
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
			url : '<%=basePath%>manage/sys/workflow/processInstance/datagrid.do?random=' + Math.random(),
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
			idField : 'id',
			frozenColumns : [ [ 
			{
				field : 'id',
				width : 50,
				checkbox : true,
			}, {
					field : 'id',
					hidden : true
			} 
			 ] ],
			columns : [ [ 
				{
					field : 'processInstanceId',
					title : '流程实例ID',
					width : 50
				} ,
				{
					field : 'processDefinitionId',
					title : '流程定义ID',
					width : 100
				} ,
				{
					field : 'businessKey',
					title : '流程业务ID',
					width : 50
				} ,
				{
					field : 'definitionKey',
					title : '流程定义KEY',
					width : 100
				} ,
				{
					field : 'definitionName',
					title : '流程定义名称',
					width : 100
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
					width : 100,
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
					width : 50,
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
					width : 50,
					formatter : function(value, rowData, rowIndex) {
						var processInstanceId = rowData.processInstanceId;
						var msg = '';
						<fcc:permission moduleId="${rightModuleId}" operateId="edit">
						msg = '<a href="javascript:void(0)" onclick="edit(\'suspend\', \'' + processInstanceId + '\')">挂起</a>';
						if (rowData.suspended == true) {
							msg = '<a href="javascript:void(0)" onclick="edit(\'activate\', \'' + processInstanceId + '\')">激活</a>'
						}
						</fcc:permission>						
						return msg;
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
			onLoadError : function(e) {
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
	
	function edit(status, id) {
		var msg = '';
		if (status == 'activate') {
			msg = '您要 激活 当前所选记录？';
		} else if (status == 'suspend') {
			msg = '您要 挂起 当前所选记录？';
		}
		$.messager.confirm('请确认', msg, function(r) {
			if (r) {
				userForm.form('submit', {
					url : '<%=basePath%>manage/sys/workflow/processInstance/edit.do?processInstanceId=' + id + "&status=" + status,
					onSubmit : function() {
						return true;
					},
					success : function(data) {
						try {
							var d = $.parseJSON(data);
							$.messager.show({
								msg : d.msg,
								title : '提示'
							});
							if (d.success) {
								datagrid.datagrid('unselectAll');
								searchFun();
							}
						} catch(e) {
							window.location.href = overUrl;
						}
					}
				});
			}
		});
	}

	function del() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要删除当前所选记录？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					var idsVal = ids.join(',');
					userForm.find('[name=ids]').val(idsVal);
					userForm.form('submit', {
						url : '<%=basePath%>manage/sys/workflow/processInstance/delete.do',
						onSubmit : function() {
							$.messager.progress({
								text : '数据处理中，请稍后....'
							});
							return true;
						},
						success : function(data) {
							try {
								$.messager.progress('close');
								var d = $.parseJSON(data);
								$.messager.show({
									msg : d.msg,
									title : '提示'
								});
								if (d.success) {
									datagrid.datagrid('unselectAll');
									searchFun();
								}
							} catch(e) {
								window.location.href = overUrl;
							}
						}
					});
				}
			});
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}
	
	function searchFun() {
		datagrid.datagrid('load', {
				businessKey : $('#toolbar input[name=businessKey]').val(),
				definitionKey  : $('#toolbar input[name=definitionKey]').val()
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
    	<tr>	
			<th>流程定义KEY</th>	
			<td colspan="2">
				<input id="definitionKey" name="definitionKey" maxlength="20"  style="width: 120px;"/>
			</td>
			<th>流程业务ID</th>	
			<td colspan="2">
				<input id="businessKey" name="businessKey" maxlength="20"  style="width: 120px;"/>
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
    <div> 
    <fcc:permission moduleId="${rightModuleId}" operateId="add">
    <a class="easyui-linkbutton" iconCls="icon-add" onClick="importData();" plain="true" href="javascript:void(0);">新增</a> 
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="edit">
    <%--
    <a class="easyui-linkbutton" iconCls="icon-edit" onClick="edit();" plain="true" href="javascript:void(0);">修改</a>
    --%>
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="delete">
    <a class="easyui-linkbutton" iconCls="icon-remove" onClick="del();" plain="true" href="javascript:void(0);">删除</a>  
    </fcc:permission>
    <a class="easyui-linkbutton" iconCls="icon-undo" onClick="datagrid.datagrid('unselectAll');" plain="true" href="javascript:void(0);">取消选中</a> 
    <span id="expordDataSizeSpan" style="color: red; font-weight: bolder;"></span>
    </div>
  </div>
  <table id="datagrid">
  </table>
  <form id="tempForm" name="tempForm" method="post" >
  <input name="processDefinitionId" value="" type="hidden">
  </form>
</div>
</body>
</html>
