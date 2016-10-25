<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false" style="overflow: hidden;">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
  	<br/>
    <fieldset>
    <legend>新增组织机构</legend>
    <form id="userForm" name="userForm" method="post">
      <input name="id" type="hidden" value=""/>
      <table class="tableForm" align="center">
        <input name="parentId" type="hidden" value="${parent.organId }"/>
        <tr>
          <th>上级组织机构</th>
          <td colspan="3">${parent.organName }</td>
        </tr>
        <tr>
          <th>组织机构名称</th>
          <td><input name="organName" type="text" class="easyui-validatebox" required="true" maxlength="100"/></td>
        </tr>
        <tr>
          <th>组织机构排序</th>
          <td><input name="organSort" type="text" class="easyui-validatebox" required="true" validType="integer" maxlength="5"/>
          </td>
        </tr>
        <tr>
          <th>组织机构说明</th>
          <td><input name="organDesc" type="text" maxlength="100"/></td>
        </tr>
        <tr>
          <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="add();" href="javascript:void(0);">保存</a> <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a> </td>
        </tr>
      </table>
    </form>
    </fieldset>
  </div>
</div>
</body>
</html>
<script type="text/javascript" charset="UTF-8">
    var userForm;
    $(function() {
    
        userForm = $('#userForm').form();
        
    });
    
    function add() {
        // 获取模块操作
        userForm.form('submit', {
            url : '${basePath}manage/sys/organ/add.do',
            success : function(data) {
                try {
                    Tool.message.progress('close');
                    Tool.operate.check(data, true);
                } catch(e) {
                    window.location.href = overUrl;
                }
            },
            onSubmit : function() {
                var isValid = $(this).form('validate');
                if (isValid) {
                    Tool.message.progress();
                }
                return isValid;
            }
        });
    }

    function toBack() {
        window.location.href = '${basePath}manage/sys/organ/view.do';
    }
</script>
