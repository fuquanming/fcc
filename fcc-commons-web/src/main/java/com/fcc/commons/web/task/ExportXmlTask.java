package com.fcc.commons.web.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.fcc.commons.web.service.ExportXmlService;

/**
 * <p>Description:导出数据任务</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ExportXmlTask extends BaseExportTask {
	
	/** 导出xml的Root */
    private Element rootElement;
	
    private ExportXmlService exportXmlService;
    
    private Document document;

	public ExportXmlTask() {
	}
	
    public ExportXmlTask(String runningKey, Element rootElement, String exportDataPath, Object queryService, String queryServiceMethodName, int pageNoSub,
            Object[] queryParams, ExportXmlService exportXmlService, int dataQueryCycle) {
        super();
        this.runningKey = runningKey;
        this.rootElement = rootElement;
        this.exportDataPath = exportDataPath;
        this.queryService = queryService;
        this.queryServiceMethodName = queryServiceMethodName;
        this.pageNoSub = pageNoSub;
        this.queryParams = queryParams;
        this.exportXmlService = exportXmlService;
        this.dataQueryCycle = dataQueryCycle;
    }
    
//    public void run() {
//		long startTime = System.currentTimeMillis();
//		FileOutputStream fos = null;
//		try {
//		    exportDataFlag = true;
//			exportData();
//		} catch (Exception e) {
//			exportMessage.setError(true);
//			logger.error("导出数据错误：" + runningKey, e);
//			e.printStackTrace();
//		} finally {
//			IOUtils.closeQuietly(fos);
//			exportDataFlag = false;
//			long endTime = System.currentTimeMillis();
//			String info = "export end:%d,time=%d,totalSize=%d,fileName=%s";
//            logger.info(String.format(info, Thread.currentThread().getId(), (endTime - startTime), exportMessage.getCurrentSize(), exportMessage.getFileName()));
//		}
//	}
//	@SuppressWarnings("unchecked")
//	private void exportData() throws Exception {
//		File exportDataPathFile = new File(exportDataPath);
//		if (exportDataPathFile.exists() == false) exportDataPathFile.mkdirs();
//		
//		List<String> filePaths = new ArrayList<String>();
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//		String fileNo = new StringBuilder().append(format.format(new Date())).append("_").append(runningKey).toString();
//		
//		int fileFlag = 1;
//		int xmlSize = 0;
//		int totalSize = 0;
//		
//		Document document = null;
//		Writer writer = null;
//		XMLWriter xmlWriter = null;
//		
//		List<Object> list = null;
//		int pageNo = 1;
//
//		Method method = null;
//		for (Method m : queryService.getClass().getMethods()) {
//			if (m.getName().equals(queryServiceMethodName)) {
//				method = m;
//				break;
//			}
//		}
//		
//		while (true) {
//			xmlSize = 0;
//			boolean endFlag = false;
//			int flag = dataQueryCycle;// 轮询10次,一个xml总数1000数据量
//			String saveFile = new StringBuilder().append(exportDataPath).append(fileNo)
//			.append(fileFlag).append(".xml").toString();
//			
//			document = DocumentHelper.createDocument();
//			document.setXMLEncoding("UTF-8");
//			// 添加ROOT信息
//			if (rootElement != null) {
//			    document.setRootElement(rootElement.createCopy());
//			} else {
//			    document.addElement("Datas");
//			}
//			
//			for (int i = 0; i < flag; i++) {
//				Object returnObj = method.invoke(queryService, queryParams);
//				list = (List<Object>) returnObj;
//				pageNo++;
//				queryParams[pageNoSub] = pageNo;
//				int dataSize = (list == null) ? 0 : list.size();
//				totalSize += dataSize;
//				xmlSize += dataSize;
//				exportMessage.setCurrentSize(totalSize);
//				if (totalSize == 0) {
//					exportMessage.setEmpty(true);
//					document = null;
//					File tempFile = new File(saveFile);
//					if (tempFile.exists()) tempFile.delete();
//					return;
//				} else if (dataSize == 0) {
//					endFlag = true;
//					break;
//				} else {
//					for (Object converObj : list) {
//					    exportXmlService.converXml(converObj, document);
//					}
//				}
//				list.clear();
//				list = null;
//			}
//			if (xmlSize > 0) {
//				File file = new File(saveFile);
//				
//				try {
//                    writer = new FileWriter(file);
//                    xmlWriter = new XMLWriter(writer); 
//                    xmlWriter.write(document);  
//                    xmlWriter.flush();  
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (xmlWriter != null) {
//                        try {
//                            xmlWriter.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//				
//				filePaths.add(saveFile);
//				
//				writer = null;
//				xmlWriter = null;
//				
//				fileFlag++;
//			}
//			if (endFlag || totalSize >= exportTotalSize) {
//				break;
//			}
//		}
//		String fileName = fileNo + ".zip";
//		new Zip().zipFile(fileName, exportDataPath, filePaths);
//		for(String filePath : filePaths){//删除文件
//			File file = new File(filePath);
//			if (file.exists()) {
//				file.delete();
//			}
//		}
//		exportMessage.setFileName(fileName);
//	}
    
    @Override
    protected void initData() {
        fileExt = "xml";
        
        document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        // 添加ROOT信息
        if (rootElement != null) {
            document.setRootElement(rootElement.createCopy());
        } else {
            document.addElement("Datas");
        }
    }
    
    @Override
    protected void converData(Object obj) throws Exception {
        exportXmlService.converXml(obj, document);
    }

    @Override
    protected void saveFile(File file) {
//        FileWriter fileWriter = null;
//        XMLWriter xmlWriter = null;
//        try {
//            fileWriter = new FileWriter(file);
//            xmlWriter = new XMLWriter(fileWriter);
//            xmlWriter.write(document);
//            xmlWriter.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (xmlWriter != null) {
//                try {
//                    xmlWriter.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        fileWriter = null;
//        xmlWriter = null;
        Writer wr = null;
        try {
            wr = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            wr.write(document.asXML());
            wr.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
             IOUtils.closeQuietly(wr);
        }
        document = null;
    }
    
	public ExportXmlService getExportXmlService() {
		return exportXmlService;
	}

	public ExportXmlTask setExportXmlService(ExportXmlService exportXmlService) {
		this.exportXmlService = exportXmlService;
		return this;
	}

    public Element getRootElement() {
        return rootElement;
    }

    public void setRootElement(Element rootElement) {
        this.rootElement = rootElement;
    }
}
