<%@page import="com.fcc.app.sys.model.*" %>
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
			<c:if test="${empty param.lazy}">url : '<%=basePath%>manage/sys/sysLog/datagrid.do?random=' + Math.random(),</c:if>
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
	        'uploader':'<%=basePath%>manage/sys/sysLog/import.do?JSESSIONID=<%=session.getId()%>', 
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
				names.push(rows[i].userName);
			}
			$.messager.show({
				msg : '只能择一条记录进行查看！您已经选择了【' + names.join(',') + '】' + rows.length + '条记录',
				title : '提示'
			});
		} else if (rows.length == 1) {
			window.location.href = "<%=basePath%>manage/sys/sysLog/toView.do?id=" + rows[0].logId;
		}
	}

	function add() {
		window.location.href = "<%=basePath%>manage/sys/sysLog/toAdd.do";
	}

	function edit() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length != 1 && rows.length != 0) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].userName);
			}
			$.messager.show({
				msg : '只能择一条记录进行编辑！您已经选择了【' + names.join(',') + '】' + rows.length + '条记录',
				title : '提示'
			});
		} else if (rows.length == 1) {
			window.location.href = "<%=basePath%>manage/sys/sysLog/toEdit.do?id=" + rows[0].logId;
		}
	}

	function del() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要删除当前所选记录？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].logId);
					}
					var idsVal = ids.join(',');
					userForm.find('[name=ids]').val(idsVal);
					userForm.form('submit', {
						url : '<%=basePath%>manage/sys/sysLog/delete.do',
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
		<c:if test="${not empty param.lazy}">datagrid = $('#datagrid').datagrid({url : '<%=basePath%>manage/sys/sysLog/datagrid.do?random=' + Math.random()});</c:if>
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
	}
	function clearFun() {
		$('#toolbar input').val('');
		$('#moduleName').val('');
		$('#operateName').val('');
		$('#eventResult').val('');
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
			url : '<%=basePath%>manage/sys/sysLog/export.do',
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
			url : '<%=basePath%>manage/sys/sysLog/queryExport.do?random=' + Math.random(),
			cache : false,
			dataType : "json",
			success : function(d) {
				try {
					if (d.empty == true) {
						$('#expordDataSizeSpan').html("无数据导出！");
						exportDataFlag = false;
					} else if (d.fileName != null) {
						$('#expordDataSizeSpan').html("已导出：" + d.currentSize + "条，导出完成！若没有提示下载，请点击 <a href='" + '<%=basePath%>exportData/sysLogExport/' + d.fileName + "'>" + d.fileName + "</a>");
						exportDataFlag = false;
						window.location.href = '<%=basePath%>exportData/sysLogExport/' + d.fileName;
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
			url : '<%=basePath%>manage/sys/sysLog/queryImport.do?random=' + Math.random(),
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
		window.location.href = "<%=basePath%>manage/sys/sysLog/report/view.do";
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
