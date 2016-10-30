package com.fcc.web.sys.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.DataFormater;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.service.ExportService;
import com.fcc.commons.web.service.ImportService;
import com.fcc.commons.web.task.ExportTask;
import com.fcc.commons.web.task.ImportTask;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.ExportMessage;
import com.fcc.commons.web.view.ImportMessage;
import com.fcc.commons.web.view.Message;
import com.fcc.commons.web.view.ReportInfo;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.SysLog;
import com.fcc.web.sys.service.SysLogService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @email fuquanming@gmail.com
 */
@Controller
@RequestMapping(value={"/manage/sys/sysLog"} )
public class SysLogController extends AppWebController {
	
	private static Logger logger = Logger.getLogger(SysLogController.class);
	
	private ExportTask exportTask;
	private ImportTask importTask;
	
	private static String exportDataPath;
	private static String importDataPath;
	
	// 标识导出文件
	private String importDataFile = "importDataFile_sysLog";
	
	private static Boolean importDataFlag = false;
	
	private static ReentrantLock lockExportData = new ReentrantLock();
	private static ReentrantLock lockImportData = new ReentrantLock();
	@Resource
	private BaseService baseService;
	//默认多列排序,example: username desc,createTime asc
	@Resource
	private SysLogService sysLogService;
	
	public SysLogController() {
//		sb.delete(0, sb.length());
//		sb.append(CacheUtil.realPath).append(Constanst.IMPORT_DATA_FILENAME)
//		.append(File.separatorChar).append("sysLogImport").append(File.separatorChar);
//		importDataPath = sb.toString();
	}

	/** 显示列表 */
	@ApiOperation(value = "显示日志列表页面")
	@GetMapping(value = {"/view.do"})
	public String view(HttpServletRequest request) {
		TreeSet<Operate> operateSet = new TreeSet<Operate>();
		operateSet.addAll(CacheUtil.operateMap.values());
		request.setAttribute("operateList", operateSet);
		return "manage/sys/sysLog/sysLog_list";
	}
	
	/** 显示统计报表 */
	@ApiOperation(value = "显示日志统计列表页面")
	@GetMapping(value = {"/report/view.do"})
	public String reportView(HttpServletRequest request) {
		TreeSet<Operate> operateSet = new TreeSet<Operate>();
		operateSet.addAll(CacheUtil.operateMap.values());
		request.setAttribute("operateList", operateSet);
		return "manage/sys/sysLog/sysLog_report_list";
	}
	
