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
    <legend>查看SysAnnex</legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="annexId" type="hidden" value="${sysAnnex.annexId}" />
      <table class="tableForm" align="center">
		<tr>	
			<th>关联ID：</th>		
			<td>${sysAnnex.linkId}</td>
		</tr>	
		<tr>	
			<th>关联类型：</th>		
			<td>${sysAnnex.linkType}</td>
		</tr>	
		<tr>	
			<th>附件名称：</th>		
			<td>${sysAnnex.annexName}</td>
		</tr>	
		<tr>	
			<th>文件名称：</th>		
			<td>${sysAnnex.fileName}</td>
		</tr>	
		<tr>	
			<th>文件类型：</th>		
			<td>${sysAnnex.fileType}</td>
		</tr>	
		<tr>	
			<th>文件地址：</th>		
			<td>${sysAnnex.fileUrl}</td>
		</tr>	
		<tr>	
			<th>文件大小：</th>		
			<td>${sysAnnex.fileSize}</td>
		</tr>	
		<tr>	
			<th>文件备注：</th>		
			<td>${sysAnnex.remark}</td>
		</tr>	
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
<script type="text/javascript" charset="UTF-8">
function toBack() {
	Tool.goPage('manage/sys/sysAnnex/view.do');
}
</script>