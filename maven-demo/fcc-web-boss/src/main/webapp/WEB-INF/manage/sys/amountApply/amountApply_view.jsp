<%@page import="com.fcc.app.workflow.amountApply.model.*" %>
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
		window.location.href = '<%=basePath%>manage/sys/amountApply/view.do';
	}

</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>查看<%=AmountApply.TABLE_ALIAS%></legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="amountApplyId" type="hidden" value="${amountApply.amountApplyId}" />
      <table class="tableForm" align="center">
		<tr>	
			<th><%=AmountApply.ALIAS_USER_ID%>：</th>		
			<td>${amountApply.userId}</td>
		</tr>	
		<tr>	
			<th><%=AmountApply.ALIAS_USER_NAME%>：</th>		
			<td>${amountApply.userName}</td>
		</tr>	
		<tr>	
			<th><%=AmountApply.ALIAS_INITIATOR_USER_ID%>：</th>		
			<td>${amountApply.initiatorUserId}</td>
		</tr>	
		<tr>	
			<th><%=AmountApply.ALIAS_PRIMARY_AMOUNT%>：</th>		
			<td>${amountApply.primaryAmount}</td>
		</tr>	
		<tr>	
			<th><%=AmountApply.ALIAS_APPLY_REMARK%>：</th>		
			<td>${amountApply.applyRemark}</td>
		</tr>	
		<tr>	
			<th><%=AmountApply.ALIAS_APPLY_VARIFY_REMARK%>：</th>		
			<td>${amountApply.applyVarifyRemark}</td>
		</tr>	
		<tr>	
			<th><%=AmountApply.ALIAS_STATUS%>：</th>		
			<td>${amountApply.status}</td>
		</tr>	
		<tr>	
			<th><%=AmountApply.ALIAS_APPLY_TIME%>：</th>		
			<td><fmt:formatDate value="${amountApply.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
