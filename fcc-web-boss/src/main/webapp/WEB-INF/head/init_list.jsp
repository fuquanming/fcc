<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- 列表:添加、修改、删除、查询 js函数 --%>
<%--
支持解析 如下 
<jsp:param name="initCss" value="/css/daterangepicker.css" />
<jsp:param name="initJs" value="/js/date-time/moment.min.js" />
<jsp:param name="initFunction" value="initSearchTime" />

<jsp:param name="initDataId" value="logId" />
<jsp:param name="initDataName" value="userId" />
<jsp:param name="initViewUrl" value="initSearchTime" />
<jsp:param name="initAddUrl" value="logId" />
<jsp:param name="initEditUrl" value="logId" />
<jsp:param name="initDelUrl" value="logId" />
<jsp:param name="initReportUrl" value="${basePath}manage/sys/sysLog/report/view.do"/>

<jsp:param name="queryParamId" value="userId"/> 多个
 --%>
<script type="text/javascript">
function refresh(){
	datagrid.datagrid('reload');
}
<c:if test="${not empty param.initViewUrl}">
function view() {
    var rows = datagrid.datagrid('getSelections');
    if (rows.length != 1 && rows.length != 0) {
        var names = [];
        for ( var i = 0; i < rows.length; i++) {
            names.push(rows[i].${param.initDataName});
        }
        Tool.message.alert(Lang.tip, Lang.recordSelectedMore.format(names.join(','),rows.length), Tool.icon.info, true);
    } else if (rows.length == 1) {
        Tool.goNewPage("${param.initViewUrl}?id=" + rows[0].${param.initDataId});
    } else {
    	Tool.message.alert(Lang.tip, Lang.recordSelectedOne, Tool.icon.info, true);
    }
}
</c:if>
<c:if test="${not empty param.initAddUrl}">
function add() {
    Tool.goNewPage("${param.initAddUrl}");
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
        Tool.goNewPage("${param.initEditUrl}?id=" + rows[0].${param.initDataId});
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
                            if (Tool.operate.check({'data':data})) {
                                searchFun();
                            }
                        } catch(e) {
                        	console.log(e);
                            Tool.goPage(overUrl);
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
    Tool.goNewPage("${param.initReportUrl}");
}
</c:if>
$(function() {
	$(document.body).append('<div id="appendDiv">111<div>')
})
</script>