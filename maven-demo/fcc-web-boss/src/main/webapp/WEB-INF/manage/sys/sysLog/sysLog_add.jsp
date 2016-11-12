<%@page import="com.fcc.web.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>新增<%=SysLog.TABLE_ALIAS%></legend>
    <form id="userForm" name="userForm" method="post">
      <table class="tableForm" align="center">
		<tr>	
			<th><%=SysLog.ALIAS_USER_ID%>：</th>		
			<td>
			<input id="userId" name="userId" type="text" class="easyui-validatebox easyui-textbox" data-options="prompt:'请输入用户账号...'" required="true" maxlength="20" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_USER_NAME%>：</th>		
			<td>
			<input id="userName" name="userName" type="text" class="easyui-validatebox" maxlength="20" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_IP_ADDRESS%>：</th>		
			<td>
			<input id="ipAddress" name="ipAddress" type="text" class="easyui-validatebox" maxlength="24" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_LOG_TIME%>：</th>		
			<td>
			<input id="logTimeString" name="logTimeString" class="easyui-datetimebox" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_MODULE_NAME%>：</th>		
			<td>
			<input id="moduleName" name="moduleName" type="text" class="easyui-validatebox" maxlength="255" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_OPERATE_NAME%>：</th>		
			<td>
			<input id="operateName" name="operateName" type="text" class="easyui-validatebox" maxlength="20" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_EVENT_PARAM%>：</th>		
			<td>
			<input id="eventParam" name="eventParam" type="text" class="easyui-validatebox" maxlength="200" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_EVENT_OBJECT%>：</th>		
			<td>
			<input id="eventObject" name="eventObject" type="text" class="easyui-validatebox" maxlength="4000" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLog.ALIAS_EVENT_RESULT%>：</th>		
			<td>
			<input id="eventResult" name="eventResult" type="text" class="easyui-validatebox" maxlength="20" />
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
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<script type="text/javascript" charset="UTF-8">
saveParam_form = 'userForm';// 提交的Form
saveParam_saveUrl = '${basePath}manage/sys/sysLog/add.do';// 保存URL地址
saveParam_backUrl = '${basePath}manage/sys/sysLog/view.do';// 跳转地址
saveParam_afterCallback = function(data, success) {
	return false;// 不执行自动跳转
}
</script>
</body>
</html>
