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
    <legend>代码生成</legend>
    <form id="userForm" name="userForm" method="post">
        <input type="hidden" id="listQuery" name="listQuery"/>
        <input type="hidden" id="listShow" name="listShow"/>
        <input type="hidden" id="addShow" name="addShow"/>
        <input type="hidden" id="editShow" name="editShow"/>
        <input type="hidden" id="viewShow" name="viewShow"/>
        <input type="hidden" id="reportQuery" name="reportQuery"/>
      <table class="tableForm" align="center">
		<tr>	
			<th colspan="3">java包名：</th>		
			<td>
			<input id="basepackage" name="basepackage" type="text" style="width: 200px;" class="easyui-validatebox easyui-textbox" maxlength="255" required="true" data-options="prompt:'请输入java包名...'"/>
			</td>
		</tr>	
		<tr>	
			<th colspan="3">jsp路径：</th>		
			<td>
			<input id="namespace" name="namespace" type="text" style="width: 200px;" class="easyui-validatebox easyui-textbox" maxlength="255" required="true" data-options="prompt:'请输入jsp路径...'"/>
			</td>
		</tr>	
		<tr>	
			<th colspan="3">生成代码路径：</th>		
			<td>
			<input id="outRoot" name="outRoot" type="text" style="width: 500px;" class="easyui-validatebox easyui-textbox" required="true" data-options="prompt:'请输入代码路径...'"/>
			</td>
		</tr>	
		<tr>	
			<th colspan="3">表名：</th>		
			<td>
			<input id="tableName" name="tableName" type="text" class="easyui-combobox" />
			<a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="queryColumn();" href="javascript:void(0);">获取列</a>
			</td>
		</tr>
		<tr>  
            <th colspan="3">类名：</th>        
            <td>
            <input id="className" name="className" type="text" class="easyui-validatebox easyui-textbox" required="true" data-options="prompt:'请输入类名...'"/>
            </td>
        </tr>
        <tr>  
            <th colspan="3">是否tree：</th>        
            <td align="left">
            <input type="radio" name="type" value="tree" />tree<input type="radio" name="type" value="table" checked="checked"/>table
            </td>
        </tr>
		<tr>  
            <th colspan="3">列字段：</th>        
            <td>
            <select id="allColumnName" name="allColumnName" type="text" multiple="multiple" >
            </select>
            </td>
        </tr>	
        </table>
        <table class="tableForm" align="center">
        <tr>
            <td>
            <!-- <input type="checkbox" checked="checked" value="listQuery" style="width: 0px; "/> -->
            <a class="easyui-linkbutton" iconCls="icon-add" plain="true" onClick="Tool.copySelect({'sourceId':'allColumnName','targetId':'listQueryColumn'});" href="javascript:void(0);">列表查询</a>
            <a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onClick="Tool.removeOperate({'sourceId':'listQueryColumn'});" href="javascript:void(0);">移除</a>
            </td>
            <td>
            <!-- <input type="checkbox" checked="checked" value="listShow" style="width: 0px; "/> -->
            <a class="easyui-linkbutton" iconCls="icon-add" plain="true" onClick="Tool.copySelect({'sourceId':'allColumnName','targetId':'listShowColumn'});" href="javascript:void(0);">列表显示</a>
            <a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onClick="Tool.removeOperate({'sourceId':'listShowColumn'});" href="javascript:void(0);">移除</a>
            </td>
            <td>
            <!-- <input type="checkbox" checked="checked" value="addShow" style="width: 0px; "/> -->
            <a class="easyui-linkbutton" iconCls="icon-add" plain="true" onClick="Tool.copySelect({'sourceId':'allColumnName','targetId':'addShowColumn'});" href="javascript:void(0);">新增显示</a>
            <a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onClick="Tool.removeOperate({'sourceId':'addShowColumn'});" href="javascript:void(0);">移除</a>
            </td>
            <td>
            <!-- <input type="checkbox" checked="checked" value="editShow" style="width: 0px; "/> -->
            <a class="easyui-linkbutton" iconCls="icon-add" plain="true" onClick="Tool.copySelect({'sourceId':'allColumnName','targetId':'editShowColumn'});" href="javascript:void(0);">修改显示</a>
            <a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onClick="Tool.removeOperate({'sourceId':'editShowColumn'});" href="javascript:void(0);">移除</a>
            </td>
            <td>
            <!-- <input type="checkbox" checked="checked" value="viewShow" style="width: 0px; "/> -->
            <a class="easyui-linkbutton" iconCls="icon-add" plain="true" onClick="Tool.copySelect({'sourceId':'allColumnName','targetId':'viewShowColumn'});" href="javascript:void(0);">查看显示</a>
            <a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onClick="Tool.removeOperate({'sourceId':'viewShowColumn'});" href="javascript:void(0);">移除</a>
            </td>
            <td>
            <!-- <input type="checkbox" checked="checked" value="reportQuery" style="width: 0px; "/> -->
            <a class="easyui-linkbutton" iconCls="icon-add" plain="true" onClick="Tool.copySelect({'sourceId':'allColumnName','targetId':'reportQueryColumn'});" href="javascript:void(0);">报表查询</a>
            <a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onClick="Tool.removeOperate({'sourceId':'reportQueryColumn'});" href="javascript:void(0);">移除</a>
            </td>
        </tr>
        <tr>
            <td>
            <select id="listQueryColumn" name="listQueryColumn" type="text" multiple="multiple" size="10">
            </select>
            </td>
            <td>
            <select id="listShowColumn" name="listShowColumn" type="text" multiple="multiple" size="10">
            </select>
            </td>
            <td>
            <select id="addShowColumn" name="addShowColumn" type="text" multiple="multiple" size="10">
            </select>
            </td>
            <td>
            <select id="editShowColumn" name="editShowColumn" type="text" multiple="multiple" size="10">
            </select>
            </td>
            <td>
            <select id="viewShowColumn" name="viewShowColumn" type="text" multiple="multiple" size="10">
            </select>
            </td>
            <td>
            <select id="reportQueryColumn" name="reportQueryColumn" type="text" multiple="multiple" size="10">
            </select>
            </td>
        </tr>
        <tr>
          <td colspan="6" align="center">
          <a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="save();" href="javascript:void(0);">生成代码</a> 
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
<%@ include file="/WEB-INF/head/init_combobox.jsp" %>
<script type="text/javascript" charset="UTF-8">
$(function() {
    getComboBoxByData({
        id : 'tableName',
        valueField : 'id',
        textField : 'text',
        data : [
            <c:forEach items="${tableList}" var="table">
            {text : '${table }', id : '${table }'},
            </c:forEach>
        ]
    })
})

