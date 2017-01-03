<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%
response.sendRedirect(basePath + "manage/sys/code/toAdd.do");
%>
<%-- <%@ include file="/WEB-INF/head/meta.jsp" %>
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
			<th>表名</th>	
			<td colspan="2">
				<input id="tableName" name="tableName" maxlength="255"  style="width: 220px;" class="easyui-textbox" data-options="prompt:'请输入java包名...'"/>
			</td>
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
datagridParam_url = 'manage/sys/code/datagrid.do';// 数据源url
datagridParam_idField = 'typeId';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
        {
            field : 'typeName',
            title : '名称',
            sortable:true,
            width : 100
        } ,
        {
            field : 'typeCode',
            title : '代码',
            sortable:true,
            width : 100
        } 
] ];// 表格的列
datagridParam_queryParamName = [
        'typeName',
        'typeCode'
];
datagridParam_load_beforeCallback = function(data) {
	console.log(data);
}

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'typeId';
operateParam_dataName = 'typeName';
operateParam_viewUrl = 'manage/sys/sysType/toView.do';
operateParam_addUrl = 'manage/sys/sysType/toAdd.do';
operateParam_editUrl = 'manage/sys/sysType/toEdit.do';
operateParam_delUrl = 'manage/sys/sysType/delete.do';

</script> --%>