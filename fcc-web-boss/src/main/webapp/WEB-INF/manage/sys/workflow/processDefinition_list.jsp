<%@page import="com.fcc.app.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%-- 流程定义及部署管理 --%>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
<script type="text/javascript" charset="UTF-8">
	<%-- var datagrid;
	var userForm;
	var userDialog;
	var importDataFlag = false;
	$(function() {

		userForm = $('#userForm').form();

		datagrid = $('#datagrid').datagrid({
			url : '<%=basePath%>manage/sys/workflow/processDefinition/datagrid.do?random=' + Math.random(),
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
			idField : 'processDefinitionId',
			frozenColumns : [ [ 
			{
				field : 'id',
				width : 50,
				checkbox : true
			}, {
					title : 'ID',
					field : 'processDefinitionId',
					hidden : false
			} 
			 ] ],
			columns : [ [ 
				{
					field : 'processDeploymentId',
					title : '流程部署ID',
					sortable: false,
					width : 50
				} ,
				{
					field : 'processName',
					title : '流程名称',
					width : 100
				} ,
				{
					field : 'processKey',
					title : '流程KEY',
					width : 100
				} ,
				{
					field : 'processVersion',
					title : '流程版本',
					width : 30
				} ,
				{
					field : 'processResourceName',
					title : 'XML',
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						return '<a target="_blank" href="<%=basePath%>manage/sys/workflow/processDefinition/resource/read.do?processDefinitionId=' + rowData.processDefinitionId + '&resourceType=xml">' + value + '</a>';
					}
				} ,
				{
					field : 'processDiagramResourceName',
					title : '图片',
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						return '<a target="_blank" href="<%=basePath%>manage/sys/workflow/processDefinition/resource/read.do?processDefinitionId=' + rowData.processDefinitionId + '&resourceType=image">' + value + '</a>';
					}
				} ,
				{
					field : 'deploymentTime',
					title : '部署时间',
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
					}
				} ,
				{
					field : 'processSuspended',
					title : '是否挂起',
					width : 30,
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
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						var processDefinitionId = rowData.processDefinitionId;
						var msg = '';
						<fcc:permission moduleId="${rightModuleId}" operateId="edit">
						msg = '<a href="javascript:void(0)" onclick="edit(\'suspend\', \'' + processDefinitionId + '\')">挂起</a>';
						if (rowData.processSuspended == true) {
							msg = '<a href="javascript:void(0)" onclick="edit(\'activate\', \'' + processDefinitionId + '\')">激活</a>'
						}
						</fcc:permission>						
						msg += ' <a href="javascript:void(0)" onclick="convertToModel(\'' + processDefinitionId + '\')">转换为Model</a>';
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
					url : '<%=basePath%>manage/sys/workflow/processDefinition/edit.do?processDefinitionId=' + id + "&status=" + status,
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
						ids.push(rows[i].processDeploymentId);
					}
					var idsVal = ids.join(',');
					userForm.find('[name=ids]').val(idsVal);
					userForm.form('submit', {
						url : '<%=basePath%>manage/sys/workflow/processDefinition/delete.do',
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
	 --%>
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
			<th>流程定义名称</th>	
			<td colspan="2">
				<input id="definitionName" name="definitionName" maxlength="20"  style="width: 150px;" class="easyui-textbox"  data-options="prompt:'请输入流程定义名称...'"/>
			</td>
			<th>流程定义KEY</th>	
			<td colspan="2">
				<input id="definitionKey" name="definitionKey" maxlength="20"  style="width: 150px;" class="easyui-textbox"  data-options="prompt:'请输入流程定义KEY...'"/>
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
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_operate.jsp" %>
<%@ include file="/WEB-INF/head/init_import.jsp" %>
<script type="text/javascript">
datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${basePath}manage/sys/workflow/processDefinition/datagrid.do';// 数据源url
datagridParam_idField = 'processDefinitionId';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
    {
        field : 'processDeploymentId',
        title : '流程部署ID',
        sortable: false,
        width : 50
    } ,
    {
        field : 'processName',
        title : '流程名称',
        width : 100
    } ,
    {
        field : 'processKey',
        title : '流程KEY',
        width : 100
    } ,
    {
        field : 'processVersion',
        title : '流程版本',
        width : 35
    } ,
    {
        field : 'processResourceName',
        title : 'XML',
        width : 100,
        formatter : function(value, rowData, rowIndex) {
            return '<a target="_blank" href="<%=basePath%>manage/sys/workflow/processDefinition/resource/read.do?processDefinitionId=' + rowData.processDefinitionId + '&resourceType=xml">' + value + '</a>';
        }
    } ,
    {
        field : 'processDiagramResourceName',
        title : '图片',
        width : 100,
        formatter : function(value, rowData, rowIndex) {
            return '<a target="_blank" href="<%=basePath%>manage/sys/workflow/processDefinition/resource/read.do?processDefinitionId=' + rowData.processDefinitionId + '&resourceType=image">' + value + '</a>';
        }
    } ,
    {
        field : 'deploymentTime',
        title : '部署时间',
        width : 80,
        formatter : function(value, rowData, rowIndex) {
            return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
        }
    } ,
    {
        field : 'processSuspended',
        title : '是否挂起',
        width : 35,
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
        width : 100,
        formatter : function(value, rowData, rowIndex) {
            var processDefinitionId = rowData.processDefinitionId;
            var msg = '';
            <fcc:permission operateId="edit">
            msg = '<a href="javascript:void(0)" onclick="edit(\'suspend\', \'' + processDefinitionId + '\')">挂起</a>';
            if (rowData.processSuspended == true) {
                msg = '<a href="javascript:void(0)" onclick="edit(\'activate\', \'' + processDefinitionId + '\')">激活</a>'
            }
            </fcc:permission>                       
            msg += ' <a href="javascript:void(0)" onclick="convertToModel(\'' + processDefinitionId + '\')">转换为Model</a>';
            return msg;
        }
    } 
] ];// 表格的列
datagridParam_queryParamName = ['definitionName', 'definitionKey'];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'processDeploymentId';
operateParam_dataName = 'processName';
operateParam_delUrl = '${basePath}manage/sys/workflow/processDefinition/delete.do';

importParam_importFileType = 'zip|bar|bpmn|bpmn20.xml';
importParam_importUrl = "${basePath}manage/sys/workflow/processDefinition/add.do";// 导入数据URL
importParam_queryImportUrl = "${basePath}manage/sys/workflow/processDefinition/queryImport.do";// 查询导入数据URL

function edit(status, id) {
    Tool.message.confirm(Lang.confirm, Lang.confirmOperate, function(r) {
        if (r) {
            saveParam_form = 'userForm';
            saveParam_saveUrl = 'manage/sys/workflow/processDefinition/edit.do?processDefinitionId=' + id + "&status=" + status;
            saveParam_afterCallback = function(data, success) {
            	gridUnselectAll();
                searchFun();
            };
            save();
        }
    });
}
function convertToModel(processDefinitionId) {
	saveParam_form = 'userForm';
    saveParam_saveUrl = 'manage/sys/workflow/processDefinition/convertToModel.do?processDefinitionId=' + processDefinitionId;
    saveParam_afterCallback = function(data, success) {
        gridUnselectAll();
    };
    save();
}
</script>