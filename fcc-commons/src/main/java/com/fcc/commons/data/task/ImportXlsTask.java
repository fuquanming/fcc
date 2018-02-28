package com.fcc.commons.data.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fcc.commons.data.ImportData;
import com.fcc.commons.data.view.ImportDataMessage;


/**
 * <p>Description:导入数据</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ImportXlsTask implements Runnable {
	
	private Logger logger = Logger.getLogger(ImportXlsTask.class);
	
	private ImportDataMessage importMessage;
	
    private String runningKey;
	
	/** 导入数据标识 */
	private boolean importDataFlag;
    /** 导入数据保存的文件夹 */
	private String importDataPath;
	/** 上传的文件 */
	private ImportFileInfo file;

    /** 上传的文件名 */
	private String fileName;
	/** 上传的文件流 */
	private InputStream fileInputStream;
	
	private ImportData importData;
	/** 从第几行数据读取 */
	private int beginRowNum;
	
	/** 上传数据总数 */
	private int totalSize = 0;
	
	private int batchSize = 5000;
	
	private int batchTaskSize = 5;
	
	private ReentrantLock lock = new ReentrantLock();
	
	private List<BatchTask> batchTaskList = new ArrayList<ImportXlsTask.BatchTask>(batchTaskSize);
	
	private ThreadPoolExecutor pool;
	
    public ImportXlsTask() {
	}
	
	public ImportXlsTask(String runningKey, String importDataPath, ImportFileInfo file, ImportData importData, int beginRowNum, ThreadPoolExecutor pool) {
        super();
        this.runningKey = runningKey;
        this.importDataPath = importDataPath;
        this.file = file;
        this.importData = importData;
        this.beginRowNum = beginRowNum;
        this.pool = pool;
    }
	
	public ImportXlsTask(String runningKey, String importDataPath, String fileName, InputStream fileInputStream, ImportData importData, int beginRowNum, ThreadPoolExecutor pool) {
        super();
        this.runningKey = runningKey;
        this.importDataPath = importDataPath;
        this.fileName = fileName;
        this.fileInputStream = fileInputStream;
        this.importData = importData;
        this.beginRowNum = beginRowNum;
        this.pool = pool;
    }
	
	public void init() {
	    if (importMessage == null) {
	        importMessage = new ImportDataMessage();
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
		        this.fileName = file.getFileName();
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
                if (this.pool != null) {
                    pool.execute(batchTask);
                } else {
                    batchTask.run();
                }

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

                    Object dataObj = importData.importDataConver(row);
                    if (dataObj != null) {
                        dataList.add(dataObj);
                    }
                }
                importData.addData(dataList);
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

    public ImportDataMessage getImportMessage() {
        return importMessage;
    }
	public void setImportMessage(ImportDataMessage importMessage) {
		this.importMessage = importMessage;
	}
	public String getRunningKey() {
		return runningKey;
	}
	public ImportXlsTask setRunningKey(String runningKey) {
		this.runningKey = runningKey;
		return this;
	}
	public String getImportDataPath() {
		return importDataPath;
	}
	public ImportXlsTask setImportDataPath(String importDataPath) {
		this.importDataPath = importDataPath;
		return this;
	}
	public ImportFileInfo getImportFileInfo() {
        return file;
    }
    public void setImportFileInfo(ImportFileInfo file) {
        this.file = file;
    }
	public String getFileName() {
		return fileName;
	}
	public ImportXlsTask setFileName(String fileName) {
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
	public ImportXlsTask setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
		return this;
	}
	public ImportData getImportData() {
		return importData;
	}
	public ImportXlsTask setImportData(ImportData importData) {
		this.importData = importData;
		return this;
	}
	public int getBeginRowNum() {
        return beginRowNum;
    }
    public void setBeginRowNum(int beginRowNum) {
        this.beginRowNum = beginRowNum;
    }
    public ThreadPoolExecutor getPool() {
        return pool;
    }
    public void setPool(ThreadPoolExecutor pool) {
        this.pool = pool;
    }
}

class ImportFileInfo {
    private String fileName;
    private InputStream inputStream;
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public InputStream getInputStream() {
        return inputStream;
    }
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}