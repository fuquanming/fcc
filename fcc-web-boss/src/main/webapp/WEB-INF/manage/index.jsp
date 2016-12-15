<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>${APP_NAME}</title>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
</head>
<body id="indexLayout" class="easyui-layout" fit="true">
    <!-- background: url('images/manage/top_01.gif') repeat-x; -->
	<div region="north" href="manage/layout.do?north" style="height:54px; overflow: hidden; "></div>
	<div region="west" href="manage/layout.do?west" title="导航" split="false" style="width:200px;overflow: hidden;"></div>
	<div region="center" href="manage/layout.do?center" title="${USER_LOGIN_SYS.userId }，欢迎使用${APP_NAME}管理系统" style="overflow: hidden;" id="centerDiv"></div>
	<div region="south" href="manage/layout.do?south" style="height:20px;overflow: hidden;"></div>
</body>
</html>