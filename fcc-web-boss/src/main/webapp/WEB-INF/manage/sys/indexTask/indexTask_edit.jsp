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
	var userForm;
	$(function() {

		userForm = $('#userForm').form();

	});

	function edit() {
		userForm.form('submit', {
			url : '<%=basePath%>manage/sys/indexTask/edit.do',
			success : function(data) {
				try {
					var d = $.parseJSON(data);
					$.messager.show({
						msg : d.msg,
						title : '提示'
					});
					if (d.success) {
						setTimeout(function(){window.location.href = '<%=basePath%>manage/sys/indexTask/view.do';},3000);
					}
				} catch(e) {
					window.location.href = overUrl;
				}
			}
		});
	}

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
    <legend>修改<%=IndexTask.TABLE_ALIAS%></legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="id" type="hidden" value="${indexTask.id}" />
      <table class="tableForm" align="center">
		<tr>	
			<th><span class="required">*</span><%=IndexTask.ALIAS_OPERATION%>：</th>		
			<td>
			<input id="operation" name="operation" type="text" value="${indexTask.operation}" class="easyui-validatebox" maxlength="255" required="true"/>
			</td>
		</tr>	
		<tr>	
			<th><span class="required">*</span><%=IndexTask.ALIAS_DOCUMENT_ID%>：</th>		
			<td>
			<input id="documentId" name="documentId" type="text" value="${indexTask.documentId}" class="easyui-validatebox" maxlength="255" required="true"/>
			</td>
		</tr>	
		<tr>	
			<th><span class="required">*</span><%=IndexTask.ALIAS_DOCUMENT_TYPE%>：</th>		
			<td>
			<input id="documentType" name="documentType" type="text" value="${indexTask.documentType}" class="easyui-validatebox" maxlength="100" required="true"/>
			</td>
		</tr>	
		<tr>	
			<th><span class="required">*</span><%=IndexTask.ALIAS_CREATE_TIME%>：</th>		
			<td>
			<input id="createTimeString" name="createTimeString" class="easyui-datetimebox" required="true" value="<fmt:formatDate value="${indexTask.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			</td>
		</tr>	
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="edit();" href="javascript:void(0);">保存</a> <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
