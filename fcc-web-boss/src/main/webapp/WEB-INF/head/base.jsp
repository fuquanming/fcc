<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String sPort = (request.getServerPort() == 80)?"":(":" + request.getServerPort());
String base = request.getScheme()+"://"+request.getServerName()+sPort;
//String base = request.getScheme()+"://"+request.getServerName();
String basePath = base + path + "/";
request.setAttribute("basePath", basePath);
%>
<%@ page isELIgnored="false" %><%-- web-app version="2.5" 时解决低版本不解析el表达式 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="fcc" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tool" %>
<base href="<%=basePath%>">
<script type="text/javascript">
var overUrl = "<%=basePath%>overtime.jsp";
var loginUrl = "<%=basePath%>login.jsp";
</script>
