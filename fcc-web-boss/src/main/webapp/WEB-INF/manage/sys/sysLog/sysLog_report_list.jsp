<%@page import="com.fcc.web.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
<%@ include file="/WEB-INF/head/highcharts.jsp" %>
<%@ include file="/WEB-INF/head/fusionCharts.jsp" %>
<script src="js/support/report.js" charset="UTF-8" type="text/javascript"></script>
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
			<th><%=SysLog.ALIAS_USER_ID%></th>	
			<td colspan="2">
				<input id="userId" name="userId" maxlength="20"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入用户ID...'"/>
			</td>
			<th><%=SysLog.ALIAS_USER_NAME%></th>	
			<td colspan="2">
				<input id="userName" name="userName" maxlength="20"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入用户名称...'"/>
			</td>
			<th><%=SysLog.ALIAS_IP_ADDRESS%></th>	
			<td colspan="2">
				<input id="ipAddress" name="ipAddress" maxlength="24"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入IP...'"/>
			</td>
			<th><%=SysLog.ALIAS_LOG_TIME%></th>	
			<td colspan="2">
				<input value="" class="easyui-datebox" style="width: 120px;" id="logTimeBegin" name="logTimeBegin"   />
				<input value="" class="easyui-datebox" style="width: 120px;" id="logTimeEnd" name="logTimeEnd"   />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_MODULE_NAME%></th>	
			<td colspan="2">
			    <select id="moduleName" name="moduleName" style="width: 130px;" class="easyui-combobox">
			</td>
			<th><%=SysLog.ALIAS_OPERATE_NAME%></th>	
			<td colspan="2">
			    <input id="operateName" name="operateName" style="width: 130px;" class="easyui-combobox"/>
			</td>
			<th><%=SysLog.ALIAS_EVENT_RESULT%></th>	
			<td colspan="2">
			    <input id="eventResult" name="eventResult" style="width: 130px;" class="easyui-combobox"/>
			</td>
			<th>分组</th>	
			<td colspan="2">
			    <input id="reportGroupName" name="reportGroupName" style="width: 130px;" class="easyui-combobox"/>
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
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="lineCharts('<%=SysLog.TABLE_ALIAS%>', '<%=basePath %>');" plain="true" href="javascript:void(0);">折线图</a>
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="columnCharts('<%=SysLog.TABLE_ALIAS%>', '<%=basePath %>');" plain="true" href="javascript:void(0);">柱形图</a>
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="pieCharts('<%=SysLog.TABLE_ALIAS%>', '<%=basePath %>');" plain="true" href="javascript:void(0);">饼图</a> |
    </div>
  </div>
  <div id="container" style="width: 800px; height: 360px; margin: 0 auto; display: none;" ></div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_combotree.jsp" %>
<%@ include file="/WEB-INF/head/init_combobox.jsp" %>
<script type="text/javascript" charset="UTF-8">
// 报表数据缓存    
var dataList;   
var dataTitle;
$(function() {
    moduleTree = getComboTree({queryUrl:'manage/sys/sysLog/moduleTree.do',id:'moduleName',closed:false});
    getComboBoxByData({
        id : 'operateName',
        valueField : 'id',
        textField : 'text',
        data : [
            <c:forEach items="${operateList}" var="operate">
            {text : '${operate.operateName }', id : '${operate.operateId }'},
            </c:forEach>
        ]
    })
    getComboBoxByData({
        id : 'eventResult',
        valueField : 'id',
        textField : 'text',
        data : [
            {text : '成功', id : '1'},
            {text : '失败', id : '0'}
        ]
    })
    getComboBoxByData({
        id : 'reportGroupName',
        valueField : 'id',
        textField : 'text',
        data : [
            {text : '用户ID', id : 'userId'},
            {text : '用户名称', id : 'userName'},
            {text : 'IP地址', id : 'ipAddress'},
            {text : '日志时间', id : 'logTime'},
            {text : '模块名称', id : 'moduleName'},
            {text : '操作名称', id : 'operateName'},
            {text : '事件结果', id : 'eventResult'},
        ]
    })
})    
function toBack() {
    window.location.href = "${basePath}manage/sys/sysLog/view.do";
} 
datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${basePath}manage/sys/sysLog/report/datagrid.do';// 数据源url
datagridParam_idField = 'groupName';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
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
] ];// 表格的列
datagridParam_queryParamName = ['userId', 'userName', 'ipAddress', 'logTimeBegin', 'logTimeEnd', 'moduleName', 'operateName', 'eventResult', 'reportGroupName'];
datagridParam_load_beforeCallback = function(data) {
	dataTitle = $('.datagrid-header-row td[field="groupName"] div span:first-child');
	var groupName = $('#toolbar input[name="reportGroupName"]').val();
    if (groupName != '') {
    	dataTitle.html($('#reportGroupName').combobox('getText'));
    }
    dataList = data.rows;
}
</script>
