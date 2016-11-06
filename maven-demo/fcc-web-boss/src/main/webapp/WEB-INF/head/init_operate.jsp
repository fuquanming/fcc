<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="fcc" %>
<%-- 按钮操作 --%>
<%-- <jsp:param name="queryParamId" value="eventResult"/> 查询输入框的ID --%> 
<style>
<!--
.operate_button {
    display: none;
}
-->
</style>
<div id="operate_div_content" class="operate_button">
    <fcc:permission operateId="view">
    <a id="view_button" class="easyui-linkbutton operate_button" iconCls="icon-search" onClick="view();" plain="true" href="javascript:void(0);">查看</a>
    </fcc:permission>
    <a class="easyui-linkbutton" iconCls="icon-reload" onClick="refresh();" plain="true" href="javascript:void(0);" >刷新</a> 
    <fcc:permission operateId="add">
    <a id="add_button" class="easyui-linkbutton operate_button" iconCls="icon-add" onClick="add();" plain="true" href="javascript:void(0);">新增</a> 
    </fcc:permission>
    <fcc:permission operateId="edit">
    <a id="edit_button" class="easyui-linkbutton operate_button" iconCls="icon-edit" onClick="edit();" plain="true" href="javascript:void(0);">修改</a>
    </fcc:permission>
    <fcc:permission operateId="delete">
    <a id="del_button" class="easyui-linkbutton operate_button" iconCls="icon-remove" onClick="del();" plain="true" href="javascript:void(0);">删除</a>  
    </fcc:permission>
    <fcc:permission operateId="export">
    <a id="export_button" class="easyui-linkbutton operate_button" iconCls="icon-download" onClick="exportData();" plain="true" href="javascript:void(0);">导出</a>
    </fcc:permission> 
    <fcc:permission operateId="import">
    <a id="import_button" class="easyui-linkbutton operate_button" iconCls="icon-upload" onClick="importData();" plain="true" href="javascript:void(0);">导入</a>
    </fcc:permission>
    <fcc:permission operateId="report">
    <a id="report_button" class="easyui-linkbutton operate_button" iconCls="icon-large-chart" onClick="report();" plain="true" href="javascript:void(0);">报表</a>
    </fcc:permission>
    <a class="easyui-linkbutton" iconCls="icon-undo" onClick="datagrid.datagrid('unselectAll');" plain="true" href="javascript:void(0);">取消选中</a> 
</div>
<script type="text/javascript">
var operateParam_operateDiv;// 在哪个Div中显示按钮
var operateParam_dataId;// 表格中的ID
var operateParam_dataName;// 表格中的ID
var operateParam_viewUrl;// 查看记录的调用的URL
var operateParam_addUrl;
var operateParam_editUrl;
var operateParam_delUrl;
var operateParam_reportUrl;
var operateParam_viewFun;// 查看记录调用的函数
var operateParam_addFun;// 添加记录调用的函数
var operateParam_editFun;// 修改记录调用的函数
var operateParam_afterDelFun;// 删除记录后调用的函数

$(function() {
	$('#operate_div_content').removeClass('operate_button');
	$('#operate_div_content').appendTo($('#' + operateParam_operateDiv));
	
	if (operateParam_viewUrl || operateParam_viewFun) $('#view_button').removeClass('operate_button');
	if (operateParam_addUrl || operateParam_addFun) $('#add_button').removeClass('operate_button');
	if (operateParam_editUrl || operateParam_editFun) $('#edit_button').removeClass('operate_button');
	if (operateParam_delUrl) $('#del_button').removeClass('operate_button');
	if ($.isFunction(exportData)) $('#export_button').removeClass('operate_button');
	if ($.isFunction(importData)) $('#import_button').removeClass('operate_button');
	if (operateParam_reportUrl) $('#report_button').removeClass('operate_button');
})

