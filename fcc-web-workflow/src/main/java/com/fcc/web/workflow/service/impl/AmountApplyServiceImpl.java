package com.fcc.web.workflow.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
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
import com.fcc.commons.workflow.service.WorkflowService;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.web.sys.model.SysUser;

import com.fcc.web.workflow.model.AmountApply;
import com.fcc.web.workflow.dao.AmountApplyDao;
import com.fcc.web.workflow.service.AmountApplyService;
import com.fcc.web.workflow.service.AmountApplyWorkflowService;
/**
 * <p>Description:AmountApply</p>
 */

@Service
public class AmountApplyServiceImpl implements AmountApplyService, ExportService, ImportService, WorkflowTaskBusinessDataFilter {
    @Resource
    private AmountApplyDao amountApplyDao;
    @Resource
    private BaseService baseService;
    @Resource
    private AmountApplyWorkflowService amountApplyWorkflowService;
    @Resource
    private WorkflowService workflowService;
    
    @PostConstruct
    public void init() {
        workflowService.getTaskBusinessDataSet().add(this);
    }
    
    @Override
    public List<String> dataConver(Object converObj) {
        List<String> dataList = new ArrayList<String>();
        if (converObj instanceof AmountApply) {
            AmountApply data = (AmountApply) converObj;
            dataList.add(DataFormater.noNullValue(data.getAmountApplyId()));
            dataList.add(DataFormater.noNullValue(data.getUserId()));
            dataList.add(DataFormater.noNullValue(data.getUserName()));
            dataList.add(DataFormater.noNullValue(data.getPrimaryAmount()));
            dataList.add(DataFormater.noNullValue(data.getApplyRemark()));
            dataList.add(DataFormater.noNullValue(data.getApplyTime(), "yyyy-MM-dd HH:mm:ss"));
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
        AmountApply amountApply = new AmountApply();
        try {
            amountApply.setAmountApplyId(UUID.randomUUID().toString().replaceAll("-", ""));
            Cell userIdCell = row.getCell(0);
            userIdCell.setCellType(Cell.CELL_TYPE_STRING);
            String userIdValue = userIdCell.getStringCellValue();
            amountApply.setUserId(java.lang.Long.valueOf(userIdValue));
            Cell userNameCell = row.getCell(1);
            userNameCell.setCellType(Cell.CELL_TYPE_STRING);
            String userNameValue = userNameCell.getStringCellValue();
            amountApply.setUserName(java.lang.String.valueOf(userNameValue));
            Cell primaryAmountCell = row.getCell(2);
            primaryAmountCell.setCellType(Cell.CELL_TYPE_STRING);
            String primaryAmountValue = primaryAmountCell.getStringCellValue();
            amountApply.setPrimaryAmount(java.lang.Double.valueOf(primaryAmountValue));
            Cell applyRemarkCell = row.getCell(3);
            applyRemarkCell.setCellType(Cell.CELL_TYPE_STRING);
            String applyRemarkValue = applyRemarkCell.getStringCellValue();
            amountApply.setApplyRemark(java.lang.String.valueOf(applyRemarkValue));
            Cell applyTimeCell = row.getCell(4);
            applyTimeCell.setCellType(Cell.CELL_TYPE_STRING);
            String applyTimeValue = applyTimeCell.getStringCellValue();
            amountApply.setApplyTime(format.parse(applyTimeValue));
            Cell processInstanceIdCell = row.getCell(5);
            processInstanceIdCell.setCellType(Cell.CELL_TYPE_STRING);
            String processInstanceIdValue = processInstanceIdCell.getStringCellValue();
            amountApply.setProcessInstanceId(java.lang.String.valueOf(processInstanceIdValue));
            Cell processDefinitionIdCell = row.getCell(6);
            processDefinitionIdCell.setCellType(Cell.CELL_TYPE_STRING);
            String processDefinitionIdValue = processDefinitionIdCell.getStringCellValue();
            amountApply.setProcessDefinitionId(java.lang.String.valueOf(processDefinitionIdValue));
            Cell processNodeNameCell = row.getCell(7);
            processNodeNameCell.setCellType(Cell.CELL_TYPE_STRING);
            String processNodeNameValue = processNodeNameCell.getStringCellValue();
            amountApply.setProcessNodeName(java.lang.String.valueOf(processNodeNameValue));
            Cell statusCell = row.getCell(8);
            statusCell.setCellType(Cell.CELL_TYPE_STRING);
            String statusValue = statusCell.getStringCellValue();
            amountApply.setStatus(java.lang.String.valueOf(statusValue));
            Cell createUserCell = row.getCell(9);
            createUserCell.setCellType(Cell.CELL_TYPE_STRING);
            String createUserValue = createUserCell.getStringCellValue();
            amountApply.setCreateUser(java.lang.String.valueOf(createUserValue));
            Cell createTimeCell = row.getCell(10);
            createTimeCell.setCellType(Cell.CELL_TYPE_STRING);
            String createTimeValue = createTimeCell.getStringCellValue();
            amountApply.setCreateTime(format.parse(createTimeValue));
            Cell updateUserCell = row.getCell(11);
            updateUserCell.setCellType(Cell.CELL_TYPE_STRING);
            String updateUserValue = updateUserCell.getStringCellValue();
            amountApply.setUpdateUser(java.lang.String.valueOf(updateUserValue));
            Cell updateTimeCell = row.getCell(12);
            updateTimeCell.setCellType(Cell.CELL_TYPE_STRING);
            String updateTimeValue = updateTimeCell.getStringCellValue();
            amountApply.setUpdateTime(format.parse(updateTimeValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amountApply;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addData(List<Object> dataList) {
        this.baseService.addListBatch(dataList);
    }
    
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void add(AmountApply amountApply) {
        baseService.add(amountApply);
        amountApplyWorkflowService.startWorkflow(amountApply.getAmountApplyId());
    }
    
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void edit(AmountApply amountApply) {
        baseService.edit(amountApply);
        amountApplyWorkflowService.startWorkflow(amountApply.getAmountApplyId());
    }
    
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void updateStatus(String dataId, String status) {
        Map<String, Object> param = new HashMap<String, Object>(2);
        param.put("status", status);
        param.put("dataId", dataId);
        baseService.executeHql("update AmountApply set status=:status where amountApplyId=:dataId", param);
    }
    
    @Transactional(readOnly = true)
    @Override
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return amountApplyDao.queryPage(pageNo, pageSize, param, isSQL);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<AmountApply> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return amountApplyDao.query(pageNo, pageSize, param, isSQL);
    }
    
    @Transactional(readOnly = true)
    @Override
    public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return amountApplyDao.report(pageNo, pageSize, param, isSQL);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> filter(ProcessTaskInfo taskInfo, String businessKey) {
        Map<String, Object> map = null;
        if (AmountApply.processDefinitionKey.equals(taskInfo.getProcessDefinitionKey())) {
            map = new HashMap<String, Object>(3);
            AmountApply data = (AmountApply) baseService.get(AmountApply.class, businessKey);
            map.put("requestUserName", ((SysUser)baseService.get(SysUser.class, data.getCreateUser())).getUserName());
            map.put("data", data);
            map.put(WorkflowVariableEnum.editTaskPage.name(), "/WEB-INF/manage/workflow/amountApply/amountApply_task_edit.jsp");
            // 判断是否可以修改绑定数据用于重新提交申请
            boolean readonly = true; 
            if ("amountApply_audit".equals(taskInfo.getTaskDefinitionKey())) {// 申请的流程ID，可以修改数据
                readonly = false;
            }
            map.put("readonly", readonly);
        }
        return map;
    }
}