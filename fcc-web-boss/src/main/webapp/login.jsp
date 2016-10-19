<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%-- 
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/dataformater_tag.tld" prefix="format" %>
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@ include file="/head/base.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
<meta HTTP-EQUIV="Expires" CONTENT="0"/>

<style type="text/css">
*{margin:0;padding:0;}
body{color:#000;font:normal 12px/1.5em arial,"宋体";}
button{cursor:pointer;border:none;}
img{cursor:pointer;}
/*body position:static;height:50px; font-size:30px;color: #12ACD0;font-weight: bold; */
body{ background:url("images/login/bg.jpg") repeat-x; height:646px; }
/*login  margin-top: -50px;margin-left: -190px; */
#login{background:url(images/login/login_bg.jpg) no-repeat; height:646px; width:556px; }
#login #loginInputForm{position:static;padding:270px 50px 100px 250px;}
#login #loginTitle{margin-top: -50px;margin-left: -190px;font-size:30px;color: #12ACD0;font-weight: bold;}
#login table {margin-left: -35px; margin-top: 30px;}
#login table tr{height: 30px;}
#login table span{font-size:18px; display:inline-block; height:21px;line-height:21px; color:#0285c2;}
#login table input{width:160px; height:21px; line-height:21px; border:#0285c2 1px solid;font:12px arial,"宋体"; }
#login #but01{ background:url(images/login/but.jpg) no-repeat; width:77px; height:22px; color:#FFF;font:13px; font-weight:700; border:0;cursor:pointer;}
</style>

<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.3.4/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.3.4/themes/icon.css">
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/jquery-easyui-1.3.4/locale/easyui-lang-zh_CN.js" charset="UTF-8"></script>

<link rel="icon" href="image/favicon.ico" type="image/x-icon" />

<title>${APP_NAME}</title>
</head>
<body>
	<div class="center" align="center">
	<div id="login" align="center" >
        <form action="login.do?login" method="post" name="loginInputForm" id="loginInputForm">
        <div id="loginTitle" align="center" style="">${APP_NAME}</div>
	    <table>
	    	<tr>
	    		<td><span>用户名:</span></td>
	    		<td colspan="2" align="left"><input name="name" type="text" class="easyui-validatebox" required="true" value="" size="20" maxlength="50"/></td>
	    	</tr>
	    	<tr>
	    		<td><span>密&nbsp;&nbsp;&nbsp;&nbsp;码:</span></td>
	    		<td colspan="2" align="left"><input name="password" type="password" class="easyui-validatebox" required="true" value="" size="20" maxlength="50"/></td>
	    	</tr>
	    	<tr>
	    		<td><span>验证码:</span></td>
	    		<td><input name="randCode" type="text" class="easyui-validatebox" required="true" value="1" size="9" maxlength="4" style="width:90px;"/></td>
	    		<td><img src="randCode?rand=Math.random()" border="1" id="codeImg" alt="验证码" style="width: 71px; height: 23px;" onclick="getCode();"/></td>
	    	</tr>
	    	<tr>
	    		<td>&nbsp;</td>
	    		<td colspan="2" align="left"><input id="but01" type="button" value="登 录" onclick="login();"/></td>
	    	</tr>
	    </table>
	    </form>
	</div>
	</div>

<script type="text/javascript">
var loginInputForm;
var	loginTabs;
var	loginAndRegDialog;
	
$(function(){
	loginInputForm = $('#loginInputForm').form({
		url : 'login.do?login',
		success : function(data) {
			var d = $.parseJSON(data);
			if (d.success) {
				window.location.href="manage/index.do";
			} else {
				loginInputForm.find('input[name=password]').focus();
				getCode();
			}
			$.messager.show({
				msg : d.msg,
				title : '提示'
			});
		}
	});
	loginInputForm.find('input').on('keyup', function(event) {/* 增加回车提交功能 */
		if (event.keyCode == '13') {
			loginInputForm.submit();
		}
	});
});	
// 登陆
function login() {
	loginInputForm.submit();
	return true;
}

// 取得验证码
function getCode() {
	document.getElementById("codeImg").src = "randCode?rand=" + Math.random();
}

</script>
</body>
</html>
