package com.fcc.web.sys.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.SysLog;
import com.fcc.web.sys.service.SysLogService;

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
	
	private static String exportDataPath;
	private static String importDataPath;
	
	// 标识导出文件
	private String exportDataFile = "exportDataFile_sysLog";
	private String importDataFile = "importDataFile_sysLog";
	
	private static Boolean exportDataFlag = false;
	private static Boolean importDataFlag = false;
	
	private static ReentrantLock lockExportData = new ReentrantLock();
	private static ReentrantLock lockImportData = new ReentrantLock();
	@Resource
	private BaseService baseService;
	//默认多列排序,example: username desc,createTime asc
	@Resource
	private SysLogService sysLogService;
	
	public SysLogController() {
		StringBuilder sb = new StringBuilder();
		sb.append(CacheUtil.realPath).append(Constanst.EXPORT_DATA_FILENAME)
		.append(File.separatorChar).append("sysLogExport").append(File.separatorChar);
		exportDataPath = sb.toString();
		sb.delete(0, sb.length());
		sb.append(CacheUtil.realPath).append(Constanst.IMPORT_DATA_FILENAME)
		.append(File.separatorChar).append("sysLogImport").append(File.separatorChar);
		importDataPath = sb.toString();
	}

	/** 显示列表 */
	@RequestMapping(value = {"/view"})
	public String view(HttpServletRequest request) {
		TreeSet<Operate> operateSet = new TreeSet<Operate>();
		operateSet.addAll(CacheUtil.operateMap.values());
		request.setAttribute("operateList", operateSet);
		return "/WEB-INF/manage/sys/sysLog/sysLog_list";
	}
	
	/** 显示统计报表 */
	@RequestMapping(value = {"/report/view"})
	public String reportView(HttpServletRequest request) {
		TreeSet<Operate> operateSet = new TreeSet<Operate>();
		operateSet.addAll(CacheUtil.operateMap.values());
		request.setAttribute("operateList", operateSet);
		return "/WEB-INF/manage/sys/sysLog/sysLog_report_list";
	}
	
	/** 跳转到查看页面 */
	@RequestMapping(value = {"/toView"})
	public String toView(HttpServletRequest request) {
		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			try {
				SysLog data = (SysLog) baseService.get(SysLog.class, java.lang.Long.valueOf(id));
				request.setAttribute("sysLog", data);
				if (data != null) {
					String moduleId = data.getModuleName();
					if (Constanst.MODULE.REQUEST_APP.equals(moduleId)) {
						data.setModuleName(Constanst.MODULE.Text.TEXT_MAP.get(Constanst.MODULE.REQUEST_APP));
					} else if (moduleId != null) {
						Module m = CacheUtil.moduleMap.get(moduleId);
						if (m != null) data.setModuleName(m.getModuleName());
					}
					String operateId = data.getOperateName();
					if (Constanst.OPERATE.LOGIN.equals(operateId)) {
						data.setOperateName(Constanst.OPERATE.Text.TEXT_MAP.get(Constanst.OPERATE.LOGIN));
					} else if (Constanst.OPERATE.LOGOUT.equals(operateId)) {
						data.setOperateName(Constanst.OPERATE.Text.TEXT_MAP.get(Constanst.OPERATE.LOGOUT));
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
		return "/WEB-INF/manage/sys/sysLog/sysLog_view";
	}
	
	/** 跳转到新增页面 */
	@RequestMapping(value = {"/toAdd"})
	public String toAdd(HttpServletRequest request, HttpServletResponse response) {
		return "/WEB-INF/manage/sys/sysLog/sysLog_add";
	}
	
	/** 跳转到修改页面 */
	@RequestMapping(value = {"/toEdit"})
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
		return "/WEB-INF/manage/sys/sysLog/sysLog_edit";
	}
	
	/** 新增 */
	@RequestMapping(value = {"/add"})
	public ModelAndView add(SysLog sysLog, HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
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
		return mav;
	}
	
	/** 修改 */
	@RequestMapping(value = {"/edit"})
	public ModelAndView edit(SysLog sysLog, HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
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
			baseService.deleteById(SysLog.class, DataFormater.getLong(ids), "logId");
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
					if (Constanst.MODULE.REQUEST_APP.equals(moduleId)) {
						data.setModuleName(Constanst.MODULE.Text.TEXT_MAP.get(Constanst.MODULE.REQUEST_APP));
					} else if (moduleId != null) {
						Module m = CacheUtil.moduleMap.get(moduleId);
						if (m != null) data.setModuleName(m.getModuleName());
					}
					String operateId = data.getOperateName();
					if (Constanst.OPERATE.LOGIN.equals(operateId)) {
						data.setOperateName(Constanst.OPERATE.Text.TEXT_MAP.get(Constanst.OPERATE.LOGIN));
					} else if (Constanst.OPERATE.LOGOUT.equals(operateId)) {
						data.setOperateName(Constanst.OPERATE.Text.TEXT_MAP.get(Constanst.OPERATE.LOGOUT));
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
    @RequestMapping("/report/datagrid")
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
		        			} else if (Constanst.MODULE.REQUEST_APP.equals(groupNameVal)) {
		        				groupName = Constanst.MODULE.Text.TEXT_MAP.get(groupNameVal);
		        			} 
		        		} else if ("operateName".equals(reportGroupName)) {// 操作名称
		        			String groupNameVal = info.getGroupName();
		        			Operate operate = CacheUtil.operateMap.get(groupNameVal);
		        			if (operate != null) {
		        				groupName = operate.getOperateName();
		        			} else if (Constanst.OPERATE.LOGIN.equals(groupNameVal)) {
		        				groupName = Constanst.OPERATE.Text.TEXT_MAP.get(groupNameVal);
		        			} else if (Constanst.OPERATE.LOGOUT.equals(groupNameVal)) {
		        				groupName = Constanst.OPERATE.Text.TEXT_MAP.get(groupNameVal);
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
				CacheUtil.getPool().execute(new SysLogExport(getParams(request), em));
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
	
	class SysLogExport implements Runnable {
		
		private Map<String, Object> param;
		private ExportMessage message;
		
		public SysLogExport(Map<String, Object> param, ExportMessage message) {
			this.param = param;
			this.message = message;
		}
		
		public void run() {
			long startTime = System.currentTimeMillis();
			String key = CacheUtil.getRunningAppKey("sysLogExportTasker_");
			FileOutputStream fos = null;
			try {
				if (CacheUtil.isAppDestroy() == true) {
					logger.info("系统停止中，sysLogExport任务停止！");
					message.setDestroy(true);
					return;
				}
				CacheUtil.addRunningApp(key);
	            File exportDataPathFile = new File(exportDataPath);
	            if (exportDataPathFile.exists() == false) exportDataPathFile.mkdirs();
	            
				List<String> filePaths = new ArrayList<String>();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				String fileNo = format.format(new Date()) + "_sysLogExport";
				int fileFlag = 1;
				int xlsSize = 0;
				int totalSize = 0;
				Workbook workbook = null;
				Sheet sheet = null;
				List<SysLog> list = null;
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
					row.createCell(0).setCellValue(SysLog.ALIAS_LOG_ID);
					row.createCell(1).setCellValue(SysLog.ALIAS_USER_ID);
					row.createCell(2).setCellValue(SysLog.ALIAS_USER_NAME);
					row.createCell(3).setCellValue(SysLog.ALIAS_IP_ADDRESS);
					row.createCell(4).setCellValue(SysLog.ALIAS_LOG_TIME);
					row.createCell(5).setCellValue(SysLog.ALIAS_MODULE_NAME);
					row.createCell(6).setCellValue(SysLog.ALIAS_OPERATE_NAME);
					row.createCell(7).setCellValue(SysLog.ALIAS_EVENT_PARAM);
					row.createCell(8).setCellValue(SysLog.ALIAS_EVENT_OBJECT);
					row.createCell(9).setCellValue(SysLog.ALIAS_EVENT_RESULT);
					for (int i = 0; i < flag; i++) {
						Map<String, Object> tempParam = new HashMap<String, Object>();
						tempParam.putAll(param);
						list = sysLogService.query(pageNo, Constanst.EXPORT_DATA_PAGE_SIZE, tempParam, false);
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
							for (SysLog data : list) {
								row = sheet.createRow(rowNum);
								row.createCell(0).setCellValue(DataFormater.noNullValue(data.getLogId()));
								row.createCell(1).setCellValue(DataFormater.noNullValue(data.getUserId()));
								row.createCell(2).setCellValue(DataFormater.noNullValue(data.getUserName()));
								row.createCell(3).setCellValue(DataFormater.noNullValue(data.getIpAddress()));
								row.createCell(4).setCellValue(DataFormater.noNullValue(data.getLogTime(), "yyyy-MM-dd HH:mm:ss"));
						        String moduleVal = DataFormater.noNullValue(data.getModuleName());
						        Module module = CacheUtil.moduleMap.get(moduleVal);
						        String moduleName = moduleVal;
						        if (module != null) {
						        	moduleName = module.getModuleName();
						        } else if (Constanst.MODULE.REQUEST_APP.equals(moduleVal)) {
						        	moduleName = Constanst.MODULE.Text.TEXT_MAP.get(moduleVal);
						        }
						        row.createCell(5).setCellValue(moduleName);
						        
						        String operateVal = DataFormater.noNullValue(data.getOperateName());
						        Operate operate = CacheUtil.operateMap.get(operateVal);
						        String operateName = operateVal;
						        if (operate != null) {
						        	operateName = operate.getOperateName();
						        } else if (Constanst.OPERATE.LOGIN.equals(operateVal)) {
						        	operateName = Constanst.OPERATE.Text.TEXT_MAP.get(operateVal);
						        } else if (Constanst.OPERATE.LOGOUT.equals(operateVal)) {
						        	operateName = Constanst.OPERATE.Text.TEXT_MAP.get(operateVal);
						        }
						        row.createCell(6).setCellValue(operateName);
						        row.createCell(7).setCellValue(DataFormater.noNullValue(data.getEventParam()));
						        row.createCell(8).setCellValue(DataFormater.noNullValue(data.getEventObject()));
						        String eventResult = DataFormater.noNullValue(data.getEventResult());
						        row.createCell(9).setCellValue(SysLog.EVENT_RESULT_OK.equals(eventResult) ? "成功" : "失败");
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
	public ModelAndView importSysLog(HttpServletRequest request, HttpServletResponse response) {
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
					String fileNo = format.format(new Date()) + "_sysLogImport";
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
					CacheUtil.getPool().execute(new SysLogImport(im, saveFile));
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
					
					if (Constanst.MODULE.Text.TEXT_MAP.get(Constanst.MODULE.REQUEST_APP).equals(ModuleNameValue)) {
						ModuleNameValue = Constanst.MODULE.REQUEST_APP;
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
					if (Constanst.OPERATE.Text.TEXT_MAP.get(Constanst.OPERATE.LOGIN).equals(OperateNameValue)) {
						OperateNameValue = Constanst.OPERATE.LOGIN;
					} else if (Constanst.OPERATE.Text.TEXT_MAP.get(Constanst.OPERATE.LOGOUT).equals(OperateNameValue)) {
						OperateNameValue = Constanst.OPERATE.LOGOUT;
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