	/** 跳转到查看页面 */
	@ApiOperation(value = "显示日志查看页面")
	@GetMapping(value = {"/toView.do"})
	public String toView(HttpServletRequest request, 
	        @ApiParam(required = true, value = "日志ID") @RequestParam(name = "id", defaultValue = "") String id) {
		if (StringUtils.isNotEmpty(id)) {
			try {
				SysLog data = (SysLog) baseService.get(SysLog.class, java.lang.Long.valueOf(id));
				request.setAttribute("sysLog", data);
				if (data != null) {
					String moduleId = data.getModuleName();
					if (Constants.Module.requestApp.equals(moduleId)) {
						data.setModuleName(Constants.Module.Text.TEXT_MAP.get(Constants.Module.requestApp));
					} else if (moduleId != null) {
						Module m = CacheUtil.moduleMap.get(moduleId);
						if (m != null) data.setModuleName(m.getModuleName());
					}
					String operateId = data.getOperateName();
					if (Constants.Operate.login.equals(operateId)) {
						data.setOperateName(Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.login));
					} else if (Constants.Operate.logout.equals(operateId)) {
						data.setOperateName(Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.logout));
					} else if (operateId != null) {
						Operate o = CacheUtil.operateMap.get(operateId);
						if (o != null) data.setOperateName(o.getOperateName());
					}
					String eventResult = data.getEventResult();
					if (SysLog.EVENT_RESULT_OK.equals(eventResult)) {
						data.setEventResult("成功");
					} else if (SysLog.EVENT_RESULT_OK.equals(eventResult)) {
						data.setEventResult("失败");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
		return "manage/sys/sysLog/sysLog_view";
	}
	
	/** 跳转到新增页面 */
	@ApiOperation(value = "显示日志新增页面")
	@GetMapping(value = {"/toAdd.do"})
	public String toAdd(HttpServletRequest request, HttpServletResponse response) {
		return "manage/sys/sysLog/sysLog_add";
	}
	
	/** 跳转到修改页面 */
	@ApiOperation(value = "显示日志修改页面")
	@GetMapping(value = {"/toEdit.do"})
	public String toEdit(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			try {
				SysLog sysLog = (SysLog) baseService.get(SysLog.class, java.lang.Long.valueOf(id));
				request.setAttribute("sysLog", sysLog);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
		return "manage/sys/sysLog/sysLog_edit";
	}
	
	/** 新增 */
	@ApiOperation(value = "新增日志")
	@PostMapping(value = {"/add.do"})
	public ModelAndView add(SysLog sysLog, HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		try {
			String logTimeString = request.getParameter("logTimeString");
			sysLog.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(logTimeString));
			baseService.create(sysLog);
			message.setSuccess(true);
			message.setMsg("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("保存失败！");
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/** 修改 */
	@ApiOperation(value = "修改日志")
	@PostMapping(value = {"/edit"})
	public ModelAndView edit(SysLog sysLog, HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		try {
			SysLog dbSysLog = null;
			String logId = request.getParameter("logId");
			if (StringUtils.isNotEmpty(logId)) {
				dbSysLog = (SysLog) baseService.get(SysLog.class, Long.valueOf(logId));
			}
			if (dbSysLog != null) {
				String logTimeString = request.getParameter("logTimeString");
				sysLog.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(logTimeString));
				BeanUtils.copyProperties(sysLog, dbSysLog);
				baseService.update(dbSysLog);
				// 更新
				message.setSuccess(true);
				message.setMsg(Constants.StatusCode.Sys.success);
			} else {
				// 修改的不存在
				message.setSuccess(false);
				message.setMsg("修改对象已不存在！");
				message.setObj("修改对象已不存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改日志失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 删除
	 * @return
	 */
	@ApiOperation("删除日志")
	@PostMapping(value = {"/delete"})
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		String id = request.getParameter("ids");
		try {
			if (id == null || "".equals(id)) {
				throw new RefusedException("请选择要删除的记录！");
			}
			String[] ids = id.split(",");
			baseService.deleteById(SysLog.class, DataFormater.getLong(ids), "logId");
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除日志失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "查询日志")
	@PostMapping("/datagrid.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
			Map<String, Object> param = getParams(request);
			ListPage listPage = sysLogService.queryPage(dg.getPage(), dg.getRows(), param, false);
			List<SysLog> list = listPage.getDataList();
			if (list != null) {
				for (SysLog data : list) {
					String moduleId = data.getModuleName();
					if (Constants.Module.requestApp.equals(moduleId)) {
						data.setModuleName(Constants.Module.Text.TEXT_MAP.get(Constants.Module.requestApp));
					} else if (moduleId != null) {
						Module m = CacheUtil.moduleMap.get(moduleId);
						if (m != null) data.setModuleName(m.getModuleName());
					}
					String operateId = data.getOperateName();
					if (Constants.Operate.login.equals(operateId)) {
						data.setOperateName(Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.login));
					} else if (Constants.Operate.logout.equals(operateId)) {
						data.setOperateName(Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.logout));
					} else if (operateId != null) {
						Operate o = CacheUtil.operateMap.get(operateId);
						if (o != null) data.setOperateName(o.getOperateName());
					}
				}
			}
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			json.setTotal(0L);
			json.setRows(new ArrayList<SysLog>());
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	/** 报表 */
	@SuppressWarnings("unchecked")
    @PostMapping("/report/datagrid.do")
	@ResponseBody
	public EasyuiDataGridJson reportDatagrid(EasyuiDataGrid dg, HttpServletRequest request) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
			Map<String, Object> param = getParams(request);
			String reportGroupName = request.getParameter("reportGroupName");
	        if (StringUtils.isNotEmpty(reportGroupName)) {
	        	param.put("reportGroupName", reportGroupName);
	        	ListPage listPage = sysLogService.report(dg.getPage(), dg.getRows(), param, false);
	        	List<ReportInfo> dataList = listPage.getDataList();
	        	if (dataList != null) {
	        		int size = dataList.size();
	        		for (int i = 0; i < size; i++) {
	        			ReportInfo info = (ReportInfo) dataList.get(i);
	        			String groupName = null;
		        		if ("moduleName".equals(reportGroupName)) {// 模块名称
		        			String groupNameVal = info.getGroupName();
		        			Module module = CacheUtil.moduleMap.get(groupNameVal);
		        			if (module != null) {
		        				groupName = module.getModuleName();
		        			} else if (Constants.Module.requestApp.equals(groupNameVal)) {
		        				groupName = Constants.Module.Text.TEXT_MAP.get(groupNameVal);
		        			} 
		        		} else if ("operateName".equals(reportGroupName)) {// 操作名称
		        			String groupNameVal = info.getGroupName();
		        			Operate operate = CacheUtil.operateMap.get(groupNameVal);
		        			if (operate != null) {
		        				groupName = operate.getOperateName();
		        			} else if (Constants.Operate.login.equals(groupNameVal)) {
		        				groupName = Constants.Operate.Text.TEXT_MAP.get(groupNameVal);
		        			} else if (Constants.Operate.logout.equals(groupNameVal)) {
		        				groupName = Constants.Operate.Text.TEXT_MAP.get(groupNameVal);
		        			}
		        		} else if ("eventResult".equals(reportGroupName)) {// 事件结果
		        			String groupNameVal = info.getGroupName();
		        			if (SysLog.EVENT_RESULT_OK.equals(groupNameVal)) {
		        				groupName = "成功";
		        			} else if (SysLog.EVENT_RESULT_FAIL.equals(groupNameVal)) {
		        				groupName = "失败";
		        			}
		        		}
	        			if (groupName != null) info.setGroupName(groupName);
	        		}
	        	}
				json.setTotal(Long.valueOf(listPage.getTotalSize()));
				json.setRows(listPage.getDataList());
	        } else {
	        	json.setTotal(0L);
				json.setRows(new ArrayList<ReportInfo>());
	        }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询日志失败！", e);
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
	        String logId = request.getParameter("logId");
	        if (StringUtils.isNotEmpty(logId)) {
	        	param.put("logId", logId);
	        }
	        String userId = request.getParameter("userId");
	        if (StringUtils.isNotEmpty(userId)) {
	        	param.put("userId", userId);
	        }
	        String userName = request.getParameter("userName");
	        if (StringUtils.isNotEmpty(userName)) {
	        	param.put("userName", userName);
	        }
	        String ipAddress = request.getParameter("ipAddress");
	        if (StringUtils.isNotEmpty(ipAddress)) {
	        	param.put("ipAddress", ipAddress);
	        }
	        String logTimeBegin = request.getParameter("logTimeBegin");
	        if (StringUtils.isNotEmpty(logTimeBegin)) {
	        	param.put("logTimeBegin", format.parse(logTimeBegin + " 00:00:00"));
	        }
	        String logTimeEnd = request.getParameter("logTimeEnd");
	        if (StringUtils.isNotEmpty(logTimeEnd)) {
	        	param.put("logTimeEnd", format.parse(logTimeEnd + " 23:59:59"));
	        }
	        String moduleName = request.getParameter("moduleName");
	        if (StringUtils.isNotEmpty(moduleName)) {
	        	param.put("moduleName", moduleName);
	        }
	        String operateName = request.getParameter("operateName");
	        if (StringUtils.isNotEmpty(operateName)) {
	        	param.put("operateName", operateName);
	        }
	        String eventParam = request.getParameter("eventParam");
	        if (StringUtils.isNotEmpty(eventParam)) {
	        	param.put("eventParam", eventParam);
	        }
	        String eventObject = request.getParameter("eventObject");
	        if (StringUtils.isNotEmpty(eventObject)) {
	        	param.put("eventObject", eventObject);
	        }
	        String eventResult = request.getParameter("eventResult");
	        if (StringUtils.isNotEmpty(eventResult)) {
	        	param.put("eventResult", eventResult);
	        }
	        String sortColumns = request.getParameter("sort");
	        if (StringUtils.isNotEmpty(sortColumns)) {
	        	param.put("sortColumns", sortColumns);
	        } else {
	        	param.put("sortColumns", "logId");
	        }
	        String orderType = request.getParameter("order");
	        if (StringUtils.isNotEmpty(orderType)) {
	        	param.put("orderType", orderType);
	        } else {
	        	param.put("orderType", "desc");
	        }
        } catch (Exception e) {
			e.printStackTrace();
		}
		return param;
	}
	
	/** 导出数据 */
	@ApiIgnore
	@PostMapping("/export")
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		lockExportData.lock();
		try {
		    if (exportTask == null || exportTask.isExportDataFlag() == false) {
		        if (exportDataPath == null) {
		            StringBuilder sb = new StringBuilder();
	                sb.append(WebUtils.getRealPath(request.getServletContext(), "/")).append(Constants.EXPORT_DATA_FILENAME)
	                .append(File.separatorChar).append("sysLogExport").append(File.separatorChar);
	                exportDataPath = sb.toString();
		        }
		        List<String> titleList = new ArrayList<String>();
                titleList.add(SysLog.ALIAS_LOG_ID);
                titleList.add(SysLog.ALIAS_USER_ID);
                titleList.add(SysLog.ALIAS_USER_NAME);
                titleList.add(SysLog.ALIAS_IP_ADDRESS);
                titleList.add(SysLog.ALIAS_LOG_TIME);
                titleList.add(SysLog.ALIAS_MODULE_NAME);
                titleList.add(SysLog.ALIAS_OPERATE_NAME);
                titleList.add(SysLog.ALIAS_EVENT_PARAM);
                titleList.add(SysLog.ALIAS_EVENT_OBJECT);
                titleList.add(SysLog.ALIAS_EVENT_RESULT);
                Object[] paramObject = new Object[]{1, Constants.EXPORT_DATA_PAGE_SIZE, getParams(request), false};
                exportTask = new ExportTask("sysLog", titleList, exportDataPath, sysLogService, 
                        "query", 0, paramObject, (ExportService) sysLogService);
		        CacheUtil.getPool().execute(exportTask);
		        message.setSuccess(true);
                message.setMsg(Constants.StatusCode.Export.exportNow);
		    } else {
		        message.setMsg(Constants.StatusCode.Export.exportBusy);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出日志失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		} finally {
			lockExportData.unlock();
		}
		return getModelAndView(message);
	}
	
	@ApiIgnore
	@PostMapping("/queryExport.do")
	@ResponseBody
	public ExportMessage queryExport(HttpServletRequest request, HttpServletResponse response) {
	    ExportMessage em = null;
	    if (exportTask != null) {
	        em = exportTask.getExportMessage();
	    }
	    if (em == null) {
            em = new ExportMessage();
            em.setEmpty(true);
        }
		return em;
	}
	
	/** 导入数据 */
	@ApiIgnore
	@PostMapping("/import.do")
	public ModelAndView importSysLog(HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		lockImportData.lock();
		FileOutputStream fos = null;
		MultipartFile file = null;
		try {
			if (importTask == null || importTask.getImportDataFlag() == false) {
				// 接收上传文件
				file = ((MultipartHttpServletRequest) request).getFile("upload");
				if (file != null && file.isEmpty() == false) {
					logger.info("上传文件的大小：" + file.getSize());
					logger.info("上传文件的类型：" + file.getContentType());
					logger.info("上传文件的名称：" + file.getOriginalFilename());
					logger.info("上传表单的名称：" + file.getName());
					
					if (importDataPath == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(WebUtils.getRealPath(request.getServletContext(), "/")).append(Constants.EXPORT_DATA_FILENAME)
                                .append(File.separatorChar).append("sysLogImport").append(File.separatorChar);
                        importDataPath = sb.toString();
	                }
					importTask = new ImportTask(importDataPath, file.getOriginalFilename(), file.getInputStream(), 
					        (ImportService)sysLogService, 9);
					CacheUtil.getPool().execute(importTask);
					message.setSuccess(true);
					message.setMsg("已上传成功！系统正在导入数据。。。");
				} else {
					message.setMsg("请选择文件！");
				}
			} else {
				message.setMsg("系统正在执行上次导入数据，请稍后。。。");
			}
		} catch (Exception e) {
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
			logger.error("上传日志失败！", e);
			e.printStackTrace();
		} finally {
			try {
				IOUtils.closeQuietly(file.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			IOUtils.closeQuietly(fos);
			lockImportData.unlock();
		}
		return getModelAndView(message);
	}
	
	@RequestMapping("/queryImport")
	@ResponseBody
	public ImportMessage queryImport(HttpServletRequest request, HttpServletResponse response) {
//		ImportMessage im = (ImportMessage) request.getSession().getAttribute(importDataFile);
//		if (im == null) {
//			im = new ImportMessage();
//			im.setImportFlag(true);
//		} else {
//			if (im.isImportFlag()) request.getSession().removeAttribute(importDataFile);
//		}
//		return im;
	    ImportMessage im = null;
        if (importTask != null) {
            im = importTask.getImportMessage();
        }
        if (im == null) {
            im = new ImportMessage();
            im.setImportFlag(true);
        }
        return im;
	}
	
	class SysLogImport implements Runnable {
		private ImportMessage message;
		private File saveFile;
		public SysLogImport(ImportMessage message, File saveFile) {
			this.message = message;
			this.saveFile = saveFile;
		}
		public void run() {
			long startTime = System.currentTimeMillis();
			int totalSize = 0;
			String key = CacheUtil.getRunningAppKey("sysLogImportTasker_");
			FileInputStream fis = null;
			try {
				if (CacheUtil.isAppDestroy() == true) {
					logger.info("系统停止中，SysLogImport任务停止！");
					message.setDestroy(true);
					return;
				}
				CacheUtil.addRunningApp(key);
				List<SysLog> sysLogList = new ArrayList<SysLog>();
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
					SysLog sysLog = new SysLog();
					
					Row row = sheet.getRow(i); 
					if (row == null) break;
					Cell cell = row.getCell(0);
					if (cell == null) break;
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String UserIdValue = cell.getStringCellValue();
					sysLog.setUserId(java.lang.String.valueOf(UserIdValue));
					
					cell = row.getCell(1);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String UserNameValue = cell.getStringCellValue();
					sysLog.setUserName(java.lang.String.valueOf(UserNameValue));
					
					cell = row.getCell(2);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String IpAddressValue = cell.getStringCellValue();
					sysLog.setIpAddress(java.lang.String.valueOf(IpAddressValue));
					
					cell = row.getCell(3);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String LogTimeValue = cell.getStringCellValue();
					sysLog.setLogTime(format.parse(LogTimeValue));
					
					cell = row.getCell(4);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String ModuleNameValue = cell.getStringCellValue();
					
					if (Constants.Module.Text.TEXT_MAP.get(Constants.Module.requestApp).equals(ModuleNameValue)) {
						ModuleNameValue = Constants.Module.requestApp;
					} else {
						Iterator<Module> mIt = CacheUtil.moduleMap.values().iterator();
						while (mIt.hasNext()) {
							Module o = mIt.next();
							if (o.getModuleName().equals(ModuleNameValue)) {
								ModuleNameValue = o.getModuleId();
								break;
							}
						}
					}
					sysLog.setModuleName(java.lang.String.valueOf(ModuleNameValue));
					
					cell = row.getCell(5);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String OperateNameValue = cell.getStringCellValue();
					if (Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.login).equals(OperateNameValue)) {
						OperateNameValue = Constants.Operate.login;
					} else if (Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.logout).equals(OperateNameValue)) {
						OperateNameValue = Constants.Operate.logout;
					} else {
						Iterator<Operate> opIt = CacheUtil.operateMap.values().iterator();
						while (opIt.hasNext()) {
							Operate o = opIt.next();
							if (o.getOperateName().equals(OperateNameValue)) {
								OperateNameValue = o.getOperateId();
								break;
							}
						}
					}
					sysLog.setOperateName(java.lang.String.valueOf(OperateNameValue));
					
					cell = row.getCell(6);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String EventParamValue = cell.getStringCellValue();
					sysLog.setEventParam(java.lang.String.valueOf(EventParamValue));
					
					cell = row.getCell(7);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String EventObjectValue = cell.getStringCellValue();
					sysLog.setEventObject(java.lang.String.valueOf(EventObjectValue));
					
					cell = row.getCell(8);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String EventResultValue = cell.getStringCellValue();
					if ("成功".equals(EventResultValue)) {
						EventResultValue = SysLog.EVENT_RESULT_OK;
					} else if ("失败".equals(EventResultValue)) {
						EventResultValue = SysLog.EVENT_RESULT_FAIL;
					}
					sysLog.setEventResult(java.lang.String.valueOf(EventResultValue));
					
					totalSize++;
					sysLogList.add(sysLog);
					if (i % 500 == 0) {
						baseService.createList(sysLogList);
						sysLogList.clear();
					}
					message.setCurrentSize(totalSize);
				}
				if (sysLogList.size() > 0) {
					baseService.createList(sysLogList);
					sysLogList.clear();
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