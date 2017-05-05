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
    <legend>新增AmountApply</legend>
    <form id="userForm" name="userForm" method="post">
      <input type="hidden" id="operate" name="operate" value="save"/>
      <table class="tableForm" align="center">
		<tr>	
			<th>借款人ID（会员）：</th>		
			<td>
			<input id="userId" name="userId" type="text" class="easyui-validatebox easyui-textbox" maxlength="19" />
			</td>
		</tr>	
		<tr>	
			<th>会员账号名称：</th>		
			<td>
			<input id="userName" name="userName" type="text" class="easyui-validatebox easyui-textbox" maxlength="20" />
			</td>
		</tr>	
		<tr>	
			<th>发起人申请的额度：</th>		
			<td>
			<input id="primaryAmount" name="primaryAmount" type="text" class="easyui-validatebox easyui-textbox" maxlength="11" />
			</td>
		</tr>	
		<tr>	
			<th>申请备注：</th>		
			<td>
            <textarea rows="5" cols="40" id="applyRemark" name="applyRemark" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 4000]"></textarea>
			</td>
		</tr>	
		<tr>	
			<th>申请时间：</th>		
			<td>
			<input id="applyTimeString" name="applyTimeString" class="easyui-datetimebox" />
			</td>
		</tr>	
        <tr>
          <td colspan="2" align="center">
          <a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="start('save');save();" href="javascript:void(0);">保存</a>
          <a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="start('start');save();" href="javascript:void(0);">保存并提交</a> 
          <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a>
          </td>
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
saveParam_saveUrl = 'manage/workflow/amountApply/add.do';// 保存URL地址
saveParam_backUrl = 'manage/workflow/amountApply/view.do';// 跳转地址
saveParam_afterCallback = function(data, success) {
    return false;// 不执行自动跳转
}
function start(start) {
    $('#operate').val(start);
}
</script>