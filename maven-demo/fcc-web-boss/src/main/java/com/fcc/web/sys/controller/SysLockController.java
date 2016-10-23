package com.fcc.web.sys.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.DataFormater;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.common.Constanst;
import com.fcc.commons.web.util.ModelAndViewUtil;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.ExportMessage;
import com.fcc.commons.web.view.ImportMessage;
import com.fcc.commons.web.view.Message;
import com.fcc.commons.web.view.ReportInfo;
import com.fcc.commons.zip.Zip;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.model.SysLock;
import com.fcc.web.sys.service.SysLockService;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @email fuquanming@gmail.com
 */
@Controller
@RequestMapping(value={"/manage/sys/sysLock"} )
public class SysLockController {
	
	private static Logger logger = Logger.getLogger(SysLockController.class);
	
	private static String exportDataPath;
	private static String importDataPath;
	
	// 标识导出文件
	private String exportDataFile = "exportDataFile_sysLock";
	private String importDataFile = "importDataFile_sysLock";
	
	private static Boolean exportDataFlag = false;
	private static Boolean importDataFlag = false;
	
	private static ReentrantLock lockExportData = new ReentrantLock();
	private static ReentrantLock lockImportData = new ReentrantLock();
	@Resource
	private BaseService baseService;
	//默认多列排序,example: username desc,createTime asc
	@Resource
	private SysLockService sysLockService;
	
	public SysLockController() {
		StringBuilder sb = new StringBuilder();
		sb.append(CacheUtil.realPath).append(Constanst.EXPORT_DATA_FILENAME)
		.append(File.separatorChar).append("sysLockExport").append(File.separatorChar);
		exportDataPath = sb.toString();
		sb.delete(0, sb.length());
		sb.append(CacheUtil.realPath).append(Constanst.IMPORT_DATA_FILENAME)
		.append(File.separatorChar).append("sysLockImport").append(File.separatorChar);
		importDataPath = sb.toString();
	}

	/** 显示列表 */
	@RequestMapping(value = {"/view"})
	public String view(HttpServletRequest request) {
		return "/WEB-INF/manage/sys/sysLock/sysLock_list";
	}
	
	/** 显示统计报表 */
	@RequestMapping(value = {"/report/view"})
	public String reportView(HttpServletRequest request) {
		return "/WEB-INF/manage/sys/sysLock/sysLock_report_list";
	}
	
	/** 跳转到查看页面 */
	@RequestMapping(value = {"/toView"})
	public String toView(HttpServletRequest request) {
		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			try {
				SysLock sysLock = (SysLock) baseService.get(SysLock.class, java.lang.String.valueOf(id));
				request.setAttribute("sysLock", sysLock);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
		return "/WEB-INF/manage/sys/sysLock/sysLock_view";
	}
	
	/** 跳转到新增页面 */
	@RequestMapping(value = {"/toAdd"})
	public String toAdd(HttpServletRequest request, HttpServletResponse response) {
		return "/WEB-INF/manage/sys/sysLock/sysLock_add";
	}
	
	/** 跳转到修改页面 */
	@RequestMapping(value = {"/toEdit"})
	public String toEdit(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			try {
				SysLock sysLock = (SysLock) baseService.get(SysLock.class, java.lang.String.valueOf(id));
				request.setAttribute("sysLock", sysLock);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
		return "/WEB-INF/manage/sys/sysLock/sysLock_edit";
	}
	
	/** 新增 */
	@RequestMapping(value = {"/add"})
	public ModelAndView add(SysLock sysLock, HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		try {
			sysLock.setLockKey(RandomStringUtils.random(8, true, false));
			String createTimeString = request.getParameter("createTimeString");
			sysLock.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTimeString));
			baseService.create(sysLock);
			message.setSuccess(true);
			message.setMsg("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("保存失败！");
			message.setObj(e.getMessage());
		}
		return mav;
	}
	
	/** 修改 */
	@RequestMapping(value = {"/edit"})
	public ModelAndView edit(SysLock sysLock, HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		try {
			SysLock dbSysLock = null;
			String lockKey = request.getParameter("lockKey");
			if (StringUtils.isNotEmpty(lockKey)) {
				dbSysLock = (SysLock) baseService.get(SysLock.class, lockKey);
			}
			if (dbSysLock != null) {
				String createTimeString = request.getParameter("createTimeString");
				sysLock.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTimeString));
				BeanUtils.copyProperties(sysLock, dbSysLock);
				baseService.update(dbSysLock);
				// 更新
				message.setSuccess(true);
				message.setMsg("修改成功！");
			} else {
				// 修改的不存在
				message.setSuccess(false);
				message.setMsg("修改对象已不存在！");
				message.setObj("修改对象已不存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("修改失败！");
			message.setObj(e.getMessage());
		}
		return mav;
	}
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(value = {"/delete"})
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		String id = request.getParameter("ids");
		try {
			if (id == null || "".equals(id)) {
				throw new RefusedException("请选择要删除的记录！");
			}
			String[] ids = id.split(",");
			baseService.deleteById(SysLock.class, ids, "lockKey");
			message.setSuccess(true);
			message.setMsg("删除成功！");
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("删除失败！");
			message.setObj(e.getMessage());
		}
		return mav;
	}
	
