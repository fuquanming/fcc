<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>修改密码</legend>
    <form id="userForm" name="userForm" method="post">
      <input name="roleValue" type="hidden" value=""/>
      <table class="tableForm" align="center">
        <tr>
          <th>旧密码</th>
          <td><input name="oldPassword" type="password" class="easyui-validatebox" required="true" maxlength="20"/></td>
        </tr>
        <tr>
          <th>新密码</th>
          <td><input name="newPassword" type="password" class="easyui-validatebox" required="true" maxlength="20"/></td>
        </tr>
        <tr>
          <th>确认新密码</th>
          <td><input name="confirmPassword" type="password" class="easyui-validatebox" required="true" validType="eqPassword['#userForm input[name=newPassword]']" maxlength="20" /></td>
        </tr>
        <fcc:permission operateId="edit">
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="save();" href="javascript:void(0);">保存</a> </td>
        </tr>
        </fcc:permission>
        <tr>
          <td colspan="2" align="center"><span id="message"></span> </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
<%@ include file="/head/init_save.jsp" %>
<script type="text/javascript" charset="UTF-8">
saveParam.saveUrl = '${basePath}manage/sys/userPassword/edit.do';
saveParam.toBack = false;
</script>
