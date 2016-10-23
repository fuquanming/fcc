<%@page import="com.fcc.web.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<script language="javascript" type="text/javascript" src="js/datepicker/WdatePicker.js" defer="defer"></script>
<link rel="stylesheet" type="text/css" href="js/datepicker/skin/WdatePicker.css" />
<script type="text/javascript" charset="UTF-8">

	function toBack() {
		window.location.href = '<%=basePath%>manage/sys/sysLog/view.do';
	}

</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>查看<%=SysLog.TABLE_ALIAS%></legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="logId" type="hidden" value="${sysLog.logId}" />
      <table class="tableForm" align="center">
		<tr>	
			<th><%=SysLog.ALIAS_USER_ID%>：</th>		
			<td>${sysLog.userId}</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_USER_NAME%>：</th>		
			<td>${sysLog.userName}</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_IP_ADDRESS%>：</th>		
			<td>${sysLog.ipAddress}</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_LOG_TIME%>：</th>		
			<td><fmt:formatDate value="${sysLog.logTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_MODULE_NAME%>：</th>		
			<td>${sysLog.moduleName}</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_OPERATE_NAME%>：</th>		
			<td>${sysLog.operateName}</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_EVENT_PARAM%>：</th>		
			<td><textarea rows="6" cols="30">${sysLog.eventParam}</textarea></td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_EVENT_OBJECT%>：</th>		
			<td><textarea rows="6" cols="30">${sysLog.eventObject}</textarea></td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_EVENT_RESULT%>：</th>		
			<td>
			<c:choose>
			<c:when test="${sysLog.eventResult == '0'}">失败</c:when>
			<c:when test="${sysLog.eventResult == '1'}">成功</c:when>
			</c:choose>
			</td>
		</tr>	
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
