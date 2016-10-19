<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<%@ include file="/head/base.jsp" %>
<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.3.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.3.2/themes/icon.css">
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js" charset="UTF-8"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>访问受限</title>
	 <script>
	 	var	errorDialog;
	 	$(function(){
	 		errorDialog = $('#errorDialog').show().dialog({
				modal : true,
				closable : false
			});
			$('#errorDialog').prev('div').children().remove();
	 	});
	 	
	 	try{parent.$.messager.progress('close');}catch(e){}
	 	
		var href = window.location.href;
		var tempWindow = window;
		while (true) {
			var tempHref = tempWindow.parent.location.href;
			if (tempHref == href) {
				break;
			} else {
				href = tempHref;
				tempWindow = tempWindow.parent;
			}
		}
		
		<c:if test="${filterMsg != 'right'}">
		window.setInterval(function(){
			var time = $('#timeSpan').text();
			$('#timeSpan').html(parseInt(time) - 1);
		}, 1000);
	 	setTimeout(function(){tempWindow.location.href=loginUrl;},3000);
	 	</c:if>
  	</script>
  </head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
<div id="errorDialog" style="width: 550px; height: 320px; display: none;">
  <div align="center" style="padding: 5px; ">
  	<div align="center" style="height: 60px; font-size: 25px; text-align: center; padding-top: 40px; ">
  	<img src = "images/overtime/error.jpg" style="float:none;margin-bottom: -20px;">访问受限!
  	</div>
  	<div style="display:inline;width:80%;height:180px;padding-top:20px; font-size: 15px;">
        <c:choose>
			<c:when test="${filterMsg == 'right'}">
			<br/>您不具备对该项的操作权限，您可以和系统管理员联系；
			</c:when>
			<c:otherwise>
			<br/>未登录用户或用户访问已超时，您可以重新登陆；
			<br/><br/><a id="loginA" href="login.jsp" target="_top">>>点击这里重新登录 </a>
			<br/><br/>浏览器将在<span id="timeSpan">3</span>秒后转向登录页...
			</c:otherwise>
		</c:choose>
    </div>
  </div>
</div>
</div>
</body>
</html>