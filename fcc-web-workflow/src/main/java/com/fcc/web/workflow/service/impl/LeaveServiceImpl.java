package com.fcc.web.workflow.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.DataFormater;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.web.service.ExportService;
import com.fcc.commons.web.service.ImportService;
import com.fcc.commons.workflow.common.WorkflowVariableEnum;
import com.fcc.commons.workflow.filter.WorkflowTaskBusinessDataFilter;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.workflow.dao.LeaveDao;
import com.fcc.web.workflow.model.Leave;
import com.fcc.web.workflow.service.LeaveApplyWorkflowService;
import com.fcc.web.workflow.service.LeaveService;
/**
 * <p>Description:Leave</p>
 */

@Service
public class LeaveServiceImpl implements LeaveService, ExportService, ImportService, WorkflowTaskBusinessDataFilter {
    @Resource
    private LeaveDao leaveDao;
    @Resource
    private BaseService baseService;
    @Resource
    private LeaveApplyWorkflowService leaveApplyWorkflowService;
    
    @Override
    public List<String> dataConver(Object converObj) {
        List<String> dataList = new ArrayList<String>();
        if (converObj instanceof Leave) {
            Leave data = (Leave) converObj;
            dataList.add(DataFormater.noNullValue(data.getLeaveId()));
            dataList.add(DataFormater.noNullValue(data.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            dataList.add(DataFormater.noNullValue(data.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            dataList.add(DataFormater.noNullValue(data.getContent()));
            dataList.add(DataFormater.noNullValue(data.getStatus()));
        }
        return dataList;
    }
    
    public Object converObject(Object src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Row row = (Row) src;
        Cell cell = row.getCell(0);
        if (cell == null) return null;
        Leave leave = new Leave();
        try {
            leave.setLeaveId(UUID.randomUUID().toString().replaceAll("-", ""));
            Cell startTimeCell = row.getCell(0);
            startTimeCell.setCellType(Cell.CELL_TYPE_STRING);
            String startTimeValue = startTimeCell.getStringCellValue();
            leave.setStartTime(format.parse(startTimeValue));
            Cell endTimeCell = row.getCell(1);
            endTimeCell.setCellType(Cell.CELL_TYPE_STRING);
            String endTimeValue = endTimeCell.getStringCellValue();
            leave.setEndTime(format.parse(endTimeValue));
            Cell contentCell = row.getCell(2);
            contentCell.setCellType(Cell.CELL_TYPE_STRING);
            String contentValue = contentCell.getStringCellValue();
            leave.setContent(java.lang.String.valueOf(contentValue));
            Cell statusCell = row.getCell(3);
            statusCell.setCellType(Cell.CELL_TYPE_STRING);
            String statusValue = statusCell.getStringCellValue();
            leave.setStatus(java.lang.String.valueOf(statusValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leave;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addData(List<Object> dataList) {
        this.baseService.addListBatch(dataList);
    }
    
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void add(Leave leave) {
        baseService.add(leave);
        leaveApplyWorkflowService.startWorkflow(leave.getLeaveId());
    }
    
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void edit(Leave leave) {
        baseService.edit(leave);
        leaveApplyWorkflowService.startWorkflow(leave.getLeaveId());
    }
    
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void updateStatus(String leaveId, String status) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("status", status);
        param.put("leaveId", leaveId);
        baseService.executeHql("update Leave set status=:status where leaveId=:leaveId", param);
    }
    
    @Transactional(readOnly = true)
    @Override
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return leaveDao.queryPage(pageNo, pageSize, param, isSQL);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<Leave> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return leaveDao.query(pageNo, pageSize, param, isSQL);
    }
    
    @Transactional(readOnly = true)
    @Override
    public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return leaveDao.report(pageNo, pageSize, param, isSQL);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> filter(ProcessTaskInfo taskInfo, String businessKey) {
        Map<String, Object> map = null;
        if (Leave.processDefinitionKey.equals(taskInfo.getProcessDefinitionKey())) {
            map = new HashMap<String, Object>(3);
            Leave data = (Leave) baseService.get(Leave.class, businessKey);
            map.put("requestUserName", ((SysUser)baseService.get(SysUser.class, data.getInitiatorUserId())).getUserName());
            map.put("data", data);
            map.put(WorkflowVariableEnum.editTaskPage.name(), "/WEB-INF/manage/workflow/leave/leave_task_edit.jsp");
            // 判断是否可以修改绑定数据用于重新提交申请
            boolean readonly = true; 
            if ("leave_audit".equals(taskInfo.getTaskDefinitionKey())) {// 申请的流程ID，可以修改数据
                readonly = false;
            }
            map.put("readonly", readonly);
        }
        return map;
    }
}