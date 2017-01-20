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
        <th>名称</th>
        <td><input id="nodeNameStr" name="nodeNameStr" class="easyui-textbox" data-options="prompt:'请输入名称...'" style="width: 150px;" />
        </td>
        <th>编码</th>
        <td><input id="nodeCode" name="nodeCode" class="easyui-textbox" data-options="prompt:'请输入编码...'" style="width: 150px;" />
        </td>
        <td>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
        </td>
      </tr>
    </table>
	</form>
    </fieldset>
    <div id="operateDiv"></div>
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
function editStatus(areaStatus) {
    gridConfirm({
        gridType : Tool.grid.data,
        userFormId : 'userForm',
        operateUrl : 'manage/sys/sysDict/show.do?nodeStatus=' + areaStatus,
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
treegridParam_url = 'manage/sys/sysDict/treegrid.do';// 数据源url
treegridParam_idField = 'id';// datagrid表格的唯一标识
treegridParam_idField_checkbox = true;// 是否显示多选框
treegridParam_page = false;// 显示分页
treegridParam_column_value = [ [ {
    field : 'text',
    title : '名称',
    width : 100
}, {
    field : 'nodeCode',
    title : '编码',
    width : 50,
    formatter : function(value, rowData, rowIndex) {
        if (rowData.attributes) {
            return rowData.attributes.nodeCode;
        }
    }
}, {
    field : 'nodeLevel',
    title : '级别',
    width : 50,
    formatter : function(value, rowData, rowIndex) {
        if (rowData.attributes) {
            return rowData.attributes.nodeLevel;
        }
    }
}, {
    field : 'nodeSort',
    title : '排序',
    width : 50,
    formatter : function(value, rowData, rowIndex) {
        if (rowData.attributes) {
            return rowData.attributes.nodeSort;
        }
    }
}, {
    field : 'show',
    title : '是否显示',
    width : 50,
    formatter : function(value, rowData, rowIndex) {
        if (rowData.attributes) {
            var show = rowData.attributes.nodeStatus;
            if (show == true) {
                return '是';
            } else if (show == false) {
                return '否';
            } else {
                return value;
            }
        }
    }
}, {
    field : 'nodeDesc',
    title : '说明',
    width : 200,
    formatter : function(value, rowData, rowIndex) {
        if (rowData.attributes) {
            return rowData.attributes.nodeDesc;
        }
    }
}] ];// 表格的列
treegridParam_queryParamName = ['nodeNameStr', 'nodeCode'];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'id';
operateParam_dataName = 'text';
operateParam_addUrl = 'manage/sys/sysDict/toAdd.do';
operateParam_add_beforeCallback = function(row) {
    var id = '';
    if (row && row != undefined) {
        id = row.id;
    }
    operateParam_addUrl = 'manage/sys/sysDict/toAdd.do?parentId=' + id;
}
operateParam_editUrl = 'manage/sys/sysDict/toEdit.do';
operateParam_edit_beforeCallback = function(row) {
}
operateParam_delUrl = 'manage/sys/sysDict/delete.do';
</script>