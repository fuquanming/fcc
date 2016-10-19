<%@page import="com.fcc.app.lucene.model.*" %>
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
		window.location.href = '<%=basePath%>manage/sys/indexTask/view.do';
	}

</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>查看<%=IndexTask.TABLE_ALIAS%></legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="id" type="hidden" value="${indexTask.id}" />
      <table class="tableForm" align="center">
		<tr>	
			<th><%=IndexTask.ALIAS_OPERATION%>：</th>		
			<td>${indexTask.operation}</td>
		</tr>	
		<tr>	
			<th><%=IndexTask.ALIAS_DOCUMENT_ID%>：</th>		
			<td>${indexTask.documentId}</td>
		</tr>	
		<tr>	
			<th><%=IndexTask.ALIAS_DOCUMENT_TYPE%>：</th>		
			<td>${indexTask.documentType}</td>
		</tr>	
		<tr>	
			<th><%=IndexTask.ALIAS_CREATE_TIME%>：</th>		
			<td><fmt:formatDate value="${indexTask.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
