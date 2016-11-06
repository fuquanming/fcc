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
			<th>分组</th>	
			<td colspan="2">
				<select name="reportGroupName" id="reportGroupName" style="width: 130px;">
					<option value="">---请选择---</option>
					<option value="userId"><%=SysLog.ALIAS_USER_ID%></option>
					<option value="userName"><%=SysLog.ALIAS_USER_NAME%></option>
					<option value="ipAddress"><%=SysLog.ALIAS_IP_ADDRESS%></option>
					<option value="logTime"><%=SysLog.ALIAS_LOG_TIME%></option>
					<option value="moduleName"><%=SysLog.ALIAS_MODULE_NAME%></option>
					<option value="operateName"><%=SysLog.ALIAS_OPERATE_NAME%></option>
					<option value="eventParam"><%=SysLog.ALIAS_EVENT_PARAM%></option>
					<option value="eventObject"><%=SysLog.ALIAS_EVENT_OBJECT%></option>
					<option value="eventResult"><%=SysLog.ALIAS_EVENT_RESULT%></option>
				</select>
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
<script type="text/javascript" charset="UTF-8">
    var datagrid;
    var userForm;
    var dataList;
    var dataTitle;
    $(function() {
        userForm = $('#userForm').form();
        
        datagrid = $('#datagrid').datagrid({
            url : '${basePath}manage/sys/sysLog/report/datagrid.do?random=' + Math.random(),
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
            idField : 'groupName',
            frozenColumns : [ [ 
             ] ],
            columns : [ [ 
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
            onLoadError : function(e) {
                window.location.href = overUrl;
            },
            onLoadSuccess : function(data) {
                if (data.msg && data.msg != '') {
                    Tool.message.alert(Lang.tip, Lang.dataError, Tool.icon.error);
                } else {
                    dataList = data.rows;
                    dataTitle = $('.datagrid-view td[field="groupName"] div span:first-child');
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
    
    function searchFun() {
        datagrid.datagrid('load', {
                userId : $('#toolbar input[name=userId]').val(),
                userName : $('#toolbar input[name=userName]').val(),
                ipAddress : $('#toolbar input[name=ipAddress]').val(),
                logTimeBegin : $('#toolbar input[name=logTimeBegin]').val(),
                logTimeEnd : $('#toolbar input[name=logTimeEnd]').val(),
                moduleName : $('#moduleName').val(),
                operateName : $('#operateName').val(),
                eventResult : $('#eventResult').val(),
                reportGroupName : $('#reportGroupName').val()
        });
        var groupName = $('#reportGroupName').val();
        if (groupName != '') {
            dataTitle.html($('#reportGroupName').find("option:selected").text());   
        }
    }
    function clearFun() {
        $('#toolbar input').val('');
        $('#reportGroupName').val('');
        datagrid.datagrid('load', {});
    }
    function toBack() {
        window.location.href = "${basePath}manage/sys/sysLog/view.do";
    }
    
</script>
