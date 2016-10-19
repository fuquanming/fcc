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
	
	function edit() {
		
		userForm.form('submit', {
			url : '<%=basePath%>manage/sys/organ/edit.do',
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
		window.location.href = '<%=basePath%>manage/sys/organ/view.do';
	}
</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>修改组织机构</legend>
    <form id="userForm" name="userForm" method="post">
      <input name="id" type="hidden" value="${data.organId }"/>
      <table class="tableForm" align="center">
        <tr>
          <th>组织机构名称</th>
          <td><input name="organName" type="text" value="${data.organName }" class="easyui-validatebox" required="true" maxlength="100"/></td>
        </tr>
        <tr>
          <th>组织机构排序</th>
          <td><input name="organSort" type="text" value="${data.organSort }" class="easyui-validatebox" required="true" validType="integer" maxlength="5"/>
          </td>
        </tr>
        <tr>
          <th>组织机构说明</th>
          <td><input name="organDesc" type="text" value="${data.organDesc }" maxlength="100"/></td>
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
