<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="fcc" %>
<%-- 按钮操作 --%>
<%-- 依赖init_save.jsp 保存数据 --%>
<%-- <jsp:param name="queryParamId" value="eventResult"/> 查询输入框的ID --%> 
<script type="text/javascript">
var datagridParam_id;// 用到的datagrid的ID
var datagridParam_url;// 数据源url
var datagridParam_idField;// datagrid表格的唯一标识
var datagridParam_idField_checkbox = true;// 是否显示多选框
var datagridParam_page = true;// 是否显示分页
var datagridParam_toolbar = '#toolbar';// 查询的搜索框
var datagridParam_fit = true;// 自适应
var datagridParam_column_value;// 表格的列 [[{field:'userId',title:'用户ID'}]]
var datagridParam_queryParamName;// 查询的参数name ['userId','userName'];
var datagridParam_load_beforeCallback;// 加载数据回调函数
var datagridParam_confirm_beforeCallback;// 确认框操作前回调
var datagridParam_confirm_afterCallback;// 确认框操作后回调

$(function() {
    var frozenColumns;
    if (datagridParam_idField_checkbox == true) {
        frozenColumns = [ [ 
            {
                field : 'id',
                width : 50,
                checkbox : datagridParam_idField_checkbox
            }, {
                    field : datagridParam_idField,
                    hidden : true
            } 
        ] ]
    } else {
        frozenColumns = [[]];
    }
    var urlStr = '';
    if (datagridParam_url) urlStr = Tool.urlAddParam(datagridParam_url, 'random=' + Math.random());
    datagrid = $('#' + datagridParam_id).datagrid({
        //url : Tool.urlAddParam(datagridParam_url, 'random=' + Math.random()),
        url : urlStr,
        toolbar : datagridParam_toolbar,
        title : '',
        iconCls : 'icon-save',
        pagination : datagridParam_page,
        rownumbers : true,
        striped : true,
        pageSize : 10,
        pageList : [ 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 ],
        fit : datagridParam_fit,
        fitColumns : true,
        nowrap : false,
        border : false,
        singleSelect : datagridParam_idField_checkbox == true ? false : true,
        idField : datagridParam_idField,
        frozenColumns : frozenColumns,
        columns :datagridParam_column_value,
        onRowContextMenu : function(e, rowIndex, rowData) {
            e.preventDefault();
            $(this).datagrid('unselectAll');
            $(this).datagrid('selectRow', rowIndex);
            $('#menu').menu('show', {
                left : e.pageX,
                top : e.pageY
            });
        },
        onLoadError : function(data) {
            console.log('error datagrid');
            //window.location.href = overUrl;
        },
        onLoadSuccess : function(data) {
            if (data.msg && data.msg != '') {
                Tool.message.alert(Lang.tip, Lang.dataError, Tool.icon.error);
            } else {
                if (datagridParam_load_beforeCallback) datagridParam_load_beforeCallback(data); 
            }
        },
        loadFilter : function(data) {
            var flag = Tool.operate.check({'data':data});
            if (flag != false) {
                return data;                         
            }
            return {'total':0,'rows':[],footer:null};
        }
    });
})


function searchFun() {
    var paramVals = {};
    if (datagridParam_queryParamName) {
        for (var i in datagridParam_queryParamName) {
            var param = $('#toolbar input[name="' + datagridParam_queryParamName[i] + '"]');
            var paramVal = '';
            if (param.is('.easyui-datebox')) {
                paramVal = param.datebox('getValue')
            } else if (param.is('.easyui-datetimebox')) {
                paramVal = param.datetimebox('getValue')
            } else {
                paramVal = param.val();
                if (paramVal == undefined) {
                    paramVal = $('#toolbar select[name="' + datagridParam_queryParamName[i] + '"]').val();
                }
            }
            if (paramVal) paramVals[datagridParam_queryParamName[i]] = paramVal
        }
    }
    var datagrid = initDatagrid();
    //datagrid.datagrid('load', paramVals);
    datagrid.datagrid({
        url : Tool.urlAddParam(datagridParam_url, 'random=' + Math.random()),
        queryParams : paramVals
    })
    datagrid.datagrid('clearSelections');
}

function clearFun() {
    $('#toolbar input').val('');
    $('#toolbar select').val('');
    var datagrid = initDatagrid();
    //datagrid.datagrid('load', {});
    datagrid.datagrid({
        url : Tool.urlAddParam(datagridParam_url, 'random=' + Math.random()),
        queryParams : {}
    })
    datagrid.datagrid('clearSelections');
}

function initDatagrid() {
    var datagrid = $('#' + datagridParam_id);
    return datagrid;
}

function gridReload() {
    initDatagrid().datagrid('reload');
}

function gridUnselectAll() {
    initDatagrid().datagrid('unselectAll');
}

function getGridSelected() {
    var rows = initDatagrid().datagrid('getSelections');
    return rows;
}

function getGridSelectedId( field) {
    var rows = getGridSelected();
    var ids = [];
    if (!field) field = datagridParam_idField;
    if (rows.length > 0) {
        for (var i = 0; i < rows.length; i++) {
            ids.push(rows[i][field]);
        }
        return ids.join(',');
    }
    return null;
}
// userFormId, operateUrl, fieldId, beforeCallback, afterCallback
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
                saveParam_closeWin = true;
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
</script>