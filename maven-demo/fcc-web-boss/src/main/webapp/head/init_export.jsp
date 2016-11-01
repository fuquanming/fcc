<%@ page language="java" pageEncoding="UTF-8"%>
<%-- 导出文件 --%>
<script type="text/javascript">
var exportParam = {}
exportParam.exportUrl;// 导出数据URL
exportParam.queryExportUrl;// 查询导出数据URL
exportParam.model;// 模块

var exportDataFlag = false;
function exportData() {
    if (exportDataFlag == true) {
        Tool.message.alert(Lang.tip, Lang.exportNow, Tool.icon.error, true);
        return;
    }
    exportDataFlag = true;
    
    /*${basePath}manage/sys/sysLog/export.do*/
    userForm.form('submit', {
        url : exportParam.exportUrl + '?random=' + Math.random(),
        onSubmit : function() {
            Tool.message.progress();
            return true;
        },
        success : function(data) {
            try {
                Tool.message.progress('close');
                if (Tool.operate.check(data, false)) {
                    var exportInfo = $(".messager-body").children().eq(1);
                    exportInfo.append('<img id="exportWaitImg" src="${basePath}/images/wait.gif"/>')
                    window.setTimeout("queryExportDataSize()", 2000);
                } else {
                    exportDataFlag = false;
                }
            } catch(e) {
                window.location.href = overUrl;
            }
        }
    });
}

function queryExportDataSize() {
    /*${basePath}manage/sys/sysLog/queryExport.do*/
    $.ajax({
        url : exportParam.queryExportUrl + '?random=' + Math.random(),
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
                    exportInfo.html(Lang.exportEnd.format(d.currentSize, 'exportData/' + exportParam.model + 'Export/' + d.fileName, d.fileName))
                    exportDataFlag = false;
                    window.location.href = 'exportData/' + exportParam.model + 'Export/' + d.fileName;
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