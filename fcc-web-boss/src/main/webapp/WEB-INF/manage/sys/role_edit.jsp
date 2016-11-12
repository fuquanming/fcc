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
    <legend>修改角色</legend>
    <form id="userForm" name="userForm" method="post">
    <input name="rightValue" type="hidden" value=""/>
    <input name="roleId" type="hidden" value="${sessionScope.sessionRole.roleId }"/>
    <table class="tableForm" align="center">
      <tr>
        <th>角色名称</th>
        <td><input name="roleName" class="easyui-validatebox easyui-textbox" data-options="prompt:'请输入角色名称...'"  required="true" maxlength="100" value="${sessionScope.sessionRole.roleName }"/></td>
      </tr>
      <tr>
        <th>角色描述</th>
        <td>
        <textarea rows="5" cols="40" name="roleDesc" class="easyui-validatebox textbox eaayui-textarea" data-options="prompt:'请输入角色描述...'" required="true" validType="length[1, 200]">${sessionScope.sessionRole.roleDesc }</textarea>
        </td>
      </tr>
      <tr>
      	<th>权    限</th>
      	<td>
      		<ul id="moduleTree" style="margin-top: 5px;"></ul>
      	</td>
      </tr>
      <tr>
        <td colspan="2" align="center">
        <a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="save();" href="javascript:void(0);">保存</a>
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
<%@ include file="/WEB-INF/head/init_tree.jsp" %>
<script type="text/javascript" charset="UTF-8">
var moduleTree;
saveParam_form = 'userForm';
saveParam_saveUrl = '${basePath}manage/sys/role/edit.do';
saveParam_backUrl = '${basePath}manage/sys/role/view.do';
saveParam_beforeCallback = function() {
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
        $('#' + saveParam_form).form().find('[name=rightValue]').val(rightValue);
    }
}
saveParam_afterCallback = function(data, success) {
    if (success == false) return false;// 失败，不执行自动跳转
}
$(function() {
    moduleTree = getTree({queryUrl:'manage/sys/module/tree.do?nodeStatus=open&id=${param.id}',id:'moduleTree',closed:false});
})
</script>
