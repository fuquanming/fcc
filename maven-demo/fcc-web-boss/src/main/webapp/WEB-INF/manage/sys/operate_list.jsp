<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;display: none;">
    <fieldset>
    <legend>筛选</legend>
    <table class="tableForm">
      <tr>
        <th>操作名称</th>
        <td><input name="searchName" style="width: 305px;" />
        </td>
        <td>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
        </td>
      </tr>
    </table>
    </fieldset>
    <div>
    <fcc:permission operateId="add">
    <a class="easyui-linkbutton" iconCls="icon-add" onClick="add();" plain="true" href="javascript:void(0);">新增</a> 
    </fcc:permission>
    <fcc:permission operateId="edit">
    <a class="easyui-linkbutton" iconCls="icon-edit" onClick="edit();" plain="true" href="javascript:void(0);">修改</a>
    </fcc:permission>
    <fcc:permission operateId="delete">
    <a class="easyui-linkbutton" iconCls="icon-remove" onClick="del();" plain="true" href="javascript:void(0);">删除</a>
    </fcc:permission>
    <a class="easyui-linkbutton" iconCls="icon-undo" onClick="datagrid.datagrid('unselectAll');" plain="true" href="javascript:void(0);">取消选中</a> </div>
  </div>
  <table id="datagrid">
  </table>
</div>
<div id="userDialog" style="display: none;overflow: hidden;">
  <form id="userForm" name="userForm" method="post">
    <input name="operateValue" type="hidden" value=""/>
    <input name="ids" type="hidden" value=""/>
    <table class="tableForm">
      <tr>
        <th>操作ID</th>
        <td><input name="operateId" class="easyui-validatebox" required="true" maxlength="100"/></td>
      </tr>
      <tr>
        <th>操作名称</th>
        <td><input name="operateName" class="easyui-validatebox" required="true" maxlength="100"/></td>
      </tr>
    </table>
  </form>
</div>
</body>
</html>
<script type="text/javascript" charset="UTF-8">
    var datagrid;
    var userDialog;
    var userForm;
    $(function() {

        userForm = $('#userForm').form();
        
        userDialog = $('#userDialog').show().dialog({
            modal : true,
            title : '模块操作',
            buttons : [ {
                text : '确定',
                handler : function() {
                    if (userForm.find('[name=operateValue]').val() != '') {
                        userForm.form('submit', {
                            url : '${basePath}manage/sys/operate/edit.do',
                            success : function(data) {
                                try {
                                	Tool.message.progress('close');
                                        // 动态添加datagrid
                                       /*  var selectedRow = datagrid.datagrid('getSelected');
                                        var rowIndex = datagrid.datagrid('getRowIndex', selectedRow);
                                        datagrid.datagrid('updateRow',{
                                            index: rowIndex,
                                            row: {
                                                operateName : userForm.find('[name=operateName]').val()
                                            }
                                        });  */
                                    if (Tool.operate.check(data, true)) {
                                    	userDialog.dialog('close');
                                        searchFun();
                                    }
                                } catch(e) {
                                	console.log(e);
                                    window.location.href = overUrl;
                                }
                            },
                            onSubmit : function() {
                                var isValid = $(this).form('validate');
                                if (isValid) Tool.message.progress();
                                return isValid;
                            }
                        });
                    } else {
                        userForm.form('submit', {
                            url : '${basePath}manage/sys/operate/add.do',
                            success : function(data) {
                                try {
                                	Tool.message.progress('close');
                                    if (Tool.operate.check(data, true)) {
                                    	searchFun();
                                    }
                                } catch(e) {
                                	console.log(e);
                                    window.location.href = overUrl;
                                }
                            },
                            onSubmit : function() {
                                var isValid = $(this).form('validate');
                                if (isValid) Tool.message.progress();
                                return isValid;
                            }
                        });
                    }
                }
            } ]
        }).dialog('close');
        
        datagrid = $('#datagrid').datagrid({
            url : '${basePath}manage/sys/operate/datagrid.do?random=' + Math.random(),
            toolbar : '#toolbar',
            title : '',
            iconCls : 'icon-save',
            pagination : true,
            pageSize : 10,
            pageList : [ 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 ],
            fit : true,
            fitColumns : true,
            nowrap : false,
            border : false,
            striped : true,
            idField : 'operateId',
            frozenColumns : [ [ {
                field : 'id',
                width : 50,
                checkbox : true
            }, {
                field : 'operateId',
                title : '操作ID',
                width : 100
            } ] ],
            columns : [ [ {
                field : 'operateName',
                title : '操作名称',
                width : 100
            } , {
                field : 'operateValue',
                title : '操作值',
                width : 150
            }] ],
            onRowContextMenu : function(e, rowIndex, rowData) {
                e.preventDefault();
                $(this).datagrid('unselectAll');
                $(this).datagrid('selectRow', rowIndex);
                $('#menu').menu('show', {
                    left : e.pageX,
                    top : e.pageY
                });
            },
            onLoadError : function(e) {
            	console.log(e);
            	window.location.href = overUrl;
            },
            onLoadSuccess : function(data) {
                if (data.msg && data.msg != '') {
                    Tool.message.alert('错误', data.msg, 'error'); 
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

    function add() {
        userDialog.dialog('open');
        userForm.find('[name=operateId]').removeAttr('readonly');
        userForm.form('clear');
    }

    function edit() {
        var rows = datagrid.datagrid('getSelections');
        if (rows.length != 1 && rows.length != 0) {
            var names = [];
            for ( var i = 0; i < rows.length; i++) {
                names.push(rows[i].operateName);
            }
            Tool.message.show({
                msg : '只能择一条记录进行编辑！您已经选择了【' + names.join(',') + '】' + rows.length + '条记录',
                title : '提示'
            });
        } else if (rows.length == 1) {
            userForm.find('[name=operateId]').attr('readonly', 'readonly');
            userDialog.dialog('open');
            userForm.form('clear');
            userForm.form('load', {
                operateId : rows[0].operateId,
                operateName : rows[0].operateName,
                operateValue : rows[0].operateValue
            });
        } else {
        	Tool.message.alert({title:'提示', msg:'请选择要修改的记录！', icon:'warning'});
        }
    }

    function del() {
        var ids = [];
        var rows = datagrid.datagrid('getSelections');
        if (rows.length > 0) {
        	Tool.message.confirm('请确认', '您要删除当前所选记录？', function(r) {
                if (r) {
                    for ( var i = 0; i < rows.length; i++) {
                        ids.push(rows[i].operateId);
                    }
                    var idsVal = ids.join(',');
                    userForm.find('[name=ids]').val(idsVal);
                    userForm.form('submit', {
                        url : '${basePath}manage/sys/operate/delete.do',
                        onSubmit : function() {
                        	Tool.message.progress();
                            return true;
                        },
                        success : function(data) {
                            try {
                            	Tool.message.progress('close');
                                if (Tool.operate.check(data, true)) {
                                    userDialog.dialog('close');
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
        	Tool.message.alert({title:'提示', msg:'请选择要删除的记录！', icon:'warning'});
        }
    }

    function searchFun() {
        datagrid.datagrid('load', {
            searchName : $('#toolbar input[name=searchName]').val()
        });
        datagrid.datagrid('clearSelections');
        // datagrid.datagrid('reload');
    }
    function clearFun() {
        $('#toolbar input').val('');
        datagrid.datagrid('load', {});
    }
</script>