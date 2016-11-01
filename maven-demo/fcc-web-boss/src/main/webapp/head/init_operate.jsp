<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- 按钮操作 --%>
<div>
    <fcc:permission operateId="view">
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="view();" plain="true" href="javascript:void(0);">查看</a>
    <a class="easyui-linkbutton" iconCls="icon-reload" onClick="refresh();" plain="true" href="javascript:void(0);" >刷新</a>
    </fcc:permission> 
    <fcc:permission operateId="add">
    <a class="easyui-linkbutton" iconCls="icon-add" onClick="add();" plain="true" href="javascript:void(0);">新增</a> 
    </fcc:permission>
    <fcc:permission operateId="edit">
    <a class="easyui-linkbutton" iconCls="icon-edit" onClick="edit();" plain="true" href="javascript:void(0);">修改</a>
    </fcc:permission>
    <fcc:permission operateId="delete">
    <a class="easyui-linkbutton" iconCls="icon-remove" onClick="del();" plain="true" href="javascript:void(0);">删除</a>  
    </fcc:permission>
    <fcc:permission operateId="export">
    <a class="easyui-linkbutton" iconCls="icon-download" onClick="exportData();" plain="true" href="javascript:void(0);">导出</a>
    </fcc:permission> 
    <fcc:permission operateId="import">
    <a class="easyui-linkbutton" iconCls="icon-upload" onClick="importData();" plain="true" href="javascript:void(0);">导入</a>
    </fcc:permission>
    <fcc:permission operateId="report">
    <a class="easyui-linkbutton" iconCls="icon-large-chart" onClick="report();" plain="true" href="javascript:void(0);">报表</a>
    </fcc:permission>
    <a class="easyui-linkbutton" iconCls="icon-undo" onClick="datagrid.datagrid('unselectAll');" plain="true" href="javascript:void(0);">取消选中</a> 
</div>
<script type="text/javascript">
var operateParam = {};
operateParam.dataId;// 表格中的ID
operateParam.viewUrl;// 查看记录的调用的URL
operateParam.addUrl;
operateParam.editUrl;
operateParam.delUrl;
operateParam.reportUrl;
operateParam.viewFun;// 查看记录调用的函数
function refresh(){
    datagrid.datagrid('reload');
}
<fcc:permission operateId="view">
function view() {
	var rows = datagrid.datagrid('getSelections');
    if (rows.length != 1 && rows.length != 0) {
        var names = [];
        for ( var i = 0; i < rows.length; i++) {
            names.push(rows[i].${param.initDataName});
        }
        Tool.message.alert(Lang.tip, Lang.recordSelectedMore.format(names.join(','),rows.length), Tool.icon.info, true);
    } else if (rows.length == 1) {
        if (operateParam.viewUrl) {
        	var url = operateParam.viewUrl;
        	if (url.indexOf("?") == -1) {
        		url = url + "?";
        	} else {
        		url = url + "&";
        	}
        	window.location.href = url + "id=" + rows[0].${param.initDataId};
        } else if (operateParam.viewFun) {
        	operateParam.viewFun(rows[0]);
        }
    } else {
        Tool.message.alert(Lang.tip, Lang.recordSelectedOne, Tool.icon.info, true);
    }
}
</fcc:permission>
<c:if test="${not empty param.initAddUrl}">
function add() {
	if (operateParam.addUrl) {
		window.location.href = operateParam.addUrl;
	}
}
</c:if>
<c:if test="${not empty param.initEditUrl}">
function edit() {
    var rows = datagrid.datagrid('getSelections');
    if (rows.length != 1 && rows.length != 0) {
        var names = [];
        for ( var i = 0; i < rows.length; i++) {
            names.push(rows[i].${param.initDataName});
        }
        Tool.message.alert(Lang.tip, Lang.recordSelectedMore.format(names.join(','),rows.length), Tool.icon.info, true);
    } else if (rows.length == 1) {
        window.location.href = "${param.initEditUrl}?id=" + rows[0].${param.initDataId};
    } else {
        Tool.message.alert(Lang.tip, Lang.recordSelectedOne, Tool.icon.info, true);
    }
}
</c:if>
<c:if test="${not empty param.initDelUrl}">
function del() {
    var ids = [];
    var rows = datagrid.datagrid('getSelections');
    if (rows.length > 0) {
        Tool.message.confirm(Lang.confirm, Lang.confirmDel, function(r) {
            if (r) {
                for ( var i = 0; i < rows.length; i++) {
                    ids.push(rows[i].${param.initDataId});
                }
                var idsVal = ids.join(',');
                userForm.find('[name=ids]').val(idsVal);
                userForm.form('submit', {
                    url : '${param.initDelUrl}',
                    onSubmit : function() {
                        Tool.message.progress();
                        return true;
                    },
                    success : function(data) {
                        try {
                            Tool.message.progress('close');
                            if (Tool.operate.check(data, true)) {
                                searchFun();
                            }
                        } catch(e) {
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
</c:if>
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
<c:if test="${not empty param.initReportUrl}">
function report()　{
    window.location.href = "${param.initReportUrl}";
}
</c:if>

</script>