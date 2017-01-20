package com.fcc.web.sys.service.impl;

import java.util.ArrayList;
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
import com.fcc.web.sys.dao.SysKeyDao;
import com.fcc.web.sys.model.SysKey;
import com.fcc.web.sys.service.SysKeyService;
/**
 * <p>Description:SysKey</p>
 */

@Service
public class SysKeyServiceImpl implements SysKeyService, ExportService, ImportService {
    @Resource
    private SysKeyDao sysKeyDao;
    @Resource
    private BaseService baseService;
    
    @Override
    public List<String> dataConver(Object converObj) {
        List<String> dataList = new ArrayList<String>();
        if (converObj instanceof SysKey) {
            SysKey data = (SysKey) converObj;
            dataList.add(DataFormater.noNullValue(data.getKyeId()));
            dataList.add(DataFormater.noNullValue(data.getLinkType()));
            dataList.add(DataFormater.noNullValue(data.getLinkId()));
            dataList.add(DataFormater.noNullValue(data.getKeyValue()));
        }
        return dataList;
    }
    
    public Object converObject(Object src) {
        Row row = (Row) src;
        Cell cell = row.getCell(0);
        if (cell == null) return null;
        SysKey sysKey = new SysKey();
        try {
            sysKey.setKyeId(UUID.randomUUID().toString().replaceAll("-", ""));
            Cell linkTypeCell = row.getCell(0);
            linkTypeCell.setCellType(Cell.CELL_TYPE_STRING);
            String linkTypeValue = linkTypeCell.getStringCellValue();
            sysKey.setLinkType(java.lang.String.valueOf(linkTypeValue));
            Cell linkIdCell = row.getCell(1);
            linkIdCell.setCellType(Cell.CELL_TYPE_STRING);
            String linkIdValue = linkIdCell.getStringCellValue();
            sysKey.setLinkId(java.lang.String.valueOf(linkIdValue));
            Cell keyValueCell = row.getCell(2);
            keyValueCell.setCellType(Cell.CELL_TYPE_STRING);
            String keyValueValue = keyValueCell.getStringCellValue();
            sysKey.setKeyValue(java.lang.String.valueOf(keyValueValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sysKey;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addData(List<Object> dataList) {
        this.baseService.addListBatch(dataList);
    }
    
    @Transactional(readOnly = true)
    @Override
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return sysKeyDao.queryPage(pageNo, pageSize, param, isSQL);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<SysKey> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return sysKeyDao.query(pageNo, pageSize, param, isSQL);
    }
    
    @Transactional(readOnly = true)
    @Override
    public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return sysKeyDao.report(pageNo, pageSize, param, isSQL);
    }
}