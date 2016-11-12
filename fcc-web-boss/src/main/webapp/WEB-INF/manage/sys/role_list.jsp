<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;display: none;">
    <fieldset>
    <legend>筛选</legend>
    <form id="userForm" name="userForm" method="post">
  	<input name="ids" type="hidden" value=""/>
    <table class="tableForm">
      <tr>
        <th>角色名称</th>
        <td><input id="searchName" name="searchName" class="easyui-textbox" data-options="prompt:'请输入角色名称...'" style="width: 305px;" />
        </td>
        <c:if test="${not empty userList}">
        <th>创建者</th>
        <td>
        <select name="createUser" id="createUser" style="width: 305px;">
        <option value="">--请选择--</option>
        <c:forEach items="${userList}" var="user">
        <option value="${user.userId }">${user.userId }</option>
        </c:forEach>
        </select>
        </td>
        </c:if>
        <td>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
        </td>
      </tr>
    </table>
    </form>
    </fieldset>
    <div id="operateDiv"></div>
  </div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_operate.jsp" %>
<script type="text/javascript">
datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${basePath}manage/sys/role/datagrid.do';// 数据源url
datagridParam_idField = 'roleId';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ {
    field : 'roleName',
    title : '角色名称',
    width : 50
} , {
    field : 'roleDesc',
    title : '角色描述',
    width : 150
}] ];// 表格的列
datagridParam_queryParamName = ['searchName'];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'roleId';
operateParam_dataName = 'roleName';
operateParam_viewUrl = '${basePath}manage/sys/role/toView.do';
operateParam_addUrl = '${basePath}manage/sys/role/toAdd.do';
operateParam_editUrl = '${basePath}manage/sys/role/toEdit.do';
operateParam_delUrl = '${basePath}manage/sys/role/delete.do';
</script>