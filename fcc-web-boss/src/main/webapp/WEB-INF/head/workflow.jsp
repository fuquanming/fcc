<%@page import="com.fcc.commons.web.config.Resources"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
// 工作流jsp
String modelerDiagramviewerUrl = Resources.ACTIVITI.getString("activiti.modeler.url") + Resources.ACTIVITI.getString("activiti.modeler.diagramviewer.path");
request.setAttribute("modelerDiagramviewerUrl", modelerDiagramviewerUrl);
%>
<script type="text/javascript">
var activitiImgDialog;
$(function() {
	// 显示图片
	activitiImgDialog = $('#activitiImgDialog').show().dialog({
		modal : true,
		title : '流程跟踪',
		width: document.documentElement.clientWidth * 0.95,
        height: document.documentElement.clientHeight * 0.95
	}).dialog('close');
	
	if ($('#status').length > 0) {
		getComboBoxByData({
	        id : 'status',
	        valueField : 'id',
	        textField : 'text',
	        data : [
	            {text : '--请选择--', id : ''},
	            {text : '${unstart.value }', id : '${unstart }'},
	            {text : '${start.value }', id : 'start'},
	            {text : '${success.value }', id : 'success'},
	            {text : '${fail.value }', id : 'fail'},
	            {text : '${cannel.value }', id : 'cannel'}
	        ]
	    })
	}
})
function showTraceImg(pid, pdid) {
	activitiImgDialog.dialog('open');
	//$('#processInstanceImg').attr('src', '${basePath}manage/sys/workflow/processInstance/trace/img.do?processInstanceId=' + pid);
	var url = '${modelerDiagramviewerUrl}' + '?processDefinitionId=' + pdid + '&processInstanceId=' + pid;
	$('#activitiImg').html('');
	$('#activitiImg').append('<iframe src="' + url + '" width="100%" height="' + document.documentElement.clientHeight * 0.88 + '" />');
}
function formartStatus(value) {
	var str = '';
    if (value == '${unstart }') {
        str = '${unstart.value }';      
    } else if (value == '${start }') {
        str = '${start.value }';    
    } else if (value == '${success }') {
        str = '${success.value }';    
    } else if (value == '${fail }') {
        str = '${fail.value }';    
    } else if (value == '${cannel }') {
        str = '${cannel.value }';    
    }
    return str;
}
</script>
<div id="activitiImgDialog" style="margin: 0 auto; overflow: hidden; display: none;">
	<img id="processInstanceImg" name="processInstanceImg" src="" style="display: none;"/>
	<div id="activitiImg"></div>
</div>
