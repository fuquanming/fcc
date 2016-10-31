<%@page import="com.fcc.web.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
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
                <input id="userId" name="userId" maxlength="20"  style="width: 120px;"/>
            </td>
            <th><%=SysLog.ALIAS_USER_NAME%></th>    
            <td colspan="2">
                <input id="userName" name="userName" maxlength="20"  style="width: 120px;"/>
            </td>
            <th><%=SysLog.ALIAS_IP_ADDRESS%></th>   
            <td colspan="2">
                <input id="ipAddress" name="ipAddress" maxlength="24"  style="width: 120px;"/>
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
                <select id="moduleName" name="moduleName" style="width: 130px;">
                <option value="">---请选择---</option>
                <c:forEach items="${USER_MENU}" var="module">
                <c:if test="${not empty module.moduleDesc}">
                <option value="${module.moduleId }">${module.moduleName }</option>
                </c:if>
                </c:forEach>
                </select>
            </td>
            <th><%=SysLog.ALIAS_OPERATE_NAME%></th> 
            <td colspan="2">
                <select id="operateName" name="operateName" style="width: 130px;">
                <option value="">---请选择---</option>
                <option value="login">登录</option>
                <option value="logout">退出</option>
                <c:forEach items="${operateList}" var="operate">
                <option value="${operate.operateId }">${operate.operateName }</option>
                </c:forEach>
                </select>
            </td>
            <th><%=SysLog.ALIAS_EVENT_RESULT%></th> 
            <td colspan="2">
                <select name="eventResult" id="eventResult" style="width: 130px;">
                    <option value="">---请选择---</option>
                    <option value="0">失败</option>
                    <option value="1">成功</option>
                </select>
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
    <%@ include file="/head/init_operate.jsp" %>
  </div> 
  <table id="datagrid">
  </table>
</div>
</body>
</html>
<script type="text/javascript" charset="UTF-8">
var datagrid;
var userForm;
var userDialog;
var importDataFlag = false;
$(function() {

    userForm = $('#userForm').form();
    
    datagrid = $('#datagrid').datagrid({
        <c:if test="${empty param.lazy}">url : '${basePath}manage/sys/sysLog/datagrid.do?random=' + Math.random(),</c:if>
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
        idField : 'logId',
        frozenColumns : [ [ 
        {
            field : 'id',
            width : 50,
            checkbox : true
        }, {
                field : 'logId',
                hidden : true
        } 
         ] ],
        columns : [ [ 
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
                    var date = new Date(value);
                    var month = date.getMonth() + 1;
                    var day = date.getDate();
                    var hour = date.getHours();
                    var minute = date.getMinutes();
                    var second = date.getSeconds();
                    return date.getFullYear() + '-' + (month < 10 ? "0" : "") + month + '-' + (day < 10 ? "0" : "") + day 
                    + " " + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "") + second;
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
                Tool.message.alert(Lang.tip, Lang.dataError, Tool.icon.error);
            }
        },
        loadFilter : function(data) {
            var flag = Tool.operate.check(data);
            if (flag != true || flag != false) {
                return data;                                            
            }
        }
    });
});
</script>

<jsp:include page="/head/init_list.jsp">
<jsp:param name="initDataId" value="logId" />
<jsp:param name="initDataName" value="userName" />
<jsp:param name="initViewUrl" value="${basePath}manage/sys/sysLog/toView.do"/>
<jsp:param name="initAddUrl" value="${basePath}manage/sys/sysLog/toAdd.do"/>
<jsp:param name="initEditUrl" value="${basePath}manage/sys/sysLog/toEdit.do"/>
<jsp:param name="initDelUrl" value="${basePath}manage/sys/sysLog/delete.do"/>
<jsp:param name="initReportUrl" value="${basePath}manage/sys/sysLog/report/view.do"/>

<jsp:param name="queryParamId" value="userId"/>
<jsp:param name="queryParamId" value="userName"/>
<jsp:param name="queryParamId" value="ipAddress"/>
<jsp:param name="queryParamId" value="logTimeBegin"/>
<jsp:param name="queryParamId" value="logTimeEnd"/>
<jsp:param name="queryParamId" value="moduleName"/>
<jsp:param name="queryParamId" value="operateName"/>
<jsp:param name="queryParamId" value="eventResult"/>
</jsp:include>
<jsp:include page="/head/init_export.jsp">
<jsp:param name="initExportUrl" value="${basePath}manage/sys/sysLog/export.do" />
<jsp:param name="initQueryExportUrl" value="${basePath}manage/sys/sysLog/queryExport.do" />
<jsp:param name="initModel" value="sysLog" />
</jsp:include>
<jsp:include page="/head/init_import.jsp">
<jsp:param name="initImportUrl" value="${basePath}manage/sys/sysLog/import.do" />
<jsp:param name="initQueryImportUrl" value="${basePath}manage/sys/sysLog/queryImport.do" />
</jsp:include>