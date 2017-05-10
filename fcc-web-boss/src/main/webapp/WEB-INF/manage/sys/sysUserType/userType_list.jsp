<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;display: none;">
    <fieldset>
    <legend>筛选</legend>
    <form id="userForm" name="userForm" method="post">
  	<input name="ids" type="hidden" value=""/>
    <table class="tableForm">
      <tr>
        <th>任务类型</th>    
        <td colspan="2">
            <input id="userType" name="userType" style="width: 155px;" class="easyui-combobox"/>
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
<%@ include file="/WEB-INF/head/init_combotree.jsp" %>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_operate.jsp" %>
<%@ include file="/WEB-INF/head/init_combobox.jsp" %>
<script type="text/javascript">
$(function(){
    <c:if test="${not empty userTypeMap}">
    getComboBoxByData({
    	id : 'userType',
    	valueField : 'id',
        textField : 'text',
        data : [
        	{text : '--请选择--', id : ''},
            <c:forEach items="${userTypeMap }" var="key" varStatus="stat">
            {text : '${key.value }', id : '${key.key }'}<c:if test="${!stat.last}" >,</c:if>
            </c:forEach>
        ]
    })
    </c:if>
})
datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${basePath}manage/sys/userType/datagrid.do';// 数据源url
datagridParam_idField = 'userType';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ {
    field : 'userType',
    title : '用户类型',
    width : 100
} , {
    field : 'userTypeName',
    title : '用户类型名称',
    width : 100
} , {
    field : 'roleNames',
    title : '角色',
    width : 150
}] ];// 表格的列
datagridParam_queryParamName = ['userType'];
datagridParam_page = false;

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'userType';
operateParam_dataName = 'userTypeName';
operateParam_editUrl = '${basePath}manage/sys/userType/toEdit.do';
operateParam_delUrl = '${basePath}manage/sys/userType/delete.do';
</script>