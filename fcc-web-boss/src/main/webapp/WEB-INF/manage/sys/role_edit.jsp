<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
</head>
<body class="easyui-layout" fit="true">

<div region="center" border="false" style="overflow: auto;">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;">
    <br/>
    <fieldset>
    <legend>修改角色</legend>
    <form id="userForm" name="userForm" method="post">
    <input name="rightValue" type="hidden" value=""/>
    <input name="roleId" type="hidden" value="${sessionScope.sessionRole.roleId }"/>
    <table class="tableForm" align="center">
      <tr>
        <th>角色名称</th>
        <td><input name="roleName" class="easyui-validatebox easyui-textbox" data-options="prompt:'请输入角色名称...'" required="true" maxlength="100" value="${sessionScope.sessionRole.roleName }"/></td>
      </tr>
      <tr>
        <th>角色描述</th>
        <td>
        <textarea rows="2" cols="40" name="roleDesc" class="easyui-validatebox textbox eaayui-textarea" data-options="prompt:'请输入角色描述...'" required="true" validType="length[1, 200]">${sessionScope.sessionRole.roleDesc }</textarea>
        </td>
      </tr>
      <!-- <tr>
        <th>模块权限</th>
        <td>
            <ul id="moduleTree" style="margin-top: 5px;"></ul>
        </td>
      </tr> -->
      <tr>
        <td colspan="2" align="center">
        <a class="easyui-linkbutton" iconCls="icon-save" plain="true" onClick="save();" href="javascript:void(0);">保存</a>
        <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a>
        </td>
      </tr>
    </table>
    </form>
    </fieldset>
  </div>
</div>
<table id="treegrid">
</table>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_treegrid.jsp" %>
<script type="text/javascript">
treegridParam_id = 'treegrid';// 用到的datagrid的ID
treegridParam_url = '${basePath}manage/sys/role/treegrid.do';// 数据源url
treegridParam_idField = 'id';// datagrid表格的唯一标识
treegridParam_idField_checkbox = true;// 是否显示多选框
treegridParam_column_value = [ [ {
    field : 'text',
    title : '模块名称',
    width : 200
}, {
    field : 'moduleDesc',
    title : '选择',
    width : 200,
    formatter : function(value, rowData, rowIndex) {
        return '<input type="checkbox" id="module_' + rowData.attributes.parentIds +'" onclick="checkModule(this)"/>';
    }
}, {
    field : 'attributes',
    title : '模块操作',
    width : 200,
    formatter : function(value, rowData, rowIndex) {
        if (value) {
            var operates = value.operate;
            var str = '';
            for (var i in operates) {
            	var flag = '';
            	var operateId = operates[i].operateId;
            	if (value[operateId]) flag = 'checked';
                //str.push(operates[i].operateName);
                str += '<input type="checkbox" name="module_' + rowData.attributes.parentIds + '" operate="' + operates[i].operateValue + '" moduleId="' + rowData.id + '" ' + flag + ' onclick="checkOperate(this)"/>' + operates[i].operateName;
            }
            return str;
        }
        return value;
    }
}] ];// 表格的列

function checkModule(checkObj) {
    var id = checkObj.id;
    var moduleCheck = $('#' + id).is(':checked');
    $('input[id^="' + id + '"]').each(function() {
        $(this).attr("checked", moduleCheck);// 模块
        var childModuleId = $(this).attr("id");
        $('input[type=checkbox][name="' + childModuleId + '"]').attr("checked", moduleCheck);// 操作
    })
}

function checkOperate(checkObj) {
    var name = checkObj.name;
    // 所有的操作都选择，该模块选中
    var operateFlag = true;
    $('input[type=checkbox][name="' + name + '"]').each(function() {
        var checkFlag = $(this).is(':checked');
        if (checkFlag == false) {
            operateFlag = checkFlag;
            return;
        }
    });
    // 该模块选中或不选择
    $('#' + name).attr("checked", operateFlag);
    /* // 上级模块是否选中
    var mids = name.split('_');
    var splitFlag = mids[1].lastIndexOf('-');
    if (splitFlag != -1) {
        var id = mids[1].substring(0, splitFlag);
        var flag = true;
        $('input[id^="' + mids[0] + '_' + id + '"]').each(function() {
            var checkFlag = $(this).is(':checked');
            console.log($(this).attr('id'));
            if (checkFlag == false) {
                flag = checkFlag;
                return;
            }
        })
        $('#' + id).attr("checked", flag);// 上级模块
    } else {
        
    } */
}

saveParam_form = 'userForm';
saveParam_saveUrl = '${basePath}manage/sys/role/edit.do';
saveParam_backUrl = '${basePath}manage/sys/role/view.do';
saveParam_beforeCallback = function() {
    var moduleRight = [];
    var rightValue = '';
    $('input[type=checkbox][operate]').each(function() {
        if ($(this).is(':checked')) {
            var moduleId = $(this).attr('moduleId');
            var operate = $(this).attr('operate');
            if (moduleRight[moduleId]) {
                moduleRight[moduleId] = parseInt(parseInt(moduleRight[moduleId]) + parseInt(operate));
            } else {
                moduleRight[moduleId] = operate;
            }
        }
    })
    for (var i in moduleRight) {
        rightValue += i + ":" + moduleRight[i] + ",";
    }
    if (rightValue.length > 0) rightValue = rightValue.substr(0, rightValue.length - 1);
    $('#' + saveParam_form).form().find('[name=rightValue]').val(rightValue);
}
saveParam_afterCallback = function(data, success) {
	if (success == false) return false;// 失败，不执行自动跳转
}
var moduleTree;
$(function() {
    //moduleTree = getTree({queryUrl:'manage/sys/role/tree.do?nodeStatus=closed',id:'moduleTree',closed:true});
})
</script>
