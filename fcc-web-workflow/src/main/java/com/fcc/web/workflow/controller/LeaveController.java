package com.fcc.web.workflow.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Permissions;
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
import com.fcc.commons.workflow.common.WorkflowStatus;
import com.fcc.commons.workflow.common.StatusCode;
import com.fcc.commons.workflow.common.WorkflowDefinitionKey;
import com.fcc.commons.workflow.controller.WorkflowController;
import com.fcc.web.sys.cache.SysUserAuthentication;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.workflow.model.Leave;
import com.fcc.web.workflow.service.LeaveApplyWorkflowService;
import com.fcc.web.workflow.service.LeaveService;

import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>Description:Leave</p>
 */

@Controller
@RequestMapping(value={"/manage/workflow/leave"} )
public class LeaveController extends WorkflowController {
    
    private Logger logger = Logger.getLogger(LeaveController.class);
    
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
    private LeaveService leaveService;
    @Resource
    private LeaveApplyWorkflowService workflowService;
    
    public LeaveController() {
        WorkflowDefinitionKey.definitionKeyMap.put(Leave.processDefinitionKey, Leave.processDefinitionName);
    }
    
    /** 显示列表 */
    @ApiOperation(value = "显示Leave列表页面")
    @RequestMapping(value = "/view.do", method = RequestMethod.GET)
    @Permissions("view")
    public String view(HttpServletRequest request) {
        setWorkflowStatus(request);
        return "manage/workflow/leave/leave_list";
    }
    
    /** 显示统计报表 */
    @ApiOperation(value = "显示Leave统计列表页面")
    @RequestMapping(value = "/report/view.do", method = RequestMethod.GET)
    @Permissions("report")
    public String reportView(HttpServletRequest request) {
        return "/manage/workflow/leave/leave_report_list";
    }
    
