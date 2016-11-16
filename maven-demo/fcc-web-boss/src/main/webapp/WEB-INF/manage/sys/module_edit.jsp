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
    <legend>修改模块</legend>
    <form id="userForm" name="userForm" method="post">
      <input name="id" type="hidden" value="${data.moduleId }"/>
      <input name="operateValue" type="hidden" value=""/>
      <table class="tableForm" align="center">
        <tr>
          <th>上级模块</th>
          <td><select id="parentId" name="parentId" class="easyui-validatebox" style="width: 200px;"></select>
          <a class="easyui-linkbutton" iconCls="icon-clear" plain="true" onClick="clearModule();" href="javascript:void(0);">清空</a>
          </td>
        </tr>
        <tr>
          <th>模块名称</th>
          <td colspan="3"><input name="moduleName" type="text" value="${data.moduleName }" class="easyui-validatebox easyui-textbox" data-options="prompt:'请输入模块名称...'" required="true" maxlength="100" style="width: 350px;"/></td>
        </tr>
        <tr>
          <th>模块地址</th>
          <td colspan="3"><input name="moduleDesc" type="text" value="${data.moduleDesc }" class="easyui-textbox" maxlength="100" style="width: 350px;"/></td>
        </tr>
        <tr>
          <th>模块排序</th>
          <td colspan="3"><input name="moduleSort" type="text" value="${data.moduleSort }" class="easyui-validatebox easyui-textbox" data-options="prompt:'请输入模块排序...'" required="true" validType="integer" style="width: 350px;"/>
          </td>
        </tr>
        <tr>
          <th></th>
          <td>可选操作</td>
          <td></td>
          <td align="left">已选操作</td>
        </tr>
        <tr>
          <th>模块操作</th>
          <td><select id="unSelectOperate" name="unSelectOperate" multiple="multiple" size="10" style="width: 150px;" class="textbox">
              <c:forEach items="${operateList}" var="operate">
                <option value="${operate.operateId }">${operate.operateName }</option>
              </c:forEach>
            </select>
          </td>
          <td>
          <a class="easyui-linkbutton" plain="true" style="width: 35px; text-align: center; border: 1px solid #D0D0BF;" onClick="Tool.removeSelect({'sourceId':'unSelectOperate','targetId':'selecetOperate'});" href="javascript:void(0);"> > </a>
            <br/>
            <a class="easyui-linkbutton" plain="true" style="width: 35px; text-align: center; border: 1px solid #D0D0BF;" onClick="Tool.removeSelect({'sourceId':'selecetOperate','targetId':'unSelectOperate'})" href="javascript:void(0);"> < </a>
            <br/>
            <a class="easyui-linkbutton" plain="true" style="width: 35px; text-align: center; border: 1px solid #D0D0BF;" onClick="Tool.removeSelect({'sourceId':'unSelectOperate','targetId':'selecetOperate','isAll':true})" href="javascript:void(0);"> >> </a>
            <br/>
            <a class="easyui-linkbutton" plain="true" style="width: 35px; text-align: center; border: 1px solid #D0D0BF;" onClick="Tool.removeSelect({'sourceId':'selecetOperate','targetId':'unSelectOperate','isAll':true})" href="javascript:void(0);"> << </a>
          </td>
          <td><select id="selecetOperate" name="selecetOperate" multiple="multiple" size="10" style="width: 150px;" class="textbox">
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
var moduleTree;
$(function() {
    var values = '${data.parentId }'
    moduleTree = getComboTree({queryUrl:'manage/sys/module/tree.do',id:'parentId',closed:false});
    setCombotreeValues(moduleTree, values);
})
function clearModule() {
    setCombotreeValues(moduleTree, '');
}
saveParam_form = 'userForm';
saveParam_saveUrl = '${basePath}manage/sys/module/edit.do';
saveParam_backUrl = '${basePath}manage/sys/module/view.do';
saveParam_beforeCallback = function() {
	var operateIds = [];
    $('#selecetOperate').children().each(function(){
        operateIds.push($(this).val());
    });
    var idsVal = operateIds.join(',');
    $('#' + saveParam_form).form().find('[name=operateValue]').val(idsVal);
}
saveParam_afterCallback = function(data, success) {
    if (success == false) return false;// 失败，不执行自动跳转
}
$(function() {
	var operateIdMap = [];
    <c:forEach items="${data.operates}" var="operate">
        operateIdMap['${operate.operateId}'] = '${operate.operateId}';
    </c:forEach>
    // 显示已有的操作
    $('#unSelectOperate').children().each(function(){
        if (operateIdMap[$(this).val()]) {
            $(this).attr("selected", "selected");
        } 
    });
    Tool.removeSelect({'sourceId':'unSelectOperate','targetId':'selecetOperate'})
})
</script>