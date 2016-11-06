<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.fcc.app.util.PropertyTool"%>
<%
// 工作流jsp
String modelerDiagramviewerUrl = PropertyTool.getModelerDiagramviewerUrl();
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
})
function showTraceImg(pid, pdid) {
	activitiImgDialog.dialog('open');
	//$('#processInstanceImg').attr('src', '${basePath}manage/sys/workflow/processInstance/trace/img.do?processInstanceId=' + pid);
	var url = '${modelerDiagramviewerUrl}' + '?processDefinitionId=' + pdid + '&processInstanceId=' + pid;
	$('#activitiImg').html('');
	$('#activitiImg').append('<iframe src="' + url + '" width="100%" height="' + document.documentElement.clientHeight * 0.88 + '" />');
}
</script>
<div id="activitiImgDialog" style="margin: 0 auto; overflow: hidden; display: none;">
	<img id="processInstanceImg" name="processInstanceImg" src="" style="display: none;"/>
	<div id="activitiImg"></div>
</div>
