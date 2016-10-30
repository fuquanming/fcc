<%@page import="com.fcc.web.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<%@ include file="/head/upload_js.jsp" %>
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
			<th>当前锁状态</th>	
			<td colspan="2">
                <select id="lockStatus" name="lockStatus" class="easyui-combobox" maxlength="2"  style="width: 120px;">
                <option value="lock">锁定</option>
                <option value="unlock">解锁</option>
                </select>
			</td>
			<th>创建时间</th>	
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
    <fcc:permission operateId="delete">
    <a class="easyui-linkbutton" iconCls="icon-remove" onClick="del();" plain="true" href="javascript:void(0);">删除</a>  
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
<script type="text/javascript" charset="UTF-8">
    var datagrid;
    var userForm;
    var userDialog;
    var importDataFlag = false;
    $(function() {

        userForm = $('#userForm').form();
        
        datagrid = $('#datagrid').datagrid({
            <c:if test="${empty param.lazy}">url : '${basePath}manage/sys/sysLock/datagrid.do?random=' + Math.random(),</c:if>
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
                    title : '锁的key'
            } 
             ] ],
            columns : [ [ 
                {
                    field : 'lockStatus',
                    title : '当前锁状态',
                    sortable:true,
                    width : 100,
                    formatter : function(value, rowData, rowIndex) {
                        var temp = value;
                        if (value == 'lock') {
                            temp = '锁定';
                        } else if (value == 'unlock') {
                            temp = '解锁'
                        }
                        return temp;
                    }
                } ,
                {
                    field : 'createTime',
                    title : '创建时间',
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
                    Tool.message.alert('错误', msg, 'error'); 
                }
            },
            loadFilter : function(data) {
                var flag = Tool.operate.check(data);
                if (flag != true || flag != false) {
                    return data;                                            
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
            'uploader':'${basePath}manage/sys/sysLock/import.do?JSESSIONID=<%=session.getId()%>', 
            //浏览按钮的背景图片路径 
            //'buttonImage':'${basePath}js/jquery-easyui-1.3.2/themes/icons/download.png', 
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
                    Tool.message.show({
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
    
    function del() {
        var ids = [];
        var rows = datagrid.datagrid('getSelections');
        if (rows.length > 0) {
            Tool.message.confirm('请确认', '您要删除当前所选记录？', function(r) {
                if (r) {
                    for ( var i = 0; i < rows.length; i++) {
                        ids.push(rows[i].lockKey);
                    }
                    var idsVal = ids.join(',');
                    userForm.find('[name=ids]').val(idsVal);
                    userForm.form('submit', {
                        url : '${basePath}manage/sys/sysLock/delete.do',
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
        <c:if test="${not empty param.lazy}">datagrid = $('#datagrid').datagrid({url : '${basePath}manage/sys/sysLock/datagrid.do?random=' + Math.random()});</c:if>
        datagrid.datagrid('load', {
                lockStatus : $('#toolbar input[name=lockStatus]').val(),
                createTimeBegin : $('#toolbar input[name=createTimeBegin]').val(),
                createTimeEnd : $('#toolbar input[name=createTimeEnd]').val()
        });
        datagrid.datagrid('clearSelections');
    }
    function clearFun() {
        $('#toolbar input').val('');
        datagrid.datagrid('load', {});
    }
</script>
