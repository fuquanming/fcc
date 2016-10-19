<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>${APP_NAME}</title>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
</head>
<body id="indexLayout" class="easyui-layout" fit="true">
	<div region="north" href="manage/layout.do?north" style="height:54px; overflow: hidden; background: url('images/manage/top_01.gif') repeat-x;"></div>
	<div region="west" href="manage/layout.do?west" title="导航" split="false" style="width:200px;overflow: hidden;"></div>
	<div region="center" href="manage/layout.do?center" title="${USER_LOGIN_SYS.userId }，欢迎使用${APP_NAME}管理系统" style="overflow: hidden;" id="centerDiv"></div>
	<div region="south" href="manage/layout.do?south" style="height:20px;overflow: hidden;"></div>
</body>
</html>