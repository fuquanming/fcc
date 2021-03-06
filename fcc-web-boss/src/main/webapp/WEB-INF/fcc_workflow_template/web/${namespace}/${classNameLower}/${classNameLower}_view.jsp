<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;display: none;">
    <fieldset>
    <legend>查看请假</legend>
    <form id="userForm" name="userForm" method="post">
      <table class="tableForm" align="center">
        <tr>  
            <th>申请人：</th>      
            <td><@jspEl "requestUserName" /></td>
        </tr>
        <jsp:include page="<@jspEl "editTaskPage" />"></jsp:include>
        <tr>
          <td colspan="2" align="center">
          <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> 
          </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
  <jsp:include page="../../sys/workflow/processHistory_task_view.jsp">
    <jsp:param value="<@jspEl "data.processInstanceId" />" name="processInstanceId"/>
  </jsp:include>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<script type="text/javascript">
saveParam_backUrl = '${jspFileBasePath}/view.do';// 跳转地址
</script>