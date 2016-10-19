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
		// 获取模块操作
		userForm.form('submit', {
			url : '<%=basePath%>manage/sys/sysType/add.do',
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
		window.location.href = '<%=basePath%>manage/sys/sysType/view.do';
	}
</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>新增数据类型</legend>
    <form id="userForm" name="userForm" method="post">
      <input name="id" type="hidden" value=""/>
      <table class="tableForm" align="center">
        <input name="parentId" type="hidden" value="${parent.typeId }"/>
        <tr>
          <th>上级数据类型</th>
          <td colspan="3">${parent.typeName }</td>
        </tr>
        <tr>
          <th>数据类型名称</th>
          <td><input name="typeName" type="text" class="easyui-validatebox" required="true" maxlength="100"/></td>
        </tr>
        <tr>
          <th>数据类型排序</th>
          <td><input name="typeSort" type="text" class="easyui-validatebox" required="true" />
          </td>
        </tr>
        <tr>
          <th>数据类型说明</th>
          <td><input name="typeDesc" type="text" maxlength="100"/></td>
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
