<%@page import="com.fcc.commons.workflow.amountApply.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
<%@ include file="/WEB-INF/head/workflow.jsp" %>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
    <fieldset>
    <legend>查看任务</legend>
    <form id="userForm" name="userForm" method="post">
      <table class="tableForm" align="center" style="width: 80%">
        <tr>  
            <th>申请人：</th>      
            <td>${requestUserName }</td>
        </tr>
        <jsp:include page="${editTaskPage }"></jsp:include>
        <jsp:include page="../../sys/workflow/processHistory_task_view.jsp">
            <jsp:param value="${param.id }" name="processInstanceId"/>
        </jsp:include>
        <div style="text-align: center;">
        <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a>
        </div>
        <!-- <tr>
          <td colspan="2" align="center">
          <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> 
          </td>
        </tr> -->
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<script type="text/javascript">
saveParam_backUrl = '${basePath}manage/workflow/task/view.do';// 跳转地址
</script>