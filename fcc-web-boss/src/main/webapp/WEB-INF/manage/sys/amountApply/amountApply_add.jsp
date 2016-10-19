<%@page import="com.fcc.app.workflow.amountApply.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

	function add() {
		userForm.form('submit', {
			url : '<%=basePath%>manage/sys/amountApply/add.do',
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
		window.location.href = '<%=basePath%>manage/sys/amountApply/view.do';
	}

</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>新增<%=AmountApply.TABLE_ALIAS%></legend>
    <form id="userForm" name="userForm" method="post">
      <table class="tableForm" align="center">
		<tr>	
			<th><%=AmountApply.ALIAS_USER_NAME%>：</th>		
			<td>
			<input id="userName" name="userName" type="text" class="easyui-validatebox" maxlength="20" />
			</td>
		</tr>	
		<tr>	
			<th><%=AmountApply.ALIAS_PRIMARY_AMOUNT%>：</th>		
			<td>
			<input id="primaryAmount" name="primaryAmount" type="text" class="easyui-validatebox" maxlength="11" />
			</td>
		</tr>	
		<tr>	
			<th><%=AmountApply.ALIAS_APPLY_REMARK%>：</th>		
			<td>
			<input id="applyRemark" name="applyRemark" type="text" class="easyui-validatebox" maxlength="4000" />
			</td>
		</tr>	
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="add();" href="javascript:void(0);">保存</a> <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
