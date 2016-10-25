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
    <legend>修改模块</legend>
    <form id="userForm" name="userForm" method="post">
      <input name="id" type="hidden" value="${data.moduleId }"/>
      <input name="operateValue" type="hidden" value=""/>
      <table class="tableForm" align="center">
        <tr>
          <th>模块名称</th>
          <td colspan="3"><input name="moduleName" type="text" value="${data.moduleName }" class="easyui-validatebox" required="true" maxlength="100" style="width: 350px;"/></td>
        </tr>
        <tr>
          <th>模块地址</th>
          <td colspan="3"><input name="moduleDesc" type="text" value="${data.moduleDesc }" maxlength="100" style="width: 350px;"/></td>
        </tr>
        <tr>
          <th>模块排序</th>
          <td colspan="3"><input name="moduleSort" type="text" value="${data.moduleSort }" class="easyui-validatebox" required="true" style="width: 350px;"/>
          </td>
        </tr>
        <tr>
          <th></th>
          <td>可选操作</td>
          <td></td>
          <td align="left">已选操作</td>
        </tr>
        <tr>
          <th>模块操作</th>
          <td><select id="unSelectOperate" name="unSelectOperate" multiple="multiple" size="10" style="width: 150px;">
              <c:forEach items="${operateList}" var="operate">
                <option value="${operate.operateId }">${operate.operateName }</option>
              </c:forEach>
            </select>
          </td>
          <td>
          <a class="easyui-linkbutton" plain="true" style="width: 35px; text-align: center; border: 1px solid #D0D0BF;" onClick="Tool.removeSelect({'sourceId':'unSelectOperate','targetId':'selecetOperate'});" href="javascript:void(0);"> > </a>
            <br/>
            <a class="easyui-linkbutton" plain="true" style="width: 35px; text-align: center; border: 1px solid #D0D0BF;" onClick="Tool.removeSelect({'sourceId':'selecetOperate','targetId':'unSelectOperate'})" href="javascript:void(0);"> < </a>
            <br/>
            <a class="easyui-linkbutton" plain="true" style="width: 35px; text-align: center; border: 1px solid #D0D0BF;" onClick="Tool.removeSelect({'sourceId':'unSelectOperate','targetId':'selecetOperate','isAll':true})" href="javascript:void(0);"> >> </a>
            <br/>
            <a class="easyui-linkbutton" plain="true" style="width: 35px; text-align: center; border: 1px solid #D0D0BF;" onClick="Tool.removeSelect({'sourceId':'selecetOperate','targetId':'unSelectOperate','isAll':true})" href="javascript:void(0);"> << </a>
          </td>
          <td><select id="selecetOperate" name="selecetOperate" multiple="multiple" size="10" style="width: 150px;">
            </select>
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
<script type="text/javascript" charset="UTF-8">
    var userForm;
    $(function() {
    
        userForm = $('#userForm').form();
        
        var operateIdMap = [];
        <c:forEach items="${data.operates}" var="operate">
            operateIdMap['${operate.operateId}'] = '${operate.operateId}';
        </c:forEach>
        
        // 显示已有的操作
        $('#unSelectOperate').children().each(function(){
            if (operateIdMap[$(this).val()]) {
                $(this).attr("selected", "selected");
            } 
        });
        Tool.removeSelect({'sourceId':'unSelectOperate','targetId':'selecetOperate'})

    });
    
    function edit() {
        
        var operateIds = [];
        $('#selecetOperate').children().each(function(){
            operateIds.push($(this).val());
        });
        var idsVal = operateIds.join(',');
        userForm.find('[name=operateValue]').val(idsVal);
        userForm.form('submit', {
            url : '${basePath}manage/sys/module/edit.do',
            success : function(data) {
                try {
                    Tool.message.progress('close');
                    if (Tool.operate.check(data, true)) {
                    	setTimeout(function() {toBack();}, 3000);
                    };
                } catch(e) {
                	console.log(e);
                    window.location.href = overUrl;
                }
            },
            onSubmit : function() {
                var isValid = $(this).form('validate');
                if (isValid) Tool.message.progress();
                return isValid;
            }
        });
    }
    
    function toBack() {
        window.location.href = '${basePath}manage/sys/module/view.do';
    }
</script>