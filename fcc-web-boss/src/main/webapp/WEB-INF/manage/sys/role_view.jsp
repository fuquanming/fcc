<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
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
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<script type="text/javascript" src="js/support/init_tree.js"></script>
<script type="text/javascript" charset="UTF-8">
var moduleTree;
saveParam.backUrl = '${basePath}manage/sys/role/view.do';
$(function() {
	moduleTree = getTree({queryUrl:'manage/sys/module/tree.do',id:'moduleTree',closed:false});
});
</script>