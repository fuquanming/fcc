<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

</body>
</html>
<script type="text/javascript" charset="UTF-8">
    var userForm;
    var treegrid;
    var iconData = [ {
        iconcls : '',
        text : '默认图标'
    }, {
        iconcls : 'icon-add',
        text : 'icon-add'
    }, {
        iconcls : 'icon-edit',
        text : 'icon-edit'
    }, {
        iconcls : 'icon-remove',
        text : 'icon-remove'
    }, {
        iconcls : 'icon-save',
        text : 'icon-save'
    }, {
        iconcls : 'icon-cut',
        text : 'icon-cut'
    }, {
        iconcls : 'icon-ok',
        text : 'icon-ok'
    }, {
        iconcls : 'icon-no',
        text : 'icon-no'
    }, {
        iconcls : 'icon-cancel',
        text : 'icon-cancel'
    }, {
        iconcls : 'icon-reload',
        text : 'icon-reload'
    }, {
        iconcls : 'icon-search',
        text : 'icon-search'
    }, {
        iconcls : 'icon-print',
        text : 'icon-print'
    }, {
        iconcls : 'icon-help',
        text : 'icon-help'
    }, {
        iconcls : 'icon-undo',
        text : 'icon-undo'
    }, {
        iconcls : 'icon-redo',
        text : 'icon-redo'
    }, {
        iconcls : 'icon-back',
        text : 'icon-back'
    }, {
        iconcls : 'icon-sum',
        text : 'icon-sum'
    }, {
        iconcls : 'icon-tip',
        text : 'icon-tip'
    } ];
    $(function() {
    
        userForm = $('#userForm').form();
        
        treegrid = $('#treegrid').treegrid({
            url : 'manage/sys/organ/treegrid.do?random=' + Math.random(),
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
            idField : 'id',
            treeField : 'text',
            frozenColumns : [ [ {
                field : 'id',
                title : '组织机构ID',
                width : 100,
                hidden : true
            } ] ],
            columns : [ [ {
                field : 'text',
                title : '组织机构名称',
                width : 100,
                editor : {
                    type : 'text'
                }
            },{
                field : 'organDesc',
                title : '组织机构说明',
                width : 200,
                editor : {
                    type : 'text'
                }
            }, {
                field : 'organSort',
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
                        Tool.message.alert('错误', msg, 'error'); 
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
        var nodeFlag = true;
        var node = treegrid.treegrid('getSelected');
        if (node == null) {
            node = treegrid.treegrid('getData')[0];
            nodeFlag = false;
        }
        var organId = '${organId}';
        var indexOf = node.id.indexOf(organId);
        if (organId != null && organId != '' && indexOf != 0) {
            Tool.message.alert('提示', '不能添加上级机构！', 'error'); 
        } else {
            var id = node.id;
            if (nodeFlag == false) id = '';
            window.location.href = "${basePath}manage/sys/organ/toAdd.do?parentId=" + id;
        }
    }
    
    function checkParent() {
        var node = treegrid.treegrid('getSelected');
        if (node) {
            var organId = '${organId}';
            var indexOf = organId.indexOf(node.id);
            if (indexOf == 0 && organId.length != node.id.length) {
                return true;    
            }
        }
        return false;
    }

    function edit() {
        var node = treegrid.treegrid('getSelected');
        if (node) {
            if (node.id == 'ORGANENTITY_ROOT') {
                Tool.message.alert('提示', '不能修改根节点!', 'error'); 
                return;
            }
            <%-- 不能修改所属上级的组织机构 --%>
            if (checkParent()) {
                Tool.message.alert('提示', '不能修改上级机构!', 'error'); 
                return;
            }
            window.location.href = "${basePath}manage/sys/organ/toEdit.do?id=" + node.id;
        }
    }
    
    function del() {
        var node = treegrid.treegrid('getSelected');
        if (node) {
            if (node.id == 'ORGANENTITY_ROOT') {
                Tool.message.alert('提示', '不能删除根节点!', 'error');
                return;
            }
            <%-- 不能删除所属上级的组织机构 --%>
            if (checkParent()) {
                Tool.message.alert('提示', '不能删除上级机构!', 'error');
                return;
            }
            Tool.message.confirm('询问', '您确定要删除【' + node.text + '】记录？', function(b) {
                if (b) {
                    userForm.find('[name=id]').val(node.id);
                    userForm.form('submit', {
                        url : '${basePath}manage/sys/organ/delete.do',
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
