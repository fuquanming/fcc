<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.fcc.app.common.Constanst"%>
<%@ page import="com.fcc.app.sys.model.SysUser"%>
<%@ page import="com.fcc.app.cache.CacheUtil"%>

<%
SysUser user = (SysUser)request.getSession().getAttribute(Constanst.SysUserSession.USER_LOGIN);
if (!CacheUtil.isAdmin(user)) {
	return;
}
%>
<script type="text/javascript" charset="UTF-8">
	var fileForm;
	var tree;
	$(function() {

		fileForm = $('#fileForm').form();
		tree = $('#tree').tree({
			checkbox: false,
			url : 'manage/sys/file/rootFile.do?random=' + Math.random(),
			animate : true,
			lines : !Tool.isLessThanIe8(),
			onClick : function(node) {
				loadDirectoryList(node.id);
				addTabFun({
					src : 'manage/sys/file/file_list.jsp?fileId=' + node.id,
					title : node.attributes.filePath
				});
			},
			onLoadSuccess : function(node, data) {
				var t = $(this);
				if (data) {
					$(data).each(function(index, d) {
						if (this.state == 'closed') {
							t.tree('expandAll');
						}
					});
				}
			},
			onLoadError : function(e) {
				alert(e)
				//window.location.href = overUrl;
			}
		});
	});
	
	function loadDirectoryList(fileId) {
		$('#fileId').val(fileId)
		fileForm.form('submit', {
			url : 'manage/sys/file/fileList.do?fileType=directory',
			success : function(data) {
				try {
					var d = $.parseJSON(data);
					var node = tree.tree('getSelected');
					if (node){
						// 移除旧数据
						var children = tree.tree('getChildren', node.target);
						if (children) {
							var len = children.length;
							for (var i = 0; i < len; i++) {
								tree.tree('remove', children[i].target);
							}
						}
						// 添加新数据
						tree.tree('append', { 
							parent: node.target, 
							data: d
						});
					}
				} catch(e) {
					alert(e)
					//window.location.href = overUrl;
				}
			}
		});
	}
	
	function collapseAll() {
		var node = tree.tree('getSelected');
		if (node) {
			tree.tree('collapseAll', node.target);
		} else {
			tree.tree('collapseAll');
		}
	}
	function expandAll() {
		var node = tree.tree('getSelected');
		if (node) {
			tree.tree('expandAll', node.target);
		} else {
			tree.tree('expandAll');
		}
	}
	/*
	String.prototype.replaceAll = function(s1,s2) { 
	    return this.replace(new RegExp(s1,"gm"),s2); 
	}
	*/ 
	
</script>

<div class="easyui-panel" fit="true" border="false">
  <div class="easyui-accordion" fit="true" border="false">
    <div title="文件管理" iconCls="icon-tip">
      <div class="easyui-layout" fit="true">
      	
        <div region="north" border="false" style="overflow: hidden;"> 
          <a href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-redo" onclick="expandAll();">展开</a><a href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-undo" onclick="collapseAll();">折叠</a><a href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-reload" onclick="tree.tree('reload');">刷新</a>
          <hr style="border-color: #fff;" />
        </div>
        
        <div region="center" border="false">
          <ul id="tree" style="margin-top: 5px;">
          </ul>
        </div>
      </div>
    </div>
  </div>
  <form id="fileForm" name="fileForm" method="post">
  <input id="fileId" name="fileId" type="hidden" value=""/>
  </form>
</div>
