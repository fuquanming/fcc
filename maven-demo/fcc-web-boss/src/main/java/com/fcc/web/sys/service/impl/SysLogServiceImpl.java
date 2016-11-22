package com.fcc.web.sys.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.DataFormater;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.web.common.Constants;
import com.fcc.commons.web.service.ExportService;
import com.fcc.commons.web.service.ImportService;
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
public class SysLogServiceImpl implements SysLogService, ExportService, ImportService {
    @Resource
    private BaseService baseService;
    @Resource
    private CacheService cacheService;
    @Resource
    private SysLogDao sysLogDao;
    
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
            } else if (Constants.MODULE.REQUEST_APP.equals(moduleVal)) {
                moduleName = Constants.MODULE.Text.TEXT_MAP.get(moduleVal);
            }
            dataList.add(moduleName);
            
            String operateVal = DataFormater.noNullValue(data.getOperateName());
            Operate operate = cacheService.getOperateMap().get(operateVal);
            String operateName = operateVal;
            if (operate != null) {
                operateName = operate.getOperateName();
            } else if (Constants.OPERATE.LOGIN.equals(operateVal)) {
                operateName = Constants.OPERATE.Text.TEXT_MAP.get(operateVal);
            } else if (Constants.OPERATE.LOGOUT.equals(operateVal)) {
                operateName = Constants.OPERATE.Text.TEXT_MAP.get(operateVal);
            }
            dataList.add(operateName);
            dataList.add(DataFormater.noNullValue(data.getEventParam()));
            dataList.add(DataFormater.noNullValue(data.getEventObject()));
            String eventResult = DataFormater.noNullValue(data.getEventResult());
            dataList.add(SysLog.EVENT_RESULT_OK.equals(eventResult) ? "成功" : "失败");
        }
        return dataList;
    }
    
    
    public Object dataConver(List<String> dataList) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int size = dataList.size();
        SysLog sysLog = new SysLog();
        for (int i = 0; i < size; i++) {
            String value = dataList.get(i);
            if (i == 0) {
                sysLog.setUserId(value);
            } else if (i == 1) {
                sysLog.setUserName(value);
            } else if (i == 2) {
                sysLog.setIpAddress(value);
            } else if (i == 3) {
                try {
                    sysLog.setLogTime(format.parse(value));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (i == 4) {
                if (Constants.MODULE.Text.TEXT_MAP.get(Constants.MODULE.REQUEST_APP).equals(value)) {
                    value = Constants.MODULE.REQUEST_APP;
                } else {
                    Iterator<Module> mIt = cacheService.getModuleMap().values().iterator();
                    while (mIt.hasNext()) {
                        Module o = mIt.next();
                        if (o.getModuleName().equals(value)) {
                            value = o.getModuleId();
                            break;
                        }
                    }
                }
                sysLog.setModuleName(value);
            } else if (i == 5) {
                if (Constants.OPERATE.Text.TEXT_MAP.get(Constants.OPERATE.LOGIN).equals(value)) {
                    value = Constants.OPERATE.LOGIN;
                } else if (Constants.OPERATE.Text.TEXT_MAP.get(Constants.OPERATE.LOGOUT).equals(value)) {
                    value = Constants.OPERATE.LOGOUT;
                } else {
                    Iterator<Operate> opIt = cacheService.getOperateMap().values().iterator();
                    while (opIt.hasNext()) {
                        Operate o = opIt.next();
                        if (o.getOperateName().equals(value)) {
                            value = o.getOperateId();
                            break;
                        }
                    }
                }
                sysLog.setOperateName(value);
            } else if (i == 6) {
                sysLog.setEventParam(value);
            } else if (i == 7) {
                sysLog.setEventObject(value);
            } else if (i == 8) {
                if ("成功".equals(value)) {
                    value = SysLog.EVENT_RESULT_OK;
                } else if ("失败".equals(value)) {
                    value = SysLog.EVENT_RESULT_FAIL;
                }
                sysLog.setEventResult(value);
            } 
        }
        return sysLog;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveData(List<Object> dataList) {
        this.baseService.createList(dataList);
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
