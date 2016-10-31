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
    <legend>资料修改</legend>
    <form id="userForm" name="userForm" method="post">
      <input name="userId" type="hidden" value="${data.userId }"/>
      <table class="tableForm" align="center">
        <tr>
          <th>帐号</th>
          <td>${data.userId }</td>
        </tr>
        <tr>
          <th>姓名</th>
          <td><input name="userName" type="text" value="${data.userName }" class="easyui-validatebox" required="true" maxlength="20"/></td>
        </tr>
        <tr>
          <th>Email</th>
          <td><input name="email" type="text" value="${data.email }" maxlength="100" validType="email" required="true"/></td>
        </tr>
        <tr>
          <th>手机</th>
          <td><input name="mobile" type="text" value="${data.mobile }" class="easyui-validatebox" validType="mobile" required="true" />
          </td>
        </tr>
        <tr>
          <th>电话</th>
          <td><input name="tel" type="text" value="${data.tel }" class="easyui-validatebox" validType="telephone" />
          </td>
        </tr>
        <tr>
          <th>备注</th>
          <td colspan="3"><textarea rows="5" cols="40" name="remark" class="easyui-validatebox" validType="length[0, 200]">${data.remark }</textarea>
          </td>
        </tr>
        <fcc:permission operateId="edit">
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="save();" href="javascript:void(0);">保存</a> </td>
        </tr>
        </fcc:permission>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
<%@ include file="/head/init_save.jsp" %>
<script type="text/javascript" charset="UTF-8">
saveParam.saveUrl = '${basePath}manage/sys/userInfo/edit.do';
saveParam.toBack = false;
</script>
