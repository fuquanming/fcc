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
    <legend>新增角色</legend>
    <form id="userForm" name="userForm" method="post">
    <input name="rightValue" type="hidden" value=""/>
    <table class="tableForm" align="center">
      <tr>
        <th>角色名称</th>
        <td><input name="roleName" class="easyui-validatebox" required="true" maxlength="100"/></td>
      </tr>
      <tr>
        <th>角色描述</th>
        <td><input name="roleDesc" class="easyui-validatebox" required="true" maxlength="100"/></td>
      </tr>
      <tr>
      	<th>模块权限</th>
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
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_tree.jsp" %>
<script type="text/javascript">
var moduleTree;
saveParam_form = 'userForm';
saveParam_saveUrl = '${basePath}manage/sys/role/add.do';
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
    return false;// 不执行自动跳转
}
$(function() {
	moduleTree = getTree({queryUrl:'manage/sys/module/tree.do',id:'moduleTree',closed:true});
})
</script>
