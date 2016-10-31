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
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>修改<%=SysLog.TABLE_ALIAS%></legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="logId" type="hidden" value="${sysLog.logId}" />
      <table class="tableForm" align="center">
		<tr>	
			<th><%=SysLog.ALIAS_USER_ID%>：</th>		
			<td>
			<input id="userId" name="userId" type="text" value="${sysLog.userId}" class="easyui-validatebox" maxlength="20" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_USER_NAME%>：</th>		
			<td>
			<input id="userName" name="userName" type="text" value="${sysLog.userName}" class="easyui-validatebox" maxlength="20" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_IP_ADDRESS%>：</th>		
			<td>
			<input id="ipAddress" name="ipAddress" type="text" value="${sysLog.ipAddress}" class="easyui-validatebox" maxlength="24" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_LOG_TIME%>：</th>		
			<td>
			<input id="logTimeString" name="logTimeString" class="easyui-datetimebox"  value="<fmt:formatDate value="${sysLog.logTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_MODULE_NAME%>：</th>		
			<td>
			<input id="moduleName" name="moduleName" type="text" value="${sysLog.moduleName}" class="easyui-validatebox" maxlength="255" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_OPERATE_NAME%>：</th>		
			<td>
			<input id="operateName" name="operateName" type="text" value="${sysLog.operateName}" class="easyui-validatebox" maxlength="20" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_EVENT_PARAM%>：</th>		
			<td>
			<input id="eventParam" name="eventParam" type="text" value="${sysLog.eventParam}" class="easyui-validatebox" maxlength="200" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_EVENT_OBJECT%>：</th>		
			<td>
			<input id="eventObject" name="eventObject" type="text" value="${sysLog.eventObject}" class="easyui-validatebox" maxlength="4000" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_EVENT_RESULT%>：</th>		
			<td>
			<input id="eventResult" name="eventResult" type="text" value="${sysLog.eventResult}" class="easyui-validatebox" maxlength="20" />
			</td>
		</tr>	
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="save();" href="javascript:void(0);">保存</a> <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
<%@ include file="/head/init_save.jsp" %>
<script type="text/javascript" charset="UTF-8">
saveParam.saveUrl = '${basePath}manage/sys/sysLog/edit.do';
saveParam.toBack = true;
saveParam.backUrl = '${basePath}manage/sys/sysLog/view.do';
</script>
