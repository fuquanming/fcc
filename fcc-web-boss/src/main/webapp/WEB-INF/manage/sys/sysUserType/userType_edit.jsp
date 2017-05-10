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
    <legend>修改用户类型角色</legend>
    <form id="userForm" name="userForm" method="post">
      <input name="userType" type="hidden" value="${data.userType }"/>
      <input name="roleValue" type="hidden" value=""/>
      <table class="tableForm" align="center">
        <tr>
          <th>用户类型</th>
          <td>${data.userType }</td>
        </tr>
        <tr>
          <th>用户类型名称</th>
          <td>${data.userTypeName }</td>
        </tr>
        <tr>
          <th></th>
          <td>可选角色</td>
          <td></td>
          <td align="left">已选角色</td>
        </tr>
        <tr>
          <th>角色列表</th>
          <td><select id="unSelectRole" name="unSelectRole" multiple="multiple" size="10" style="width: 200px;" class="textbox">
              <c:forEach items="${roleList}" var="role">
                <option value="${role.roleId }">${role.roleName }</option>
              </c:forEach>
            </select>
          </td>
          <td>
            <a class="easyui-linkbutton" plain="true" style="width: 35px; text-align: center; border: 1px solid #D0D0BF;" onClick="Tool.removeSelect({'sourceId':'unSelectRole','targetId':'selecetRole'})" href="javascript:void(0);"> > </a>
            <br/>
            <a class="easyui-linkbutton" plain="true" style="width: 35px; text-align: center; border: 1px solid #D0D0BF;" onClick="Tool.removeSelect({'sourceId':'selecetRole','targetId':'unSelectRole'})" href="javascript:void(0);"> < </a>
            <br/>
            <a class="easyui-linkbutton" plain="true" style="width: 35px; text-align: center; border: 1px solid #D0D0BF;" onClick="Tool.removeSelect({'sourceId':'unSelectRole','targetId':'selecetRole','isAll':true})" href="javascript:void(0);"> >> </a>
            <br/>
            <a class="easyui-linkbutton" plain="true" style="width: 35px; text-align: center; border: 1px solid #D0D0BF;" onClick="Tool.removeSelect({'sourceId':'selecetRole','targetId':'unSelectRole','isAll':true})" href="javascript:void(0);"> << </a>
          </td>
          <td><select id="selecetRole" name="selecetRole" multiple="multiple" size="10" style="width: 200px;" class="textbox">
            </select>
          </td>
        </tr>
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="save();" href="javascript:void(0);">保存</a> <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_combotree.jsp" %>
<script type="text/javascript" charset="UTF-8">
var organTree;
saveParam_form = 'userForm';
saveParam_saveUrl = '${basePath}manage/sys/userType/edit.do';
saveParam_backUrl = '${basePath}manage/sys/userType/view.do';
saveParam_beforeCallback = function() {
    var roleIds = [];
    $('#selecetRole').children().each(function(){
        roleIds.push($(this).val());
    });
    var idsVal = roleIds.join(',');
    $('#' + saveParam_form).form().find('[name=roleValue]').val(idsVal);
}
saveParam_afterCallback = function(data, success) {
    if (success == false) return false;// 失败，不执行自动跳转
}
$(function() {
	// 显示已有的操作
    var roleIdMap = [];
    <c:forEach items="${data.roles}" var="role">
        roleIdMap['${role.roleId}'] = '${role.roleId}';
    </c:forEach>
    
    $('#unSelectRole').children().each(function(){
        if (roleIdMap[$(this).val()]) {
            $(this).attr("selected", "selected");
        } 
    });
    Tool.removeSelect({'sourceId':'unSelectRole','targetId':'selecetRole'})
})
</script>
