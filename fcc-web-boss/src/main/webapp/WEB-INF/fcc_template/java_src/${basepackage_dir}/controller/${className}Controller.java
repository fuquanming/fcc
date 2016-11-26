<#include "/custom.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.controller;

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
import com.fcc.web.sys.controller.AppWebController;
import ${basepackage}.model.${className};
import ${basepackage}.service.${className}Service;

import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>Description:${table.tableAlias}</p>
 */

@Controller
@RequestMapping(value={"${actionBasePath}"} )
public class ${className}Controller extends AppWebController {
	
	private Logger logger = Logger.getLogger(${className}Controller.class);
	
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
    private ${className}Service ${classNameLower}Service;
	
    /** 显示列表 */
    @ApiOperation(value = "显示${table.tableAlias}列表页面")
    @RequestMapping(value = "/view.do", method = RequestMethod.GET)
    @Permissions("view")
	public String view(HttpServletRequest request) {
		return "${actionBasePath}/${classNameLower}_list";
	}
	
	/** 显示统计报表 */
	@ApiOperation(value = "显示${table.tableAlias}统计列表页面")
    @RequestMapping(value = "/report/view.do", method = RequestMethod.GET)
    @Permissions("report")
	public String reportView(HttpServletRequest request) {
		return "${actionBasePath}/${classNameLower}_report_list";
	}
	
