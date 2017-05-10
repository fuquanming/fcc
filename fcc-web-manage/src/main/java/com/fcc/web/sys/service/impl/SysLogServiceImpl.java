package com.fcc.web.sys.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.DataFormater;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.web.service.ExportService;
import com.fcc.commons.web.service.ImportService;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.dao.SysLogDao;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.SysLog;
import com.fcc.web.sys.service.CacheService;
import com.fcc.web.sys.service.SysLogService;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @email fuquanming@gmail.com
 */
@Service
public class SysLogServiceImpl implements SysLogService, ImportService, ExportService {
    @Resource
    private BaseService baseService;
    @Resource
    private CacheService cacheService;
    @Resource
    private SysLogDao sysLogDao;
    
    @Override
    public List<String> dataConver(Object converObj) {
        List<String> dataList = new ArrayList<String>();
        if (converObj instanceof SysLog) {
            SysLog data = (SysLog) converObj;
            dataList.add(DataFormater.noNullValue(data.getLogId()));
            dataList.add(DataFormater.noNullValue(data.getUserId()));
            dataList.add(DataFormater.noNullValue(data.getUserName()));
            dataList.add(DataFormater.noNullValue(data.getIpAddress()));
            dataList.add(DataFormater.noNullValue(data.getLogTime(), "yyyy-MM-dd HH:mm:ss"));
            String moduleVal = DataFormater.noNullValue(data.getModuleName());
            Module module = cacheService.getModuleMap().get(moduleVal);
            String moduleName = moduleVal;
            if (module != null) {
                moduleName = module.getModuleName();
            } else if (Constants.Module.requestApp.equals(moduleVal)) {
                moduleName = Constants.Module.Text.TEXT_MAP.get(moduleVal);
            }
            dataList.add(moduleName);
            
            String operateVal = DataFormater.noNullValue(data.getOperateName());
            Operate operate = cacheService.getOperateMap().get(operateVal);
            String operateName = operateVal;
            if (operate != null) {
                operateName = operate.getOperateName();
            } else if (Constants.Operate.login.equals(operateVal)) {
                operateName = Constants.Operate.Text.TEXT_MAP.get(operateVal);
            } else if (Constants.Operate.logout.equals(operateVal)) {
                operateName = Constants.Operate.Text.TEXT_MAP.get(operateVal);
            }
            dataList.add(operateName);
            dataList.add(DataFormater.noNullValue(data.getEventParam()));
            dataList.add(DataFormater.noNullValue(data.getEventObject()));
            String eventResult = DataFormater.noNullValue(data.getEventResult());
            dataList.add(SysLog.EVENT_RESULT_OK.equals(eventResult) ? "成功" : "失败");
        }
        return dataList;
    }
    
    @Override
    public Object converObject(Object src) {
        Row row = (Row) src;
        int i = 0;
        Cell cell = row.getCell(i++);
        if (cell == null) return null;
        SysLog sysLog = new SysLog();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sysLog.setLogId(UUID.randomUUID().toString().replaceAll("-", ""));
        
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String UserIdValue = cell.getStringCellValue();
        sysLog.setUserId(java.lang.String.valueOf(UserIdValue));
        
        cell = row.getCell(i++);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String UserNameValue = cell.getStringCellValue();
        sysLog.setUserName(java.lang.String.valueOf(UserNameValue));
        
        cell = row.getCell(i++);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String IpAddressValue = cell.getStringCellValue();
        sysLog.setIpAddress(java.lang.String.valueOf(IpAddressValue));
        
        cell = row.getCell(i++);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String LogTimeValue = cell.getStringCellValue();
        try {
            sysLog.setLogTime(format.parse(LogTimeValue));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        cell = row.getCell(i++);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String ModuleNameValue = cell.getStringCellValue();

        if (Constants.Module.Text.TEXT_MAP.get(Constants.Module.requestApp).equals(ModuleNameValue)) {
            ModuleNameValue = Constants.Module.requestApp;
        } else {
            Iterator<Module> mIt = cacheService.getModuleMap().values().iterator();
            while (mIt.hasNext()) {
                Module o = mIt.next();
                if (o.getModuleName().equals(ModuleNameValue)) {
                    ModuleNameValue = o.getModuleId();
                    break;
                }
            }
        }
        sysLog.setModuleName(ModuleNameValue);
        
        cell = row.getCell(i++);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String OperateNameValue = cell.getStringCellValue();

        if (Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.login).equals(OperateNameValue)) {
            OperateNameValue = Constants.Operate.login;
        } else if (Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.logout).equals(OperateNameValue)) {
            OperateNameValue = Constants.Operate.logout;
        } else {
            Iterator<Operate> opIt = cacheService.getOperateMap().values().iterator();
            while (opIt.hasNext()) {
                Operate o = opIt.next();
                if (o.getOperateName().equals(OperateNameValue)) {
                    OperateNameValue = o.getOperateId();
                    break;
                }
            }
        }
        
        cell = row.getCell(i++);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String EventParamValue = cell.getStringCellValue();
        sysLog.setEventParam(java.lang.String.valueOf(EventParamValue));
        
        cell = row.getCell(i++);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String EventObjectValue = cell.getStringCellValue();
        sysLog.setEventObject(EventObjectValue + RandomStringUtils.random(4, true, true));
        
        cell = row.getCell(i++);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String EventResultValue = cell.getStringCellValue();
        if ("成功".equals(EventResultValue)) {
            EventResultValue = SysLog.EVENT_RESULT_OK;
        } else if ("失败".equals(EventResultValue)) {
            EventResultValue = SysLog.EVENT_RESULT_FAIL;
        }
        sysLog.setEventResult(java.lang.String.valueOf(EventResultValue));
            
        return sysLog;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addData(List<Object> dataList) {
        this.baseService.addListBatch(dataList);
//        this.baseService.addList(dataList);
    }
    
    @Transactional(readOnly = true)
    @Override
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return sysLogDao.queryPage(pageNo, pageSize, param, isSQL);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SysLog> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return sysLogDao.query(pageNo, pageSize, param, isSQL);
    }

    @Transactional(readOnly = true)
    @Override
    public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return sysLogDao.report(pageNo, pageSize, param, isSQL);
    }
    
}
