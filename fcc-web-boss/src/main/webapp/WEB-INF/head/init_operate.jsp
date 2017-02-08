<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="fcc" %>
<%-- 按钮操作 --%>
<%-- 依赖init_save.jsp 保存数据 --%>
<%-- 依赖init_datagrid.jsp 初始化表格 --%>
<%-- <jsp:param name="queryParamId" value="eventResult"/> 查询输入框的ID --%> 
<style>
<!--
.operate-button {
    display: none;
}
-->
</style>
<div id="operate_div_content" class="operate-button">
    <fcc:permission operateId="view">
    <a id="view_button" class="easyui-linkbutton operate-button" iconCls="icon-search" onClick="view();" plain="true" href="javascript:void(0);">查看</a>
    </fcc:permission>
    <a id="refresh_button" class="easyui-linkbutton" iconCls="icon-reload" onClick="refresh();" plain="true" href="javascript:void(0);" >刷新</a> 
    <fcc:permission operateId="add">
    <a id="add_button" class="easyui-linkbutton operate-button" iconCls="icon-add" onClick="add();" plain="true" href="javascript:void(0);">新增</a> 
    </fcc:permission>
    <fcc:permission operateId="edit">
    <a id="edit_button" class="easyui-linkbutton operate-button" iconCls="icon-edit" onClick="edit();" plain="true" href="javascript:void(0);">修改</a>
    </fcc:permission>
    <fcc:permission operateId="delete">
    <a id="del_button" class="easyui-linkbutton operate-button" iconCls="icon-remove" onClick="del();" plain="true" href="javascript:void(0);">删除</a>  
    </fcc:permission>
    <fcc:permission operateId="export">
    <a id="export_button" class="easyui-linkbutton operate-button" iconCls="icon-download" onClick="exportData();" plain="true" href="javascript:void(0);">导出</a>
    </fcc:permission> 
    <fcc:permission operateId="import">
    <a id="import_button" class="easyui-linkbutton operate-button" iconCls="icon-upload" onClick="importData();" plain="true" href="javascript:void(0);">导入</a>
    </fcc:permission>
    <fcc:permission operateId="report">
    <a id="report_button" class="easyui-linkbutton operate-button" iconCls="icon-large-chart" onClick="report();" plain="true" href="javascript:void(0);">报表</a>
    </fcc:permission>
    <a class="easyui-linkbutton" iconCls="icon-undo" onClick="unselectAll()" plain="true" href="javascript:void(0);">取消选中</a> 
</div>
<script type="text/javascript">
var operateParam_form;// 删除时用的表单
var operateParam_operateDiv;// 在哪个Div中显示按钮
var operateParam_dataId;// 表格中的ID
var operateParam_dataName;// 表格中的ID
var operateParam_viewUrl;// 查看记录的调用的URL
var operateParam_addUrl;// 跳转到添加页面
var operateParam_editUrl;// 跳转到修改页面
var operateParam_delUrl;// 删除的地址
var operateParam_reportUrl;// 导出的地址
var operateParam_view_beforeCallback;// 查看记录前调用的函数
var operateParam_add_beforeCallback;// 添加记录前调用的函数
var operateParam_edit_beforeCallback;// 修改记录前调用的函数
var operateParam_del_beforeCallback;// 删除记录前调用的函数
var operateParam_del_afterCallback;// 删除记录后调用的函数

$(function() {
	var hiddenCss = 'operate-button';
	$('#operate_div_content').removeClass(hiddenCss);
	$('#operate_div_content').appendTo($('#' + operateParam_operateDiv));
	
	if (operateParam_viewUrl || operateParam_view_beforeCallback) $('#view_button').removeClass(hiddenCss);
	if (operateParam_addUrl || operateParam_add_beforeCallback) $('#add_button').removeClass(hiddenCss);
	if (operateParam_editUrl || operateParam_edit_beforeCallback) $('#edit_button').removeClass(hiddenCss);
	if (operateParam_delUrl || operateParam_del_beforeCallback || operateParam_del_afterCallback) $('#del_button').removeClass(hiddenCss);
	try {
		if ($.isFunction(exportData)) $('#export_button').removeClass(hiddenCss);
	} catch (e) {}
	try {
        if ($.isFunction(importData)) $('#import_button').removeClass(hiddenCss);
    } catch (e) {}
	if (operateParam_reportUrl) $('#report_button').removeClass(hiddenCss);
})

function refresh(){
	gridReload();
}

function unselectAll() {
	gridUnselectAll();
}

<fcc:permission operateId="view">
function view() {
	var rows = getGridSelected();
    if (rows.length != 1 && rows.length != 0) {
        var names = [];
        for ( var i = 0; i < rows.length; i++) {
            names.push(rows[i][operateParam_dataName]);
        }
        Tool.message.alert(Lang.tip, Lang.recordSelectedMore.format(names.join(','),rows.length), Tool.icon.info, true);
    } else if (rows.length == 1) {
    	if (operateParam_view_beforeCallback) {
    		if (operateParam_view_beforeCallback() == false) return;
    	}
        if (operateParam_viewUrl) {
        	window.location.href = Tool.urlAddParam(operateParam_viewUrl, "id=" + rows[0][operateParam_dataId]);
        }
    } else {
        Tool.message.alert(Lang.tip, Lang.recordSelectedOne, Tool.icon.info, true);
    }
}
</fcc:permission>
<fcc:permission operateId="add">
function add() {
	if (operateParam_add_beforeCallback) {
		var rows = getGridSelected();
		var row;
		if (rows.length >= 1) {
			row = rows[0];
		}
		if (operateParam_add_beforeCallback(row) == false) return;
	}
	if (operateParam_addUrl) {
		window.location.href = operateParam_addUrl;
	}
}
</fcc:permission>
<fcc:permission operateId="edit">
function edit() {
    var rows = getGridSelected();
    if (rows.length != 1 && rows.length != 0) {
        var names = [];
        for ( var i = 0; i < rows.length; i++) {
            names.push(rows[i][operateParam_dataName]);
        }
        Tool.message.alert(Lang.tip, Lang.recordSelectedMore.format(names.join(','),rows.length), Tool.icon.info, true);
    } else if (rows.length == 1) {
    	if (operateParam_edit_beforeCallback) {
            if (operateParam_edit_beforeCallback(rows[0]) == false) return;
        }
    	if (operateParam_editUrl) {
            window.location.href = Tool.urlAddParam(operateParam_editUrl, "id=" + rows[0][operateParam_dataId]);
        }
    } else {
        Tool.message.alert(Lang.tip, Lang.recordSelectedOne, Tool.icon.info, true);
    }
}
</fcc:permission>
<fcc:permission operateId="delete">
function del() {
	if (operateParam_delUrl) {
	    <%-- init_datagrid userFormId, operateUrl, beforeCallback, afterCallback --%>
	    gridConfirm({
		    	userFormId : operateParam_form,
		    	operateUrl : operateParam_delUrl,
		    	beforeCallback : function(rows) {
		    		if (operateParam_del_beforeCallback) {
		                if (operateParam_del_beforeCallback(rows) == false) return;
		            }
		    	},
		    	afterCallback : function(data, success) {
		    		if (success == true) searchFun();
                    if (operateParam_del_afterCallback) operateParam_del_afterCallback(data, success);
		    	}
		    }		
	    );
	}
}
</fcc:permission>
<fcc:permission operateId="report">
function report()　{
    if (operateParam_reportUrl) window.location.href = operateParam_reportUrl;
}
</fcc:permission>
</script>