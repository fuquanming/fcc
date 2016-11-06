<%@ page language="java" pageEncoding="UTF-8"%>
<%-- 导出文件 依赖init_save.jsp --%>
<script type="text/javascript">
var exportParam_form;// 表单
var exportParam_exportUrl;// 导出数据URL
var exportParam_queryExportUrl;// 查询导出数据URL
var exportParam_model;// 模块

var exportDataFlag = false;
function exportData() {
	
    if (exportDataFlag == true) {
        Tool.message.alert(Lang.tip, Lang.exportNow, Tool.icon.error, true);
        return;
    }
    exportDataFlag = true;
    
    saveParam_form = exportParam_form;
    saveParam_saveUrl = exportParam_exportUrl;
    saveParam_closeWin = false;
    saveParam_afterCallback = function(data, success) {
    	if (success) {
    		var exportInfo = $(".messager-body").children().eq(1);
            exportInfo.append('<img id="exportWaitImg" src="${basePath}/images/wait.gif"/>')
            window.setTimeout("queryExportDataSize()", 2000);
    	} else {
    		exportDataFlag = false;
    	}
    }
    save();
    /*${basePath}manage/sys/sysLog/export.do*/
}

function queryExportDataSize() {
    /*${basePath}manage/sys/sysLog/queryExport.do*/
    $.ajax({
        url : Tool.urlAddParam(exportParam_queryExportUrl, 'random=' + Math.random()),
        cache : false,
        dataType : "json",
        success : function(d) {
            try {
                var exportInfo = $(".messager-body").children().eq(1);
                exportInfo.css({'color': 'red', 'font-size' : '15px;'})
                if (d.empty == true) {
                    exportInfo.html(Lang.exportEmpty);
                    exportDataFlag = false;
                } else if (d.fileName != null) {
                    exportInfo.html(Lang.exportEnd.format(d.currentSize, 'exportData/' + exportParam_model + 'Export/' + d.fileName, d.fileName))
                    exportDataFlag = false;
                    window.location.href = 'exportData/' + exportParam_model + 'Export/' + d.fileName;
                } else if (d.error == true) {
                    exportInfo.html(Lang.exportError);
                    exportDataFlag = false;
                } else if (d.detroy == true) {
                    exportInfo.html(Lang.exportStop);
                    exportDataFlag = false;
                } else {
                    exportInfo.html(Lang.exportGoing.format(d.currentSize));
                    window.setTimeout("queryExportDataSize()", 2000);
                }
                if (exportDataFlag == false) {
                    $('#exportWaitImg').hide();
                }
            } catch(e) {
                console.log(e)
                exportDataFlag = false;
                $('#exportWaitImg').hide();
                window.location.href = overUrl;
            }
        }
    });
}
</script>