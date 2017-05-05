<%-- 文件路径显示  --%>
<%@ tag import="com.fcc.web.sys.config.ConfigUtil"%>
<%
request.setAttribute("fileAccessPath", ConfigUtil.getFileAccessPath());
%>
${fileAccessPath }