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
        <th>帐号</th>
        <td><input name="userId" style="width: 150px;" /></td>
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
        <th>组织机构</th>
        <td ><input id="organId" name="organId" style="width: 150px;" /></td>
        <td colspan="2">
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
        </td>
      </tr>
    </table>
    </form>
    </fieldset>
    <div id="operateDiv"></div>
    <a id="resetPassword_button" class="easyui-linkbutton operate_button" iconCls="icon-edit" onClick="resetPassword();" plain="true" href="javascript:void(0);">重置密码</a>
    <a id="lock_button" class="easyui-linkbutton operate_button" iconCls="icon-lock" onClick="editStatus('${userStatusLock }');" plain="true" href="javascript:void(0);">锁定</a>  
    <a id="unlock_button" class="easyui-linkbutton operate_button" iconCls="icon-unlock" onClick="editStatus('${userStatusActivation }');" plain="true" href="javascript:void(0);">解锁</a>  
  </div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
<script type="text/javascript" charset="UTF-8">
</script>

<%@ include file="/WEB-INF/head/init_combotree.jsp" %>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_operate.jsp" %>
<script type="text/javascript">
$(function(){
	organTree = getComboTree({queryUrl:'${basePath}manage/sys/organ/tree.do',id:'organId',closed:false});
	<fcc:permission operateId="edit">
	$("#resetPassword_button").removeClass('operate-button');
	$('#edit_button').after($('#resetPassword_button'));
	
	$("#lock_button").removeClass('operate-button');
    $('#edit_button').after($('#lock_button'));
    
    $("#unlock_button").removeClass('operate-button');
    $('#edit_button').after($('#unlock_button'));
    </fcc:permission>
})
<fcc:permission operateId="edit">
function resetPassword() {
	gridConfirm({
        gridType : Tool.grid.data,
        userFormId : 'userForm',
        operateUrl : '${basePath}manage/sys/user/resetPassword.do',
        fieldId : 'userId',
        beforeCallback : function(rows) {
        },
        afterCallback : function(data, success) {
        }
    });
}
function editStatus(userStatus) {
	gridConfirm({
        gridType : Tool.grid.data,
        userFormId : 'userForm',
        operateUrl : '${basePath}manage/sys/user/lock.do?userStatus=' + userStatus,
        fieldId : 'userId',
        beforeCallback : function(rows) {
        },
        afterCallback : function(data, success) {
        	if (success == true) searchFun();
        }
    });
}
</fcc:permission>
datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${basePath}manage/sys/user/datagrid.do';// 数据源url
datagridParam_idField = 'userId';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ {
    field : 'userName',
    title : '用户名称',
    width : 100
} , {
    field : 'mobile',
    title : '手机',
    width : 150
} , {
    field : 'email',
    title : 'Email',
    width : 150
} , {
    field : 'regDate',
    title : '注册时间',
    width : 150,
    formatter : function(value, rowData, rowIndex) {
    	return Tool.dateFormat({'value':value, 'format':'yyyy-MM-dd HH:mm:ss'});
    }
} ,{
    field : 'userStatus',
    title : '状态',
    width : 150,
    formatter : function(value, rowData, rowIndex) {
        var temp = value;
        if (value == '${userStatusLock}') {
            temp = '锁定';
        } else {
            temp = '正常'
        }
        return temp;
    }
} , {
    field : 'roleNames',
    title : '角色',
    width : 150
}] ];// 表格的列
datagridParam_queryParamName = ['userId', 'orgId'];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'userId';
operateParam_dataName = 'userName';
operateParam_viewUrl = '${basePath}manage/sys/user/toView.do';
operateParam_addUrl = '${basePath}manage/sys/user/toAdd.do';
operateParam_editUrl = '${basePath}manage/sys/user/toEdit.do';
operateParam_delUrl = '${basePath}manage/sys/user/delete.do';
</script>