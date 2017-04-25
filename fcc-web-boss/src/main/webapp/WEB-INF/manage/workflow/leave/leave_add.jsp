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
    <legend>新增Leave</legend>
    <form id="userForm" name="userForm" method="post">
      <input type="hidden" id="operate" name="operate" value="save"/>
      <table class="tableForm" align="center">
		<tr>	
			<th>开始时间：</th>		
			<td>
			<input id="startTimeString" name="startTimeString" class="easyui-datetimebox" />
			</td>
		</tr>	
		<tr>	
			<th>结束时间：</th>		
			<td>
			<input id="endTimeString" name="endTimeString" class="easyui-datetimebox" />
			</td>
		</tr>	
		<tr>	
			<th>内容：</th>		
			<td>
            <textarea rows="5" cols="40" id="content" name="content" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 500]"></textarea>
			</td>
		</tr>	
        <tr>
          <td colspan="2" align="center">
          <a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="start('save');save();" href="javascript:void(0);">保存</a>
          <a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="start('start');save();" href="javascript:void(0);">保存并提交</a> 
          <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<script type="text/javascript" charset="UTF-8">
saveParam_form = 'userForm';// 提交的Form
saveParam_saveUrl = 'manage/workflow/leave/add.do';// 保存URL地址
saveParam_backUrl = 'manage/workflow/leave/view.do';// 跳转地址
saveParam_afterCallback = function(data, success) {
    return false;// 不执行自动跳转
}
function start(start) {
	$('#operate').val(start);
}
</script>