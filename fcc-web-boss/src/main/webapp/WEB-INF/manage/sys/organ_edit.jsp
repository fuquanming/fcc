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
<div region="center" border="false" style="overflow: hidden;">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>修改组织机构</legend>
    <form id="userForm" name="userForm" method="post">
      <input name="id" type="hidden" value="${data.nodeId }"/>
      <table class="tableForm" align="center">
        <tr>
          <th>上级机构</th>
          <td><select id="parentId" name="parentId" class="easyui-validatebox" style="width: 155px;"></select>
          <a class="easyui-linkbutton" iconCls="icon-clear" plain="true" onClick="clearOrgan();" href="javascript:void(0);">清空</a>
          </td>
        </tr>
        <tr>
          <th>组织机构名称</th>
          <td><input name="nodeNameStr" type="text" value="${data.nodeName }" class="easyui-validatebox easyui-textbox" data-options="prompt:'请输入组织机构名称...'" required="true" maxlength="100"/></td>
        </tr>
        <tr>
          <th>组织机构编码</th>
          <td><input name="nodeCode" type="text" value="${data.nodeCode }" class="easyui-validatebox easyui-textbox" maxlength="10"/>
          </td>
        </tr>
        <tr>
          <th>组织机构排序</th>
          <td><input name="nodeSort" type="text" value="${data.nodeSort }" class="easyui-validatebox easyui-textbox" data-options="prompt:'请输入组织机构排序...'" required="true" validType="integer" maxlength="5"/>
          </td>
        </tr>
        <tr>
          <th>是否显示</th>
          <td>
          <input id="nodeStatus" name="nodeStatus" style="width: 155px;" class="easyui-combobox"/>
          </td>
        </tr>
        <tr>
          <th>组织机构说明</th>
          <td>
          <textarea rows="5" cols="40" name="nodeDesc" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 200]">${data.nodeDesc }</textarea>
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
<%@ include file="/WEB-INF/head/init_combobox.jsp" %>
<%@ include file="/WEB-INF/head/init_combotree.jsp" %>
<script type="text/javascript">
var organTree;
$(function() {
	var values = '${data.parentId }';
	if (values == 'ROOT') values = '';
	organTree = getComboTree({queryUrl:'manage/sys/organ/tree.do?all=true',id:'parentId',closed:false,selectValue:values});
	//setCombotreeValues(organTree, values);
	
	var nodeStatus = getComboBoxByData({
        id : 'nodeStatus',
        valueField : 'id',
        textField : 'text',
        data : [
            {text : '显示', id : 'true'},
            {text : '隐藏', id : 'false'}
        ],
        editable : false,
        selectValue : '${data.nodeStatus }'
    })
})
function clearOrgan() {
	setCombotreeValues(organTree, '');
}
saveParam_form = 'userForm';
saveParam_saveUrl = '${basePath}manage/sys/organ/edit.do';
saveParam_backUrl = '${basePath}manage/sys/organ/view.do';
saveParam_afterCallback = function(data, success) {
    if (success == false) return false;// 失败，不执行自动跳转
}
</script>