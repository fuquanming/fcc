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
    <legend>新增用户</legend>
    <form id="userForm" name="userForm" method="post">
      <input name="roleValue" type="hidden" value=""/>
      <table class="tableForm" align="center">
        <tr>
          <th>帐号</th>
          <td><input name="userId" type="text" class="easyui-validatebox" required="true" maxlength="20"/></td>
        </tr>
        <tr>
          <th>姓名</th>
          <td><input name="userName" type="text" class="easyui-validatebox" required="true" maxlength="20"/></td>
        </tr>
        <tr>
          <th>Email</th>
          <td><input name="email" type="text" class="easyui-validatebox" validType="email" maxlength="100" required="true"/></td>
        </tr>
        <tr>
          <th>手机</th>
          <td><input name="mobile" type="text" class="easyui-validatebox" validType="mobile" required="true" />
          </td>
        </tr>
        <tr>
          <th>电话</th>
          <td><input name="tel" type="text" class="easyui-validatebox" validType="telephone" maxlength="20"/>
          </td>
        </tr>
        <tr>
			<th>组织机构</th>
			<td><select id="organId" name="organId" style="width: 200px;"></select></td>
		</tr>
        <tr>
          <th>备注</th>
          <td colspan="3"><textarea rows="5" cols="40" name="remark" class="easyui-validatebox" validType="length[0, 200]"></textarea>
          </td>
        </tr>
        <tr>
          <th></th>
          <td>可选角色</td>
          <td></td>
          <td align="left">已选角色</td>
        </tr>
        <tr>
          <th>角色列表</th>
          <td><select id="unSelectRole" name="unSelectRole" multiple="multiple" size="10" style="width: 200px;">
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
          <td><select id="selecetRole" name="selecetRole" multiple="multiple" size="10" style="width: 200px;">
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
saveParam_saveUrl = '${basePath}manage/sys/user/add.do';
saveParam_backUrl = '${basePath}manage/sys/user/view.do';
saveParam_beforeCallback = function() {
	// 获取角色
    var roleIds = [];
    $('#selecetRole').children().each(function(){
        roleIds.push($(this).val());
    });
    var idsVal = roleIds.join(',');
    $('#' + saveParam_form).form().find('[name=roleValue]').val(idsVal);
}
saveParam_afterCallback = function(data, success) {
    return false;// 不执行自动跳转
}
$(function() {
	organTree = getComboTree({queryUrl:'manage/sys/organ/tree.do',id:'organId',closed:false});
})
</script>
