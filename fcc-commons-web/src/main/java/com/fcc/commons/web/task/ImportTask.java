package com.fcc.commons.web.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.fcc.commons.web.service.ConfigService;
import com.fcc.commons.web.service.ImportService;
import com.fcc.commons.web.util.SpringContextUtil;
import com.fcc.commons.web.view.ImportMessage;


/**
 * <p>Description:导入数据</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ImportTask implements Runnable {
	
	private Logger logger = Logger.getLogger(ImportTask.class);
	
	private ImportMessage importMessage;
	
    private String runningKey;
	
	/** 导入数据标识 */
	private boolean importDataFlag;
    /** 导入数据保存的文件夹 */
	private String importDataPath;
	/** 上传的文件 */
	private MultipartFile file;

    /** 上传的文件名 */
	private String fileName;
	/** 上传的文件流 */
	private InputStream fileInputStream;
	
	private ImportService importService;
	/** 从第几行数据读取 */
	private int beginRowNum;
	
	/** 上传数据总数 */
	private int totalSize = 0;
	
	private int batchSize = 5000;
	
	private int batchTaskSize = 5;
	
	private ReentrantLock lock = new ReentrantLock();
	
	private List<BatchTask> batchTaskList = new ArrayList<ImportTask.BatchTask>(batchTaskSize);
	
	public ImportTask() {
	}
	
	public ImportTask(String runningKey, String importDataPath, MultipartFile file, ImportService importService, int beginRowNum) {
        super();
        this.runningKey = runningKey;
        this.importDataPath = importDataPath;
        this.file = file;
        this.importService = importService;
        this.beginRowNum = beginRowNum;
    }
	
	public ImportTask(String runningKey, String importDataPath, String fileName, InputStream fileInputStream, ImportService importService, int beginRowNum) {
        super();
        this.runningKey = runningKey;
        this.importDataPath = importDataPath;
        this.fileName = fileName;
        this.fileInputStream = fileInputStream;
        this.importService = importService;
        this.beginRowNum = beginRowNum;
    }
	
	public void init() {
	    if (importMessage == null) {
	        importMessage = new ImportMessage();
	    } else {
	        importMessage.setCurrentSize(0);
	        importMessage.setFileFlag(false);
	        importMessage.setImportFlag(false);
	    }
        if (batchTaskList.size() == 0) {
            for (int i = 0; i < batchTaskSize; i++) {
                BatchTask batchTask = new BatchTask();
                batchTaskList.add(batchTask);
            }
        }
        beginRowNum = beginRowNum - 1;
        importDataFlag = true;
        totalSize = 0;
	}
	
    public void run() {
		long startTime = System.currentTimeMillis();
		InputStream is = null;
		Workbook rwb = null;
		try {
		    boolean fileFlag = false;
		    if (file != null) {
		        this.fileInputStream = file.getInputStream();
		        this.fileName = file.getOriginalFilename();
		    }
			File saveFile = saveFile();
			// 读取Execle
			try {
				if (saveFile != null) {
				    is = new FileInputStream(saveFile);
				} else {
				    is = fileInputStream;
				}
				String fileNameTemp = fileName.toLowerCase();
				fileNameTemp = fileNameTemp.substring(fileNameTemp.lastIndexOf("."));
				if (fileNameTemp.contains(".xlsx")) { // 2007
					rwb = new XSSFWorkbook(is);
				} else if (fileNameTemp.contains(".xls")) { // 97-03
					rwb = new HSSFWorkbook(is);
				}
				fileFlag = true;
			} catch (Exception e) {
				logger.error("打开文件出错：", e);
			} finally {
				IOUtils.closeQuietly(is);
				is = null;
			}
			importMessage.setFileFlag(fileFlag);
			if (fileFlag) {
			    importExcelData(rwb);
			    waitBatchTask();
			}
		} catch (Exception e) {
			logger.error("导入数据失败！", e);
			e.printStackTrace();
		} finally {
		    IOUtils.closeQuietly(fileInputStream);
			importMessage.setImportFlag(true);
			rwb = null;
			fileInputStream = null;
			file = null;
			long endTime = System.currentTimeMillis();
			String info = "import end:%d,time=%d,totalSize=%s";
			logger.info(String.format(info, Thread.currentThread().getId(), (endTime - startTime), importMessage.getCurrentSize()));
			importDataFlag = false;
		}
	}
	/** 保存文件 */
	public File saveFile() throws Exception {
	    if (importDataPath == null) return null;
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
    public void importExcelData(Workbook rwb) throws Exception {
        ConfigService configService = SpringContextUtil.getBean(ConfigService.class);
        Sheet sheet = rwb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        totalSize = rows - beginRowNum;
        if (totalSize < 0) {
            totalSize = 0;
            importMessage.setCurrentSize(totalSize);
            return;
        }
        int begin = 1;
        int end = batchSize;
        while (true) {
            BatchTask batchTask = getBatchTask();
            if (batchTask == null) {
                Thread.sleep(100);
            } else {
                batchTask.sheet = sheet;
                batchTask.running = true;
                if (end > totalSize) {
                    end = totalSize;
                }
                batchTask.begin = begin;
                batchTask.end = end;
                configService.getThreadPool().execute(batchTask);

                begin = end + 1;
                end = begin + batchSize - 1;
                if (begin > totalSize) {
                    break;
                }
                if (end > totalSize) {
                    end = totalSize;
                }
            }
        }
    }
    
    public void waitBatchTask() throws Exception {
        while (true) {
            BatchTask task = null;
            for (BatchTask batchTask : batchTaskList) {
                task = batchTask;
                if (task.running) {
                    break;
                }
            }
            if (task.running) {
                Thread.sleep(100);
            } else {
                break;
            }
        }
    }
    
    public BatchTask getBatchTask() {
        for (int i = 0; i < batchTaskSize; i++) {
            BatchTask batchTask = batchTaskList.get(i);
            if (batchTask.running == false) {
                return batchTask;
            }
        }
        return null;
    }

    public void addSize(int size) {
        lock.lock();
        try {
            int currentSize = importMessage.getCurrentSize();
            importMessage.setCurrentSize(currentSize + size);
        } finally {
            lock.unlock();
        }
    }
	
    class BatchTask implements Runnable {
        Logger logger = Logger.getLogger(BatchTask.class);
        boolean running = false;
        Sheet sheet;
        int begin;
        int end;
        List<Object> dataList = new ArrayList<Object>(batchSize);
        @Override
        public void run() {
            running = true;
//            long beginTime = System.currentTimeMillis();
            try {
                for (int i = begin; i <= end; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) break;
                    Cell cell = row.getCell(0);
                    if (cell == null) break;

                    Object dataObj = importService.converObject(row);
                    if (dataObj != null) {
                        dataList.add(dataObj);
                    }
                }
                importService.addData(dataList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                addSize(dataList.size());
                dataList.clear();
                running = false;
//                long endTime = System.currentTimeMillis();
//                logger.info(Thread.currentThread().getId() + ":end:" + (endTime - beginTime) + ":" + (Runtime.getRuntime().freeMemory() / 1024));
            }
        }
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
	public MultipartFile getFile() {
        return file;
    }
    public void setFile(MultipartFile file) {
        this.file = file;
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
	public int getBeginRowNum() {
        return beginRowNum;
    }
    public void setBeginRowNum(int beginRowNum) {
        this.beginRowNum = beginRowNum;
    }
}
