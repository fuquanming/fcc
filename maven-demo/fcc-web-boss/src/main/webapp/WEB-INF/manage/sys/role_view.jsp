<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<script type="text/javascript" charset="UTF-8">
	var userForm;
	var moduleTree;
	$(function() {

		userForm = $('#userForm').form();

		moduleTree = $('#moduleTree').tree({
			checkbox: true,
			url : 'manage/sys/module/tree.do?random=' + Math.random(),
			animate : true,
			lines : !Tool.isLessThanIe8(),
			onClick : function(node) {
			},
			onLoadSuccess : function(node, data) {
				var t = $(this);
				if (data) {
					$(data).each(function(index, d) {
						if (this.state == 'closed') {
							t.tree('expandAll');
						}
					});
					if (data[0] && data[0].msg && data[0].msg != '') {
						$.messager.show({
							msg : '数据库异常！',
							title : '提示'
						});
						return;
					}
				}
			},
			onLoadError : function() {
				window.location.href = overUrl;
			}
		});
		
	});

	
	function toBack() {
		window.location.href = '<%=basePath%>manage/sys/role/view.do';
	}
</script>
</head>
<body class="easyui-layout" fit="true">

<div region="center" border="false" style="overflow: auto;">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>查看角色</legend>
    <form id="userForm" name="userForm" method="post">
    <input name="rightValue" type="hidden" value=""/>
    <input name="roleId" type="hidden" value="${sessionScope.sessionRole.roleId }"/>
    <table class="tableForm" align="center">
      <tr>
        <th>角色名称</th>
        <td><span id="roleName">${sessionScope.sessionRole.roleName }</span></td>
      </tr>
      <tr>
        <th>角色描述</th>
        <td><span id="roleDesc">${sessionScope.sessionRole.roleDesc }</span></td>
      </tr>
      <tr>
      	<th>权    限</th>
      	<td>
      		<ul id="moduleTree" style="margin-top: 5px;"></ul>
      	</td>
      </tr>
      <tr>
        <td colspan="2" align="center">
        <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a>
        </td>
      </tr>
    </table>
    </form>
    </fieldset>
  <table id="datagrid">
  </table>
</div>

</body>
</html>
