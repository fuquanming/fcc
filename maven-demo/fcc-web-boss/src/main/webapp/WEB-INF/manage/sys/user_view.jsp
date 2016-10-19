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

	function toBack() {
		window.location.href = '<%=basePath%>manage/sys/user/view.do';
	}

</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>查看用户</legend>
    <form id="userForm" name="userForm" method="post">
      <input name="userId" type="hidden" value="${data.userId }"/>
      <input name="roleValue" type="hidden" value=""/>
      <table class="tableForm" align="center">
        <tr>
          <th>帐号：</th>
          <td>${data.userId }</td>
        </tr>
        <tr>
          <th>姓名：</th>
          <td>${data.userName }</td>
        </tr>
        <tr>
          <th>Email：</th>
          <td>${data.email }</td>
        </tr>
        <tr>
          <th>手机：</th>
          <td>${data.mobile }</td>
        </tr>
        <tr>
          <th>电话：</th>
          <td>${data.tel }</td>
        </tr>
        <tr>
			<th>组织机构：</th>
			<td>${organ.organName }</td>
		</tr>
        <tr>
          <th>备注：</th>
          <td colspan="3">${data.remark }</td>
        </tr>
        <tr>
          <th>角色：</th>
          <td colspan="3">${data.remark }</td>
        </tr>
        <tr>
          <th>角色列表</th>
          <td><select size="10" style="width: 200px;">
              <c:forEach items="${data.roles}" var="role">
                <option value="${role.roleId }">${role.roleName }</option>
              </c:forEach>
            </select>
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