package com.fcc.commons.web.task;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.fcc.commons.web.service.ConfigService;
import com.fcc.commons.web.service.ExportService;
import com.fcc.commons.web.util.SpringContextUtil;
import com.fcc.commons.web.view.ExportMessage;
import com.fcc.commons.zip.Zip;

/**
 * <p>Description:导出数据任务</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ExportTask implements Runnable {
	
	private static final Logger logger = Logger.getLogger(ExportTask.class);

	private ExportMessage exportMessage = new ExportMessage();
	
	private String runningKey;
	
	private boolean exportDataFlag;
	/** 导出xls的标题 */
	private List<String> titleList;
	/** 导出数据保存的文件夹 */
	private String exportDataPath;
	/** 查询接口 */
	private Object queryService;
	/** 查询接口方法名 */
	private String queryServiceMethodName;
	/** 页码在参数的下标位置 */
	private int pageNoSub;
	/** 查询参数 */
	private Object[] queryParams;
	
	private ExportService exportService;
	/** 导出数据总大小 */
	private int exportTotalSize = 500000;
	
	public ExportTask() {
	}
	
    public ExportTask(String runningKey, List<String> titleList, String exportDataPath, Object queryService, String queryServiceMethodName, int pageNoSub,
            Object[] queryParams, ExportService exportService) {
        super();
        this.runningKey = runningKey;
        this.titleList = titleList;
        this.exportDataPath = exportDataPath;
        this.queryService = queryService;
        this.queryServiceMethodName = queryServiceMethodName;
        this.pageNoSub = pageNoSub;
        this.queryParams = queryParams;
        this.exportService = exportService;
    }
	
    public void init() {
        ConfigService configService = SpringContextUtil.getBean(ConfigService.class);
        exportTotalSize = configService.getExportDataSize();
        exportMessage.setEmpty(false);
        exportMessage.setCurrentSize(0);
        exportMessage.setFileName(null);
        exportMessage.setError(false);
        exportDataFlag = true;
    }
    
    public void run() {
		long startTime = System.currentTimeMillis();
		FileOutputStream fos = null;
		try {
		    exportDataFlag = true;
			exportData();
		} catch (Exception e) {
			exportMessage.setError(true);
			logger.error("导出数据错误：" + runningKey, e);
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fos);
			exportDataFlag = false;
			long endTime = System.currentTimeMillis();
			String info = "export end:%d,time=%d,totalSize=%d,fileName=%s";
            logger.info(String.format(info, Thread.currentThread().getId(), (endTime - startTime), exportMessage.getCurrentSize(), exportMessage.getFileName()));
		}
	}
	@SuppressWarnings("unchecked")
	private void exportData() throws Exception {
		FileOutputStream fos = null;
		File exportDataPathFile = new File(exportDataPath);
		if (exportDataPathFile.exists() == false) exportDataPathFile.mkdirs();
		
		List<String> filePaths = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileNo = new StringBuilder().append(format.format(new Date())).append("_").append(runningKey).toString();
		int fileFlag = 1;
		int xlsSize = 0;
		int totalSize = 0;
		Workbook workbook = null;
		Sheet sheet = null;
		List<Object> list = null;
		int pageNo = 1;

		Method method = null;
		for (Method m : queryService.getClass().getMethods()) {
			if (m.getName().equals(queryServiceMethodName)) {
				method = m;
				break;
			}
		}
		
		while (true) {
			xlsSize = 0;
			int rowNum = 1;
			boolean endFlag = false;
			int flag = 10;// 轮询10次,一个xls总数5W数据量
			String saveFile = new StringBuilder().append(exportDataPath).append(fileNo)
			.append(fileFlag).append(".xls").toString();
			workbook = new HSSFWorkbook();
			sheet = workbook.createSheet("报表");
			//添加表头、列头
			Row row = sheet.createRow(0);
			int titleSize = titleList.size();
			for (int i = 0; i < titleSize; i++) {
				row.createCell(i).setCellValue(titleList.get(i));
			}
			
			List<String> dataList = null;
			
			for (int i = 0; i < flag; i++) {
				Object returnObj = method.invoke(queryService, queryParams);
				list = (List<Object>) returnObj;
				pageNo++;
				queryParams[pageNoSub] = pageNo;
				int dataSize = (list == null) ? 0 : list.size();
				totalSize += dataSize;
				xlsSize += dataSize;
				exportMessage.setCurrentSize(totalSize);
				if (totalSize == 0) {
					exportMessage.setEmpty(true);
					workbook = null;
					File tempFile = new File(saveFile);
					if (tempFile.exists()) tempFile.delete();
					return;
				} else if (dataSize == 0) {
					endFlag = true;
					break;
				} else {
					for (Object converObj : list) {
						row = sheet.createRow(rowNum);
						dataList = exportService.dataConver(converObj);
						int size = dataList.size();
						for (int k = 0; k < size; k++) {
							row.createCell(k).setCellValue(dataList.get(k));
						}
						dataList.clear();
						rowNum++;
//						Thread.sleep(100);
					}
				}
				list.clear();
				list = null;
			}
			if (xlsSize > 0) {
				File file = new File(saveFile);
				fos = new FileOutputStream(file);
				workbook.write(fos);
				IOUtils.closeQuietly(fos);
				filePaths.add(saveFile);
				workbook = null;
				fileFlag++;
			}
			if (endFlag || totalSize >= exportTotalSize) {
				break;
			}
		}
		String fileName = fileNo + ".zip";
		new Zip().zipFile(fileName, exportDataPath, filePaths);
		for(String filePath : filePaths){//删除文件
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
		}
		exportMessage.setFileName(fileName);
	}

	public ExportMessage getExportMessage() {
		return exportMessage;
	}

	public void setExportMessage(ExportMessage exportMessage) {
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

	public ExportService getExportService() {
		return exportService;
	}

	public ExportTask setExportService(ExportService exportService) {
		this.exportService = exportService;
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
