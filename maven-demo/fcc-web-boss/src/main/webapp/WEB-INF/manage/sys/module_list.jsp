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
<form id="userForm" name="userForm" method="post">
  <input name="id" type="hidden" value=""/>
</form>
  <table id="treegrid">
  </table>
</div>
</html>
<script type="text/javascript" charset="UTF-8">
var userForm;
var treegrid;
$(function() {

    userForm = $('#userForm').form();
    
    treegrid = $('#treegrid').treegrid({
        url : 'manage/sys/module/treegrid.do?random=' + Math.random(),
        toolbar : [ {
            text : '展开',
            iconCls : 'icon-redo',
            handler : function() {
                var node = treegrid.treegrid('getSelected');
                if (node) {
                    treegrid.treegrid('expandAll', node.id);
                } else {
                    treegrid.treegrid('expandAll');
                }
            }
        }, '-', {
            text : '折叠',
            iconCls : 'icon-undo',
            handler : function() {
                var node = treegrid.treegrid('getSelected');
                if (node) {
                    treegrid.treegrid('collapseAll', node.id);
                } else {
                    treegrid.treegrid('collapseAll');
                }
            }
        }, '-', {
            text : '刷新',
            iconCls : 'icon-reload',
            handler : function() {
                editRow = undefined;
                treegrid.treegrid('reload');
            }
        <fcc:permission operateId="add">
        }, '-', {
            text : '新增',
            iconCls : 'icon-add',
            handler : function() {
                //append();
                add();
            }
        </fcc:permission>
        <fcc:permission operateId="edit">
        }, '-', {
            text : '修改',
            iconCls : 'icon-edit',
            handler : function() {
                edit();
            }
        </fcc:permission>
        <fcc:permission operateId="delete">
        }, '-', {
            text : '删除',
            iconCls : 'icon-remove',
            handler : function() {
                del();
            }
        </fcc:permission>
        }, '-', {
            text : '取消选中',
            iconCls : 'icon-undo',
            handler : function() {
                treegrid.treegrid('unselectAll');
            }
        }, '-' ],
        title : '',
        iconCls : 'icon-save',
        fit : true,
        fitColumns : true,
        nowrap : true,
        animate : false,
        border : false,
        striped : true,
        idField : 'id',
        treeField : 'text',
        frozenColumns : [ [ {
            field : 'id',
            title : '操作ID',
            width : 100,
            hidden : true
        } ] ],
        columns : [ [ {
            field : 'text',
            title : '模块名称',
            width : 200,
            editor : {
                type : 'text'
            }
        },{
            field : 'moduleDesc',
            title : '模块地址',
            width : 200,
            editor : {
                type : 'text'
            }
        }, {
            field : 'moduleSort',
            title : '排序',
            width : 50,
            editor : {
                type : 'numberbox',
                options : {
                    min : 0,
                    max : 999,
                    required : true
                }
            }
        }, {
            field : 'operateNames',
            title : '模块操作',
            width : 200
        }, {
            field : 'operateIds',
            title : '模块操作ID',
            width : 200,
            hidden : true
        }] ],
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
                $(data).each(function(index, d) {
                    if (this.state == 'closed') {
                        t.treegrid('expandAll');
                    }
                });
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
        onLoadError : function(e) {
            window.location.href = overUrl;
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
    var node = treegrid.treegrid('getSelected');
    if (node == null) node = treegrid.treegrid('getData')[0];
    window.location.href = "${basePath}manage/sys/module/toAdd.do?parentId=" + node.id;
}

function edit() {
    var node = treegrid.treegrid('getSelected');
    if (node) {
        if (node.id == '${module_root}') {
            Tool.message.alert(Lang.tip, Lang.operateRoot, Tool.icon.info, true);
            return;
        }
        window.location.href = "${basePath}manage/sys/module/toEdit.do?id=" + node.id;
    }
}

function del() {
    var node = treegrid.treegrid('getSelected');
    if (node) {
        if (node.id == '${module_root}') {
            Tool.message.alert(Lang.tip, Lang.operateRoot, Tool.icon.info, true);
            return;
        }
        Tool.message.confirm(Lang.confirm, Lang.confirmDel, function(b) {
            if (b) {
                userForm.find('[name=id]').val(node.id);
                userForm.form('submit', {
                    url : '${basePath}manage/sys/module/delete.do',
                    onSubmit : function() {
                    	Tool.message.progress();
                        return true;
                    },
                    success : function(data) {
                        try {
                            Tool.message.progress('close');
                            if (Tool.operate.check(data, true)) {
                            	treegrid.treegrid("remove", node.id);
                            };
                        } catch(e) {
                            window.location.href = overUrl;
                        }
                    }
                });
            }
        });
    }
}
</script>