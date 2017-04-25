<%@page import="com.fcc.app.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%-- 流程模型管理 --%>
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
  	<input id="ids" name="ids" type="hidden" value=""/>
    <table class="tableForm">
		<tr>	
			<th>模型KEY</th>	
			<td colspan="2">
				<input id="modelKey" name="modelKey" maxlength="20"  style="width: 120px;" class="easyui-textbox"  data-options="prompt:'模型KEY...'"/>
			</td>
			<th>模型名称</th>	
			<td colspan="2">
				<input id="modelName" name="modelName" maxlength="20"  style="width: 120px;" class="easyui-textbox"  data-options="prompt:'模型名称...'"/>
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
  <table id="datagrid"></table>
</div>
<div id="userDialog" style="display: none;overflow: hidden;">
  <form id="userAddForm" name="userAddForm" method="post" >
    <table class="tableForm" style="width: 400px; height: 200px;" align="center">
      <tr>
        <th>模型KEY</th>
        <td><input id="modelKey" name="modelKey" class="easyui-validatebox easyui-textbox" required="true" maxlength="100"/></td>
      </tr>
      <tr>
        <th>模型名称</th>
        <td><input id="modelName" name="modelName" class="easyui-validatebox easyui-textbox" required="true" maxlength="100"/></td>
      </tr>
      <tr>
        <th>模型描述</th>
        <td><textarea id="modelDescription" name="modelDescription" rows="5" cols="20" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 200]"></textarea> </td>
      </tr>
    </table>
  </form>
  <form id="tempForm" name="tempForm" method="post" target="_blank">
  <input name="modelId" value="" type="hidden"/>
  </form>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_operate.jsp" %>
<script type="text/javascript">
var userDialog;
$(function() {
	userDialog = $('#userDialog').show().dialog({
        modal : true,
        title : '添加模型',
        buttons : [ {
            text : '确定',
            handler : function() {
            	saveParam_form = 'userAddForm';// 提交的Form
            	saveParam_saveUrl = 'manage/sys/workflow/processModel/add.do';// 保存URL地址
            	saveParam_afterCallback = function(data, success) {
            		if (success) {
            			var modelId = data.obj;
                        userDialog.dialog('close');
                        editModeler(modelId);
                        searchFun();
            		}
            		return false;
            	}
            	saveParam_async = false;// 异步
            	save();
            }
        } ]
    }).dialog('close');
})

function editModeler(id) {
    var tempForm = document.forms['tempForm'];
    tempForm.modelId.value = id;
    //tempForm.action = "http://localhost:10000/activiti-modeler/modeler.html?modelId=" + id;
    tempForm.action = "${modelerUrl}" + id;
    tempForm.submit();
}

function deploy(id) {
	Tool.message.confirm(Lang.confirm, Lang.confirmOperate, function(r) {
        if (r) {
        	$('#ids').val(id);
            saveParam_form = 'userForm';
            saveParam_saveUrl = 'manage/sys/workflow/processModel/deploy.do';
            saveParam_afterCallback = function(data, success) {
                gridUnselectAll();
                searchFun();
            };
            save();
        }
    });
}

datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${basePath}manage/sys/workflow/processModel/datagrid.do';// 数据源url
datagridParam_idField = 'id';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
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
        	var str = '';
        	<fcc:permission operateId="edit">
        	str += '<a href="javascript:void(0)" onclick="deploy(\'' + rowData.id + '\')">部署</a> ';
        	</fcc:permission>
        	str += '<a href="${basePath}manage/sys/workflow/processModel/export.do?modelId=' + rowData.id + '" target="_blank">导出</a>';
            return str;
        }
    }
] ];// 表格的列
datagridParam_queryParamName = ['modelKey', 'modelName'];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'id';
operateParam_dataName = 'name';
operateParam_add_beforeCallback = function() {
	userDialog.dialog('open');
    $('#userAddForm').form('clear');
    return false;
}
operateParam_edit_beforeCallback = function(row) {
	editModeler(row.id);
    return false;
}
operateParam_delUrl = '${basePath}manage/sys/workflow/processModel/delete.do';
</script>