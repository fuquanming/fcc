<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<%@ include file="/head/easyui-portal.jsp" %>
<script type="text/javascript" charset="UTF-8">
	var portal;
	var col;
	$(function() {
		col = $('#portal div').length;
		portal = $('#portal').portal({
			border : false,
			fit : true
		});
	});
</script>
</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="padding: 5px;">
		<div>●欢迎使用${APP_NAME}</div>
	</div>
</body>
</html>