<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
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
  	<input name="ids" type="hidden" value=""/>
    <table class="tableForm">
		<tr>	
			<th>关联类型</th>	
			<td colspan="2">
				<input id="linkType" name="linkType" maxlength="32"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入关联类型...'"/>
			</td>
			<th>附件名称</th>	
			<td colspan="2">
				<input id="annexName" name="annexName" maxlength="50"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入附件名称...'"/>
			</td>
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
datagridParam_url = 'manage/sys/sysAnnex/datagrid.do';// 数据源url
datagridParam_idField = 'annexId';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
        {
            field : 'linkId',
            title : '关联ID',
            sortable:true,
            width : 100
        } ,
        {
            field : 'linkType',
            title : '关联类型',
            sortable:true,
            width : 100
        } ,
        {
            field : 'annexName',
            title : '附件名称',
            sortable:true,
            width : 100
        } ,
        {
            field : 'fileName',
            title : '文件名称',
            sortable:true,
            width : 100
        } ,
        {
            field : 'fileType',
            title : '文件类型',
            sortable:true,
            width : 100
        } ,
        {
            field : 'fileUrl',
            title : '文件地址',
            sortable:true,
            width : 100
        } ,
        {
            field : 'fileSize',
            title : '文件大小',
            sortable:true,
            width : 100
        } ,
        {
            field : 'remark',
            title : '文件备注',
            sortable:true,
            width : 100
        } 
] ];// 表格的列
datagridParam_queryParamName = [
        'linkType',
        'annexName'
];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'annexId';
operateParam_dataName = 'linkId';
operateParam_viewUrl = 'manage/sys/sysAnnex/toView.do';
operateParam_addUrl = 'manage/sys/sysAnnex/toAdd.do';
operateParam_editUrl = 'manage/sys/sysAnnex/toEdit.do';
operateParam_delUrl = 'manage/sys/sysAnnex/delete.do';
operateParam_reportUrl = 'manage/sys/sysAnnex/report/view.do';

exportParam_form = 'userForm';
exportParam_exportUrl = "manage/sys/sysAnnex/export.do";// 导出数据URL
exportParam_queryExportUrl = "manage/sys/sysAnnex/queryExport.do";// 查询导出数据URL
exportParam_model = "sysAnnex";// 模块

importParam_importUrl = "manage/sys/sysAnnex/import.do";// 导入数据URL
importParam_queryImportUrl = "manage/sys/sysAnnex/queryImport.do";// 查询导入数据URL
</script>