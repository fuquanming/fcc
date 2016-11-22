<%@page import="com.fcc.web.sys.model.*" %>
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
            <th><%=SysLog.ALIAS_USER_ID%></th>  
            <td colspan="2">
                <input name="userId" maxlength="20"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入用户ID...'"/>
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
                <%-- <option value="">---请选择---</option>
                <c:forEach items="${moduleList}" var="module">
                <c:if test="${not empty module.moduleDesc}">
                <option value="${module.moduleId }">${module.moduleName }</option>
                </c:if>
                </c:forEach> --%>
                </select>
            </td>
            <th><%=SysLog.ALIAS_OPERATE_NAME%></th> 
            <td colspan="2">
                <input id="operateName" name="operateName" style="width: 130px;" class="easyui-combobox"/>
                <%-- <select id="operateName" name="operateName" style="width: 130px;" class="easyui-combobox">
                <option value="">---请选择---</option>
                <option value="login">登录</option>
                <option value="logout">退出</option>
                <c:forEach items="${operateList}" var="operate">
                <option value="${operate.operateId }">${operate.operateName }</option>
                </c:forEach>
                </select> --%>
            </td>
            <th><%=SysLog.ALIAS_EVENT_RESULT%></th> 
            <td colspan="2">
                <input id="eventResult" name="eventResult" style="width: 130px;" class="easyui-combobox"/>
                <!-- <select name="eventResult" id="eventResult" style="width: 130px;" class="easyui-combobox">
                    <option value="">---请选择---</option>
                    <option value="0">失败</option>
                    <option value="1">成功</option>
                </select> -->
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
<script type="text/javascript" charset="UTF-8">
/* var datagrid;
var userForm;
var userDialog;
var importDataFlag = false;
$(function() {
    userForm = $('#userForm').form();
}); */
</script>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_operate.jsp" %>
<%@ include file="/WEB-INF/head/init_export.jsp" %>
<%@ include file="/WEB-INF/head/init_import.jsp" %>
<%@ include file="/WEB-INF/head/init_combotree.jsp" %>
<%@ include file="/WEB-INF/head/init_combobox.jsp" %>
<script type="text/javascript">
var moduleTree;

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
})

datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${basePath}manage/sys/sysLog/datagrid.do';// 数据源url
datagridParam_idField = 'logId';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
    {
        field : 'userId',
        title : '<%=SysLog.ALIAS_USER_ID%>',
        sortable:true,
        width : 100
    } ,
    {
        field : 'userName',
        title : '<%=SysLog.ALIAS_USER_NAME%>',
        sortable:true,
        width : 100
    } ,
    {
        field : 'ipAddress',
        title : '<%=SysLog.ALIAS_IP_ADDRESS%>',
        sortable:true,
        width : 100
    } ,
    {
        field : 'logTime',
        title : '<%=SysLog.ALIAS_LOG_TIME%>',
        sortable:true,
        width : 100,
        formatter : function(value, rowData, rowIndex) {
        	return Tool.dateFormat({'value':value, 'format':'yyyy-MM-dd HH:mm:ss'});
        }
    } ,
    {
        field : 'moduleName',
        title : '<%=SysLog.ALIAS_MODULE_NAME%>',
        sortable:true,
        width : 100
    } ,
    {
        field : 'operateName',
        title : '<%=SysLog.ALIAS_OPERATE_NAME%>',
        sortable:true,
        width : 100
    } ,
    {
        field : 'eventResult',
        title : '<%=SysLog.ALIAS_EVENT_RESULT%>',
        sortable:true,
        width : 100,
        formatter : function(value, rowData, rowIndex) {
            var msg = '失败'
            if (value == '1') {
                msg = '成功';
            }
            return msg;
        }
    } 
] ];// 表格的列
datagridParam_queryParamName = ['userId', 'userName', 'ipAddress', 'logTimeBegin', 'logTimeEnd', 'moduleName', 'operateName', 'eventResult'];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'logId';
operateParam_dataName = 'userName';
operateParam_viewUrl = '${basePath}manage/sys/sysLog/toView.do';
operateParam_addUrl = '${basePath}manage/sys/sysLog/toAdd.do';
operateParam_editUrl = '${basePath}manage/sys/sysLog/toEdit.do';
operateParam_delUrl = '${basePath}manage/sys/sysLog/delete.do';
operateParam_reportUrl = '${basePath}manage/sys/sysLog/report/view.do';

exportParam_form = 'userForm';
exportParam_exportUrl = "${basePath}manage/sys/sysLog/export.do";// 导出数据URL
exportParam_queryExportUrl = "${basePath}manage/sys/sysLog/queryExport.do";// 查询导出数据URL
exportParam_model = "sysLog";// 模块

importParam_importUrl = "${basePath}manage/sys/sysLog/import.do";// 导入数据URL
importParam_queryImportUrl = "${basePath}manage/sys/sysLog/queryImport.do";// 查询导入数据URL
</script>