	/** 跳转到查看页面 */
	@ApiOperation(value = "显示${table.tableAlias}查看页面")
    @RequestMapping(value = "/toView.do", method = RequestMethod.GET)
    @Permissions("view")
	public String toView(HttpServletRequest request) {
		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			<#list table.columns as column>
			<#if column.pk>
			try {
				${className} ${classNameLower} = (${className}) baseService.get(${className}.class, ${column.javaType}.valueOf(id));
				request.setAttribute("${classNameLower}", ${classNameLower});
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("查看${table.tableAlias}详情页数据加载失败！", e);
			}
			</#if>
			</#list>
		}
		return "${actionBasePath}/${classNameLower}_view";
	}
	
	/** 跳转到新增页面 */
	@ApiOperation(value = "显示${table.tableAlias}新增页面")
    @RequestMapping(value = "/toAdd.do", method = RequestMethod.GET)
    @Permissions("add")
	public String toAdd(HttpServletRequest request, HttpServletResponse response) {
		return "${actionBasePath}/${classNameLower}_add";
	}
	
	/** 跳转到修改页面 */
	@ApiOperation(value = "显示${table.tableAlias}修改页面")
    @RequestMapping(value = "/toEdit.do", method = RequestMethod.GET)
    @Permissions("edit")
	public String toEdit(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			<#list table.columns as column>
			<#if column.pk>
			try {
				${className} ${classNameLower} = (${className}) baseService.get(${className}.class, ${column.javaType}.valueOf(id));
				request.setAttribute("${classNameLower}", ${classNameLower});
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("修改${table.tableAlias}详情页数据加载失败！", e);
			}
			</#if>
			</#list>
		}
		return "${actionBasePath}/${classNameLower}_edit";
	}
	
	/** 新增 */
	@ApiOperation(value = "新增${table.tableAlias}")
    @RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @Permissions("add")
	public ModelAndView add(${className} ${classNameLower}, HttpServletRequest request, HttpServletResponse response) {
	    Message message = new Message();
		try {
			<#list table.columns as column>
			<#if column.isDateTimeColumn>
			String ${column.columnNameLower}String = request.getParameter("${column.columnNameLower}String");
			${classNameLower}.set${column.columnName}(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(${column.columnNameLower}String));
			<#else>
			</#if>
			</#list>
			baseService.add(${classNameLower});
			message.setSuccess(true);
            message.setMsg(Constants.StatusCode.Sys.success);
		} catch (Exception e) {
		    e.printStackTrace();
            logger.error("保存${table.tableAlias}失败！", e);
            message.setMsg(Constants.StatusCode.Sys.fail);
            message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/** 修改 */
	@ApiOperation(value = "修改${table.tableAlias}")
    @RequestMapping(value = "/edit.do", method = RequestMethod.POST)
    @Permissions("edit")
	public ModelAndView edit(${className} ${classNameLower}, HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		try {
			<#list table.pkColumns as column>
			${className} db${className} = null;
			String ${column.columnNameLower} = request.getParameter("${column.columnNameLower}");
			if (StringUtils.isNotEmpty(${column.columnNameLower})) {
				<#if column.isNumberColumn>
				db${className} = (${className}) baseService.get(${className}.class, ${column.javaType}.valueOf(${column.columnNameLower}));
				<#else>
				db${className} = (${className}) baseService.get(${className}.class, ${column.columnNameLower});
				</#if>
			}
			</#list>
			if (db${className} != null) {
				<#list table.columns as column>
				<#if column.isDateTimeColumn>
				String ${column.columnNameLower}String = request.getParameter("${column.columnNameLower}String");
				${classNameLower}.set${column.columnName}(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(${column.columnNameLower}String));
				<#else>
				</#if>
				</#list>
				BeanUtils.copyProperties(${classNameLower}, db${className});
				baseService.edit(db${className});
				// 更新
                message.setSuccess(true);
                message.setMsg(Constants.StatusCode.Sys.success);
            } else {
                // 修改的不存在
                message.setSuccess(false);
                message.setMsg(Constants.StatusCode.Sys.emptyUpdateId);
			}
		} catch (Exception e) {
		    e.printStackTrace();
            logger.error("修改${table.tableAlias}失败！", e);
            message.setMsg(Constants.StatusCode.Sys.fail);
            message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 删除
	 * @return
	 */
	@ApiOperation("删除${table.tableAlias}")
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @Permissions("delete")
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		String id = request.getParameter("ids");
		try {
		    if (id == null || "".equals(id)) throw new RefusedException(Constants.StatusCode.Sys.emptyDeleteId);
			String[] ids = StringUtils.split(id, ",");
			<#list table.pkColumns as column>
			<#if column.javaType == "java.lang.Integer">
			baseService.deleteById(${className}.class, DataFormater.getInteger(ids), "${column.columnNameLower}");
			<#elseif column.javaType == "java.lang.Long">
			baseService.deleteById(${className}.class, DataFormater.getLong(ids), "${column.columnNameLower}");
			<#else>
			baseService.deleteById(${className}.class, ids, "${column.columnNameLower}");
			</#if>
			</#list>
			message.setSuccess(true);
            message.setMsg(Constants.StatusCode.Sys.success);
        } catch (RefusedException e) {
            message.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除${table.tableAlias}失败！", e);
            message.setMsg(Constants.StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
		return getModelAndView(message);
	}
	
	/**
	 * 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "查询${table.tableAlias}")
    @RequestMapping(value = "/datagrid.do", method = RequestMethod.POST)
    @ResponseBody
    @Permissions("view")
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
			Map<String, Object> param = getParams(request);
			ListPage listPage = ${classNameLower}Service.queryPage(dg.getPage(), dg.getRows(), param, false);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
		    e.printStackTrace();
            logger.error("加载查询${table.tableAlias}数据失败！", e);
            json.setTotal(0L);
            json.setRows(new ArrayList<${className}>());
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
	        	ListPage listPage = ${classNameLower}Service.report(dg.getPage(), dg.getRows(), param, false);
				json.setTotal(Long.valueOf(listPage.getTotalSize()));
				json.setRows(listPage.getDataList());
	        } else {
	        	json.setTotal(0L);
				json.setRows(new ArrayList<ReportInfo>());
	        }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询${table.tableAlias}报表数据失败！", e);
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
			<#list table.columns as column>
	        <#if column.isDateTimeColumn>
	        String ${column.columnNameLower}Begin = request.getParameter("${column.columnNameLower}Begin");
	        if (StringUtils.isNotEmpty(${column.columnNameLower}Begin)) {
	        	param.put("${column.columnNameLower}Begin", format.parse(${column.columnNameLower}Begin + " 00:00:00"));
	        }
	        String ${column.columnNameLower}End = request.getParameter("${column.columnNameLower}End");
	        if (StringUtils.isNotEmpty(${column.columnNameLower}End)) {
	        	param.put("${column.columnNameLower}End", format.parse(${column.columnNameLower}End + " 23:59:59"));
	        }
	        <#else>
	        String ${column.columnNameLower} = request.getParameter("${column.columnNameLower}");
	        if (StringUtils.isNotEmpty(${column.columnNameLower})) {
	        	param.put("${column.columnNameLower}", ${column.columnNameLower});
	        }
	        </#if>
	        </#list>
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
		        if (exportDataPath == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(WebUtils.getRealPath(request.getServletContext(), "/")).append(Constants.EXPORT_DATA_FILENAME)
                    .append(File.separatorChar).append("${classNameLower}Export").append(File.separatorChar);
                    exportDataPath = sb.toString();
                }
		        List<String> titleList = new ArrayList<String>();
                <#list table.columns as column>
                titleList.add("${column.columnAlias}");
                </#list>
                
                Object[] paramObject = new Object[]{1, Constants.EXPORT_DATA_PAGE_SIZE, getParams(request), false};
                exportTask = new ExportTask("${classNameLower}", titleList, exportDataPath, ${classNameLower}Service, 
                        "query", 0, paramObject, (ExportService) ${classNameLower}Service);
                execute(exportTask);
                message.setSuccess(true);
                message.setMsg(Constants.StatusCode.Export.exportNow);
            } else {
                message.setMsg(Constants.StatusCode.Export.exportBusy);
            }
    	} catch (Exception e) {
            e.printStackTrace();
            logger.error("导出${table.tableAlias}失败！", e);
            message.setMsg(Constants.StatusCode.Sys.fail);
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
	public ModelAndView import${className}(HttpServletRequest request, HttpServletResponse response) {
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
                                .append(File.separatorChar).append("${classNameLower}Import").append(File.separatorChar);
                        importDataPath = sb.toString();
                    }
                    importTask = new ImportTask("${classNameLower}", importDataPath, file.getOriginalFilename(), file.getInputStream(), 
                            (ImportService)${classNameLower}Service, 9);
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
            message.setMsg(Constants.StatusCode.Sys.fail);
            message.setObj(e.getMessage());
            logger.error("上传${table.tableAlias}失败！", e);
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

<#macro generateIdParameter>
	<#if table.compositeId>
		${className}Id id = new ${className}Id();
		bind(request, id);
	<#else>
		<#list table.compositeIdColumns as column>
		${column.javaType} id = new ${column.javaType}(request.getParameter("${column.columnNameLower}"));
		</#list>
	</#if>
</#macro>