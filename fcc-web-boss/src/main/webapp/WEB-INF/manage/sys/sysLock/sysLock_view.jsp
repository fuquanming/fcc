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
<script type="text/javascript" charset="UTF-8">

	function toBack() {
		window.location.href = '<%=basePath%>manage/sys/sysLock/view.do';
	}

</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>查看<%=SysLock.TABLE_ALIAS%></legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="lockKey" type="hidden" value="${sysLock.lockKey}" />
      <table class="tableForm" align="center">
		<tr>	
			<th><%=SysLock.ALIAS_LOCK_STATUS%>：</th>		
			<td>${sysLock.lockStatus}</td>
		</tr>	
		<tr>	
			<th><%=SysLock.ALIAS_CREATE_TIME%>：</th>		
			<td><fmt:formatDate value="${sysLock.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
