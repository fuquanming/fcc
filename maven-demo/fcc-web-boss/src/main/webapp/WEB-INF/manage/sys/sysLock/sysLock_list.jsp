<%@page import="com.fcc.web.sys.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/head/base.jsp" %>
<%@ include file="/WEB-INF/head/meta.jsp" %>
<%@ include file="/WEB-INF/head/easyui.jsp" %>
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
            <th>当前锁状态</th>  
            <td colspan="2">
                <select id="lockStatus" name="lockStatus" class="easyui-combobox" maxlength="2"  style="width: 120px;">
                <option value="" selected="selected">---请选择---</option>
                <option value="lock">锁定</option>
                <option value="unlock">解锁</option>
                </select>
            </td>
            <th>创建时间</th>   
            <td colspan="2">
                <input value="" class="easyui-datebox" style="width: 120px;" id="createTimeBegin" name="createTimeBegin"   />
                <input value="" class="easyui-datebox" style="width: 120px;" id="createTimeEnd" name="createTimeEnd"   />
            </td>
        </tr>   
        <tr>
            <td colspan="3" align="left">
            <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
            <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
            <span id="importDataSizeSpan" style="color: red; font-weight: bolder;"></span> 
            </td>
        </tr>
    </table>
    </form>
    </fieldset>
    <div id="operateDiv"></div>
  </div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
<%@ include file="/WEB-INF/head/init_save.jsp" %>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<%@ include file="/WEB-INF/head/init_operate.jsp" %>
<script type="text/javascript">
datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = '${basePath}manage/sys/sysLock/datagrid.do';// 数据源url
datagridParam_idField = 'lockKey';// datagrid表格的唯一标识
datagridParam_idField_checkbox = true;// 是否显示多选框
datagridParam_column_value = [ [ {
	    field : 'lockKey',
	    title : '锁的key',
	    width : 100
	} ,
	{
	    field : 'lockStatus',
	    title : '当前锁状态',
	    sortable:true,
	    width : 100,
	    formatter : function(value, rowData, rowIndex) {
	        var temp = value;
	        if (value == 'lock') {
	            temp = '锁定';
	        } else if (value == 'unlock') {
	            temp = '解锁'
	        }
	        return temp;
	    }
	} ,
	{
	    field : 'createTime',
	    title : '创建时间',
	    sortable:true,
	    width : 100,
	    formatter : function(value, rowData, rowIndex) {
	    	return Tool.dateFormat({'value':value, 'format':'yyyy-MM-dd HH:mm:ss'});
	    }
	} 
] ];// 表格的列
datagridParam_queryParamName = ['lockStatus', 'createTimeBegin', 'createTimeEnd'];

operateParam_form = 'userForm';
operateParam_operateDiv = 'operateDiv';
operateParam_dataId = 'lockKey';
operateParam_dataName = 'lockKey';
operateParam_delUrl = '${basePath}manage/sys/sysLock/delete.do';
</script>