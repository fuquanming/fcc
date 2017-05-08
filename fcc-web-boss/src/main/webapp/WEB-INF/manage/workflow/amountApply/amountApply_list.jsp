<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
<%@ include file="/WEB-INF/head/workflow.jsp" %>
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
			<th>借款人ID（会员）</th>	
			<td colspan="2">
				<input id="userId" name="userId" maxlength="19"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入借款人ID（会员）...'"/>
			</td>
			<th>会员账号名称</th>	
			<td colspan="2">
				<input id="userName" name="userName" maxlength="20"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入会员账号名称...'"/>
			</td>
			<th>发起人申请的额度</th>	
			<td colspan="2">
				<input id="primaryAmount" name="primaryAmount" maxlength="11"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入发起人申请的额度...'"/>
			</td>
			<th>申请时间</th>	
			<td colspan="2">
				<input value="" class="easyui-datebox" style="width: 120px;" id="applyTimeBegin" name="applyTimeBegin"   />
				<input value="" class="easyui-datebox" style="width: 120px;" id="applyTimeEnd" name="applyTimeEnd"   />
			</td>
		</tr>	
		<tr>	
			<th>状态</th>	
			<td colspan="2">
				<input id="status" name="status" maxlength="16"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入状态...'"/>
			</td>
		</tr>	
		<tr>	
		</tr>	
		<tr>
	        <td colspan="3" align="left">
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
<%@ include file="/WEB-INF/head/init_export.jsp" %>
<%@ include file="/WEB-INF/head/init_import.jsp" %>
<%@ include file="/WEB-INF/head/init_combotree.jsp" %>
<%@ include file="/WEB-INF/head/init_combobox.jsp" %>
<script type="text/javascript">

datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = 'manage/workflow/amountApply/datagrid.do';// 数据源url
datagridParam_idField = 'amountApplyId';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
        {
            field : 'userId',
            title : '借款人ID（会员）',
            sortable:true,
            width : 100
        } ,
        {
            field : 'userName',
            title : '会员账号名称',
            sortable:true,
            width : 100
        } ,
        {
            field : 'primaryAmount',
            title : '发起人申请的额度',
            sortable:true,
            width : 100
        } ,
        {
            field : 'applyRemark',
            title : '申请备注',
            sortable:true,
            width : 100
        } ,
        {
            field : 'applyTime',
            title : '申请时间',
            sortable:true,
            width : 100,
            formatter : function(value, rowData, rowIndex) {
            	return Tool.dateFormat({'value':value, 'format':'yyyy-MM-dd HH:mm:ss'});
            }
        } ,
    {
        field : 'processNodeName',
        title : '当前节点',
        width : 100,
        formatter : function(value, rowData, rowIndex) {
            if (value == null || value == '') {
                return '';      
            }
            return '<a class="trace" href="javascript:void(0)" onclick="showTraceImg(\'' + rowData.processInstanceId + '\', \'' + rowData.processDefinitionId + '\')" title="点击查看流程图">' + rowData.processNodeName + '</a>';
        }
    } ,
    {
        field : 'status',
        title : '状态',
        sortable:true,
        width : 100,
        formatter : function(value, rowData, rowIndex) {
            return formartStatus(value);
        }
    } 
] ];// 表格的列
datagridParam_queryParamName = [
        'userId',
        'userName',
        'primaryAmount',
        'applyRemark',
        'applyTimeBegin','applyTimeEnd',
        'processInstanceId',
        'processDefinitionId',
        'processNodeName',
        'status',
        'createUser',
        'createTimeBegin','createTimeEnd',
        'updateUser',
        'updateTimeBegin','updateTimeEnd'
];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'amountApplyId';
operateParam_dataName = 'userId';
operateParam_viewUrl = 'manage/workflow/amountApply/toView.do';
operateParam_addUrl = 'manage/workflow/amountApply/toAdd.do';
operateParam_editUrl = 'manage/workflow/amountApply/toEdit.do';
operateParam_delUrl = 'manage/workflow/amountApply/delete.do';
operateParam_reportUrl = 'manage/workflow/amountApply/report/view.do';
operateParam_edit_beforeCallback = function(row) {
    if (row.status == 'unstart') {
        return true;
    } else {
        Tool.message.alert(Lang.tip, StatusCode.msg('workflow_000'), Tool.icon.error);
        return false;
    }
}

exportParam_form = 'userForm';
exportParam_exportUrl = "manage/workflow/amountApply/export.do";// 导出数据URL
exportParam_queryExportUrl = "manage/workflow/amountApply/queryExport.do";// 查询导出数据URL
exportParam_model = "amountApply";// 模块

importParam_importUrl = "manage/workflow/amountApply/import.do";// 导入数据URL
importParam_queryImportUrl = "manage/workflow/amountApply/queryImport.do";// 查询导入数据URL
</script>