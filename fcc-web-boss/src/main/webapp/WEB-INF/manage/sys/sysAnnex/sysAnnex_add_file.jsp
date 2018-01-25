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
            <th>关联ID：</th>      
            <td>
            <input id="linkId" name="linkId" type="text" class="easyui-validatebox easyui-textbox" maxlength="32" />
            </td>
        </tr>   
        <tr>    
            <th>关联类型：</th>      
            <td>
            ${linkType }
            </td>
        </tr>   
        <tr>    
            <th>附件类型：</th>      
            <td>
            ${annexType }
            </td>
        </tr>   
        <tr>    
            <th>文件备注：</th>      
            <td>
            <textarea rows="5" cols="40" id="remark" name="remark" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 200]"></textarea>
            </td>
        </tr>
        <tr>  
            <th>文件：</th>      
            <td>
            <tool:fileUpload linkType="${linkType }" annexType="${annexType }" ></tool:fileUpload>
            </td>
        </tr>   
        <tr>
            <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="save();" href="javascript:void(0);">保存</a> <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">上一步</a> </td>
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
saveParam_backUrl = 'manage/sys/sysAnnex/toAdd.do';// 跳转地址
saveParam_afterCallback = function(data, success) {
    return false;// 不执行自动跳转
}
</script>