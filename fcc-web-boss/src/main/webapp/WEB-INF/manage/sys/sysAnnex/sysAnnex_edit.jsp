<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <legend>修改SysAnnex</legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="annexId" type="hidden" value="${sysAnnex.annexId}" />
      <table class="tableForm" align="center">
		<tr>	
			<th>关联ID：</th>		
			<td>
            <input id="linkId" name="linkId" type="text" value="${sysAnnex.linkId}" class="easyui-validatebox easyui-textbox" maxlength="32" />
			</td>
		</tr>	
		<tr>	
			<th>关联类型：</th>		
			<td>
            <input id="linkType" name="linkType" type="text" value="${sysAnnex.linkType}" class="easyui-validatebox easyui-textbox" maxlength="32" />
			</td>
		</tr>	
		<tr>	
			<th>附件名称：</th>		
			<td>
            <input id="annexName" name="annexName" type="text" value="${sysAnnex.annexName}" class="easyui-validatebox easyui-textbox" maxlength="50" />
			</td>
		</tr>	
		<tr>	
			<th>文件名称：</th>		
			<td>
            <input id="fileName" name="fileName" type="text" value="${sysAnnex.fileName}" class="easyui-validatebox easyui-textbox" maxlength="50" />
			</td>
		</tr>	
		<tr>	
			<th>文件类型：</th>		
			<td>
            <input id="fileType" name="fileType" type="text" value="${sysAnnex.fileType}" class="easyui-validatebox easyui-textbox" maxlength="50" />
			</td>
		</tr>	
		<tr>	
			<th>文件地址：</th>		
			<td>
            <textarea rows="5" cols="40" id="fileUrl" name="fileUrl" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 100]">${sysAnnex.fileUrl}</textarea>
			</td>
		</tr>	
		<tr>	
			<th>文件大小：</th>		
			<td>
            <input id="fileSize" name="fileSize" type="text" value="${sysAnnex.fileSize}" class="easyui-validatebox easyui-textbox" maxlength="19" />
			</td>
		</tr>	
		<tr>	
			<th>文件备注：</th>		
			<td>
            <textarea rows="5" cols="40" id="remark" name="remark" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 200]">${sysAnnex.remark}</textarea>
			</td>
		</tr>	
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="save();" href="javascript:void(0);">保存</a> <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> </td>
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
saveParam_saveUrl = 'manage/sys/sysAnnex/edit.do';// 保存URL地址
saveParam_backUrl = 'manage/sys/sysAnnex/view.do';// 跳转地址
saveParam_afterCallback = function(data, success) {
    if (success == false) return false;// 失败，不执行自动跳转
}
</script>