<%@page import="com.fcc.app.workflow.amountApply.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<script type="text/javascript" charset="UTF-8">
	var userForm;
	$(function() {

		userForm = $('#userForm').form();

	});

	function edit(conditionKey, conditionValue) {
		userForm.find('[name=conditionKey]').val(conditionKey);
		userForm.find('[name=conditionValue]').val(conditionValue);
		userForm.form('submit', {
			url : '<%=basePath%>manage/sys/amountApply/workflow/processTask/edit.do',
			success : function(data) {
				try {
					$.messager.progress('close');
					var d = $.parseJSON(data);
					if (d.success) {
						$.messager.show({
							msg : d.msg,
							title : '提示'
						});
						setTimeout(function(){toBack();},3000);
					} else {
						$.messager.alert('错误', d.msg, 'error');
					}
				} catch(e) {
					window.location.href = overUrl;
				}
			},
			onSubmit : function() {
				var isValid = $(this).form('validate');
				if (isValid) {
					$.messager.progress({
						text : '数据处理中，请稍后....'
					});
				}
				return isValid;
			}
		});
	}

	function toBack() {
		window.location.href = '<%=basePath%>manage/sys/amountApply/workflow/processTask/view.do';
	}

</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>审批<%=AmountApply.TABLE_ALIAS%></legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="amountApplyId" type="hidden" value="${amountApply.amountApplyId}" />
	  <input name="status" type="hidden" value="${amountApply.status}" />
	  <input name="type" type="hidden" value="taskExecute" />
	  <input name="requestUserId" type="hidden" value="${amountApply.initiatorUserId }" />
	  <input name="taskId" type="hidden" value="${amountApply.processTaskInfo.id }" />
	  <input name="processInstanceId" type="hidden" value="${amountApply.processTaskInfo.processInstanceId }" />
	  <input name="taskDefinitionKey" type="hidden" value="${amountApply.processTaskInfo.taskDefinitionKey }" />
	  <input name="conditionKey" type="hidden" value="" />
	  <input name="conditionValue" type="hidden" value="" />
	  <input name="readonly" type="hidden" value="${readonly}" />
      <table class="tableForm" align="center">
		<tr>	
			<th><%=AmountApply.ALIAS_USER_NAME%>：</th>		
			<td>
			<c:choose>
			<c:when test="${!readonly}"><input id="userName" name="userName" type="text" value="${amountApply.userName}" class="easyui-validatebox" maxlength="20" /></c:when>
			<c:otherwise>${amountApply.userName}
			<input id="userName" name="userName" type="hidden" value="${amountApply.userName}"/>
			</c:otherwise>
			</c:choose>
			</td>
		</tr>
		<tr>
			<th><%=AmountApply.ALIAS_PRIMARY_AMOUNT%>：</th>		
			<td>
			<c:choose>
			<c:when test="${!readonly}"><input id="primaryAmount" name="primaryAmount" type="text" value="<fmt:formatNumber value="${amountApply.primaryAmount}" pattern=".00"/>" class="easyui-validatebox" maxlength="11" /></c:when>
			<c:otherwise>${amountApply.primaryAmount}
			<input id="primaryAmount" name="primaryAmount" type="hidden" value="${amountApply.primaryAmount}"/>
			</c:otherwise>
			</c:choose>
			</td>
		</tr>	
		<tr>	
			<th><%=AmountApply.ALIAS_APPLY_REMARK%>：</th>		
			<td>
			<c:choose>
			<c:when test="${!readonly}"><input id="applyRemark" name="applyRemark" type="text" value="${amountApply.applyRemark}" class="easyui-validatebox" maxlength="4000" /></c:when>
			<c:otherwise>${amountApply.applyRemark}
			<input id="applyRemark" name="applyRemark" type="hidden" value="${amountApply.applyRemark}"/>
			</c:otherwise>
			</c:choose>
			</td>
		</tr>	
		<tr>	
			<th><%=AmountApply.ALIAS_APPLY_TIME%>：</th>		
			<td>
			<fmt:formatDate value="${amountApply.applyTime}" pattern="yyyy-MM-dd HH:mm:ss" />
			</td>
		</tr>	
		<tr>	
			<th>审批意见：</th>		
			<td>
			<input id="message" name="message" type="text" value="" class="easyui-validatebox" maxlength="100" />
			</td>
		</tr>
		<c:if test="${memberFlg == true}">
		<tr>	
			<th>建议金额：</th>		
			<td>
			<input id="memberAmount" name="memberAmount" type="text" value="" class="easyui-validatebox" maxlength="100" />
			</td>
		</tr>
		</c:if>
		<c:forEach items="${commentList}" var="task">
		<tr>	
			<th>${task.name }(${task.assignee })：</th>		
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
