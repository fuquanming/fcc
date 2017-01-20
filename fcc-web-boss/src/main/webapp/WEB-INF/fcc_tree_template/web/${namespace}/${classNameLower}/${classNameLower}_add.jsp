<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
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
    <legend>新增${table.tableAlias}</legend>
    <form id="userForm" name="userForm" method="post">
      <table class="tableForm" align="center">
        <input name="parentId" type="hidden" value="<@jspEl "parent.nodeId"/>"/>
        <tr>
          <th>上级节点：</th>
          <td colspan="3"><@jspEl "parent.nodeName"/></td>
        </tr>
        <tr>    
            <th><span class="required">*</span>名称：</th>       
            <td>
            <input id="nodeNameStr" name="nodeNameStr" type="text" class="easyui-validatebox easyui-textbox" maxlength="50" required="true" data-options="prompt:'请输入地区名称...'"/>
            </td>
        </tr>   
        <tr>    
            <th><span class="required">*</span>编码：</th>       
            <td>
            <input id="nodeCode" name="nodeCode" type="text" class="easyui-validatebox easyui-textbox" maxlength="50" required="true" data-options="prompt:'请输入地区编码...'"/>
            </td>
        </tr>   
        <tr>    
            <th><span class="required">*</span>排序：</th>       
            <td>
            <input id="nodeSort" name="nodeSort" type="text" class="easyui-validatebox easyui-textbox" maxlength="10" required="true" data-options="prompt:'请输入地区排序...'"/>
            </td>
        </tr>   
        <tr>
          <th><span class="required">*</span>是否显示</th>
          <td>
          <input id="nodeStatus" name="nodeStatus" style="width: 155px;" class="easyui-combobox"/>
          </td>
        </tr>
        <tr>    
            <th>备注：</th>      
            <td>
            <textarea rows="5" cols="40" name="nodeDesc" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 200]"></textarea>
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
<script type="text/javascript" charset="UTF-8">
$(function() {
    var values = '<@jspEl "data.parentId"/>';
    if (values == 'ROOT') values = '';
    var nodeStatus = getComboBoxByData({
        id : 'nodeStatus',
        valueField : 'id',
        textField : 'text',
        data : [
            {text : '显示', id : 'true'},
            {text : '隐藏', id : 'false'}
        ],
        editable : false,
        selectValue : 'true'
    })
})
saveParam_form = 'userForm';// 提交的Form
saveParam_saveUrl = '${jspFileBasePath}/add.do';// 保存URL地址
saveParam_backUrl = '${jspFileBasePath}/view.do';// 跳转地址
saveParam_afterCallback = function(data, success) {
    return false;// 不执行自动跳转
}
</script>