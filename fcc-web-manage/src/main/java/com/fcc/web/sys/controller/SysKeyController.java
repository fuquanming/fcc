package com.fcc.web.sys.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.springframework.beans.BeanUtils;
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
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.common.StatusCode;
import com.fcc.web.sys.model.SysKey;
import com.fcc.web.sys.service.SysKeyService;

import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>Description:SysKey</p>
 */

@Controller
@RequestMapping(value={"/manage/sys/sysKey"} )
public class SysKeyController extends AppWebController {
    
    private Logger logger = Logger.getLogger(SysKeyController.class);
    
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
    private SysKeyService sysKeyService;
    
    /** 显示列表 */
    @ApiOperation(value = "显示SysKey列表页面")
    @RequestMapping(value = "/view.do", method = RequestMethod.GET)
    @Permissions("view")
    public String view(HttpServletRequest request) {
        return "/manage/sys/sysKey/sysKey_list";
    }
    
    /** 显示统计报表 */
    @ApiOperation(value = "显示SysKey统计列表页面")
    @RequestMapping(value = "/report/view.do", method = RequestMethod.GET)
    @Permissions("report")
    public String reportView(HttpServletRequest request) {
        return "/manage/sys/sysKey/sysKey_report_list";
    }
    
    /** 跳转到查看页面 */
    @ApiOperation(value = "显示SysKey查看页面")
    @RequestMapping(value = "/toView.do", method = RequestMethod.GET)
    @Permissions("view")
    public String toView(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isNotEmpty(id)) {
            try {
                SysKey sysKey = (SysKey) baseService.get(SysKey.class, java.lang.String.valueOf(id));
                request.setAttribute("sysKey", sysKey);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("查看SysKey详情页数据加载失败！", e);
            }
        }
        return "/manage/sys/sysKey/sysKey_view";
    }
    
    /** 跳转到新增页面 */
    @ApiOperation(value = "显示SysKey新增页面")
    @RequestMapping(value = "/toAdd.do", method = RequestMethod.GET)
    @Permissions("add")
    public String toAdd(HttpServletRequest request, HttpServletResponse response) {
        return "/manage/sys/sysKey/sysKey_add";
    }
    
    /** 跳转到修改页面 */
    @ApiOperation(value = "显示SysKey修改页面")
    @RequestMapping(value = "/toEdit.do", method = RequestMethod.GET)
    @Permissions("edit")
    public String toEdit(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (StringUtils.isNotEmpty(id)) {
            try {
                SysKey sysKey = (SysKey) baseService.get(SysKey.class, java.lang.String.valueOf(id));
                request.setAttribute("sysKey", sysKey);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("修改SysKey详情页数据加载失败！", e);
            }
        }
        return "/manage/sys/sysKey/sysKey_edit";
    }
    
    /** 新增 */
    @ApiOperation(value = "新增SysKey")
    @RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @Permissions("add")
    public ModelAndView add(SysKey sysKey, HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        try {
            baseService.add(sysKey);
            message.setSuccess(true);
            message.setMsg(StatusCode.Sys.success);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存SysKey失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
    
    /** 修改 */
    @ApiOperation(value = "修改SysKey")
    @RequestMapping(value = "/edit.do", method = RequestMethod.POST)
    @Permissions("edit")
    public ModelAndView edit(SysKey sysKey, HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        try {
            SysKey dbSysKey = null;
            String kyeId = request.getParameter("kyeId");
            if (StringUtils.isNotEmpty(kyeId)) {
                dbSysKey = (SysKey) baseService.get(SysKey.class, kyeId);
            }
            if (dbSysKey != null) {
                BeanUtils.copyProperties(sysKey, dbSysKey);
                baseService.edit(dbSysKey);
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
            logger.error("修改SysKey失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
    
    /**
     * 删除
     * @return
     */
    @ApiOperation("删除SysKey")
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @Permissions("delete")
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        String id = request.getParameter("ids");
        try {
            if (id == null || "".equals(id)) throw new RefusedException(StatusCode.Sys.emptyDeleteId);
            String[] ids = StringUtils.split(id, ",");
            baseService.deleteById(SysKey.class, ids, "kyeId");
            message.setSuccess(true);
            message.setMsg(StatusCode.Sys.success);
        } catch (RefusedException e) {
            message.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除SysKey失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
    
    /**
     * 列表 返回json 给 easyUI 
     * @return
     */
    @ApiOperation(value = "查询SysKey")
    @RequestMapping(value = "/datagrid.do", method = RequestMethod.POST)
    @ResponseBody
    @Permissions("view")
    public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request) {
        EasyuiDataGridJson json = new EasyuiDataGridJson();
        try {
            Map<String, Object> param = getParams(request);
            ListPage listPage = sysKeyService.queryPage(dg.getPage(), dg.getRows(), param, false);
            json.setTotal(Long.valueOf(listPage.getTotalSize()));
            json.setRows(listPage.getDataList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载查询SysKey数据失败！", e);
            json.setTotal(0L);
            json.setRows(new ArrayList<SysKey>());
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
                ListPage listPage = sysKeyService.report(dg.getPage(), dg.getRows(), param, false);
                json.setTotal(Long.valueOf(listPage.getTotalSize()));
                json.setRows(listPage.getDataList());
            } else {
                json.setTotal(0L);
                json.setRows(new ArrayList<ReportInfo>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询SysKey报表数据失败！", e);
            json.setTotal(0L);
            json.setRows(new ArrayList<ReportInfo>());
            json.setMsg(e.getMessage());
        }
        return json;
    }
    
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<String, Object>();
        try {
            String kyeId = request.getParameter("kyeId");
            if (StringUtils.isNotEmpty(kyeId)) {
                param.put("kyeId", kyeId);
            }
            String linkType = request.getParameter("linkType");
            if (StringUtils.isNotEmpty(linkType)) {
                param.put("linkType", linkType);
            }
            String linkId = request.getParameter("linkId");
            if (StringUtils.isNotEmpty(linkId)) {
                param.put("linkId", linkId);
            }
            String keyValue = request.getParameter("keyValue");
            if (StringUtils.isNotEmpty(keyValue)) {
                param.put("keyValue", keyValue);
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
                    .append(File.separatorChar).append("sysKeyExport").append(File.separatorChar);
                    exportDataPath = sb.toString();
                }
                List<String> titleList = new ArrayList<String>();
                titleList.add("KEY主键");
                titleList.add("关联类型");
                titleList.add("关联ID");
                titleList.add("key值");
                
                Object[] paramObject = new Object[]{1, Constants.exportDataPageSize, getParams(request), false};
                exportTask.setRunningKey("sysKey");
                exportTask.setTitleList(titleList);
                exportTask.setExportDataPath(exportDataPath);
                exportTask.setQueryService(sysKeyService);
                exportTask.setQueryServiceMethodName("query");
                exportTask.setPageNoSub(0);
                exportTask.setQueryParams(paramObject);
                exportTask.setExportService((ExportService) sysKeyService);
                exportTask.init();
                
                execute(exportTask);
                message.setSuccess(true);
                message.setMsg(StatusCode.Export.exportNow);
            } else {
                message.setMsg(StatusCode.Export.exportBusy);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出SysKey失败！", e);
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
    public ModelAndView importSysKey(HttpServletRequest request, HttpServletResponse response) {
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
                                .append(File.separatorChar).append("sysKeyImport").append(File.separatorChar);
                        importDataPath = sb.toString();
                    }
                    importTask.setRunningKey("sysKey");
                    importTask.setImportDataPath(importDataPath);
                    importTask.setFile(file);
                    importTask.setImportService((ImportService) sysKeyService);
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
            logger.error("上传SysKey失败！", e);
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

