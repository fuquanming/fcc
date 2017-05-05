package com.fcc.web.workflow.controller;

import java.io.File;
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
import com.fcc.commons.workflow.common.StatusCode;
import com.fcc.commons.workflow.common.WorkflowDefinitionKey;
import com.fcc.commons.workflow.common.WorkflowStatus;
import com.fcc.commons.workflow.controller.WorkflowController;
import com.fcc.web.sys.cache.SysUserAuthentication;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.workflow.model.AmountApply;
import com.fcc.web.workflow.service.AmountApplyService;

import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>Description:AmountApply</p>
 */

@Controller
@RequestMapping(value={"/manage/workflow/amountApply"} )
public class AmountApplyController extends WorkflowController {
    
    private Logger logger = Logger.getLogger(AmountApplyController.class);
    
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
    private AmountApplyService amountApplyService;
    
    public AmountApplyController() {
        WorkflowDefinitionKey.definitionKeyMap.put(AmountApply.processDefinitionKey, AmountApply.processDefinitionName);
    }
    
    /** 显示列表 */
    @ApiOperation(value = "显示AmountApply列表页面")
    @RequestMapping(value = "/view.do", method = RequestMethod.GET)
    @Permissions("view")
    public String view(HttpServletRequest request) {
        setWorkflowStatus(request);
        return "/manage/workflow/amountApply/amountApply_list";
    }
    
    /** 显示统计报表 */
    @ApiOperation(value = "显示AmountApply统计列表页面")
    @RequestMapping(value = "/report/view.do", method = RequestMethod.GET)
    @Permissions("report")
    public String reportView(HttpServletRequest request) {
        return "/manage/workflow/amountApply/amountApply_report_list";
    }
    
