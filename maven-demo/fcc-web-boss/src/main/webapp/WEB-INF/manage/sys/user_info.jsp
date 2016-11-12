<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
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
          <td><input name="userName" type="text" value="${data.userName }" class="easyui-validatebox easyui-textbox" data-options="prompt:'请输入姓名...'" required="true" maxlength="20"/></td>
        </tr>
        <tr>
          <th>Email</th>
          <td><input name="email" type="text" value="${data.email }" class="easyui-validatebox easyui-textbox" data-options="prompt:'请输入邮箱...'"maxlength="100" validType="email" required="true"/></td>
        </tr>
        <tr>
          <th>手机</th>
          <td><input name="mobile" type="text" value="${data.mobile }" class="easyui-validatebox easyui-textbox" data-options="prompt:'请输入手机...'" validType="mobile" required="true" />
          </td>
        </tr>
        <tr>
          <th>电话</th>
          <td><input name="tel" type="text" value="${data.tel }" class="easyui-validatebox easyui-textbox" validType="telephone" />
          </td>
        </tr>
        <tr>
          <th>备注</th>
          <td colspan="3">
          <textarea rows="5" cols="40" name="remark" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 200]">${data.remark }</textarea>
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
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<script type="text/javascript" charset="UTF-8">
saveParam_form = 'userForm';
saveParam_saveUrl = '${basePath}manage/sys/userInfo/edit.do';
</script>
