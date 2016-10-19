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
			url : 'manage/sys/module/tree.do?random=' + Math.random() + "&id=${param.id}",
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
			onLoadError : function(e) {
				window.location.href = overUrl;
			}
		});
		
	});

	function edit() {
		var node = moduleTree.tree('getChecked');
		if (node) {
			var rightValue = '';
			var moduleRight = [];
			for (var i in node) {
				var id = node[i].id;
				if (id.indexOf(":") != -1) {
					var moduleId = id.split(":")[0];
					if (moduleRight[moduleId] == null) {
						moduleRight[moduleId] = node[i].attributes.operateValue;
					} else {
						moduleRight[moduleId] = parseInt(parseInt(moduleRight[moduleId]) + parseInt(node[i].attributes.operateValue));
					}
				}
			}
			for (var i in moduleRight) {
				rightValue += i + ":" + moduleRight[i] + ",";
			}
			if (rightValue.length > 0) rightValue = rightValue.substr(0, rightValue.length - 1);
			userForm.find('[name=rightValue]').val(rightValue);
		}
		userForm.form('submit', {
			url : '<%=basePath%>manage/sys/role/edit.do',
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
		window.location.href = '<%=basePath%>manage/sys/role/view.do';
	}
</script>
</head>
<body class="easyui-layout" fit="true">

<div region="center" border="false" style="overflow: auto;">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>修改角色</legend>
    <form id="userForm" name="userForm" method="post">
    <input name="rightValue" type="hidden" value=""/>
    <input name="roleId" type="hidden" value="${sessionScope.sessionRole.roleId }"/>
    <table class="tableForm" align="center">
      <tr>
        <th>角色名称</th>
        <td><input name="roleName" class="easyui-validatebox" required="true" maxlength="100" value="${sessionScope.sessionRole.roleName }"/></td>
      </tr>
      <tr>
        <th>角色描述</th>
        <td><input name="roleDesc" class="easyui-validatebox" required="true" maxlength="100" value="${sessionScope.sessionRole.roleDesc }"/></td>
      </tr>
      <tr>
      	<th>权    限</th>
      	<td>
      		<ul id="moduleTree" style="margin-top: 5px;"></ul>
      	</td>
      </tr>
      <tr>
        <td colspan="2" align="center">
        <a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="edit();" href="javascript:void(0);">保存</a>
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
