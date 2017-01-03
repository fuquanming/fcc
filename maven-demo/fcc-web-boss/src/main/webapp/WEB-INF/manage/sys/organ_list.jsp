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
<div region="center" border="false" style="overflow: hidden;">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;display: none;">
	<form id="userForm" name="userForm" method="post">
	  <input name="ids" type="hidden" value=""/>
	</form>
  <div id="operateDiv"></div>
  <a id="expandAll_button" class="easyui-linkbutton operate_button" iconCls="icon-folder-open" onClick="gridExpandAll()" plain="true" href="javascript:void(0);">展开</a>
  <a id="collapseAll_button" class="easyui-linkbutton operate_button" iconCls="icon-folder-close" onClick="gridCollapseAll();" plain="true" href="javascript:void(0);">折叠</a>
  <a id="show_button" class="easyui-linkbutton operate_button" iconCls="icon-edit" onClick="editStatus('show');" plain="true" href="javascript:void(0);">显示</a>  
  <a id="unShow_button" class="easyui-linkbutton operate_button" iconCls="icon-edit" onClick="editStatus('unShow');" plain="true" href="javascript:void(0);">隐藏</a>  
  </div>
  <table id="treegrid">
  </table>
</div>

</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_treegrid.jsp" %>
<%@ include file="/WEB-INF/head/init_operate.jsp" %>
<script type="text/javascript">
$(function() {
	$("#expandAll_button").removeClass('operate-button');
    $('#refresh_button').after($('#expandAll_button'));
    
    $("#collapseAll_button").removeClass('operate-button');
    $('#refresh_button').after($('#collapseAll_button'));
    
    <fcc:permission operateId="edit">
    $("#show_button").removeClass('operate-button');
    $('#edit_button').after($('#show_button'));
    
    $("#unShow_button").removeClass('operate-button');
    $('#edit_button').after($('#unShow_button'));
    </fcc:permission>
})

<fcc:permission operateId="edit">
function editStatus(organStatus) {
    gridConfirm({
        gridType : Tool.grid.data,
        userFormId : 'userForm',
        operateUrl : '${basePath}manage/sys/organ/show.do?organStatus=' + organStatus,
        fieldId : 'id',
        beforeCallback : function(rows) {
        },
        afterCallback : function(data, success) {
            if (success == true) searchFun();
        }
    });
}
</fcc:permission>

treegridParam_id = 'treegrid';// 用到的datagrid的ID
treegridParam_url = '${basePath}manage/sys/organ/treegrid.do';// 数据源url
treegridParam_idField = 'id';// datagrid表格的唯一标识
treegridParam_idField_checkbox = true;// 是否显示多选框
treegridParam_column_value = [ [ {
    field : 'text',
    title : '组织机构名称',
    width : 100
},{
    field : 'organDesc',
    title : '组织机构说明',
    width : 200
}, {
    field : 'organSort',
    title : '排序',
    width : 50
}, {
    field : 'show',
    title : '是否显示',
    width : 50,
    formatter : function(value, rowData, rowIndex) {
        if (rowData.attributes) {
            var show = rowData.attributes.show;
            if (show == true) {
                return '是';
            } else if (show == false) {
                return '否';
            } else {
            	return value;
            }
        }
    }
}] ];// 表格的列

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'id';
operateParam_dataName = 'text';
operateParam_addUrl = '${basePath}manage/sys/organ/toAdd.do';
operateParam_add_beforeCallback = function(row) {
	var id = '';
	if (row && row != undefined) {
		id = row.id;
	}
	operateParam_addUrl = '${basePath}manage/sys/organ/toAdd.do?parentId=' + id;
}
operateParam_editUrl = '${basePath}manage/sys/organ/toEdit.do';
operateParam_edit_beforeCallback = function(row) {
}
operateParam_delUrl = '${basePath}manage/sys/organ/delete.do';
</script>