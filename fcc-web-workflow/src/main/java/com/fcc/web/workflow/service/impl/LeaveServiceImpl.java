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
import com.fcc.commons.workflow.service.WorkflowService;

import com.fcc.web.workflow.model.Leave;
import com.fcc.web.workflow.dao.LeaveDao;
import com.fcc.web.workflow.service.LeaveService;
import com.fcc.web.workflow.service.LeaveWorkflowService;
/**
 * <p>Description:Leave</p>
 */

@Service
public class LeaveServiceImpl implements LeaveService, ExportService, ImportService {
    @Resource
    private LeaveDao leaveDao;
    @Resource
    private BaseService baseService;
    @Resource
    private LeaveWorkflowService leaveWorkflowService;
    @Resource
    private WorkflowService workflowService;
    
    @Override
    public List<String> dataConver(Object converObj) {
        List<String> dataList = new ArrayList<String>();
        if (converObj instanceof Leave) {
            Leave data = (Leave) converObj;
            dataList.add(DataFormater.noNullValue(data.getLeaveId()));
            dataList.add(DataFormater.noNullValue(data.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            dataList.add(DataFormater.noNullValue(data.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            dataList.add(DataFormater.noNullValue(data.getContent()));
            dataList.add(DataFormater.noNullValue(data.getProcessInstanceId()));
            dataList.add(DataFormater.noNullValue(data.getProcessDefinitionId()));
            dataList.add(DataFormater.noNullValue(data.getProcessNodeName()));
            dataList.add(DataFormater.noNullValue(data.getStatus()));
            dataList.add(DataFormater.noNullValue(data.getCreateUser()));
            dataList.add(DataFormater.noNullValue(data.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            dataList.add(DataFormater.noNullValue(data.getUpdateUser()));
            dataList.add(DataFormater.noNullValue(data.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
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
            Cell processInstanceIdCell = row.getCell(3);
            processInstanceIdCell.setCellType(Cell.CELL_TYPE_STRING);
            String processInstanceIdValue = processInstanceIdCell.getStringCellValue();
            leave.setProcessInstanceId(java.lang.String.valueOf(processInstanceIdValue));
            Cell processDefinitionIdCell = row.getCell(4);
            processDefinitionIdCell.setCellType(Cell.CELL_TYPE_STRING);
            String processDefinitionIdValue = processDefinitionIdCell.getStringCellValue();
            leave.setProcessDefinitionId(java.lang.String.valueOf(processDefinitionIdValue));
            Cell processNodeNameCell = row.getCell(5);
            processNodeNameCell.setCellType(Cell.CELL_TYPE_STRING);
            String processNodeNameValue = processNodeNameCell.getStringCellValue();
            leave.setProcessNodeName(java.lang.String.valueOf(processNodeNameValue));
            Cell statusCell = row.getCell(6);
            statusCell.setCellType(Cell.CELL_TYPE_STRING);
            String statusValue = statusCell.getStringCellValue();
            leave.setStatus(java.lang.String.valueOf(statusValue));
            Cell createUserCell = row.getCell(7);
            createUserCell.setCellType(Cell.CELL_TYPE_STRING);
            String createUserValue = createUserCell.getStringCellValue();
            leave.setCreateUser(java.lang.String.valueOf(createUserValue));
            Cell createTimeCell = row.getCell(8);
            createTimeCell.setCellType(Cell.CELL_TYPE_STRING);
            String createTimeValue = createTimeCell.getStringCellValue();
            leave.setCreateTime(format.parse(createTimeValue));
            Cell updateUserCell = row.getCell(9);
            updateUserCell.setCellType(Cell.CELL_TYPE_STRING);
            String updateUserValue = updateUserCell.getStringCellValue();
            leave.setUpdateUser(java.lang.String.valueOf(updateUserValue));
            Cell updateTimeCell = row.getCell(10);
            updateTimeCell.setCellType(Cell.CELL_TYPE_STRING);
            String updateTimeValue = updateTimeCell.getStringCellValue();
            leave.setUpdateTime(format.parse(updateTimeValue));
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
        leaveWorkflowService.startWorkflow(leave.getLeaveId());
    }
    
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void edit(Leave leave) {
        baseService.edit(leave);
        leaveWorkflowService.startWorkflow(leave.getLeaveId());
    }
    
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void updateStatus(String dataId, String status) {
        Map<String, Object> param = new HashMap<String, Object>(2);
        param.put("status", status);
        param.put("dataId", dataId);
        baseService.executeHql("update Leave set status=:status where leaveId=:dataId", param);
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
    
}