	/**
	 * 列表 返回json 给 easyUI 
	 * @return
	 */
	@RequestMapping("/datagrid")
	@ResponseBody
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
			Map<String, Object> param = getParams(request);
			ListPage listPage = sysLockService.queryPage(dg.getPage(), dg.getRows(), param, false);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			json.setTotal(0L);
			json.setRows(new ArrayList<SysLock>());
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	/** 报表 */
	@RequestMapping("/report/datagrid")
	@ResponseBody
	public EasyuiDataGridJson reportDatagrid(EasyuiDataGrid dg, HttpServletRequest request) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
			Map<String, Object> param = getParams(request);
			String reportGroupName = request.getParameter("reportGroupName");
	        if (StringUtils.isNotEmpty(reportGroupName)) {
	        	param.put("reportGroupName", reportGroupName);
	        	ListPage listPage = sysLockService.report(dg.getPage(), dg.getRows(), param, false);
				json.setTotal(Long.valueOf(listPage.getTotalSize()));
				json.setRows(listPage.getDataList());
	        } else {
	        	json.setTotal(0L);
				json.setRows(new ArrayList<ReportInfo>());
	        }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			json.setTotal(0L);
			json.setRows(new ArrayList<ReportInfo>());
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	private Map<String, Object> getParams(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
	        String lockKey = request.getParameter("lockKey");
	        if (StringUtils.isNotEmpty(lockKey)) {
	        	param.put("lockKey", lockKey);
	        }
	        String lockStatus = request.getParameter("lockStatus");
	        if (StringUtils.isNotEmpty(lockStatus)) {
	        	param.put("lockStatus", lockStatus);
	        }
	        String createTimeBegin = request.getParameter("createTimeBegin");
	        if (StringUtils.isNotEmpty(createTimeBegin)) {
	        	param.put("createTimeBegin", format.parse(createTimeBegin + " 00:00:00"));
	        }
	        String createTimeEnd = request.getParameter("createTimeEnd");
	        if (StringUtils.isNotEmpty(createTimeEnd)) {
	        	param.put("createTimeEnd", format.parse(createTimeEnd + " 23:59:59"));
	        }
	        String sortColumns = request.getParameter("sort");
	        if (StringUtils.isNotEmpty(sortColumns)) {
	        	param.put("sortColumns", sortColumns);
	        }
	        String orderType = request.getParameter("order");
	        if (StringUtils.isNotEmpty(orderType)) {
	        	param.put("orderType", orderType);
	        }
        } catch (Exception e) {
			e.printStackTrace();
		}
		return param;
	}
	
	/** 导出数据 */
	@RequestMapping("/export")
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		if (CacheUtil.isAppDestroy() == true) {
			message.setMsg("系统停止中，请稍后！");
			return mav;
		}
		lockExportData.lock();
		try {
			if (exportDataFlag == false) {
				exportDataFlag = true;
				ExportMessage em = new ExportMessage();
				request.getSession().setAttribute(exportDataFile, em);// 标识导出的文件
				message.setSuccess(true);
				message.setMsg("系统正在导出数据。。。");
				CacheUtil.getPool().execute(new SysLockExport(getParams(request), em));
			} else {
				message.setMsg("系统正在执行上次导出数据，请稍后。。。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			exportDataFlag = false;
			message.setMsg("系统异常");
			message.setObj(e.getMessage());
		} finally {
			lockExportData.unlock();
		}
		return mav;
	}
	
	@RequestMapping("/queryExport")
	@ResponseBody
	public ExportMessage queryExport(HttpServletRequest request, HttpServletResponse response) {
		ExportMessage em = (ExportMessage) request.getSession().getAttribute(exportDataFile);
		if (em == null) {
			em = new ExportMessage();
			em.setEmpty(true);
		} else {
			if (em.getFileName() != null) request.getSession().removeAttribute(exportDataFile);
		}
		return em;
	}
	
	class SysLockExport implements Runnable {
		
		private Map<String, Object> param;
		private ExportMessage message;
		
		public SysLockExport(Map<String, Object> param, ExportMessage message) {
			this.param = param;
			this.message = message;
		}
		
		@SuppressWarnings("resource")
        public void run() {
			long startTime = System.currentTimeMillis();
			String key = CacheUtil.getRunningAppKey("sysLockExportTasker_");
			FileOutputStream fos = null;
			try {
				if (CacheUtil.isAppDestroy() == true) {
					logger.info("系统停止中，sysLockExport任务停止！");
					message.setDestroy(true);
					return;
				}
				CacheUtil.addRunningApp(key);
	            File exportDataPathFile = new File(exportDataPath);
	            if (exportDataPathFile.exists() == false) exportDataPathFile.mkdirs();
	            
				List<String> filePaths = new ArrayList<String>();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				String fileNo = format.format(new Date()) + "_sysLockExport";
				int fileFlag = 1;
				int xlsSize = 0;
				int totalSize = 0;
				Workbook workbook = null;
				Sheet sheet = null;
				List<SysLock> list = null;
				int pageNo = 1;
			
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
					row.createCell(0).setCellValue(SysLock.ALIAS_LOCK_KEY);
					row.createCell(1).setCellValue(SysLock.ALIAS_LOCK_STATUS);
					row.createCell(2).setCellValue(SysLock.ALIAS_CREATE_TIME);
					for (int i = 0; i < flag; i++) {
						Map<String, Object> tempParam = new HashMap<String, Object>();
						tempParam.putAll(param);
						list = sysLockService.query(pageNo, Constanst.EXPORT_DATA_PAGE_SIZE, tempParam, false);
						pageNo++;
						int dataSize = (list == null) ? 0 : list.size();
						totalSize += dataSize;
						xlsSize += dataSize;
						message.setCurrentSize(totalSize);
						if (totalSize == 0) {
							message.setEmpty(true);
							workbook = null;
							File tempFile = new File(saveFile);
							if (tempFile.exists()) tempFile.delete();
							return;
						} else if (dataSize == 0) {
							endFlag = true;
							break;
						} else {
							int listSize = list.size();
							for (int j = 0; j < listSize; j++) {
								row = sheet.createRow(rowNum);
								SysLock data = list.get(j);
						        row.createCell(0).setCellValue(DataFormater.noNullValue(data.getLockKey()));
						        row.createCell(1).setCellValue(DataFormater.noNullValue(data.getLockStatus()));
						        row.createCell(2).setCellValue(DataFormater.noNullValue(data.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
						        rowNum++;
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
						workbook = null;
						filePaths.add(saveFile);
						workbook = null;
						fileFlag++;
					}
					if (endFlag || totalSize >= CacheUtil.getExportDataTotalSize()) {
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
				message.setFileName(fileName);
			} catch (Exception e) {
				message.setError(true);
				logger.error(e);
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(fos);
				long endTime = System.currentTimeMillis();
				logger.info("time=" + (endTime - startTime) + ",fileName=" + message.getFileName());
				exportDataFlag = false;
				CacheUtil.removeRunningApp(key);
			}
		}
	}
	
	/** 导入数据 */
	@RequestMapping("/import")
	public ModelAndView importSysLock(HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		if (CacheUtil.isAppDestroy() == true) {
			message.setMsg("系统停止中，请稍后！");
			return mav;
		}
		lockImportData.lock();
		FileOutputStream fos = null;
		MultipartFile file = null;
		try {
			if (importDataFlag == false) {
				importDataFlag = true;
				// 接收上传文件
				file = ((MultipartHttpServletRequest) request).getFile("upload");
				if (file != null && file.isEmpty() == false) {
					logger.info("上传文件的大小：" + file.getSize());
					logger.info("上传文件的类型：" + file.getContentType());
					logger.info("上传文件的名称：" + file.getOriginalFilename());
					logger.info("上传表单的名称：" + file.getName());
					
					File importDataPathFile = new File(importDataPath);
		            if (importDataPathFile.exists() == false) importDataPathFile.mkdirs();
		            
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
					String fileNo = format.format(new Date()) + "_sysLockImport";
					// 文件名
					String fileName = file.getOriginalFilename();
					// 文件后缀
					String fileSuffix = fileName.substring(fileName.indexOf(".")).toLowerCase();
					// 保存文件
					String saveFilePath = new StringBuilder().append(importDataPath).append(fileNo)
					.append(fileSuffix).toString();
					File saveFile = new File(saveFilePath);
					fos = new FileOutputStream(saveFile);
					IOUtils.copy(file.getInputStream(), fos);
					
					ImportMessage im = new ImportMessage();
					// uploadify-3.2.1 使用flash上传文件 没有携带cookie 代码绑定 原来sessionId
					String jsessionId = request.getParameter("JSESSIONID");
					if (jsessionId != null) {
				    	HttpSession session = (HttpSession) request.getSession().getServletContext().getAttribute(jsessionId);
				    	if (session != null) {
				    		session.setAttribute(importDataFile, im);// 标识导入的文件
				    	} else {
				    		request.getSession().setAttribute(importDataFile, im);
				    	}
					} else {
			    		request.getSession().setAttribute(importDataFile, im);
			    	}
					message.setSuccess(true);
					message.setMsg("已上传成功！系统正在导入数据。。。");
					CacheUtil.getPool().execute(new SysLockImport(im, saveFile));
				} else {
					message.setMsg("请选择文件！");
				}
			} else {
				message.setMsg("系统正在执行上次导入数据，请稍后。。。");
			}
		} catch (Exception e) {
			message.setMsg("上传失败！");
			message.setObj(e.getMessage());
			e.printStackTrace();
			importDataFlag = false;
		} finally {
			try {
				IOUtils.closeQuietly(file.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			IOUtils.closeQuietly(fos);
			lockImportData.unlock();
		}
		return mav;
	}
	
	@RequestMapping("/queryImport")
	@ResponseBody
	public ImportMessage queryImport(HttpServletRequest request, HttpServletResponse response) {
		ImportMessage im = (ImportMessage) request.getSession().getAttribute(importDataFile);
		if (im == null) {
			im = new ImportMessage();
			im.setImportFlag(true);
		} else {
			if (im.isImportFlag()) request.getSession().removeAttribute(importDataFile);
		}
		return im;
	}
	
	class SysLockImport implements Runnable {
		private ImportMessage message;
		private File saveFile;
		public SysLockImport(ImportMessage message, File saveFile) {
			this.message = message;
			this.saveFile = saveFile;
		}
		@SuppressWarnings("deprecation")
        public void run() {
			long startTime = System.currentTimeMillis();
			int totalSize = 0;
			String key = CacheUtil.getRunningAppKey("sysLockImportTasker_");
			FileInputStream fis = null;
			try {
				if (CacheUtil.isAppDestroy() == true) {
					logger.info("系统停止中，SysLockImport任务停止！");
					message.setDestroy(true);
					return;
				}
				CacheUtil.addRunningApp(key);
				List<SysLock> sysLockList = new ArrayList<SysLock>();
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
				} catch (Exception e) {
					logger.error("打开文件出错：" + e);
					return;
				} finally {
					IOUtils.closeQuietly(fis);
				}
				Sheet sheet = rwb.getSheetAt(0);
				int rows = sheet.getPhysicalNumberOfRows();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (int i = 1; i < rows; i++) {
					SysLock sysLock = new SysLock();
					
					sysLock.setLockKey(RandomStringUtils.random(8, true, false));
					Row row = sheet.getRow(i); 
					row.getCell(0);
					Cell lockStatusCell = row.getCell(0);
					lockStatusCell.setCellType(Cell.CELL_TYPE_STRING);
					String lockStatusValue = lockStatusCell.getStringCellValue();
					sysLock.setLockStatus(java.lang.String.valueOf(lockStatusValue));
					Cell createTimeCell = row.getCell(1);
					createTimeCell.setCellType(Cell.CELL_TYPE_STRING);
					String createTimeValue = createTimeCell.getStringCellValue();
					sysLock.setCreateTime(format.parse(createTimeValue));
					totalSize++;
					sysLockList.add(sysLock);
					if (i % 500 == 0) {
						baseService.createList(sysLockList);
						sysLockList.clear();
					}
					message.setCurrentSize(totalSize);
				}
				if (sysLockList.size() > 0) {
					baseService.createList(sysLockList);
					sysLockList.clear();
				}
				message.setCurrentSize(totalSize);
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			} finally {
				message.setImportFlag(true);
				long endTime = System.currentTimeMillis();
				logger.info("time=" + (endTime - startTime) + ",totalSize=" + totalSize);
				importDataFlag = false;
				CacheUtil.removeRunningApp(key);
			}
		}
	}
}

