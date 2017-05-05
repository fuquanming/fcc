<%@page import="com.fcc.commons.workflow.amountApply.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<table id="datagrid">
</table>
<%@ include file="/WEB-INF/head/init_datagrid.jsp" %>
<script type="text/javascript">
datagridParam_id = 'datagrid';// 用到的datagrid的ID
datagridParam_url = 'manage/sys/workflow/processHistory/viewDatagrid.do?processInstanceId=${param.processInstanceId}';// 数据源url
datagridParam_idField = 'id';// datagrid表格的唯一标识
datagridParam_idField_checkbox = false;// 是否显示多选框
datagridParam_page = false;// 不显示分页
datagridParam_column_value = [ [ 
    {
        field : 'processDefinitionName',
        title : '流程名称',
        width : 80
    } ,
    {
        field : 'name',
        title : '当前节点',
        width : 80,
        formatter : function(value, rowData, rowIndex) {
            /* return '<a class="trace" href="javascript:void(0)" onclick="showTraceImg(\'' + rowData.processInstanceId + '\', \'' + rowData.processDefinitionId + '\')" title="点击查看流程图">' + rowData.name + '</a>'; */
            return value;
        }
    } ,
    {
        field : 'assignee',
        title : '当前处理人',
        width : 80
    } ,
    {
        field : 'startTime',
        title : '任务创建时间',
        width : 90,
        formatter : function(value, rowData, rowIndex) {
            value = rowData.createTime;
            return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
        }
    } ,
    {
        field : 'endTime',
        title : '任务结束时间',
        width : 90,
        formatter : function(value, rowData, rowIndex) {
            value = rowData.endTime;
            return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
        }
    } ,
    {
        field : 'durationInMillis',
        title : '持续时间',
        width : 80,
        formatter : function(value, rowData, rowIndex) {
        	if (value < 0) return '';
            return Tool.dateFormat({'value' : value, 'interval' : true});
        }
    }  ,
    {
        field : 'comment',
        title : '审批内容',
        width : 70
    }  ,
    {
        field : 'commentTime',
        title : '审批时间',
        width : 90,
        formatter : function(value, rowData, rowIndex) {
            value = rowData.commentTime;
            return Tool.dateFormat({'value' : value, 'format' : 'yyyy-MM-dd HH:mm:ss'});
        }
    }  ,
    {
        field : 'processDefinitionVersion',
        title : '版本',
        width : 30,
        formatter : function(value, rowData, rowIndex) {
            value = rowData.processDefinitionVersion;
            return '<b title="流程版本号">V: ' + value + '</b>';;
        }
    } 
] ];// 表格的列
</script>