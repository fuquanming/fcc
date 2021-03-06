<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/head/upload_js.jsp"%>
<%-- 导入数据 --%>
<style type="text/css">
.fileuploadTable tr {
    background-color: #F9F9F9;
}
.fileuploadTable td {
    border: 1px solid #F9F9F9;
    font-size: 15px;
}
</style>
<script type="text/javascript">
var importParam_importFileType;
var importParam_importUrl;// 导入数据URL
var importParam_queryImportUrl;// 查询导入数据URL

var importDialog;
var importDataFlag = false;
var fileuploadTable;
var fileupload_data;
var fileParamMap = {};
$(function() {
	fileuploadTable = $('#fileuploadTable');
	if (importParam_importFileType) {
		importParam_importFileType = /(\.|\/)( + importParam_importFileType + )$/i;
	} else {
		importParam_importFileType = /(\.|\/)(jpg|png)$/i;
	}
	console.log(importParam_importFileType)
	$('#fileupload').attr('data-url', importParam_importUrl);
	$('#fileupload').fileupload({
        dataType: 'json', // xls|xlsx
        acceptFileTypes:  importParam_importFileType,// 文件类型
        maxFileSize: 52428800,// 50M
        maxNumberOfFiles : 1,// 上传一个文件
        add: function (e, data) {
            // 文件名
            var fileSize = data.fileInput.context.files[0].size;
            var fileName = data.fileInput.context.files[0].name;
            fileSize = fileSize / 1024; // Kb
            cleanFileuploadTable();
            $('<tr><td width="50%">' + fileName + '</td>' + 
                    '<td width="20%">' + fileSize.toFixed(2) + 'KB</td>' +
                    '<td width="30%"><a href="javascript:void(0)" onclick="importFileAction()" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">上传文件</span></span></a></td></tr>')
            .appendTo(fileuploadTable)
            .click(function () {
                //data.context = $('<p/>').text('上传中...').replaceAll($(this));
            });    
            fileupload_data = data;
        },
        done: function (e, data) {
        	Tool.message.progress('close');
        	if (Tool.operate.check({'data':data.result}) == true) {
                cleanFileuploadTable();
                $('<tr><td id="importWatiImgTd"><img id="importWaitImg" src="images/wait.gif"/></td><td id="importDataTd" style="color:red;"></td></tr>').appendTo(fileuploadTable)
                queryImportDataSize();
        	}
        }
	}).bind('fileuploadsubmit', function (e, data) {
        //data.formData = { userId: '123' };  //如果需要额外添加参数可以在这里添加
        data.formData = fileParamMap;
    });
	
	// 上传文件
    importDialog = $('#importDialog').dialog({
        modal : true,
        title : '数据导入'
    }).dialog('close');
});

function importFileAction() {
	Tool.message.progress();
	fileupload_data.submit();
}

function importData() {
    if (importDataFlag == true) {
        Tool.message.alert(Lang.tip, Lang.importNow, Tool.icon.info, true);
        return;
    }
    cleanFileuploadTable();
    importDialog.dialog('open');
}

function queryImportDataSize() {
	/* ${basePath}manage/sys/sysLog/queryImport.do */
    $.ajax({
        url : Tool.urlAddParam(importParam_queryImportUrl, 'random=' + Math.random()),
        cache : false,
        dataType : "json",
        success : function(d) {
            try {
                if (d.importFlag == true) {
                	$('#importWatiImgTd').addClass('messager-icon messager-info');
                	$('#importWaitImg').hide();
                    $('#importDataTd').html(Lang.importEnd.format(d.currentSize));
                    importDataFlag = false;
                } else {
                    $('#importDataTd').html(Lang.importGoing.format(d.currentSize));
                    window.setTimeout("queryImportDataSize()", 2000);
                }
            } catch(e) {
            	console.log(e);
                importDataFlag = false;
                Tool.goPage(overUrl);
            }
        }
    });
}
function cleanFileuploadTable() {
	fileuploadTable.html('');
}
</script>
<div id="importDialog"
	style="width: 500px; height: 350px; margin: 0 auto; overflow: hidden;">
	<input id="fileupload" type="file" name="upload"
		data-url=""
		accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
		multiple />
	<div>
		<table id="fileuploadTable"
			style="width: 100%; border: 1px solid #DDDDDD;">
		</table>
	</div>
</div>
