<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;display: none;">
    <fieldset>
    <legend>筛选</legend>
    <table class="tableForm">
      <tr>
        <th>操作名称</th>
        <td><input name="searchName" style="width: 305px;" />
        </td>
        <td>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
        </td>
      </tr>
    </table>
    </fieldset>
    <div id="operateDiv"></div>
  </div>
  <table id="datagrid">
  </table>
</div>
<div id="userDialog" style="display: none;overflow: hidden;">
  <form id="userForm" name="userForm" method="post">
    <input name="operateValue" type="hidden" value=""/>
    <input name="ids" type="hidden" value=""/>
    <table class="tableForm">
      <tr>
        <th>操作ID</th>
        <td><input name="operateId" class="easyui-validatebox" required="true" maxlength="100"/></td>
      </tr>
      <tr>
        <th>操作名称</th>
        <td><input name="operateName" class="easyui-validatebox" required="true" maxlength="100"/></td>
      </tr>
    </table>
  </form>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_dialog.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_operate.jsp" %>
<script type="text/javascript">
var userDialog;
var userForm;
$(function() {
	userForm = $('#userForm').form();
	userDialog = getDialog({
		id : 'userDialog',
		title : '模块操作',
		modal :　true,
		buttons : [ {
            text : '确定',
            handler : function() {
                if (userForm.find('[name=operateValue]').val() != '') {
                    saveParam_form = 'userForm';
                    saveParam_saveUrl = '${basePath}manage/sys/operate/edit.do';
                    saveParam_afterCallback = function(data, success) {
                        if (success == true) {
                            closeDialog(userDialog);
                            searchFun();    
                        }
                    }
                    save();
                } else {
                    saveParam_form = 'userForm';
                    saveParam_saveUrl = '${basePath}manage/sys/operate/add.do';
                    saveParam_afterCallback = function(data, success) {
                        if (success == true) {
                            searchFun();
                        }
                    }
                    save();
                }
            }
        } ]
	});
})


datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${basePath}manage/sys/operate/datagrid.do';// 数据源url
datagridParam_idField = 'operateId';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ {
    field : 'operateId',
    title : '操作ID',
    width : 100
} , {
    field : 'operateName',
    title : '操作名称',
    width : 100
} , {
    field : 'operateValue',
    title : '操作值',
    width : 150
}] ];// 表格的列
datagridParam_queryParamName = ['searchName'];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'operateId';
operateParam_dataName = 'operateName';
operateParam_add_beforeCallback = function() {
	openDialog(userDialog);
    userForm.find('[name=operateId]').removeAttr('readonly');
    userForm.form('clear');
}
operateParam_edit_beforeCallback = function(row) {
	userForm.find('[name=operateId]').attr('readonly', 'readonly');
    openDialog(userDialog);
    userForm.form('clear');
    userForm.form('load', {
        operateId : row.operateId,
        operateName : row.operateName,
        operateValue : row.operateValue
    });
}
operateParam_delUrl = '${basePath}manage/sys/operate/delete.do';
</script>