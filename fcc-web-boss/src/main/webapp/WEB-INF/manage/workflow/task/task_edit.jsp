<%@page import="com.fcc.commons.workflow.amountApply.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <legend>办理-${taskInfo.processDefinitionName }</legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="type" type="hidden" value="taskExecute" />
	  <input name="taskId" type="hidden" value="${taskInfo.id }" />
	  <input name="processInstanceId" type="hidden" value="${taskInfo.processInstanceId }" />
	  <input name="taskDefinitionKey" type="hidden" value="${taskInfo.taskDefinitionKey }" />
	  <input name="processDefinitionKey" type="hidden" value="${taskInfo.processDefinitionKey }" />
	  <input name="conditionKey" type="hidden" value="" />
	  <input name="conditionValue" type="hidden" value="" />
      <table class="tableForm" align="center">
        <tr>  
            <th>申请人：</th>      
            <td>${taskInfo.processVariables.requestUserName }</td>
        </tr>
		<jsp:include page="${editTaskPage }"></jsp:include>
		<tr>	
			<th>审批意见：</th>		
			<td>
			<textarea rows="5" cols="40" id="message" name="message" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 500]"></textarea>
			</td>
		</tr>
		<c:forEach items="${commentList}" var="task">
		<tr>	
			<th>${task.name }(${task.assignee })：<%-- <br/><fmt:formatDate value="${task.commentTime}" pattern="yyyy-MM-dd HH:mm:ss"/> --%></th>		
			<td>
			${task.comment }
			</td>
		</tr>
		</c:forEach>
        <tr>
          <td colspan="2" align="center">
          <c:forEach items="${flowList}" var="flow">
          <a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="edit('${flow.conditionKey }', '${flow.conditionValue }');" href="javascript:void(0);">${flow.name }</a> 
          </c:forEach>
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
saveParam_saveUrl = '${basePath}manage/workflow/task/edit.do';// 保存URL地址
saveParam_backUrl = '${basePath}manage/workflow/task/view.do';// 跳转地址
saveParam_afterCallback = function(data, success) {
    return true;// 执行自动跳转
}
function edit(conditionKey, conditionValue) {
	var userForm = $('#userForm').form();
    userForm.find('[name=conditionKey]').val(conditionKey);
    userForm.find('[name=conditionValue]').val(conditionValue);
    save();
}
</script>