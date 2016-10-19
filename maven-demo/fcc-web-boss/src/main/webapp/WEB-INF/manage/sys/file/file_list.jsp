<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.fcc.app.common.Constanst"%>
<%@ page import="com.fcc.app.sys.model.SysUser"%>
<%@ page import="com.fcc.app.cache.CacheUtil"%>

<%
SysUser user = (SysUser)request.getSession().getAttribute(Constanst.SysUserSession.USER_LOGIN);
if (!CacheUtil.isAdmin(user)) {
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<%@ include file="/head/upload_js.jsp" %>
<script type="text/javascript" charset="UTF-8">
	var userForm;
	var treegrid;
	var userDialog;
	$(function() {
		userForm = $('#userForm').form();
		treegrid = $('#treegrid').treegrid({
			url : 'manage/sys/file/fileList.do?fileId=${param.fileId}',
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
					editingId = undefined;
					treegrid.treegrid('reload');
				}
			}, '-', {
				text : '新建文件夹',
				iconCls : 'icon-add',
				handler : function() {
					addFile();
				}
			}, '-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					edit();
				}
			}, '-', {
				text : '保存',
				iconCls : 'icon-edit',
				handler : function() {
					save();
				}
			}, '-', {
				text : '取消',
				iconCls : 'icon-edit',
				handler : function() {
					cancel();
				}
			}, '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					remove();
				}
			}, '-', {
				text : '上传',
				iconCls : 'icon-upload',
				handler : function() {
					var data = getSelectRowAndFileType();
					if (data == null) {
						$.messager.show({
							msg : '请选择一个文件',
							title : '提示'
						});
						return;
					}
					userDialog.dialog('open');
				}
			}, '-', {
				text : '下载',
				iconCls : 'icon-download',
				handler : function() {
					download();
				}
			}, '-', {
				text : '压缩',
				iconCls : 'icon-download',
				handler : function() {
					zip();
				}
			}, '-', {
				text : '取消选中',
				iconCls : 'icon-undo',
				handler : function() {
					treegrid.treegrid('unselectAll');
				}
			}, '-' ],
			singleSelect : false,
			selectOnCheck : true,
			checkOnSelect : true,
			title : '',
			iconCls : 'icon-save',
			fit : true,
			fitColumns : true,
			nowrap : true,
			animate : false,
			border : false,
			idField : 'id',
			treeField : 'text',
			rownumbers : true,
			frozenColumns : [ [ {
				field : 'id',
				title : '操作ID',
				width : 100,
				hidden : true
			} ] ],
			columns : [ [ {
				field : 'text',
				title : '名称',
				width : 500,
				editor : 'text'
			},{
				field : 'modifyTime',
				title : '修改时间',
				width : 200,
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
			}, {
				field : 'fileType',
				title : '类型',
				width : 200
			}, {
				field : 'fileSize',
				title : '大小',
				width : 200,
				formatter : function(value, rowData, rowIndex) {
					if (rowData.fileType == 'file') {
						var s = (value % 1024);
						var size = parseInt(value / 1024) + parseInt(s > 0 ? 1 : 0);
						return size + "KB";
					}
					return "";
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
			onLoadError : function(e) {
				alert(e)
				//window.location.href = overUrl;
			},
			onClickRow : function(row) {
				$('#selectFilePath').html(row.filePath);
			},
			onDblClickRow : function(row) {
				// 文件夹双击
				if (row.fileType == 'directory') {
					treegrid.treegrid("loading");
					userForm.find('[name="fileId"]').val(row.id);
					userForm.find('[name="fileType"]').val('');
					userForm.form('submit', {
						url : 'manage/sys/file/fileList.do',
						onSubmit : function() {
							return true;
						},
						success : function(data) {
							try {
								var d = $.parseJSON(data);
								//treegrid.treegrid('loadData', d);
								// 移除
								var children = treegrid.treegrid('getChildren', row.id)
								if (children) {
									var len = children.length;
									for (var i = 0; i < len; i++) {
										treegrid.treegrid('remove', children[i].id);
									}
								}
								// 添加
								treegrid.treegrid('append', {
									parent: row.id,
									data: d
								});
								treegrid.treegrid("loaded")
							} catch(e) {
								alert(e);
							}
						}
					});
				}
			}
		});


		// 上传文件
		userDialog = $('#userDialog').show().dialog({
			modal : true,
			title : '数据导入',
			buttons : [ {
				text : '上传文件',
				handler : function() {
					var data = getSelectRowAndFileType();
					if (data == null) return;
					var row = data[0];
					var fileType = data[1];
					var url = '<%=basePath%>manage/sys/file/upload.do?JSESSIONID=<%=session.getId()%>';
					url += '&fileId=' + row.id + "&fileType=" + fileType;
					try {
						$('#file_upload').uploadify('settings','uploader',url); 
						$('#file_upload').uploadify('upload', '*')
					} catch (e) {alert(e)}
				}
			}, {
				text : '取消文件',
				handler : function() {
					$('#file_upload').uploadify('cancel', '*')
				}
			}]
		}).dialog('close');

		// 上传文件
		$("#file_upload").uploadify({ 
	        //开启调试 
	        'debug' : false, 
	        //是否自动上传 
	        'auto':false, 
	        //超时时间 
	        'successTimeout':99999, 
	        //附带值 
	        'formData':{ 
	        }, 
	        //flash 
	        'swf': "js/uploadify-3.2.1/uploadify.swf", 
	        //不执行默认的onSelect事件 
	        'overrideEvents' : ['onDialogClose'], 
	        //文件选择后的容器ID 
	        //'queueID':'uploadfileQueue', 
	        //服务器端脚本使用的文件对象的名称 $_FILES个['upload'] 
	        'fileObjName':'upload', 
	        //上传处理程序  IE下不会丢失cookie
	        'uploader':'<%=basePath%>manage/sys/file/upload.do?JSESSIONID=<%=session.getId()%>', 
	        //浏览按钮的背景图片路径 
	        //'buttonImage':'<%=basePath%>js/jquery-easyui-1.3.2/themes/icons/download.png', 
	        'buttonText':'浏览',
	        //浏览按钮的宽度 
	        'width':'55', 
	        //浏览按钮的高度 
	        'height':'25', 
	        //expressInstall.swf文件的路径。 
	        'expressInstall':'expressInstall.swf', 
	        //在浏览窗口底部的文件类型下拉菜单中显示的文本 
	        'fileTypeDesc':'支持的格式：', 
	        //允许上传的文件后缀 
	        //'fileTypeExts':'*.xls;', 
	        //上传文件的大小限制 
	        //'fileSizeLimit':'1024MB', 
	        //上传数量 
	        'queueSizeLimit' : 5, 
	        //每次更新上载的文件的进展 
	        'onUploadProgress' : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) { 
	             //有时候上传进度什么想自己个性化控制，可以利用这个方法 
	             //使用方法见官方说明 
	        }, 
	        //选择上传文件后调用 
	        'onSelect' : function(file) { 
				
	        }, 
	        //返回一个错误，选择文件的时候触发 
	        'onSelectError':function(file, errorCode, errorMsg){ 
	            switch(errorCode) { 
	                case -100: 
	                    alert("上传的文件数量已经超出系统限制的"+$('#file_upload').uploadify('settings','queueSizeLimit')+"个文件！"); 
	                    break; 
	                case -110: 
	                    alert("文件 ["+file.name+"] 大小超出系统限制的"+$('#file_upload').uploadify('settings','fileSizeLimit')+"大小！"); 
	                    break; 
	                case -120: 
	                    alert("文件 ["+file.name+"] 大小异常！"); 
	                    break; 
	                case -130: 
	                    alert("文件 ["+file.name+"] 类型不正确！"); 
	                    break; 
	            } 
	        }, 
	        //检测FLASH失败调用 
	        'onFallback':function(){ 
	            alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。"); 
	        }, 
	        //上传到服务器，服务器返回相应信息到data里 
	        'onUploadSuccess':function(file, data, response){ 
	            try {
	            	var d = $.parseJSON(data);
	            	$.messager.show({
						msg : d.msg,
						title : '提示'
					});
					if (d.success) {
						userDialog.dialog('close');
					}
	            } catch(e) {
					window.location.href = overUrl;
				}
	            
	        } 
	        
	    });
	});
	// 只选择一个文件或文件夹
	function getSelectRowAndFileType() {
		var data = [];
		var rows = treegrid.treegrid('getSelections');
		var row;
		var fileType = 'file';
		if (rows){
			var length = rows.length;
			if (length == 0) {// 根目录添加 
				row = treegrid.treegrid('getRoot');
			} else if (length > 1) {// 选择的文件夹添加
				$.messager.show({
					msg : '请选择一个文件',
					title : '提示'
				});
				return;
			} else {// 选中
				row = rows[0];
				if (row.fileType == 'file') {// 选择文件
				} else if (row.fileType == 'directory') {// 选择文件夹
					fileType = 'directory';
				}
			}
		}
		data[0] = row;
		data[1] = fileType;
		return data;
	}
	
	function addFile() {
		// 提交
		var data = getSelectRowAndFileType();
		if (data == null) return;
		var row = data[0];
		var fileType = data[1];
		//return;
		userForm.find('[name=fileId]').val(row.id);
		userForm.find('[name=fileName]').val('新建文件夹');
		userForm.find('[name=fileType]').val(fileType);
		userForm.form('submit', {
			url : '<%=basePath%>manage/sys/file/addDirectory.do',
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
					// 加载添加的文件
					if (d.success) {
						var file = d.obj;
						var parentRow = treegrid.treegrid('find', file.attributes.parentId);
						var addData = [{id : file.id, text : file.text , modifyTime : file.modifyTime , fileSize : file.filsSize, iconCls : file.iconCls, fileType : file.fileType }]
						if (parentRow) {
							treegrid.treegrid('append', {
								parent : parentRow.id,
								data : addData
							});
						} else {
							treegrid.treegrid('append', {
								data : addData
							});
						}
					}
				} catch(e) {
					alert(e)
					//window.location.href = overUrl;
				}
			}
		});
	}
	
	
	var editingId;
	// 编辑文件名
	function edit() {
		if (editingId != undefined){
			treegrid.treegrid('select', editingId);
			return;
		}
		var row = treegrid.treegrid('getSelected');
		if (row){
			editingId = row.id
			treegrid.treegrid('beginEdit', editingId);
		}
	}
	// 保存文件名
	function save(){
		if (editingId != undefined){
			treegrid.treegrid('endEdit', editingId);
			editingId = undefined;
			// 提交保存文件名
			var row = treegrid.treegrid('getSelected');
			userForm.find('[name=fileId]').val(row.id);
			userForm.find('[name=fileName]').val(row.text);
			userForm.form('submit', {
				url : '<%=basePath%>manage/sys/file/edit.do',
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
						treegrid.treegrid('reload');
					} catch(e) {
						window.location.href = overUrl;
					}
				}
			});
		}
	}
	// 取消编辑
	function cancel(){
		if (editingId != undefined){
			treegrid.treegrid('cancelEdit', editingId);
			editingId = undefined;
		}
	}
	
	// 删除文件
	function remove() {
		var rows = treegrid.treegrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('询问', '您要删除当前所选记录？', function(b) {
				if (b) {
					var ids = [];
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					var idsVal = ids.join(',');
					userForm.find('[name=fileId]').val(ids);
					userForm.form('submit', {
						url : '<%=basePath%>manage/sys/file/delete.do',
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
									for ( var i = 0; i < rows.length; i++) {
										var row = treegrid.treegrid("find", rows[i].id);
										if (row) {
											treegrid.treegrid("remove", row.id);
										}
									}
								}
							} catch(e) {
								alert(e)
								//window.location.href = overUrl;
							}
						}
					});
				}
			});
		}
	}
	// 下载文件
	function download() {
		var node = treegrid.treegrid('getSelected');
		if (node) {
			if (node.fileType != 'file') {
				$.messager.show({
					msg : '请选择文件',
					title : '提示'
				});
				return;
			}
			userForm.find('[name="fileId"]').val(node.id);
			userForm.form('submit', {
				url : '<%=basePath%>manage/sys/file/downloadFile.do',
				onSubmit : function() {
					return true;
				}
			});
		}
	}
	
	// 压缩文件
	function zip() {
		var rows = treegrid.treegrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('询问', '您要压缩当前所选记录？', function(b) {
				if (b) {
					var ids = [];
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					var idsVal = ids.join(',');
					var row = treegrid.treegrid('getRoot');// 选择的保存位置的文件
					userForm.find('[name=fileId]').val(ids);
					userForm.find('[name=currentFileId]').val(row.id);
					treegrid.treegrid("loading");
					userForm.form('submit', {
						url : '<%=basePath%>manage/sys/file/zip.do',
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
									var file = d.obj;
									var addData = [{id : file.id, text : file.text , modifyTime : file.modifyTime , fileSize : file.fileSize, iconCls : file.iconCls, fileType : file.fileType }]
									treegrid.treegrid('append', {
										data : addData
									});
								}
								treegrid.treegrid("loaded");
							} catch (e) {
								alert(e);
								treegrid.treegrid("loaded");
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
<form id="userForm" name="userForm" method="post" action="manage/sys/file/downloadFile.do">
  <input name="fileId" type="hidden" value=""/>
  <input name="fileName" type="hidden" value=""/>
  <input name="fileType" type="hidden" value=""/>
  <input name="currentFileId" type="hidden" value=""/>
</form>
  <div style="height: 20px; font-size: 20px; margin-left: 10px; margin-top: 5px; margin-bottom: 5px;">当前位置：<span id="selectFilePath"></span></div>
  <table id="treegrid">
  </table>
  	<div id="userDialog" style="width: 500px; height: 350px; margin: 0 auto; overflow: hidden;">
	<span name="file_upload" id="file_upload"></span>
	</div>
</div>

</body>
</html>