    /** 跳转到查看页面 */
    @ApiOperation(value = "显示AmountApply查看页面")
    @RequestMapping(value = "/toView.do", method = RequestMethod.GET)
    @Permissions("view")
    public String toView(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isNotEmpty(id)) {
            try {
                AmountApply amountApply = (AmountApply) baseService.get(AmountApply.class, java.lang.String.valueOf(id));
                setProcessHistory(request, amountApply.getProcessInstanceId());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("查看AmountApply详情页数据加载失败！", e);
            }
        }
        return "/manage/workflow/amountApply/amountApply_view";
    }
    
    /** 跳转到新增页面 */
    @ApiOperation(value = "显示AmountApply新增页面")
    @RequestMapping(value = "/toAdd.do", method = RequestMethod.GET)
    @Permissions("add")
    public String toAdd(HttpServletRequest request, HttpServletResponse response) {
        return "/manage/workflow/amountApply/amountApply_add";
    }
    
    /** 跳转到修改页面 */
    @ApiOperation(value = "显示AmountApply修改页面")
    @RequestMapping(value = "/toEdit.do", method = RequestMethod.GET)
    @Permissions("edit")
    public String toEdit(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (StringUtils.isNotEmpty(id)) {
            try {
                AmountApply amountApply = (AmountApply) baseService.get(AmountApply.class, java.lang.String.valueOf(id));
                request.setAttribute("amountApply", amountApply);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("修改AmountApply详情页数据加载失败！", e);
            }
        }
        return "/manage/workflow/amountApply/amountApply_edit";
    }
    
    /** 新增 */
    @ApiOperation(value = "新增AmountApply")
    @RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @Permissions("add")
    public ModelAndView add(AmountApply amountApply, HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        try {
            String applyTimeString = request.getParameter("applyTimeString");
            amountApply.setApplyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(applyTimeString));
            amountApply.setStatus(WorkflowStatus.unstart.name());
            amountApply.setCreateTime(new Date());
            amountApply.setCreateUser(SysUserAuthentication.getSysUser().getUserId());
            String operate = request.getParameter("operate");
            if ("save".equals(operate)) {// 保存
                baseService.add(amountApply);
            } else if ("start".equals(operate)) {// 保存、启动流程
                // 启动流程
                amountApplyService.add(amountApply);
            }
            message.setSuccess(true);
            message.setMsg(StatusCode.Sys.success);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存AmountApply失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
    
    /** 修改 */
    @ApiOperation(value = "修改AmountApply")
    @RequestMapping(value = "/edit.do", method = RequestMethod.POST)
    @Permissions("edit")
    public ModelAndView edit(AmountApply amountApply, HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        try {
            AmountApply dbAmountApply = null;
            String amountApplyId = request.getParameter("amountApplyId");
            if (StringUtils.isNotEmpty(amountApplyId)) {
                dbAmountApply = (AmountApply) baseService.get(AmountApply.class, amountApplyId);
            }
            if (dbAmountApply != null) {
                // 未启动状态可以修改
                if (WorkflowStatus.unstart.name().equals(dbAmountApply.getStatus())) {
                    if (amountApply.getUserId() != null) dbAmountApply.setUserId(amountApply.getUserId());
                    if (amountApply.getUserName() != null) dbAmountApply.setUserName(amountApply.getUserName());
                    if (amountApply.getPrimaryAmount() != null) dbAmountApply.setPrimaryAmount(amountApply.getPrimaryAmount());
                    if (amountApply.getApplyRemark() != null) dbAmountApply.setApplyRemark(amountApply.getApplyRemark());
                    String applyTimeString = request.getParameter("applyTimeString");
                    dbAmountApply.setApplyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(applyTimeString));
                    dbAmountApply.setUpdateTime(new Date());
                    dbAmountApply.setUpdateUser(SysUserAuthentication.getSysUser().getUserId());
                    String operate = request.getParameter("operate");
                    if ("save".equals(operate)) {// 保存
                        baseService.edit(dbAmountApply);
                    } else if ("start".equals(operate)) {// 保存、启动流程
                        // 启动流程
                        amountApplyService.edit(dbAmountApply);
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
            logger.error("修改AmountApply失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
    
    /**
     * 删除
     * @return
     */
    @ApiOperation("删除AmountApply")
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @Permissions("delete")
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        String id = request.getParameter("ids");
        try {
            if (id == null || "".equals(id)) throw new RefusedException(StatusCode.Sys.emptyDeleteId);
            String[] ids = StringUtils.split(id, ",");
            baseService.deleteById(AmountApply.class, ids, "amountApplyId");
            message.setSuccess(true);
            message.setMsg(StatusCode.Sys.success);
        } catch (RefusedException e) {
            message.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除AmountApply失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
    
    /**
     * 列表 返回json 给 easyUI 
     * @return
     */
    @ApiOperation(value = "查询AmountApply")
    @RequestMapping(value = "/datagrid.do", method = RequestMethod.POST)
    @ResponseBody
    @Permissions("view")
    public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request) {
        EasyuiDataGridJson json = new EasyuiDataGridJson();
        try {
            Map<String, Object> param = getParams(request);
            ListPage listPage = amountApplyService.queryPage(dg.getPage(), dg.getRows(), param, false);
            json.setTotal(Long.valueOf(listPage.getTotalSize()));
            json.setRows(listPage.getDataList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载查询AmountApply数据失败！", e);
            json.setTotal(0L);
            json.setRows(new ArrayList<AmountApply>());
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
                ListPage listPage = amountApplyService.report(dg.getPage(), dg.getRows(), param, false);
                json.setTotal(Long.valueOf(listPage.getTotalSize()));
                json.setRows(listPage.getDataList());
            } else {
                json.setTotal(0L);
                json.setRows(new ArrayList<ReportInfo>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询AmountApply报表数据失败！", e);
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
            String amountApplyId = request.getParameter("amountApplyId");
            if (StringUtils.isNotEmpty(amountApplyId)) {
                param.put("amountApplyId", amountApplyId);
            }
            String userId = request.getParameter("userId");
            if (StringUtils.isNotEmpty(userId)) {
                param.put("userId", userId);
            }
            String userName = request.getParameter("userName");
            if (StringUtils.isNotEmpty(userName)) {
                param.put("userName", userName);
            }
            String primaryAmount = request.getParameter("primaryAmount");
            if (StringUtils.isNotEmpty(primaryAmount)) {
                param.put("primaryAmount", primaryAmount);
            }
            String applyRemark = request.getParameter("applyRemark");
            if (StringUtils.isNotEmpty(applyRemark)) {
                param.put("applyRemark", applyRemark);
            }
            String applyTimeBegin = request.getParameter("applyTimeBegin");
            if (StringUtils.isNotEmpty(applyTimeBegin)) {
                param.put("applyTimeBegin", format.parse(applyTimeBegin + " 00:00:00"));
            }
            String applyTimeEnd = request.getParameter("applyTimeEnd");
            if (StringUtils.isNotEmpty(applyTimeEnd)) {
                param.put("applyTimeEnd", format.parse(applyTimeEnd + " 23:59:59"));
            }
            String processInstanceId = request.getParameter("processInstanceId");
            if (StringUtils.isNotEmpty(processInstanceId)) {
                param.put("processInstanceId", processInstanceId);
            }
            String processDefinitionId = request.getParameter("processDefinitionId");
            if (StringUtils.isNotEmpty(processDefinitionId)) {
                param.put("processDefinitionId", processDefinitionId);
            }
            String processNodeName = request.getParameter("processNodeName");
            if (StringUtils.isNotEmpty(processNodeName)) {
                param.put("processNodeName", processNodeName);
            }
            String status = request.getParameter("status");
            if (StringUtils.isNotEmpty(status)) {
                param.put("status", status);
            }
            String createUser = request.getParameter("createUser");
            if (StringUtils.isNotEmpty(createUser)) {
                param.put("createUser", createUser);
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
                    .append(File.separatorChar).append("amountApplyExport").append(File.separatorChar);
                    exportDataPath = sb.toString();
                }
                List<String> titleList = new ArrayList<String>();
                titleList.add("借款额度申请ID");
                titleList.add("借款人ID（会员）");
                titleList.add("会员账号名称");
                titleList.add("发起人申请的额度");
                titleList.add("申请备注");
                titleList.add("申请时间");
                titleList.add("流程实例ID");
                titleList.add("流程定义ID");
                titleList.add("当前节点名称");
                titleList.add("状态");
                titleList.add("创建者");
                titleList.add("创建时间");
                
                Object[] paramObject = new Object[]{1, Constants.exportDataPageSize, getParams(request), false};
                exportTask.setRunningKey("amountApply");
                exportTask.setTitleList(titleList);
                exportTask.setExportDataPath(exportDataPath);
                exportTask.setQueryService(amountApplyService);
                exportTask.setQueryServiceMethodName("query");
                exportTask.setPageNoSub(0);
                exportTask.setQueryParams(paramObject);
                exportTask.setExportService((ExportService) amountApplyService);
                exportTask.init();
                
                execute(exportTask);
                message.setSuccess(true);
                message.setMsg(StatusCode.Export.exportNow);
            } else {
                message.setMsg(StatusCode.Export.exportBusy);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出AmountApply失败！", e);
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
    public ModelAndView importAmountApply(HttpServletRequest request, HttpServletResponse response) {
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
                                .append(File.separatorChar).append("amountApplyImport").append(File.separatorChar);
                        importDataPath = sb.toString();
                    }
                    importTask.setRunningKey("amountApply");
                    importTask.setImportDataPath(importDataPath);
                    importTask.setFile(file);
                    importTask.setImportService((ImportService) amountApplyService);
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
            logger.error("上传AmountApply失败！", e);
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

