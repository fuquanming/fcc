<%@page import="com.fcc.app.util.PropertyUtil"%>
<%@page import="com.fcc.commons.fcc_commons.App"%>
<html>
<body>
<h2>Hello World!</h2>
</body>
</html>

<%
App app = new App();
out.println(app);

out.println(PropertyUtil.getValue("jdbc.username"));
 %>
