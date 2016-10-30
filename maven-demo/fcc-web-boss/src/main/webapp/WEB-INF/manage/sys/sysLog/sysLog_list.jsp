<%@page import="com.fcc.web.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<script type="text/javascript" charset="UTF-8">
    var datagrid;
    var userForm;
    var userDialog;
    var importDataFlag = false;
    $(function() {

        userForm = $('#userForm').form();
        
        datagrid = $('#datagrid').datagrid({
            <c:if test="${empty param.lazy}">url : '${basePath}manage/sys/sysLog/datagrid.do?random=' + Math.random(),</c:if>
            toolbar : '#toolbar',
            title : '',
            iconCls : 'icon-save',
            pagination : true,
            rownumbers:true,
            striped:true,
            pageSize : 10,
            pageList : [ 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 ],
            fit : true,
            fitColumns : true,
            nowrap : false,
            border : false,
            idField : 'logId',
            frozenColumns : [ [ 
            {
                field : 'id',
                width : 50,
                checkbox : true
            }, {
                    field : 'logId',
                    hidden : true
            } 
             ] ],
            columns : [ [ 
                {
                    field : 'userId',
                    title : '<%=SysLog.ALIAS_USER_ID%>',
                    sortable:true,
                    width : 100
                } ,
                {
                    field : 'userName',
                    title : '<%=SysLog.ALIAS_USER_NAME%>',
                    sortable:true,
                    width : 100
                } ,
                {
                    field : 'ipAddress',
                    title : '<%=SysLog.ALIAS_IP_ADDRESS%>',
                    sortable:true,
                    width : 100
                } ,
                {
                    field : 'logTime',
                    title : '<%=SysLog.ALIAS_LOG_TIME%>',
                    sortable:true,
                    width : 100,
                    formatter : function(value, rowData, rowIndex) {
                        var date = new Date(value);
                        var month = date.getMonth() + 1;
                        var day = date.getDate();
                        var hour = date.getHours();
                        var minute = date.getMinutes();
                        var second = date.getSeconds();
                        return date.getFullYear() + '-' + (month < 10 ? "0" : "") + month + '-' + (day < 10 ? "0" : "") + day 
                        + " " + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "") + second;
                    }
                } ,
                {
                    field : 'moduleName',
                    title : '<%=SysLog.ALIAS_MODULE_NAME%>',
                    sortable:true,
                    width : 100
                } ,
                {
                    field : 'operateName',
                    title : '<%=SysLog.ALIAS_OPERATE_NAME%>',
                    sortable:true,
                    width : 100
                } ,
                {
                    field : 'eventResult',
                    title : '<%=SysLog.ALIAS_EVENT_RESULT%>',
                    sortable:true,
                    width : 100,
                    formatter : function(value, rowData, rowIndex) {
                        var msg = '失败'
                        if (value == '1') {
                            msg = '成功';
                        }
                        return msg;
                    }
                } 
            ] ],
            onRowContextMenu : function(e, rowIndex, rowData) {
                e.preventDefault();
                $(this).datagrid('unselectAll');
                $(this).datagrid('selectRow', rowIndex);
                $('#menu').menu('show', {
                    left : e.pageX,
                    top : e.pageY
                });
            },
            onLoadError : function() {
                window.location.href = overUrl;
            },
            onLoadSuccess : function(data) {
                if (data.msg && data.msg != '') {
                    Tool.message.show({
                        msg : '数据库异常！',
                        title : '提示'
                    });
                }
            },
            loadFilter : function(data) {
                var flag = Tool.operate.check(data);
                if (flag != true || flag != false) {
                    return data;                                            
                }
            }
        });
        

    });
    
    function view() {
        var rows = datagrid.datagrid('getSelections');
        if (rows.length != 1 && rows.length != 0) {
            var names = [];
            for ( var i = 0; i < rows.length; i++) {
                names.push(rows[i].userName);
            }
            Tool.message.show({
                msg : '只能择一条记录进行查看！您已经选择了【' + names.join(',') + '】' + rows.length + '条记录',
                title : '提示'
            });
        } else if (rows.length == 1) {
            window.location.href = "${basePath}manage/sys/sysLog/toView.do?id=" + rows[0].logId;
        }
    }

    function add() {
        window.location.href = "${basePath}manage/sys/sysLog/toAdd.do";
    }

    function edit() {
        var rows = datagrid.datagrid('getSelections');
        if (rows.length != 1 && rows.length != 0) {
            var names = [];
            for ( var i = 0; i < rows.length; i++) {
                names.push(rows[i].userName);
            }
            Tool.message.show({
                msg : '只能择一条记录进行编辑！您已经选择了【' + names.join(',') + '】' + rows.length + '条记录',
                title : '提示'
            });
        } else if (rows.length == 1) {
            window.location.href = "${basePath}manage/sys/sysLog/toEdit.do?id=" + rows[0].logId;
        }
    }

    function del() {
        var ids = [];
        var rows = datagrid.datagrid('getSelections');
        if (rows.length > 0) {
            Tool.message.confirm('请确认', '您要删除当前所选记录？', function(r) {
                if (r) {
                    for ( var i = 0; i < rows.length; i++) {
                        ids.push(rows[i].logId);
                    }
                    var idsVal = ids.join(',');
                    userForm.find('[name=ids]').val(idsVal);
                    userForm.form('submit', {
                        url : '${basePath}manage/sys/sysLog/delete.do',
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
            Tool.message.alert('提示', '请选择要删除的记录！', 'error');
        }
    }

    function searchFun() {
        <c:if test="${not empty param.lazy}">datagrid = $('#datagrid').datagrid({url : '${basePath}manage/sys/sysLog/datagrid.do?random=' + Math.random()});</c:if>
        datagrid.datagrid('load', {
                userId : $('#toolbar input[name=userId]').val(),
                userName : $('#toolbar input[name=userName]').val(),
                ipAddress : $('#toolbar input[name=ipAddress]').val(),
                logTimeBegin : $('#toolbar input[name=logTimeBegin]').val(),
                logTimeEnd : $('#toolbar input[name=logTimeEnd]').val(),
                moduleName : $('#moduleName').val(),
                operateName : $('#operateName').val(),
                eventResult : $('#eventResult').val()
        });
        datagrid.datagrid('clearSelections');
    }
    function clearFun() {
        $('#toolbar input').val('');
        $('#moduleName').val('');
        $('#operateName').val('');
        $('#eventResult').val('');
        datagrid.datagrid('load', {});
    }
    
    function report()　{
        window.location.href = "${basePath}manage/sys/sysLog/report/view.do";
    }
</script>
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
            <th><%=SysLog.ALIAS_USER_ID%></th>  
            <td colspan="2">
                <input id="userId" name="userId" maxlength="20"  style="width: 120px;"/>
            </td>
            <th><%=SysLog.ALIAS_USER_NAME%></th>    
            <td colspan="2">
                <input id="userName" name="userName" maxlength="20"  style="width: 120px;"/>
            </td>
            <th><%=SysLog.ALIAS_IP_ADDRESS%></th>   
            <td colspan="2">
                <input id="ipAddress" name="ipAddress" maxlength="24"  style="width: 120px;"/>
            </td>
            <th><%=SysLog.ALIAS_LOG_TIME%></th> 
            <td colspan="2">
                <input value="" class="easyui-datebox" style="width: 120px;" id="logTimeBegin" name="logTimeBegin"   />
                <input value="" class="easyui-datebox" style="width: 120px;" id="logTimeEnd" name="logTimeEnd"   />
            </td>
        </tr>   
        <tr>    
            <th><%=SysLog.ALIAS_MODULE_NAME%></th>  
            <td colspan="2">
                <select id="moduleName" name="moduleName" style="width: 130px;">
                <option value="">---请选择---</option>
                <c:forEach items="${USER_MENU}" var="module">
                <c:if test="${not empty module.moduleDesc}">
                <option value="${module.moduleId }">${module.moduleName }</option>
                </c:if>
                </c:forEach>
                </select>
            </td>
            <th><%=SysLog.ALIAS_OPERATE_NAME%></th> 
            <td colspan="2">
                <select id="operateName" name="operateName" style="width: 130px;">
                <option value="">---请选择---</option>
                <option value="login">登录</option>
                <option value="logout">退出</option>
                <c:forEach items="${operateList}" var="operate">
                <option value="${operate.operateId }">${operate.operateName }</option>
                </c:forEach>
                </select>
            </td>
            <th><%=SysLog.ALIAS_EVENT_RESULT%></th> 
            <td colspan="2">
                <select name="eventResult" id="eventResult" style="width: 130px;">
                    <option value="">---请选择---</option>
                    <option value="0">失败</option>
                    <option value="1">成功</option>
                </select>
            </td>
        </tr>   
        <tr>
            <td colspan="3" align="center">
            <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
            <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
            <span id="importDataSizeSpan" style="color: red; font-weight: bolder;"></span>
            </td>
        </tr>
    </table>
    </form>
    </fieldset>
    <%-- <%@ include file="/head/init_operate.jsp" %> --%>
    <div> 
	    <a class="easyui-linkbutton" iconCls="icon-search" onClick="view();" plain="true" href="javascript:void(0);">查看</a>
	    <a class="easyui-linkbutton" iconCls="icon-add" onClick="refresh();" plain="true" href="javascript:void(0);" >刷新</a> 
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
	    <a class="easyui-linkbutton" iconCls="icon-search" onClick="report();" plain="true" href="javascript:void(0);">报表</a>
	    </fcc:permission>
	    <a class="easyui-linkbutton" iconCls="icon-undo" onClick="datagrid.datagrid('unselectAll');" plain="true" href="javascript:void(0);">取消选中</a> 
	</div> 
  </div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
<jsp:include page="/head/init_export.jsp">
<jsp:param name="initExportUrl" value="${basePath}manage/sys/sysLog/export.do" />
<jsp:param name="initQueryExportUrl" value="${basePath}manage/sys/sysLog/queryExport.do" />
<jsp:param name="initModel" value="sysLog" />
</jsp:include>
<jsp:include page="/head/init_import.jsp">
<jsp:param name="initImportUrl" value="${basePath}manage/sys/sysLog/import.do" />
<jsp:param name="initQueryImportUrl" value="${basePath}manage/sys/sysLog/queryImport.do" />
</jsp:include>