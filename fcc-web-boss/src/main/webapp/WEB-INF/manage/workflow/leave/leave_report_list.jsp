<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
<%@ include file="/WEB-INF/head/highcharts.jsp" %>
<%@ include file="/WEB-INF/head/fusionCharts.jsp" %>
<script src="js/support/report.js" charset="UTF-8" type="text/javascript"></script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;display: none;">
    <fieldset>
    <legend>筛选</legend>
    <form id="userForm" name="userForm" method="post">
  	<input name="ids" type="hidden" value=""/>
    <table class="tableForm">
		<tr>	
			<th>开始时间</th>	
			<td colspan="2">
				<input value="" class="easyui-datebox" style="width: 120px;" id="startTimeBegin" name="startTimeBegin"   />
				<input value="" class="easyui-datebox" style="width: 120px;" id="startTimeEnd" name="startTimeEnd"   />
			</td>
			<th>结束时间</th>	
			<td colspan="2">
				<input value="" class="easyui-datebox" style="width: 120px;" id="endTimeBegin" name="endTimeBegin"   />
				<input value="" class="easyui-datebox" style="width: 120px;" id="endTimeEnd" name="endTimeEnd"   />
			</td>
			<th>内容</th>	
			<td colspan="2">
				<input id="content" name="content" maxlength="500"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入内容...'"/>
			</td>
			<th>状态</th>	
			<td colspan="2">
				<input id="status" name="status" maxlength="16"  style="width: 120px;" class="easyui-textbox" data-options="prompt:'请输入状态...'"/>
			</td>
		</tr>	
	    <tr>
	       <th>分组</th>  
           <td colspan="2">
               <input id="reportGroupName" name="reportGroupName" style="width: 130px;" class="easyui-combobox"/>
           </td>
	    </tr>
		<tr>
	        <td colspan="3" align="center">
	        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
	        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
	        <a class="easyui-linkbutton" iconCls="icon-back" plain="true" onClick="toBack();" href="javascript:void(0);">返回</a>
        	</td>
      	</tr>
	</table>
	</form>
    </fieldset>
    <div> 
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="lineCharts('Leave', '<%=basePath %>');" plain="true" href="javascript:void(0);">折线图</a>
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="columnCharts('Leave', '<%=basePath %>');" plain="true" href="javascript:void(0);">柱形图</a>
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="pieCharts('Leave', '<%=basePath %>');" plain="true" href="javascript:void(0);">饼图</a> |
    </div>
  </div>
  <div id="container" style="width: 800px; height: 360px; margin: 0 auto; display: none;" ></div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_combotree.jsp" %>
<%@ include file="/WEB-INF/head/init_combobox.jsp" %>
<script type="text/javascript" charset="UTF-8">
// 报表数据缓存    
var dataList;   
var dataTitle;
$(function() {
    getComboBoxByData({
        id : 'reportGroupName',
        valueField : 'id',
        textField : 'text',
        data : [
            {text : '开始时间', id : 'startTime'},
            {text : '结束时间', id : 'endTime'},
            {text : '内容', id : 'content'},
            {text : '状态', id : 'status'},
        ]
    })
})   
function toBack() {
	Tool.goPage("manage/workflow/leave/view.do");
} 
datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = 'manage/workflow/leave/report/datagrid.do';// 数据源url
datagridParam_idField = 'groupName';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ 
    {
        field : 'groupName',
        title : '',
        sortable:true,
        width : 100
    } ,
    {
        field : 'count',
        title : '总数',
        sortable:true,
        width : 100
    } 
] ];// 表格的列
datagridParam_queryParamName = [
        'startTimeBegin','startTimeEnd',
        'endTimeBegin','endTimeEnd',
        'content',
        'status'
, 'reportGroupName'];
datagridParam_load_beforeCallback = function(data) {
    dataTitle = $('.datagrid-header-row td[field="groupName"] div span:first-child');
    var groupName = $('#toolbar input[name="reportGroupName"]').val();
    if (groupName != '') {
        dataTitle.html($('#reportGroupName').combobox('getText'));
    }
    dataList = data.rows;
}
</script>