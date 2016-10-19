<%@page import="com.fcc.app.sys.model.*" %>
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
			url : '<%=basePath%>manage/sys/sysLock/edit.do',
			success : function(data) {
				try {
					$.messager.progress('close');
					var d = $.parseJSON(data);
					if (d.success) {
						$.messager.show({
							msg : d.msg,
							title : '提示'
						});
						setTimeout(function(){toBack();},3000);
					} else {
						$.messager.alert('错误', d.msg, 'error');
					}
				} catch(e) {
					window.location.href = overUrl;
				}
			},
			onSubmit : function() {
				var isValid = $(this).form('validate');
				if (isValid) {
					$.messager.progress({
						text : '数据处理中，请稍后....'
					});
				}
				return isValid;
			}
		});
	}

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
    <legend>修改<%=SysLock.TABLE_ALIAS%></legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="lockKey" type="hidden" value="${sysLock.lockKey}" />
      <table class="tableForm" align="center">
		<tr>	
			<th><%=SysLock.ALIAS_LOCK_STATUS%>：</th>		
			<td>
			<input id="lockStatus" name="lockStatus" type="text" value="${sysLock.lockStatus}" class="easyui-validatebox" maxlength="10" />
			</td>
		</tr>	
		<tr>	
			<th><%=SysLock.ALIAS_CREATE_TIME%>：</th>		
			<td>
			<input id="createTimeString" name="createTimeString" class="easyui-datetimebox"  value="<fmt:formatDate value="${sysLock.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
