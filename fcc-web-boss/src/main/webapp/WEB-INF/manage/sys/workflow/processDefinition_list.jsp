<%@page import="com.fcc.app.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%-- 流程定义及部署管理 --%>
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
			url : '<%=basePath%>manage/sys/workflow/processDefinition/datagrid.do?random=' + Math.random(),
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
			idField : 'processDefinitionId',
			frozenColumns : [ [ 
			{
				field : 'id',
				width : 50,
				checkbox : true
			}, {
					title : 'ID',
					field : 'processDefinitionId',
					hidden : false
			} 
			 ] ],
			columns : [ [ 
				{
					field : 'processDeploymentId',
					title : '流程部署ID',
					sortable: false,
					width : 50
				} ,
				{
					field : 'processName',
					title : '流程名称',
					width : 100
				} ,
				{
					field : 'processKey',
					title : '流程KEY',
					width : 100
				} ,
				{
					field : 'processVersion',
					title : '流程版本',
					width : 30
				} ,
				{
					field : 'processResourceName',
					title : 'XML',
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						return '<a target="_blank" href="<%=basePath%>manage/sys/workflow/processDefinition/resource/read.do?processDefinitionId=' + rowData.processDefinitionId + '&resourceType=xml">' + value + '</a>';
					}
				} ,
				{
					field : 'processDiagramResourceName',
					title : '图片',
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						return '<a target="_blank" href="<%=basePath%>manage/sys/workflow/processDefinition/resource/read.do?processDefinitionId=' + rowData.processDefinitionId + '&resourceType=image">' + value + '</a>';
					}
				} ,
				{
					field : 'deploymentTime',
					title : '部署时间',
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
					}
				} ,
				{
					field : 'processSuspended',
					title : '是否挂起',
					width : 30,
					formatter : function(value, rowData, rowIndex) {
						var msg = '否'
						if (value == true) {
							msg = '是';
						}
						return msg;
					}
				}  ,
				{
					field : 'processSuspended1',
					title : '操作',
					width : 100,
					formatter : function(value, rowData, rowIndex) {
						var processDefinitionId = rowData.processDefinitionId;
						var msg = '';
						<fcc:permission moduleId="${rightModuleId}" operateId="edit">
						msg = '<a href="javascript:void(0)" onclick="edit(\'suspend\', \'' + processDefinitionId + '\')">挂起</a>';
						if (rowData.processSuspended == true) {
							msg = '<a href="javascript:void(0)" onclick="edit(\'activate\', \'' + processDefinitionId + '\')">激活</a>'
						}
						</fcc:permission>						
						msg += ' <a href="javascript:void(0)" onclick="convertToModel(\'' + processDefinitionId + '\')">转换为Model</a>';
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
	        'uploader':'<%=basePath%>manage/sys/workflow/processDefinition/add.do?JSESSIONID=<%=session.getId()%>', 
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
	        'fileTypeExts':'*.zip;*.bar;*.bpmn;*.bpmn20.xml', 
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
	
	function edit(status, id) {
		var msg = '';
		if (status == 'activate') {
			msg = '您要 激活 当前所选记录？';
		} else if (status == 'suspend') {
			msg = '您要 挂起 当前所选记录？';
		}
		$.messager.confirm('请确认', msg, function(r) {
			if (r) {
				userForm.form('submit', {
					url : '<%=basePath%>manage/sys/workflow/processDefinition/edit.do?processDefinitionId=' + id + "&status=" + status,
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
								datagrid.datagrid('unselectAll');
								searchFun();
							}
						} catch(e) {
							window.location.href = overUrl;
						}
					}
				});
			}
		});
	}

	function del() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要删除当前所选记录？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].processDeploymentId);
					}
					var idsVal = ids.join(',');
					userForm.find('[name=ids]').val(idsVal);
					userForm.form('submit', {
						url : '<%=basePath%>manage/sys/workflow/processDefinition/delete.do',
						onSubmit : function() {
							$.messager.progress({
								text : '数据处理中，请稍后....'
							});
							return true;
						},
						success : function(data) {
							try {
								$.messager.progress('close');
								var d = $.parseJSON(data);
								$.messager.show({
									msg : d.msg,
									title : '提示'
								});
								if (d.success) {
									datagrid.datagrid('unselectAll');
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
	
	function convertToModel(processDefinitionId) {
		var tempForm = $('#tempForm').form();
		tempForm.find('[name=processDefinitionId]').val(processDefinitionId);
		tempForm.form('submit', {
			url : '<%=basePath%>manage/sys/workflow/processDefinition/convertToModel.do',
			onSubmit : function() {
				$.messager.progress({
					text : '数据处理中，请稍后....'
				});
				return true;
			},
			success : function(data) {
				try {
					$.messager.progress('close');
					var d = $.parseJSON(data);
					$.messager.show({
						msg : d.msg,
						title : '提示'
					});
					if (d.success) {
						datagrid.datagrid('unselectAll');
					}
				} catch(e) {
					window.location.href = overUrl;
				}
			}
		});
	}

	function searchFun() {
		datagrid.datagrid('load', {
				definitionKey : $('#toolbar input[name=definitionKey]').val(),
				definitionName : $('#toolbar input[name=definitionName]').val()
		});
	}
	function clearFun() {
		$('#toolbar input').val('');
		datagrid.datagrid('load', {});
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
			url : '<%=basePath%>manage/sys/workflow/processDefinition/queryImport.do?random=' + Math.random(),
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
			<th>流程定义名称</th>	
			<td colspan="2">
				<input id="definitionName" name="definitionName" maxlength="20"  style="width: 120px;"/>
			</td>
			<th>流程定义KEY</th>	
			<td colspan="2">
				<input id="definitionKey" name="definitionKey" maxlength="20"  style="width: 120px;"/>
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
    <fcc:permission moduleId="${rightModuleId}" operateId="add">
    <a class="easyui-linkbutton" iconCls="icon-add" onClick="importData();" plain="true" href="javascript:void(0);">新增</a> 
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="edit">
    <%--
    <a class="easyui-linkbutton" iconCls="icon-edit" onClick="edit();" plain="true" href="javascript:void(0);">修改</a>
    --%>
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="delete">
    <a class="easyui-linkbutton" iconCls="icon-remove" onClick="del();" plain="true" href="javascript:void(0);">删除</a>  
    </fcc:permission>
    <a class="easyui-linkbutton" iconCls="icon-undo" onClick="datagrid.datagrid('unselectAll');" plain="true" href="javascript:void(0);">取消选中</a> 
    <span id="expordDataSizeSpan" style="color: red; font-weight: bolder;"></span>
    </div>
  </div>
  <table id="datagrid">
  </table>
  <form id="tempForm" name="tempForm" method="post" >
  <input name="processDefinitionId" value="" type="hidden">
  </form>
</div>
</body>
</html>
