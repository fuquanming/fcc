<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--
支持解析 如下 
<jsp:param name="initCss" value="/css/daterangepicker.css" />
<jsp:param name="initJs" value="/js/date-time/moment.min.js" />
<jsp:param name="initFunction" value="initSearchTime" />

<jsp:param name="initDataGrid" value="datagrid" />
<jsp:param name="initDataId" value="logId" />
<jsp:param name="initDataName" value="userId" />
<jsp:param name="initViewUrl" value="initSearchTime" />
<jsp:param name="initAddUrl" value="logId" />
<jsp:param name="initEditUrl" value="logId" />
<jsp:param name="initDelUrl" value="logId" />
 --%>
<script type="text/javascript">
function refresh(){
    ${param.initDataGrid}.datagrid('reload');
}
<c:if test="${not empty param.initViewUrl}">
function view() {
    var rows = ${param.initDataGrid}.datagrid('getSelections');
    if (rows.length != 1 && rows.length != 0) {
        var names = [];
        for ( var i = 0; i < rows.length; i++) {
            names.push(rows[i].${param.initDataName});
        }
        Tool.message.alert(Lang.tip, Lang.recordSelectedMore.format(names.join(','),rows.length), Tool.icon.info, true);
    } else if (rows.length == 1) {
        window.location.href = "${param.initViewUrl}?id=" + rows[0].${param.initDataId};
    } else {
    	Tool.message.alert(Lang.tip, Lang.recordSelectedOne, Tool.icon.info, true);
    }
}
</c:if>
<c:if test="${not empty param.initAddUrl}">
function add() {
    window.location.href = "${param.initAddUrl}";
}
</c:if>
<c:if test="${not empty param.initEditUrl}">
function edit() {
    var rows = ${param.initDataGrid}.datagrid('getSelections');
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
    var rows = ${param.initDataGrid}.datagrid('getSelections');
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
</script>