function refresh(){
    datagrid.datagrid('reload');
}
<fcc:permission operateId="view">
function view() {
	var rows = datagrid.datagrid('getSelections');
    if (rows.length != 1 && rows.length != 0) {
        var names = [];
        for ( var i = 0; i < rows.length; i++) {
            names.push(rows[i][operateParam_dataName]);
        }
        Tool.message.alert(Lang.tip, Lang.recordSelectedMore.format(names.join(','),rows.length), Tool.icon.info, true);
    } else if (rows.length == 1) {
        if (operateParam_viewUrl) {
        	window.location.href = Tool.urlAddParam(operateParam_viewUrl, "id=" + rows[0][operateParam_dataId]);
        } else if (operateParam_viewFun) {
        	operateParam_viewFun(rows[0]);
        }
    } else {
        Tool.message.alert(Lang.tip, Lang.recordSelectedOne, Tool.icon.info, true);
    }
}
</fcc:permission>
<fcc:permission operateId="add">
function add() {
	if (operateParam_addUrl) {
		window.location.href = operateParam_addUrl;
	} else if (operateParam_addFun) {
		operateParam_addFun();
	}
}
</fcc:permission>
<fcc:permission operateId="edit">
function edit() {
    var rows = datagrid.datagrid('getSelections');
    if (rows.length != 1 && rows.length != 0) {
        var names = [];
        for ( var i = 0; i < rows.length; i++) {
            names.push(rows[i][operateParam_dataName]);
        }
        Tool.message.alert(Lang.tip, Lang.recordSelectedMore.format(names.join(','),rows.length), Tool.icon.info, true);
    } else if (rows.length == 1) {
    	if (operateParam_editUrl) {
            window.location.href = Tool.urlAddParam(operateParam_editUrl, "id=" + rows[0][operateParam_dataId]);
        } else if (operateParam_editFun) {
            operateParam_editFun(rows[0]);
        }
    } else {
        Tool.message.alert(Lang.tip, Lang.recordSelectedOne, Tool.icon.info, true);
    }
}
</fcc:permission>
<fcc:permission operateId="delete">
function del() {
	if (operateParam_delUrl) {
		var ids = [];
	    var rows = datagrid.datagrid('getSelections');
	    if (rows.length > 0) {
	        Tool.message.confirm(Lang.confirm, Lang.confirmDel, function(r) {
	            if (r) {
	                for ( var i = 0; i < rows.length; i++) {
	                    ids.push(rows[i][operateParam_dataId]);
	                }
	                var idsVal = ids.join(',');
	                userForm.find('[name=ids]').val(idsVal);
	                userForm.form('submit', {
	                    url : operateParam_delUrl,
	                    onSubmit : function() {
	                        Tool.message.progress();
	                        return true;
	                    },
	                    success : function(data) {
	                        try {
	                            Tool.message.progress('close');
	                            if (Tool.operate.check(data, true)) {
	                                searchFun();
	                                if (operateParam_afterDelFun) operateParam_afterDelFun();
	                            }
	                        } catch(e) {
	                            console.log(e);
	                            window.location.href = overUrl;
	                        }
	                    }
	                });
	            }
	        });
	    } else {
	        Tool.message.alert(Lang.tip, Lang.recordSelectedOne, Tool.icon.info, true);
	    }
	}
}
</fcc:permission>

function searchFun() {
    datagrid.datagrid('load', {
        <c:forEach items="${paramValues.queryParamId }" var="p" varStatus="stat">
        ${p} : $('#${p}').val()<c:if test="${!stat.last}" >,</c:if>
        </c:forEach>
    });
    datagrid.datagrid('clearSelections');
}

function clearFun() {
    $('#toolbar input').val('');
    $('#toolbar select').val('');
    datagrid.datagrid('load', {});
}
<fcc:permission operateId="add">
function report()　{
	if (operateParam_reportUrl) window.location.href = operateParam_reportUrl;
}
</fcc:permission>

</script>