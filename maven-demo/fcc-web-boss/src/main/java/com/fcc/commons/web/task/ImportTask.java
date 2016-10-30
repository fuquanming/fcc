package com.fcc.commons.web.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fcc.commons.web.service.ImportService;
import com.fcc.commons.web.view.ImportMessage;


/**
 * <p>Description:导入数据</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ImportTask implements Runnable {
	
	private static Logger logger = Logger.getLogger(ImportTask.class);
	
	private ImportMessage importMessage = new ImportMessage();
	
	private String runningKey;
	
	/** 导入数据标识 */
	private boolean importDataFlag;
    /** 导入数据保存的文件夹 */
	private String importDataPath;
	/** 上传的文件名 */
	private String fileName;
	/** 上传的文件流 */
	private InputStream fileInputStream;
	
	private ImportService importService;
	
    /** 上传excel里共几列 */
	private int cellNum;
	
	/** 上传数据总数 */
	private int totalSize = 0;
	
	public ImportTask() {
	}
	
	public ImportTask(String importDataPath, String fileName, InputStream fileInputStream, ImportService importService, int cellNum) {
        super();
        this.importDataPath = importDataPath;
        this.fileName = fileName;
        this.fileInputStream = fileInputStream;
        this.importService = importService;
        this.cellNum = cellNum;
    }
	
    public void run() {
		long startTime = System.currentTimeMillis();
		FileInputStream fis = null;
		try {
			File saveFile = saveFile();
			// 读取Execle
			Workbook rwb = null;
			try {
				fis = new FileInputStream(saveFile);
				String fileName = saveFile.getName().toLowerCase();
				fileName = fileName.substring(fileName.lastIndexOf("."));
				if (fileName.contains(".xlsx")) { // 2007
					rwb = new XSSFWorkbook(fis);
				} else if (fileName.contains(".xls")) { // 97-03
					rwb = new HSSFWorkbook(fis);
				}
				importData(rwb);
			} catch (Exception e) {
				logger.error("打开文件出错：", e);
			} finally {
				IOUtils.closeQuietly(fis);
			}
		} catch (Exception e) {
			logger.error("导入数据失败！", e);
			e.printStackTrace();
		} finally {
			importMessage.setImportFlag(true);
			long endTime = System.currentTimeMillis();
			logger.info("time=" + (endTime - startTime) + ",totalSize=" + totalSize);
			importDataFlag = false;
		}
	}
	/** 保存文件 */
	public File saveFile() throws Exception {
		File importDataPathFile = new File(importDataPath);
        if (importDataPathFile.exists() == false) importDataPathFile.mkdirs();
        
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileNo = format.format(new Date()) + "_" + runningKey;
		// 文件后缀
		String fileSuffix = fileName.substring(fileName.indexOf(".")).toLowerCase();
		// 保存文件
		String saveFilePath = new StringBuilder().append(importDataPath).append(fileNo)
		.append(fileSuffix).toString();
		File saveFile = new File(saveFilePath);
		FileOutputStream fos = new FileOutputStream(saveFile);
		IOUtils.copy(fileInputStream, fos);
		return saveFile;
	}
	
	/** 导入数据 */
	public void importData(Workbook rwb) throws Exception {
		List<Object> dataList = new ArrayList<Object>();
		Sheet sheet = rwb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			Row row = sheet.getRow(i); 
			if (row == null) break;
			Cell cell = row.getCell(0);
			if (cell == null) break;
			
			List<String> cellList = new ArrayList<String>(cellNum);
			for (int j = 0; j < cellNum; j++) {
				cell = row.getCell(j);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cellList.add(cell.getStringCellValue());
			}
			
			Object dataObj = importService.dataConver(cellList);
			
			totalSize++;
			dataList.add(dataObj);
			if (i % 500 == 0) {
				importService.saveData(dataList);
				dataList.clear();
			}
			importMessage.setCurrentSize(totalSize);
//			Thread.sleep(1000);
		}
		if (dataList.size() > 0) {
			importService.saveData(dataList);
			dataList.clear();
		}
		importMessage.setCurrentSize(totalSize);
	}
	public ImportMessage getImportMessage() {
		return importMessage;
	}
	public void setImportMessage(ImportMessage importMessage) {
		this.importMessage = importMessage;
	}
	public String getRunningKey() {
		return runningKey;
	}
	public ImportTask setRunningKey(String runningKey) {
		this.runningKey = runningKey;
		return this;
	}
	public String getImportDataPath() {
		return importDataPath;
	}
	public ImportTask setImportDataPath(String importDataPath) {
		this.importDataPath = importDataPath;
		return this;
	}
	public String getFileName() {
		return fileName;
	}
	public ImportTask setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}
	public boolean getImportDataFlag() {
        return importDataFlag;
    }
    public void setImportDataFlag(boolean importDataFlag) {
        this.importDataFlag = importDataFlag;
    }
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	public ImportTask setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
		return this;
	}
	public ImportService getmportService() {
		return importService;
	}
	public ImportTask setImportService(ImportService importService) {
		this.importService = importService;
		return this;
	}
	public int getCellNum() {
		return cellNum;
	}
	public ImportTask setCellNum(int cellNum) {
		this.cellNum = cellNum;
		return this;
	}

}
