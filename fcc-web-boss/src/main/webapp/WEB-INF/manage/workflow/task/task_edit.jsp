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
      <table class="tableForm" align="center" style="width: 80%">
        <tr>  
            <th width="30%">申请人：</th>      
            <td>${taskInfo.processVariables.requestUserName }</td>
        </tr>
        <jsp:include page="${editTaskPage }"></jsp:include>
        <c:if test="${empty appealMessage }">
        <tr>    
            <th>审核意见：</th>      
            <td>
            <textarea rows="5" cols="40" id="message" name="message" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 500]"></textarea>
            </td>
        </tr>
        <tr>  
            <th>附件：</th>      
            <td>
            <tool:fileUpload linkType="${linkType }" annexType="${annexType }" maxFileTotal="2" ></tool:fileUpload>
            </td>
        </tr>
        </c:if>
        <c:forEach items="${commentList}" var="task">
        <c:if test="${task.id != taskInfo.id  && task.durationInMillis > 2000}">
        <tr>    
            <th>${task.name }(${task.assignee })：<%-- <br/><fmt:formatDate value="${task.commentTime}" pattern="yyyy-MM-dd HH:mm:ss"/> --%></th>        
            <td>
            <table>
            <tr>
                <td>${task.comment }</td>
                <td>
                <c:forEach items="${task.attachmentList}" var="attachment">
                <a href="<tool:fileUrl />${attachment.url }" target="_blank" >${attachment.attachmentName }</a>
                </c:forEach>
                </td>
            </tr>
            </table>
            </td>
        </tr>
        </c:if>
        </c:forEach>
        <tr>
          <td colspan="3" align="center">
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
    return success;// 失败不跳转
}
function edit(conditionKey, conditionValue) {
    var userForm = $('#userForm').form();
    userForm.find('[name=conditionKey]').val(conditionKey);
    userForm.find('[name=conditionValue]').val(conditionValue);
    save();
}
</script>