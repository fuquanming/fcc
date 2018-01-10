package com.fcc.commons.data.task;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.fcc.commons.data.ExportData;
import com.fcc.commons.data.view.ExportDataMessage;

/**
 * <p>Description:导出数据任务</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ExportXlsTask extends BaseExportTask {
	
	/** 导出xls的标题 */
	private List<String> titleList;
	
	private ExportData exportData;
	/** excel行数 */
	private int rowNum;
	/** excel */
	private Workbook workbook;
	/** excel一个sheet */
	private Sheet sheet;
	
	public ExportXlsTask() {
	}
	
    public ExportXlsTask(String runningKey, List<String> titleList, String exportDataPath, Object queryService, String queryServiceMethodName, int pageNoSub,
            Object[] queryParams, ExportData exportData, int dataQueryCycle) {
        super();
        this.runningKey = runningKey;
        this.titleList = titleList;
        this.exportDataPath = exportDataPath;
        this.queryService = queryService;
        this.queryServiceMethodName = queryServiceMethodName;
        this.pageNoSub = pageNoSub;
        this.queryParams = queryParams;
        this.exportData = exportData;
        this.dataQueryCycle = dataQueryCycle;
    }
	
	@Override
	protected void initData() {
	    fileExt = "xls";
	    
	    workbook = new HSSFWorkbook();
        sheet = workbook.createSheet("报表");
        //添加表头、列头
        Row row = sheet.createRow(0);
        int titleSize = titleList.size();
        for (int i = 0; i < titleSize; i++) {
            row.createCell(i).setCellValue(titleList.get(i));
        }
        rowNum = 1;
	}
	
	@SuppressWarnings("unchecked")
    @Override
	protected void converData(Object obj) throws Exception {
	    List<String> dataList = (List<String>) exportData.exportDataConver(obj);
        int size = dataList.size();
        Row row = sheet.createRow(rowNum);
        for (int k = 0; k < size; k++) {
            row.createCell(k).setCellValue(dataList.get(k));
        }
        dataList.clear();
        rowNum++;
	}
	
	@Override
	protected void saveFile(File file) {
	    FileOutputStream fos = null;
	    try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fos);
        }
        workbook = null;
	}

	public ExportDataMessage getExportMessage() {
		return exportMessage;
	}

	public void setExportMessage(ExportDataMessage exportMessage) {
		this.exportMessage = exportMessage;
	}

	public String getRunningKey() {
		return runningKey;
	}

	public void setRunningKey(String runningKey) {
		this.runningKey = runningKey;
	}

	public List<String> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<String> titleList) {
		this.titleList = titleList;
	}

	public String getExportDataPath() {
		return exportDataPath;
	}

	public void setExportDataPath(String exportDataPath) {
		this.exportDataPath = exportDataPath;
	}

	public Object getQueryService() {
		return queryService;
	}

	public void setQueryService(Object queryService) {
		this.queryService = queryService;
	}

	public String getQueryServiceMethodName() {
		return queryServiceMethodName;
	}

	public void setQueryServiceMethodName(String queryServiceMethodName) {
		this.queryServiceMethodName = queryServiceMethodName;
	}

	public int getPageNoSub() {
		return pageNoSub;
	}

	public void setPageNoSub(int pageNoSub) {
		this.pageNoSub = pageNoSub;
	}

	public Object[] getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Object[] queryParams) {
		this.queryParams = queryParams;
	}

	public ExportData getExportData() {
		return exportData;
	}

	public ExportXlsTask setExportData(ExportData exportData) {
		this.exportData = exportData;
		return this;
	}

	public boolean isExportDataFlag() {
		return exportDataFlag;
	}

	public void setExportDataFlag(boolean exportDataFlag) {
		this.exportDataFlag = exportDataFlag;
	}
	
	public int getExportTotalSize() {
        return exportTotalSize;
    }

    public void setExportTotalSize(int exportTotalSize) {
        this.exportTotalSize = exportTotalSize;
    }

}
