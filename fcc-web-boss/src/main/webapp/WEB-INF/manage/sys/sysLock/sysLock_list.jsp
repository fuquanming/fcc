<%@page import="com.fcc.web.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<%@ include file="/head/upload_js.jsp" %>
<script type="text/javascript" charset="UTF-8">
	var datagrid;
	var userForm;
	var userDialog;
	var importDataFlag = false;
	$(function() {

		userForm = $('#userForm').form();

		datagrid = $('#datagrid').datagrid({
			<c:if test="${empty param.lazy}">url : '<%=basePath%>manage/sys/sysLock/datagrid.do?random=' + Math.random(),</c:if>
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
			idField : 'lockKey',
			frozenColumns : [ [ 
			{
				field : 'id',
				width : 50,
				checkbox : true
			}, {
					field : 'lockKey',
					title : '<%=SysLock.ALIAS_LOCK_KEY%>'
			} 
			 ] ],
			columns : [ [ 
				{
					field : 'lockStatus',
					title : '<%=SysLock.ALIAS_LOCK_STATUS%>',
					sortable:true,
					width : 100
				} ,
				{
					field : 'createTime',
					title : '<%=SysLock.ALIAS_CREATE_TIME%>',
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
					$.messager.show({
						msg : '数据库异常！',
						title : '提示'
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
					$('#file_upload').uploadify('upload', '*')
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
	        'uploader':'<%=basePath%>manage/sys/sysLock/import.do?JSESSIONID=<%=session.getId()%>', 
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
	        'fileTypeExts':'*.xls;*.xlsx;', 
	        //上传文件的大小限制 
	        'fileSizeLimit':'10MB', 
	        //上传数量 
	        'queueSizeLimit' : 1, 
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
						importDataFlag = true;
						searchImportDataSize();
						userDialog.dialog('close');
					} else {
						importDataFlag = false;
					}
	            } catch(e) {
					window.location.href = overUrl;
				}
	            
	        } 
	        
	    });

	});
	
	function view() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length != 1 && rows.length != 0) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].lockKey);
			}
			$.messager.show({
				msg : '只能择一条记录进行查看！您已经选择了【' + names.join(',') + '】' + rows.length + '条记录',
				title : '提示'
			});
		} else if (rows.length == 1) {
			window.location.href = "<%=basePath%>manage/sys/sysLock/toView.do?id=" + rows[0].lockKey;
		}
	}

	function add() {
		window.location.href = "<%=basePath%>manage/sys/sysLock/toAdd.do";
	}

	function edit() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length != 1 && rows.length != 0) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].lockKey);
			}
			$.messager.show({
				msg : '只能择一条记录进行编辑！您已经选择了【' + names.join(',') + '】' + rows.length + '条记录',
				title : '提示'
			});
		} else if (rows.length == 1) {
			window.location.href = "<%=basePath%>manage/sys/sysLock/toEdit.do?id=" + rows[0].lockKey;
		}
	}

	function del() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要删除当前所选记录？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].lockKey);
					}
					var idsVal = ids.join(',');
					userForm.find('[name=ids]').val(idsVal);
					userForm.form('submit', {
						url : '<%=basePath%>manage/sys/sysLock/delete.do',
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
									// 动态删除datagrid
									var rows = datagrid.datagrid('getSelections');
									while (rows.length > 0) {
										var rowIndex = datagrid.datagrid('getRowIndex', rows[0]);
										datagrid.datagrid('deleteRow',rowIndex);
										rows = datagrid.datagrid('getSelections');
									}
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
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}

	function searchFun() {
		<c:if test="${not empty param.lazy}">datagrid = $('#datagrid').datagrid({url : '<%=basePath%>manage/sys/sysLock/datagrid.do?random=' + Math.random()});</c:if>
		datagrid.datagrid('load', {
				lockStatus : $('#toolbar input[name=lockStatus]').val(),
				createTimeBegin : $('#toolbar input[name=createTimeBegin]').val(),
				createTimeEnd : $('#toolbar input[name=createTimeEnd]').val()
		});
	}
	function clearFun() {
		$('#toolbar input').val('');
		datagrid.datagrid('load', {});
	}
	
	var exportDataFlag = false;
	function exportData() {
		if (exportDataFlag) {
			$.messager.show({
				msg : '正在导出请稍后...',
				title : '提示'
			});
			return;
		}
		exportDataFlag = true;
		cleanExportDataSizeSpan();
		userForm.form('submit', {
			url : '<%=basePath%>manage/sys/sysLock/export.do',
			success : function(data) {
				try {
					var d = $.parseJSON(data);
					$.messager.show({
						msg : d.msg,
						title : '提示'
					});
					if (d.success) {
						$('#expordDataSizeSpan').html(d.msg);
						window.setTimeout("searchExportDataSize()", 2000);
					} else {
						exportDataFlag = false;
					}
				} catch(e) {
					window.location.href = overUrl;
				}
			}
		});
	}
	
	function searchExportDataSize() {
		$.ajax({
			url : '<%=basePath%>manage/sys/sysLock/queryExport.do?random=' + Math.random(),
			cache : false,
			dataType : "json",
			success : function(d) {
				try {
					if (d.empty == true) {
						$('#expordDataSizeSpan').html("无数据导出！");
						exportDataFlag = false;
					} else if (d.fileName != null) {
						$('#expordDataSizeSpan').html("已导出：" + d.currentSize + "条，导出完成！若没有提示下载，请点击 <a href='" + '<%=basePath%>exportData/sysLockExport/' + d.fileName + "'>" + d.fileName + "</a>");
						exportDataFlag = false;
						window.location.href = '<%=basePath%>exportData/sysLockExport/' + d.fileName;
					} else if (d.error == true) {
						$('#expordDataSizeSpan').html("导出异常！");
						exportDataFlag = false;
					} else if (d.detroy == true) {
						$('#expordDataSizeSpan').html("系统停止中！");
						exportDataFlag = false;
					} else {
						$('#expordDataSizeSpan').html("已导出：" + d.currentSize + "条");
						window.setTimeout("searchExportDataSize()", 2000);
					}
				} catch(e) {
					exportDataFlag = false;
					window.location.href = overUrl;
				}
			}
		});
	}
	
	function cleanExportDataSizeSpan() {
		$('#expordDataSizeSpan').html('');
	}
	
	function importData() {
		if (importDataFlag) {
			$.messager.show({
				msg : '正在导入请稍后...',
				title : '提示'
			});
			return;
		}
		userDialog.dialog('open');
	}
	
	function searchImportDataSize() {
		$.ajax({
			url : '<%=basePath%>manage/sys/sysLock/queryImport.do?random=' + Math.random(),
			cache : false,
			dataType : "json",
			success : function(d) {
				try {
					if (d.importFlag == true) {
						$('#importDataSizeSpan').html("导入完成！已导入：" + d.currentSize + "条");
						importDataFlag = false;
					} else {
						$('#importDataSizeSpan').html("已导入：" + d.currentSize + "条");
						window.setTimeout("searchImportDataSize()", 2000);
					}
				} catch(e) {
					importDataFlag = false;
					window.location.href = overUrl;
				}
			}
		});
	}
	
	function cleanImportDataSizeSpan() {
		$('#importDataSizeSpan').html('');
	}
	
	function report()　{
		window.location.href = "<%=basePath%>manage/sys/sysLock/report/view.do";
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
			<th><%=SysLock.ALIAS_LOCK_STATUS%></th>	
			<td colspan="2">
				<input id="lockStatus" name="lockStatus" maxlength="10"  style="width: 120px;"/>
			</td>
			<th><%=SysLock.ALIAS_CREATE_TIME%></th>	
			<td colspan="2">
				<input value="" class="easyui-datebox" style="width: 120px;" id="createTimeBegin" name="createTimeBegin"   />
				<input value="" class="easyui-datebox" style="width: 120px;" id="createTimeEnd" name="createTimeEnd"   />
			</td>
		</tr>	
		<tr>
	        <td colspan="3" align="left">
	        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
	        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
	        <span id="importDataSizeSpan" style="color: red; font-weight: bolder;"></span> 
        	</td>
      	</tr>
	</table>
	</form>
    </fieldset>
    <div id="userDialog" style="width: 500px; height: 350px; margin: 0 auto; overflow: hidden;">
	<span name="file_upload" id="file_upload"></span>
	</div>
    <div> 
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="view();" plain="true" href="javascript:void(0);">查看</a> 
    <fcc:permission moduleId="${rightModuleId}" operateId="add">
    <a class="easyui-linkbutton" iconCls="icon-add" onClick="add();" plain="true" href="javascript:void(0);">新增</a> 
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="edit">
    <a class="easyui-linkbutton" iconCls="icon-edit" onClick="edit();" plain="true" href="javascript:void(0);">修改</a>
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="delete">
    <a class="easyui-linkbutton" iconCls="icon-remove" onClick="del();" plain="true" href="javascript:void(0);">删除</a>  
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="export">
    <a class="easyui-linkbutton" iconCls="icon-download" onClick="exportData();" plain="true" href="javascript:void(0);">导出</a>
    </fcc:permission> 
    <fcc:permission moduleId="${rightModuleId}" operateId="import">
    <a class="easyui-linkbutton" iconCls="icon-upload" onClick="importData();" plain="true" href="javascript:void(0);">导入</a>
    </fcc:permission> 
    <fcc:permission moduleId="${rightModuleId}" operateId="report">
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="report();" plain="true" href="javascript:void(0);">报表</a>
    </fcc:permission>
    <a class="easyui-linkbutton" iconCls="icon-undo" onClick="datagrid.datagrid('unselectAll');" plain="true" href="javascript:void(0);">取消选中</a> 
    <span id="expordDataSizeSpan" style="color: red; font-weight: bolder;"></span>
    </div>
  </div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
