<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>新增附件</legend>
    <form id="userForm" name="userForm" method="post">
      <table class="tableForm" align="center">
		<tr>	
			<th>关联类型：</th>		
			<td>
			<input id="linkType" name="linkType" type="text" class="easyui-validatebox easyui-textbox" maxlength="32" />
			</td>
		</tr>	
		<tr>	
			<th>附件类型：</th>		
			<td>
			<input id="annexType" name="annexType" type="text" class="easyui-validatebox easyui-textbox" maxlength="50" />
			</td>
		</tr>	
        <tr id="fileFrame" style="display: none;">
            <th>文件</th>
            <td>
            <iframe id='fileUploadFrame' allowTransparency='true' style='border:0;width:99%;height:99%;padding-left:2px;' frameBorder='0'></iframe>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
            <a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="showFileUpload();" href="javascript:void(0);">下一步</a>
            <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a>
            </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<script type="text/javascript" charset="UTF-8">
saveParam_form = 'userForm';// 提交的Form
saveParam_saveUrl = 'manage/sys/sysAnnex/add.do';// 保存URL地址
saveParam_backUrl = 'manage/sys/sysAnnex/view.do';// 跳转地址
saveParam_afterCallback = function(data, success) {
    return false;// 不执行自动跳转
}

function showFileUpload() {
	Tool.goPage('${basePath}manage/sys/sysAnnex/toAddFile.do?linkType=' + $("input[name='linkType']").val() + "&annexType=" + $("input[name='annexType']").val());
	return;
	$('#fileFrame').show();
	console.log("linkType=" + $("input[name='linkType']").val())
	console.log("annexType=" + $("input[name='annexType']").val())
	$('#fileUploadFrame').attr('src', '${basePath}manage/sys/sysAnnex/toAddFile.do?linkType=' + $("input[name='linkType']").val() + "&annexType=" + $("input[name='annexType']").val());
}
</script>