    /** 跳转到查看页面 */
    @ApiOperation(value = "显示Leave查看页面")
    @RequestMapping(value = "/toView.do", method = RequestMethod.GET)
    @Permissions("view")
    public String toView(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isNotEmpty(id)) {
            try {
                Leave leave = (Leave) baseService.get(Leave.class, java.lang.String.valueOf(id));
                setProcessHistory(request, leave.getProcessInstanceId());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("查看Leave详情页数据加载失败！", e);
            }
        }
        return "/manage/workflow/leave/leave_view";
    }
    
    /** 跳转到新增页面 */
    @ApiOperation(value = "显示Leave新增页面")
    @RequestMapping(value = "/toAdd.do", method = RequestMethod.GET)
    @Permissions("add")
    public String toAdd(HttpServletRequest request, HttpServletResponse response) {
        return "/manage/workflow/leave/leave_add";
    }
    
    /** 跳转到修改页面 */
    @ApiOperation(value = "显示Leave修改页面")
    @RequestMapping(value = "/toEdit.do", method = RequestMethod.GET)
    @Permissions("edit")
    public String toEdit(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (StringUtils.isNotEmpty(id)) {
            try {
                Leave leave = (Leave) baseService.get(Leave.class, java.lang.String.valueOf(id));
                request.setAttribute("leave", leave);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("修改Leave详情页数据加载失败！", e);
            }
        }
        return "/manage/workflow/leave/leave_edit";
    }
    
    /** 新增 */
    @ApiOperation(value = "新增Leave")
    @RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @Permissions("add")
    public ModelAndView add(Leave leave, HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        try {
            String startTimeString = request.getParameter("startTimeString");
            leave.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTimeString));
            String endTimeString = request.getParameter("endTimeString");
            leave.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTimeString));
            leave.setInitiatorUserId(SysUserAuthentication.getSysUser().getUserId());
            leave.setStatus(WorkflowStatus.unstart.name());
            String operate = request.getParameter("operate");
            if ("save".equals(operate)) {// 保存
                baseService.add(leave);
            } else if ("start".equals(operate)) {// 保存、启动流程
                // 启动流程
                leaveService.add(leave);
            }
            message.setSuccess(true);
            message.setMsg(StatusCode.Sys.success);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存Leave失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
    
    /** 修改 */
    @ApiOperation(value = "修改Leave")
    @RequestMapping(value = "/edit.do", method = RequestMethod.POST)
    @Permissions("edit")
    public ModelAndView edit(Leave leave, HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        try {
            Leave dbLeave = null;
            String leaveId = request.getParameter("leaveId");
            if (StringUtils.isNotEmpty(leaveId)) {
                dbLeave = (Leave) baseService.get(Leave.class, leaveId);
            }
            if (dbLeave != null) {
                // 未启动状态可以修改
                if (WorkflowStatus.unstart.name().equals(dbLeave.getStatus())) {
                    String startTimeString = request.getParameter("startTimeString");
                    leave.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTimeString));
                    String endTimeString = request.getParameter("endTimeString");
                    leave.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTimeString));
                    dbLeave.setStartTime(leave.getStartTime());
                    dbLeave.setEndTime(leave.getEndTime());
                    dbLeave.setContent(leave.getContent());
                    String operate = request.getParameter("operate");
                    if ("save".equals(operate)) {// 保存
                        baseService.edit(dbLeave);
                    } else if ("start".equals(operate)) {// 保存、启动流程
                        // 启动流程
                        leaveService.edit(dbLeave);
                    }
                    // 更新
                    message.setSuccess(true);
                    message.setMsg(StatusCode.Sys.success);
                } else {
                    message.setMsg(StatusCode.Workflow.errorStatus);
                }
            } else {
                // 修改的不存在
                message.setSuccess(false);
                message.setMsg(StatusCode.Sys.emptyUpdateId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("修改Leave失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
    
    /**
     * 删除
     * @return
     */
    @ApiOperation("删除Leave")
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @Permissions("delete")
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        String id = request.getParameter("ids");
        try {
            if (id == null || "".equals(id)) throw new RefusedException(StatusCode.Sys.emptyDeleteId);
            String[] ids = StringUtils.split(id, ",");
            baseService.deleteById(Leave.class, ids, "leaveId");
            message.setSuccess(true);
            message.setMsg(StatusCode.Sys.success);
        } catch (RefusedException e) {
            message.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除Leave失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
    
    /**
     * 列表 返回json 给 easyUI 
     * @return
     */
    @ApiOperation(value = "查询Leave")
    @RequestMapping(value = "/datagrid.do", method = RequestMethod.POST)
    @ResponseBody
    @Permissions("view")
    public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request) {
        EasyuiDataGridJson json = new EasyuiDataGridJson();
        try {
            Map<String, Object> param = getParams(request);
            ListPage listPage = leaveService.queryPage(dg.getPage(), dg.getRows(), param, false);
            json.setTotal(Long.valueOf(listPage.getTotalSize()));
            json.setRows(listPage.getDataList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载查询Leave数据失败！", e);
            json.setTotal(0L);
            json.setRows(new ArrayList<Leave>());
            json.setMsg(e.getMessage());
        }
        return json;
    }
    
    /** 报表 */
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
                ListPage listPage = leaveService.report(dg.getPage(), dg.getRows(), param, false);
                json.setTotal(Long.valueOf(listPage.getTotalSize()));
                json.setRows(listPage.getDataList());
            } else {
                json.setTotal(0L);
                json.setRows(new ArrayList<ReportInfo>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询Leave报表数据失败！", e);
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
            String leaveId = request.getParameter("leaveId");
            if (StringUtils.isNotEmpty(leaveId)) {
                param.put("leaveId", leaveId);
            }
            String startTimeBegin = request.getParameter("startTimeBegin");
            if (StringUtils.isNotEmpty(startTimeBegin)) {
                param.put("startTimeBegin", format.parse(startTimeBegin + " 00:00:00"));
            }
            String startTimeEnd = request.getParameter("startTimeEnd");
            if (StringUtils.isNotEmpty(startTimeEnd)) {
                param.put("startTimeEnd", format.parse(startTimeEnd + " 23:59:59"));
            }
            String endTimeBegin = request.getParameter("endTimeBegin");
            if (StringUtils.isNotEmpty(endTimeBegin)) {
                param.put("endTimeBegin", format.parse(endTimeBegin + " 00:00:00"));
            }
            String endTimeEnd = request.getParameter("endTimeEnd");
            if (StringUtils.isNotEmpty(endTimeEnd)) {
                param.put("endTimeEnd", format.parse(endTimeEnd + " 23:59:59"));
            }
            String content = request.getParameter("content");
            if (StringUtils.isNotEmpty(content)) {
                param.put("content", content);
            }
            String status = request.getParameter("status");
            if (StringUtils.isNotEmpty(status)) {
                param.put("status", status);
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
                    sb.append(WebUtils.getRealPath(request.getServletContext(), "/")).append(Constants.exportDataFileName)
                    .append(File.separatorChar).append("leaveExport").append(File.separatorChar);
                    exportDataPath = sb.toString();
                }
                List<String> titleList = new ArrayList<String>();
                titleList.add("主键");
                titleList.add("开始时间");
                titleList.add("结束时间");
                titleList.add("内容");
                titleList.add("状态");
                
                Object[] paramObject = new Object[]{1, Constants.exportDataPageSize, getParams(request), false};
                exportTask.setRunningKey("leave");
                exportTask.setTitleList(titleList);
                exportTask.setExportDataPath(exportDataPath);
                exportTask.setQueryService(leaveService);
                exportTask.setQueryServiceMethodName("query");
                exportTask.setPageNoSub(0);
                exportTask.setQueryParams(paramObject);
                exportTask.setExportService((ExportService) leaveService);
                exportTask.init();
                
                execute(exportTask);
                message.setSuccess(true);
                message.setMsg(StatusCode.Export.exportNow);
            } else {
                message.setMsg(StatusCode.Export.exportBusy);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出Leave失败！", e);
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
    public ModelAndView importLeave(HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        lockImportData.lock();
        FileOutputStream fos = null;
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
                        sb.append(WebUtils.getRealPath(request.getServletContext(), "/")).append(Constants.importDataFileName)
                                .append(File.separatorChar).append("leaveImport").append(File.separatorChar);
                        importDataPath = sb.toString();
                    }
                    importTask.setRunningKey("leave");
                    importTask.setImportDataPath(importDataPath);
                    importTask.setFile(file);
                    importTask.setImportService((ImportService) leaveService);
                    importTask.setBeginRowNum(2);
                    importTask.init();
                    execute(importTask);
                    message.setSuccess(true);
                    message.setMsg(StatusCode.Import.importNow);
                } else {
                    message.setMsg(StatusCode.Import.emptyFile);
                }
            } else {
                message.setMsg(StatusCode.Import.importBusy);
            }
        } catch (Exception e) {
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
            logger.error("上传Leave失败！", e);
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
    
    @ApiIgnore
    @RequestMapping(value = "/queryImport.do")
    @ResponseBody
    public ImportMessage queryImport(HttpServletRequest request, HttpServletResponse response) {
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
}

