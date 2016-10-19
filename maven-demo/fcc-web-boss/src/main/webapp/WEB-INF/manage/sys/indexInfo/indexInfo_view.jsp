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
		window.location.href = '<%=basePath%>manage/sys/indexInfo/view.do';
	}

</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>查看<%=IndexInfo.TABLE_ALIAS%></legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="infoId" type="hidden" value="${indexInfo.infoId}" />
      <table class="tableForm" align="center">
		<tr>	
			<th><%=IndexInfo.ALIAS_INFO_KEY%>：</th>		
			<td>${indexInfo.infoKey}</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_INDEX_DIR%>：</th>		
			<td>${indexInfo.indexDir}</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_DOCUMENT_TYPE%>：</th>		
			<td>${indexInfo.documentType}</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_CLASS_NAME%>：</th>		
			<td>${indexInfo.className}</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_ID_TYPE%>：</th>		
			<td>${indexInfo.idType}</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_METHOD_NAMES%>：</th>		
			<td>${indexInfo.methodNames}</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_IS_TOKENIZEDS%>：</th>		
			<td>${indexInfo.isTokenizeds}</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_IS_BACKUP_FIELDS%>：</th>		
			<td>${indexInfo.isBackupFields}</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_FIELD_NAMES%>：</th>		
			<td>${indexInfo.fieldNames}</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_DEL_FIELD_NAME%>：</th>		
			<td>${indexInfo.delFieldName}</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_ANALYZER_NAME%>：</th>		
			<td>${indexInfo.analyzerName}</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_CREATE_TIME%>：</th>		
			<td><fmt:formatDate value="${indexInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