function queryColumn() {
    var queryTable = $('input[name="tableName"]').val();
    $('#allColumnName').attr('size', 10);
    Tool.removeSelect({
        'sourceId':'allColumnName',
        'targetId':'sourceId',
        'isAll':true
	})
	$.ajax({
        url : Tool.urlAddParam('manage/sys/code/column.do?tableName=' + queryTable, 'random=' + Math.random()),
        cache : false,
        dataType : "json",
        success : function(d) {
            try {
            	var success = Tool.operate.check(d, true);
            	if (success) {
            	} else {
            		for (var i in d) {
            			Tool.addOption({
            			    'sourceId' : 'allColumnName',
            			    'text' : d[i].columnName + ',' + d[i].columnAlias,
            			    'val' : d[i].columnName
            			})
            		}
            	}
            } catch(e) {
                console.log(e)
            }
        }
    });
}

saveParam_form = 'userForm';// 提交的Form
saveParam_saveUrl = 'manage/sys/code/add.do';// 保存URL地址
saveParam_beforeCallback = function(data, success) {
	var form = $('#' + saveParam_form).form();
	// 获取列表查询
    var listQuery = [];
    $('#listQueryColumn').children().each(function(){
    	listQuery.push($(this).val());
    });
    form.find('[name=listQuery]').val(listQuery.join(','));
    
    var listShow = [];
    $('#listShowColumn').children().each(function(){
    	listShow.push($(this).val());
    });
    form.find('[name=listShow]').val(listShow.join(','));
    
    var addShow = [];
    $('#addShowColumn').children().each(function(){
    	addShow.push($(this).val());
    });
    form.find('[name=addShow]').val(addShow.join(','));
    
    var editShow = [];
    $('#editShowColumn').children().each(function(){
    	editShow.push($(this).val());
    });
    form.find('[name=editShow]').val(editShow.join(','));
    
    var viewShow = [];
    $('#viewShowColumn').children().each(function(){
    	viewShow.push($(this).val());
    });
    form.find('[name=viewShow]').val(viewShow.join(','));
    
    var reportQuery = [];
    $('#reportQueryColumn').children().each(function(){
    	reportQuery.push($(this).val());
    });
    form.find('[name=reportQuery]').val(reportQuery.join(','));
    
}
saveParam_afterCallback = function(data, success) {
    return false;// 不执行自动跳转
}
</script>