<%@page import="com.fcc.app.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%-- 流程模型管理 --%>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<%@ include file="/head/upload_js.jsp" %>
<script type="text/javascript" charset="UTF-8">
	var datagrid;
	var userForm;
	var userDialog;
	var importDataFlag = false;
	$(function() {

		userForm = $('#userForm').form();

		userDialog = $('#userDialog').show().dialog({
			modal : true,
			title : '添加模型',
			buttons : [ {
				text : '确定',
				handler : function() {
					$('#userAddForm').form('submit', {
						url : '<%=basePath%>manage/sys/workflow/processModel/add.do',
						success : function(data) {
							try {
								var d = $.parseJSON(data);
								if (d.success) {
									var modelId = d.msg;
									userDialog.dialog('close');
									editModeler(modelId);
								} else {
									$.messager.show({
										msg : d.msg,
										title : '提示'
									});
								}
							} catch(e) {
								window.location.href = overUrl;
							}
						}
					});
				}
			} ]
		}).dialog('close');

		datagrid = $('#datagrid').datagrid({
			url : '<%=basePath%>manage/sys/workflow/processModel/datagrid.do?random=' + Math.random(),
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
				checkbox : true
			}, {
					field : 'id',
					hidden : true
			} 
			 ] ],
			columns : [ [ 
				{
					field : 'key',
					title : '模型KEY',
					sortable: false,
					width : 100
				} ,
				{
					field : 'name',
					title : '模型名称',
					width : 100
				} ,
				{
					field : 'version',
					title : '版本号',
					width : 50
				} ,
				{
					field : 'createTime',
					title : '创建时间',
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
					}
				} ,
				{
					field : 'lastUpdateTime',
					title : '更新时间',
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
					}
				} ,
				{
					field : 'metaInfo',
					title : '元数据',
					width : 300
				} ,
				{
					field : 'metaInfo1',
					title : '操作',
					width : 50,
					formatter : function(value, rowData, rowIndex) {
						return '<a href="${basePath}manage/sys/workflow/processModel/export.do?modelId=' + rowData.id + '" target="_blank">导出</a>';
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
	
	function editModeler(id) {
		var tempForm = document.forms['tempForm'];
		tempForm.id.value = id;
		tempForm.action = "${modelerUrl}";
		tempForm.submit();
	}
	
	function add() {
		userDialog.dialog('open');
		$('#userAddForm').form('clear');
	}

	function edit() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length != 1 && rows.length != 0) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].name);
			}
			$.messager.show({
				msg : '只能择一条记录进行编辑！您已经选择了【' + names.join(',') + '】' + rows.length + '条记录',
				title : '提示'
			});
		} else if (rows.length == 1) {
			editModeler(rows[0].id);
			//window.location.href = "<%=base%>/activiti-modeler/modeler/service/editor?id=" + rows[0].id;
		}
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
						url : '<%=basePath%>manage/sys/workflow/processModel/delete.do',
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
	
	function deploy() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要部署当前所选记录？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					var idsVal = ids.join(',');
					userForm.find('[name=ids]').val(idsVal);
					userForm.form('submit', {
						url : '<%=basePath%>manage/sys/workflow/processModel/deploy.do',
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
			$.messager.alert('提示', '请选择要部署的记录！', 'error');
		}
	}
	
	function searchFun() {
		datagrid.datagrid('load', {
				modelKey : $('#toolbar input[name=modelKey]').val(),
				modelName : $('#toolbar input[name=modelName]').val()
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
			<th>模型KEY</th>	
			<td colspan="2">
				<input id="modelKey" name="modelKey" maxlength="20"  style="width: 120px;"/>
			</td>
			<th>模型名称</th>	
			<td colspan="2">
				<input id="modelName" name="modelName" maxlength="20"  style="width: 120px;"/>
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
    <a class="easyui-linkbutton" iconCls="icon-add" onClick="add();" plain="true" href="javascript:void(0);">新增</a> 
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="edit">
    <a class="easyui-linkbutton" iconCls="icon-edit" onClick="edit();" plain="true" href="javascript:void(0);">修改</a>
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="delete">
    <a class="easyui-linkbutton" iconCls="icon-remove" onClick="del();" plain="true" href="javascript:void(0);">删除</a>  
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="deploy">
    <a class="easyui-linkbutton" iconCls="icon-upload" onClick="deploy();" plain="true" href="javascript:void(0);">部署</a>
    </fcc:permission>
    <a class="easyui-linkbutton" iconCls="icon-undo" onClick="datagrid.datagrid('unselectAll');" plain="true" href="javascript:void(0);">取消选中</a> 
    <span id="expordDataSizeSpan" style="color: red; font-weight: bolder;"></span>
    </div>
  </div>
  <table id="datagrid"></table>
</div>
<div id="userDialog" style="display: none;overflow: hidden;">
  <form id="userAddForm" name="userAddForm" method="post">
    <table class="tableForm" style="width: 400px; height: 200px;" align="center">
      <tr>
        <th>模型KEY</th>
        <td><input id="modelKey" name="modelKey" class="easyui-validatebox" required="true" maxlength="100"/></td>
      </tr>
      <tr>
        <th>模型名称</th>
        <td><input id="modelName" name="modelName" class="easyui-validatebox" required="true" maxlength="100"/></td>
      </tr>
      <tr>
        <th>模型描述</th>
        <td><textarea id="modelDescription" name="modelDescription" rows="5" cols="20"></textarea> </td>
      </tr>
    </table>
  </form>
  <form id="tempForm" name="tempForm" method="get" target="_blank">
  <input name="id" value="" type="hidden"/>
  </form>
</div>
</body>
</html>
