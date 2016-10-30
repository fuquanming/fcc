<%@ page language="java" pageEncoding="UTF-8"%>
<%-- 导出文件 --%>
<script type="text/javascript">
var exportDataFlag = false;
function exportData() {
    if (exportDataFlag == true) {
        Tool.message.alert('提示', '正在导出请稍后...', 'error', true);
        return;
    }
    exportDataFlag = true;
    
    <%-- ${basePath}manage/sys/sysLog/export.do --%>
    userForm.form('submit', {
        url : '${param.initExportUrl}?random=' + Math.random(),
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
	<%-- ${basePath}manage/sys/sysLog/queryExport.do --%>
	$.ajax({
        url : '${param.initQueryExportUrl}?random=' + Math.random(),
        cache : false,
        dataType : "json",
        success : function(d) {
            try {
            	var exportInfo = $(".messager-body").children().eq(1);
                exportInfo.css({'color': 'red', 'font-size' : '15px;'})
                if (d.empty == true) {
                    exportInfo.html("无数据导出！");
                    exportDataFlag = false;
                } else if (d.fileName != null) {
                    exportInfo.html("已导出：" + d.currentSize + "条，导出完成！<br/>若没有提示下载，请点击<br/> <a href='" + '${basePath}exportData/sysLogExport/' + d.fileName + "'>" + d.fileName + "</a>");
                    exportDataFlag = false;
                    window.location.href = '${basePath}exportData/${param.initModel}Export/' + d.fileName;
                } else if (d.error == true) {
                    exportInfo.html("导出异常！");
                    exportDataFlag = false;
                } else if (d.detroy == true) {
                    exportInfo.html("系统停止中！");
                    exportDataFlag = false;
                } else {
                    exportInfo.html("已导出：" + d.currentSize + "条，继续导出中！");
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