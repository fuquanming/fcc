<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.fcc.app.common.Constanst"%>
<%@ page import="com.fcc.app.sys.model.SysUser"%>
<%@ page import="com.fcc.app.cache.CacheUtil"%>

<%
SysUser user = (SysUser)request.getSession().getAttribute(Constanst.SysUserSession.USER_LOGIN);
if (!CacheUtil.isAdmin(user)) {
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<title>${APP_NAME}-文件管理</title>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
</head>
<body id="indexLayout" class="easyui-layout" fit="true">
	<div region="west" href="manage/sys/file/file_tree.jsp" title="导航" split="false" style="width:300px;overflow: hidden;"></div>
	<div region="center" href="manage/layout.do?center" title="${USER_LOGIN_SYS.userId }，欢迎使用${APP_NAME}管理系统-文件管理" style="overflow: hidden;" id="centerDiv"></div>
</body>
</html>