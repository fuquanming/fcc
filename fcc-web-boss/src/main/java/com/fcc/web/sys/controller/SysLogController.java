package com.fcc.web.sys.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.common.StatusCode;
import com.fcc.commons.web.service.ExportService;
import com.fcc.commons.web.service.ImportService;
import com.fcc.commons.web.task.ExportTask;
import com.fcc.commons.web.task.ImportTask;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.EasyuiTreeGridModule;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.commons.web.view.ExportMessage;
import com.fcc.commons.web.view.ImportMessage;
import com.fcc.commons.web.view.Message;
import com.fcc.commons.web.view.ReportInfo;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.SysLog;
import com.fcc.web.sys.service.ModuleService;
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
	
	private Logger logger = Logger.getLogger(SysLogController.class);
	
	private ExportTask exportTask;
	private ImportTask importTask;
	
	private String exportDataPath;
	private String importDataPath;
	
	private ReentrantLock lockExportData = new ReentrantLock();
	private ReentrantLock lockImportData = new ReentrantLock();
	@Resource
	private BaseService baseService;
	//默认多列排序,example: username desc,createTime asc
	@Resource
	private SysLogService sysLogService;
	@Resource
	private ModuleService moduleService;
	
	/** 显示列表 */
	@ApiOperation(value = "显示日志列表页面")
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	@Permissions("view")
	public String view(HttpServletRequest request) {
		TreeSet<Operate> operateSet = new TreeSet<Operate>();
		operateSet.addAll(cacheService.getOperateMap().values());
		Operate loginOperate = new Operate();
		loginOperate.setOperateId(Constants.Operate.login);
		loginOperate.setOperateName(Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.login));
		loginOperate.setOperateValue(-2L);
		operateSet.add(loginOperate);
		Operate logoutOperate = new Operate();
		logoutOperate.setOperateId(Constants.Operate.logout);
		logoutOperate.setOperateName(Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.logout));
		logoutOperate.setOperateValue(-1L);
		operateSet.add(logoutOperate);
		request.setAttribute("operateList", operateSet);
		return "manage/sys/sysLog/sysLog_list";
	}
	
	/** 显示统计报表 */
	@ApiOperation(value = "显示日志统计列表页面")
	@RequestMapping(value = "/report/view.do", method = RequestMethod.GET)
	@Permissions("report")
	public String reportView(HttpServletRequest request) {
		TreeSet<Operate> operateSet = new TreeSet<Operate>();
		operateSet.addAll(cacheService.getOperateMap().values());
		request.setAttribute("operateList", operateSet);
		return "manage/sys/sysLog/sysLog_report_list";
	}
	
	/** 跳转到查看页面 */
	@ApiOperation(value = "显示日志查看页面")
	@RequestMapping(value = "/toView.do", method = RequestMethod.GET)
	@Permissions("view")
	public String toView(HttpServletRequest request, 
	        @ApiParam(required = true, value = "日志ID") @RequestParam(name = "id", defaultValue = "") String id) {
		if (StringUtils.isNotEmpty(id)) {
			try {
				SysLog data = (SysLog) baseService.get(SysLog.class, id);
				request.setAttribute("sysLog", data);
				if (data != null) {
					String moduleId = data.getModuleName();
					if (Constants.Module.requestApp.equals(moduleId)) {
						data.setModuleName(Constants.Module.Text.TEXT_MAP.get(Constants.Module.requestApp));
					} else if (moduleId != null) {
						Module m = cacheService.getModuleMap().get(moduleId);
						if (m != null) data.setModuleName(m.getModuleName());
					}
					String operateId = data.getOperateName();
					if (Constants.Operate.login.equals(operateId)) {
						data.setOperateName(Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.login));
					} else if (Constants.Operate.logout.equals(operateId)) {
						data.setOperateName(Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.logout));
					} else if (operateId != null) {
						Operate o = cacheService.getOperateMap().get(operateId);
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
	@RequestMapping(value = "/toAdd.do", method = RequestMethod.GET)
	@Permissions("add")
	public String toAdd(HttpServletRequest request, HttpServletResponse response) {
		return "manage/sys/sysLog/sysLog_add";
	}
	
	/** 跳转到修改页面 */
	@ApiOperation(value = "显示日志修改页面")
	@RequestMapping(value = "/toEdit.do", method = RequestMethod.GET)
	@Permissions("edit")
	public String toEdit(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			try {
				SysLog sysLog = (SysLog) baseService.get(SysLog.class, id);
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
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
	@Permissions("add")
	public ModelAndView add(SysLog sysLog, HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		try {
			String logTimeString = request.getParameter("logTimeString");
			sysLog.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(logTimeString));
			baseService.add(sysLog);
			message.setSuccess(true);
			message.setMsg(StatusCode.Sys.success);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存日志失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/** 修改 */
	@ApiOperation(value = "修改日志")
	@RequestMapping(value = "/edit.do", method = RequestMethod.POST)
	@Permissions("edit")
	public ModelAndView edit(SysLog sysLog, HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		try {
			SysLog dbSysLog = null;
			String logId = request.getParameter("logId");
			if (StringUtils.isNotEmpty(logId)) {
				dbSysLog = (SysLog) baseService.get(SysLog.class, logId);
			}
			if (dbSysLog != null) {
				String logTimeString = request.getParameter("logTimeString");
				sysLog.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(logTimeString));
				BeanUtils.copyProperties(sysLog, dbSysLog);
				baseService.edit(dbSysLog);
				// 更新
				message.setSuccess(true);
				message.setMsg(StatusCode.Sys.success);
			} else {
				// 修改的不存在
				message.setSuccess(false);
				message.setMsg(StatusCode.Sys.emptyUpdateId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改日志失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 删除
	 * @return
	 */
	@ApiOperation("删除日志")
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	@Permissions("delete")
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		String id = request.getParameter("ids");
		try {
			if (id == null || "".equals(id)) throw new RefusedException(StatusCode.Sys.emptyDeleteId);
			String[] ids = StringUtils.split(id, ",");
			baseService.deleteById(SysLog.class, ids, "logId");
			message.setSuccess(true);
			message.setMsg(StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除日志失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "查询日志")
	@RequestMapping(value = "/datagrid.do", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	@Permissions("view")
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
						Module m = cacheService.getModuleMap().get(moduleId);
						if (m != null) data.setModuleName(m.getModuleName());
					}
					String operateId = data.getOperateName();
					if (Constants.Operate.login.equals(operateId)) {
						data.setOperateName(Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.login));
					} else if (Constants.Operate.logout.equals(operateId)) {
						data.setOperateName(Constants.Operate.Text.TEXT_MAP.get(Constants.Operate.logout));
					} else if (operateId != null) {
						Operate o = cacheService.getOperateMap().get(operateId);
						if (o != null) data.setOperateName(o.getOperateName());
					}
				}
			}
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载查询日志数据失败！", e);
			json.setTotal(0L);
			json.setRows(Collections.EMPTY_LIST);
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	/**
     * 模块 Tree 返回json 给 easyUI
     * @return
     */
    @ApiOperation(value = "查询模块树形数据")
    @RequestMapping(value = "/moduleTree.do", method = RequestMethod.POST)
    @ResponseBody
    @Permissions("view")
    public List<EasyuiTreeNode> moduleTree(HttpServletRequest request,
            @ApiParam(required = false, value = "节点状态，open、closed") @RequestParam(name = "nodeStatus", defaultValue = "") String nodeStatus) {
        if (StringUtils.isEmpty(nodeStatus)) nodeStatus = EasyuiTreeNode.STATE_OPEN;
        List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
        try {
            // 查询所有模块
            nodeList = moduleService.getModuleTree(null, nodeStatus, false, null);
            EasyuiTreeNode node = new EasyuiTreeNode();
            node.setId(Constants.Module.requestApp);
            node.setText(Constants.Module.Text.TEXT_MAP.get(Constants.Module.requestApp));
            nodeList.add(node);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询模块树形失败", e);
            nodeList = new ArrayList<EasyuiTreeNode>();
            EasyuiTreeGridModule node = new EasyuiTreeGridModule();
            node.setMsg(e.getMessage());
            nodeList.add(node);
        }
        return nodeList;
    }
	
	/** 报表 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/report/datagrid.do", method = RequestMethod.POST)
	@ResponseBody
	@Permissions("report")
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
		        			Module module = cacheService.getModuleMap().get(groupNameVal);
		        			if (module != null) {
		        				groupName = module.getModuleName();
		        			} else if (Constants.Module.requestApp.equals(groupNameVal)) {
		        				groupName = Constants.Module.Text.TEXT_MAP.get(groupNameVal);
		        			} 
		        		} else if ("operateName".equals(reportGroupName)) {// 操作名称
		        			String groupNameVal = info.getGroupName();
		        			Operate operate = cacheService.getOperateMap().get(groupNameVal);
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
				json.setRows(Collections.EMPTY_LIST);
	        }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询日志报表数据失败！", e);
			json.setTotal(0L);
			json.setRows(Collections.EMPTY_LIST);
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
	@RequestMapping(value = "/export.do", method = RequestMethod.POST)
	@Permissions("export")
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		lockExportData.lock();
		try {
		    if (exportTask == null || exportTask.isExportDataFlag() == false) {
		        if (exportTask == null) exportTask = new ExportTask();
		        if (exportDataPath == null) {
		            StringBuilder sb = new StringBuilder();
	                sb.append(WebUtils.getRealPath(request.getServletContext(), "/")).append(Constants.EXPORT_DATA_FILENAME)
	                .append(File.separatorChar).append("sysLogExport").append(File.separatorChar);
	                exportDataPath = sb.toString();
		        }
		        List<String> titleList = new ArrayList<String>(10);
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
                exportTask.setRunningKey("sysLog");
                exportTask.setTitleList(titleList);
                exportTask.setExportDataPath(exportDataPath);
                exportTask.setQueryService(sysLogService);
                exportTask.setQueryServiceMethodName("query");
                exportTask.setPageNoSub(0);
                exportTask.setQueryParams(paramObject);
                exportTask.setExportService((ExportService) sysLogService);
                exportTask.init();
		        execute(exportTask);
		        message.setSuccess(true);
                message.setMsg(Constants.StatusCode.Export.exportNow);
		    } else {
		        message.setMsg(Constants.StatusCode.Export.exportBusy);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出日志失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		} finally {
			lockExportData.unlock();
		}
		return getModelAndView(message);
	}
	
	@ApiIgnore
	@RequestMapping(value = "/queryExport.do", method = RequestMethod.POST)
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
	@RequestMapping(value = "/import.do", method = RequestMethod.POST)
	@Permissions("import")
	public ModelAndView importSysLog(HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		lockImportData.lock();
		MultipartFile file = null;
		try {
			if (importTask == null || importTask.getImportDataFlag() == false) {
			    if (importTask == null) importTask = new ImportTask();
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
					importTask.setRunningKey("sysLog");
                    importTask.setImportDataPath(importDataPath);
                    importTask.setFile(file);
                    importTask.setImportService((ImportService) sysLogService);
                    importTask.setBeginRowNum(2);
                    importTask.init();
                    execute(importTask);
                    message.setSuccess(true);
                    message.setMsg(Constants.StatusCode.Import.importNow);
				} else {
					message.setMsg(Constants.StatusCode.Import.emptyFile);
				}
			} else {
				message.setMsg(Constants.StatusCode.Import.importBusy);
			}
		} catch (Exception e) {
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
			logger.error("上传日志失败！", e);
			e.printStackTrace();
		} finally {
			lockImportData.unlock();
		}
		return getModelAndView(message);
	}
	
	@ApiIgnore
	@RequestMapping(value = "/queryImport.do")
	@ResponseBody
	public ImportMessage queryImport(HttpServletRequest request, HttpServletResponse response) {
	    ImportMessage im = null;
        if (importTask != null) {
            im = importTask.getImportMessage();
//            if (im.isImportFlag()) {
//                im = new ImportMessage();
//                im.setImportFlag(true);
//                im.setCurrentSize(importTask.getImportMessage().getCurrentSize());
//                importTask = null;
//            }
        }
        if (im == null) {
            im = new ImportMessage();
            im.setImportFlag(true);
        }
        return im;
	}
	
}