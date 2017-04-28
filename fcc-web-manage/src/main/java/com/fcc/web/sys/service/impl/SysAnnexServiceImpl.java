package com.fcc.web.sys.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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
import com.fcc.web.sys.config.ConfigUtil;
import com.fcc.web.sys.dao.SysAnnexDao;
import com.fcc.web.sys.model.SysAnnex;
import com.fcc.web.sys.service.SysAnnexService;
/**
 * <p>Description:SysAnnex</p>
 */

@Service
public class SysAnnexServiceImpl implements SysAnnexService, ExportService, ImportService {
    @Resource
    private SysAnnexDao sysAnnexDao;
    @Resource
    private BaseService baseService;
    
    @Override
    public List<String> dataConver(Object converObj) {
        List<String> dataList = new ArrayList<String>();
        if (converObj instanceof SysAnnex) {
            SysAnnex data = (SysAnnex) converObj;
            dataList.add(DataFormater.noNullValue(data.getAnnexId()));
            dataList.add(DataFormater.noNullValue(data.getLinkId()));
            dataList.add(DataFormater.noNullValue(data.getLinkType()));
            dataList.add(DataFormater.noNullValue(data.getAnnexName()));
            dataList.add(DataFormater.noNullValue(data.getFileName()));
            dataList.add(DataFormater.noNullValue(data.getFileType()));
            dataList.add(DataFormater.noNullValue(data.getFileUrl()));
            dataList.add(DataFormater.noNullValue(data.getFileSize()));
            dataList.add(DataFormater.noNullValue(data.getRemark()));
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
        SysAnnex sysAnnex = new SysAnnex();
        try {
            sysAnnex.setAnnexId(UUID.randomUUID().toString().replaceAll("-", ""));
            Cell linkIdCell = row.getCell(0);
            linkIdCell.setCellType(Cell.CELL_TYPE_STRING);
            String linkIdValue = linkIdCell.getStringCellValue();
            sysAnnex.setLinkId(java.lang.String.valueOf(linkIdValue));
            Cell linkTypeCell = row.getCell(1);
            linkTypeCell.setCellType(Cell.CELL_TYPE_STRING);
            String linkTypeValue = linkTypeCell.getStringCellValue();
            sysAnnex.setLinkType(java.lang.String.valueOf(linkTypeValue));
            Cell annexNameCell = row.getCell(2);
            annexNameCell.setCellType(Cell.CELL_TYPE_STRING);
            String annexNameValue = annexNameCell.getStringCellValue();
            sysAnnex.setAnnexName(java.lang.String.valueOf(annexNameValue));
            Cell fileNameCell = row.getCell(3);
            fileNameCell.setCellType(Cell.CELL_TYPE_STRING);
            String fileNameValue = fileNameCell.getStringCellValue();
            sysAnnex.setFileName(java.lang.String.valueOf(fileNameValue));
            Cell fileTypeCell = row.getCell(4);
            fileTypeCell.setCellType(Cell.CELL_TYPE_STRING);
            String fileTypeValue = fileTypeCell.getStringCellValue();
            sysAnnex.setFileType(java.lang.String.valueOf(fileTypeValue));
            Cell fileUrlCell = row.getCell(5);
            fileUrlCell.setCellType(Cell.CELL_TYPE_STRING);
            String fileUrlValue = fileUrlCell.getStringCellValue();
            sysAnnex.setFileUrl(java.lang.String.valueOf(fileUrlValue));
            Cell fileSizeCell = row.getCell(6);
            fileSizeCell.setCellType(Cell.CELL_TYPE_STRING);
            String fileSizeValue = fileSizeCell.getStringCellValue();
            sysAnnex.setFileSize(java.lang.Long.valueOf(fileSizeValue));
            Cell remarkCell = row.getCell(7);
            remarkCell.setCellType(Cell.CELL_TYPE_STRING);
            String remarkValue = remarkCell.getStringCellValue();
            sysAnnex.setRemark(java.lang.String.valueOf(remarkValue));
            Cell createUserCell = row.getCell(8);
            createUserCell.setCellType(Cell.CELL_TYPE_STRING);
            String createUserValue = createUserCell.getStringCellValue();
            sysAnnex.setCreateUser(java.lang.String.valueOf(createUserValue));
            Cell createTimeCell = row.getCell(9);
            createTimeCell.setCellType(Cell.CELL_TYPE_STRING);
            String createTimeValue = createTimeCell.getStringCellValue();
            sysAnnex.setCreateTime(format.parse(createTimeValue));
            Cell updateUserCell = row.getCell(10);
            updateUserCell.setCellType(Cell.CELL_TYPE_STRING);
            String updateUserValue = updateUserCell.getStringCellValue();
            sysAnnex.setUpdateUser(java.lang.String.valueOf(updateUserValue));
            Cell updateTimeCell = row.getCell(11);
            updateTimeCell.setCellType(Cell.CELL_TYPE_STRING);
            String updateTimeValue = updateTimeCell.getStringCellValue();
            sysAnnex.setUpdateTime(format.parse(updateTimeValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sysAnnex;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addData(List<Object> dataList) {
        this.baseService.addListBatch(dataList);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysAnnex> add(String linkType, String linkId, String annexType, String[] uploadFileNames, String[] uploadFileRealNames) {
        Date now = new Date();
        String timePath = DateFormatUtils.format(now, "yyyyMMdd");
        int length = uploadFileNames.length;
        StringBuilder sb = new StringBuilder();
        sb.append(ConfigUtil.getFileUploadPath()).append(Constants.uploadFileTempPath);
        String tempParentPath = sb.toString();
        sb = new StringBuilder();
        sb.append(ConfigUtil.getFileUploadPath())
        .append(Constants.uploadFilePath)
        .append(File.separatorChar).append(linkType)
        .append(File.separatorChar).append(annexType)
        .append(File.separatorChar).append(timePath);
        StringBuilder fileUrlSb = new StringBuilder();
        fileUrlSb.append(Constants.uploadFilePath)
        .append(File.separatorChar).append(linkType)
        .append(File.separatorChar).append(annexType)
        .append(File.separatorChar).append(timePath);
        String parentPath = sb.toString();
        String urlPath = fileUrlSb.toString();
        File parentFile = new File(parentPath);
        if (parentFile.exists() == false) parentFile.mkdirs();
        List<SysAnnex> list = new ArrayList<SysAnnex>(length);
        for (int i = 0; i < length; i++) {
            boolean temp = false;
            String fileName = uploadFileNames[i];
            String fileRealName = uploadFileRealNames[i];
            File file = new File(tempParentPath, fileRealName);
            if (file.exists()) {
                // 移动文件
                try {
                    SysAnnex sysAnnex = new SysAnnex();
                    sysAnnex.setLinkId(linkId);
                    sysAnnex.setLinkType(linkType);
                    sysAnnex.setAnnexType(annexType);
                    sysAnnex.setAnnexName(fileName);
                    sysAnnex.setFileName(fileRealName);
                    sysAnnex.setFileType(fileRealName.substring(fileRealName.lastIndexOf(".") + 1).toLowerCase());
                    sysAnnex.setFileSize(file.length());
                    FileUtils.moveFileToDirectory(file, parentFile, true);
                    sysAnnex.setFileUrl(urlPath);
                    sysAnnex.setCreateTime(now);
                    baseService.add(sysAnnex);
                    list.add(sysAnnex);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (temp == false) {
            }
        }
        return list;
    }
    
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String[] annexIds) {
        if (annexIds == null || annexIds.length == 0) return;
        // 删除附件
        List<String> idList = new ArrayList<String>();
        for (String annexId : annexIds) {
            idList.add(annexId);
        }
        List<SysAnnex> dataList = baseService.get(SysAnnex.class, idList, "annexId");
        for (SysAnnex data : dataList) {
            deleteFile(data);
        }
        baseService.deleteById(SysAnnex.class, annexIds, "annexId");
    }
    
    public void deleteFile(SysAnnex sysAnnex) {
        String fileName = sysAnnex.getFileName();
        String fileUrl = sysAnnex.getFileUrl();
        StringBuilder sb = new StringBuilder();
        sb.append(ConfigUtil.getFileUploadPath()).append(fileUrl).append(File.separatorChar).append(fileName);
        File file = new File(sb.toString());
        if (file.exists()) {
            file.delete();
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(SysAnnex sysAnnex) {
        deleteFile(sysAnnex);
        baseService.delete(sysAnnex);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<SysAnnex> query(String linkId, String linkType, String annexType) {
        return sysAnnexDao.query(linkId, linkType, annexType);
    }
    
    @Transactional(readOnly = true)
    @Override
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return sysAnnexDao.queryPage(pageNo, pageSize, param, isSQL);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<SysAnnex> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return sysAnnexDao.query(pageNo, pageSize, param, isSQL);
    }
    
    @Transactional(readOnly = true)
    @Override
    public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        return sysAnnexDao.report(pageNo, pageSize, param, isSQL);
    }
}