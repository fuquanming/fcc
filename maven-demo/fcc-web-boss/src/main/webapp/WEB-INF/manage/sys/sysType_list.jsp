<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
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
			url : 'manage/sys/sysType/treegrid.do?random=' + Math.random(),
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
			<fcc:permission moduleId="${rightModuleId}" operateId="add">
			}, '-', {
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					//append();
					add();
				}
			</fcc:permission>
			<fcc:permission moduleId="${rightModuleId}" operateId="edit">
			}, '-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					edit();
				}
			</fcc:permission>
			<fcc:permission moduleId="${rightModuleId}" operateId="delete">
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
				title : '数据类型ID',
				width : 100,
				hidden : true
			} ] ],
			columns : [ [ {
				field : 'text',
				title : '数据类型名称',
				width : 100,
				editor : {
					type : 'text'
				}
			},{
				field : 'organDesc',
				title : '数据类型说明',
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
						$.messager.show({
							msg : '数据库异常！',
							title : '提示'
						});
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
			}
		});

	});
	
	function add() {
		var node = treegrid.treegrid('getSelected');
		if (node == null) node = treegrid.treegrid('getData')[0];
		window.location.href = "<%=basePath%>manage/sys/sysType/toAdd.do?parentId=" + node.id;
	}

	function edit() {
		var node = treegrid.treegrid('getSelected');
		if (node) {
			if (node.id == 'SYSTYPE_ROOT') {
				$.messager.show({
					msg : '不能修改根节点!',
					title : '提示'
				});
				return;
			}
			window.location.href = "<%=basePath%>manage/sys/sysType/toEdit.do?id=" + node.id;
		}
	}
	
	function del() {
		var node = treegrid.treegrid('getSelected');
		if (node) {
			if (node.id == '0') {
				$.messager.show({
					msg : '不能删除根节点!',
					title : '提示'
				});
				return;
			}
			$.messager.confirm('询问', '您确定要删除【' + node.text + '】记录？', function(b) {
				if (b) {
					userForm.find('[name=id]').val(node.id);
					userForm.form('submit', {
						url : '<%=basePath%>manage/sys/sysType/delete.do',
						onSubmit : function() {
							return true;
						},
						success : function(data) {
							try {
								var d = $.parseJSON(data);
								$.messager.show({
									msg : d.msg,
									title : '提示'
								});
								if (d.success) {
									treegrid.treegrid("remove", node.id);
									//treegrid.treegrid('reload');
									//parent.tree.tree('reload');
								}
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
