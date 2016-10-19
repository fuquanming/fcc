<%@page import="com.fcc.app.lucene.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<script type="text/javascript" charset="UTF-8">
	var userForm;
	$(function() {

		userForm = $('#userForm').form();

	});

	function edit() {
		userForm.form('submit', {
			url : '<%=basePath%>manage/sys/indexInfo/edit.do',
			success : function(data) {
				try {
					var d = $.parseJSON(data);
					$.messager.show({
						msg : d.msg,
						title : '提示'
					});
					if (d.success) {
						setTimeout(function(){window.location.href = '<%=basePath%>manage/sys/indexInfo/view.do';},3000);
					}
				} catch(e) {
					window.location.href = overUrl;
				}
			}
		});
	}

	function toBack() {
		window.location.href = '<%=basePath%>manage/sys/indexInfo/view.do';
	}

</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>修改<%=IndexInfo.TABLE_ALIAS%></legend>
    <form id="userForm" name="userForm" method="post">
	  <input name="infoId" type="hidden" value="${indexInfo.infoId}" />
      <table class="tableForm" align="center">
		<tr>	
			<th><%=IndexInfo.ALIAS_INFO_KEY%>：</th>		
			<td>
			<input id="infoKey" name="infoKey" type="text" value="${indexInfo.infoKey}" class="easyui-validatebox" maxlength="100" />
			</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_INDEX_DIR%>：</th>		
			<td>
			<input id="indexDir" name="indexDir" type="text" value="${indexInfo.indexDir}" class="easyui-validatebox" maxlength="255" />
			</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_DOCUMENT_TYPE%>：</th>		
			<td>
			<input id="documentType" name="documentType" type="text" value="${indexInfo.documentType}" class="easyui-validatebox" maxlength="100" />
			</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_CLASS_NAME%>：</th>		
			<td>
			<input id="className" name="className" type="text" value="${indexInfo.className}" class="easyui-validatebox" maxlength="100" />
			</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_ID_TYPE%>：</th>		
			<td>
			<input id="idType" name="idType" type="text" value="${indexInfo.idType}" class="easyui-validatebox" maxlength="100" />
			</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_METHOD_NAMES%>：</th>		
			<td>
			<input id="methodNames" name="methodNames" type="text" value="${indexInfo.methodNames}" class="easyui-validatebox" maxlength="200" />
			</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_IS_TOKENIZEDS%>：</th>		
			<td>
			<input id="isTokenizeds" name="isTokenizeds" type="text" value="${indexInfo.isTokenizeds}" class="easyui-validatebox" maxlength="200" />
			</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_IS_BACKUP_FIELDS%>：</th>		
			<td>
			<input id="isBackupFields" name="isBackupFields" type="text" value="${indexInfo.isBackupFields}" class="easyui-validatebox" maxlength="200" />
			</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_FIELD_NAMES%>：</th>		
			<td>
			<input id="fieldNames" name="fieldNames" type="text" value="${indexInfo.fieldNames}" class="easyui-validatebox" maxlength="200" />
			</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_DEL_FIELD_NAME%>：</th>		
			<td>
			<input id="delFieldName" name="delFieldName" type="text" value="${indexInfo.delFieldName}" class="easyui-validatebox" maxlength="10" />
			</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_ANALYZER_NAME%>：</th>		
			<td>
			<input id="analyzerName" name="analyzerName" type="text" value="${indexInfo.analyzerName}" class="easyui-validatebox" maxlength="50" />
			</td>
		</tr>	
		<tr>	
			<th><%=IndexInfo.ALIAS_CREATE_TIME%>：</th>		
			<td>
			<input id="createTimeString" name="createTimeString" class="easyui-datetimebox"  value="<fmt:formatDate value="${indexInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			</td>
		</tr>	
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="edit();" href="javascript:void(0);">保存</a> <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
