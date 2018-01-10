package com.fcc.commons.data.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.fcc.commons.data.ExportData;

/**
 * <p>Description:导出数据任务</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ExportXmlTask extends BaseExportTask {
	
	/** 导出xml的Root */
    private Element rootElement;
	
    private ExportData exportData;
    
    private Document document;
    /** 需要保存的数据 */
    private List<Object> dataList;
    
	public ExportXmlTask() {
	}
	
    public ExportXmlTask(String runningKey, Element rootElement, String exportDataPath, Object queryService, String queryServiceMethodName, int pageNoSub,
            Object[] queryParams, ExportData exportData, int dataQueryCycle) {
        super();
        this.runningKey = runningKey;
        this.rootElement = rootElement;
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
        fileExt = "xml";
        this.appendDataFlag = true;
        
        document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        // 添加ROOT信息
        if (rootElement != null) {
            document.setRootElement(rootElement.createCopy());
        } else {
            rootElement = document.addElement("Datas");
        }
    }
    
    @Override
    protected void converData(Object obj) throws Exception {
        Element element = (Element) exportData.exportDataConver(obj);
        if (document != null) {
            document.getRootElement().add(element);
        } else {
            if (dataList == null) dataList = new ArrayList<Object>(this.queryDataSize);
            dataList.add(element);
        }
    }

    @Override
    protected void saveFile(File file) {
        if (file.exists() == false) {// 写文件
            FileWriter fileWriter = null;
            XMLWriter xmlWriter = null;
            try {
                fileWriter = new FileWriter(file);
                xmlWriter = new XMLWriter(fileWriter);
                xmlWriter.write(document);
                xmlWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (xmlWriter != null) {
                    try {
                        xmlWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (document != null) document.clearContent();
            document = null;
            fileWriter = null;
            xmlWriter = null;
        } else {// 追加文件
            if (dataList != null) {
                int size = dataList.size();
                if (size == 0) return;
                // 读取大文件
                RandomAccessFile raf = null;
                try {
                    raf = new RandomAccessFile(file, "rw");
                    String rootName = rootElement.getName();
                    // 结束标示如 </Products>
                    raf.seek(raf.length() - rootName.length() - 3);
                    for (int i = 0; i < size; i++) {
                        Element element = (Element) dataList.get(i);
                        raf.write(element.asXML().getBytes("UTF-8"));
                    }
                    raf.writeBytes("</");
                    raf.write(rootName.getBytes("UTF-8"));
                    raf.writeBytes(">");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (raf != null) {
                        try {
                            raf.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                dataList.clear();
            }
        }
    }
    
	public ExportData getExportData() {
		return exportData;
	}

	public ExportXmlTask setExportData(ExportData exportData) {
		this.exportData = exportData;
		return this;
	}

    public Element getRootElement() {
        return rootElement;
    }

    public void setRootElement(Element rootElement) {
        this.rootElement = rootElement;
    }
}
