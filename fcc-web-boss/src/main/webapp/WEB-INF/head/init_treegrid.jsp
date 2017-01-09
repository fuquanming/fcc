<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="fcc" %>
<%-- 按钮操作 --%>
<%-- 依赖init_save.jsp 保存数据 --%>
<%-- <jsp:param name="queryParamId" value="eventResult"/> 查询输入框的ID --%> 
<script type="text/javascript">
var treegridParam_id;// 用到的treegrid的ID
var treegridParam_url;// 数据源url
var treegridParam_idField;// treegrid表格的唯一标识
var treegridParam_page = false;// 是否显示分页：true、false
//var treegridParam_idField_checkbox = true;// 是否显示多选框
var treegridParam_column_value;// 表格的列 [[{field:'userId',title:'用户ID'}]]
var treegridParam_queryParamName;// 查询的参数name ['userId','userName'];
var treegridParam_confirm_beforeCallback;// 确认框操作前回调
var treegridParam_confirm_afterCallback;// 确认框操作后回调

$(function() {
	treegrid = $('#treegrid').treegrid({
		url : Tool.urlAddParam(treegridParam_url, 'random=' + Math.random()),
		toolbar : '#toolbar',
        title : '',
        iconCls : 'icon-save',
        fit : true,
        fitColumns : true,
        nowrap : true,
        animate : false,
        border : false,
        striped : true,
        rownumbers : true,
        pagination : treegridParam_page,
        pageSize : 10,
        pageList : [ 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 ],
        idField : treegridParam_idField,
        treeField : 'text',
        frozenColumns : [ [ {
            field : treegridParam_idField,
            width : 100,
            hidden : true
        } ] ],
        columns : treegridParam_column_value,
        onContextMenu : function(e, row) {
            e.preventDefault();
            $(this).treegrid('unselectAll');
            $(this).treegrid('select', row.id);
            $('#menu').menu('show', {
                left : e.pageX,
                top : e.pageY
            });
        },
        onLoadSuccess : function(row, data) {
            var t = $(this);
            if (data) {
                if (data[0] && data[0].msg && data[0].msg != '') {
                    Tool.message.alert(Lang.tip, data[0].msg, Tool.icon.error);
                }
            }
        },
        onExpand : function(row) {
            treegrid.treegrid('unselectAll');
        },
        onCollapse : function(row) {
            treegrid.treegrid('unselectAll');
        },
        onLoadError : function() {
            //window.location.href = overUrl;
        },
        loadFilter : function(data) {
            var flag = Tool.operate.check(data);
            if (flag != true || flag != false) {
                return data;                                            
            }
        },
        onBeforeExpand : function(row) {
            if (row) $(this).treegrid('options').url = Tool.urlAddParam(treegridParam_url, 'parentId=' + row.id);
            return true;
        }
    });
})

function searchFun() {
	//gridReload();
	var paramVals = {};
    if (treegridParam_queryParamName) {
        for (var i in treegridParam_queryParamName) {
            var param = $('#toolbar input[name="' + treegridParam_queryParamName[i] + '"]');
            var paramVal = '';
            if (param.is('.easyui-datebox')) {
                paramVal = param.datebox('getValue')
            } else if (param.is('.easyui-datetimebox')) {
                paramVal = param.datetimebox('getValue')
            } else {
                paramVal = param.val();
                if (paramVal == undefined) {
                    paramVal = $('#toolbar select[name="' + treegridParam_queryParamName[i] + '"]').val();
                }
            }
            if (paramVal) paramVals[treegridParam_queryParamName[i]] = paramVal
        }
    }
    var datagrid = initDatagrid();
    //datagrid.datagrid('load', paramVals);
    datagrid.treegrid({
        url : Tool.urlAddParam(treegridParam_url, 'random=' + Math.random()),
        queryParams : paramVals
    })
    datagrid.treegrid('clearSelections');
}

function clearFun() {
    $('#toolbar input').val('');
    $('#toolbar select').val('');
    var datagrid = initDatagrid();
    //datagrid.datagrid('load', {});
    datagrid.treegrid({
        url : Tool.urlAddParam(treegridParam_url, 'random=' + Math.random()),
        queryParams : {}
    })
    datagrid.treegrid('clearSelections');
}

function initDatagrid() {
	var datagrid = $('#' + treegridParam_id);
    return datagrid;
}

function gridReload() {
	searchFun();// 有点击事件，url可能改变
	//initDatagrid().treegrid('reload');
}

function gridUnselectAll() {
    initDatagrid().treegrid('unselectAll');
}

function getGridSelected() {
	var rows = initDatagrid().treegrid('getSelections');
    return rows;
}

function getGridSelectedId(field) {
	var rows = getGridSelected();
	var ids = [];
	if (!field) field = treegridParam_idField;
	if (rows.length > 0) {
		for (var i = 0; i < rows.length; i++) {
	        ids.push(rows[i][field]);
	    }
		return ids.join(',');
	}
	return null;
}
//  userFormId, operateUrl, fieldId, beforeCallback, afterCallback
function gridConfirm(params) {
    var rows = getGridSelected();
    if (rows.length > 0) {
        if (params.beforeCallback) {
            if (params.beforeCallback(rows) == false) return;
        }
        Tool.message.confirm(Lang.confirm, Lang.confirmOperate, function(r) {
            if (r) {
                var userForm = $('#' + params.userFormId).form();
                var idsVal = getGridSelectedId(params.fieldId);
                userForm.find('[name=ids]').val(idsVal);
                
                saveParam_form = params.userFormId;
                saveParam_saveUrl = params.operateUrl;
                saveParam_afterCallback = function(data, success) {
                    if (params.afterCallback) params.afterCallback(data, success);
                    return false;
                };
                save();
            }
        });
    } else {
        Tool.message.alert(Lang.tip, Lang.recordSelectedOne, Tool.icon.info, true);
    }
}
// 展开
function gridExpandAll() {
	var treegrid = initDatagrid();
	var rows = getGridSelected();
    if (rows) {
        treegrid.treegrid('expandAll', rows.treegridParam_idField);
    } else {
        treegrid.treegrid('expandAll');
    }
}
// 折叠
function gridCollapseAll() {
	var treegrid = initDatagrid();
	var rows = getGridSelected();
    if (rows) {
        treegrid.treegrid('collapseAll', rows.treegridParam_idField);
    } else {
        treegrid.treegrid('collapseAll');
    }
}
</script>