package com.fcc.commons.workflow.controller;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Logical;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.common.Constants;
import com.fcc.commons.web.common.StatusCode;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.Message;
import com.fcc.commons.workflow.config.Resources;
import com.fcc.commons.workflow.query.WorkflowModelQuery;
import com.fcc.commons.workflow.service.ProcessModelService;

import io.swagger.annotations.ApiParam;

/**
 * <p>Description: 工作流-流程模型管理</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/sys/workflow/processModel")
public class ProcessModelController extends WorkflowController {

	private Logger logger = Logger.getLogger(ProcessModelController.class);
	
	@Resource
	private ProcessModelService processModelService;
	
	/** 显示流程模型列表 */
	@RequestMapping("/view.do")
	@Permissions("view")
	public String view(HttpServletRequest request) {
		request.setAttribute("modelerUrl", Resources.ACTIVITI.getString("activiti.modeler.url") + Resources.ACTIVITI.getString("activiti.modeler.editor.path"));
		return "manage/sys/workflow/processModel_list";
	}
	
	/** 添加模型 */
	@RequestMapping("/add.do")
	@Permissions("add")
	public ModelAndView add(@RequestParam(name = "modelName", defaultValue = "") String modelName, @RequestParam(name = "modelKey", defaultValue = "") String modelKey, 
			@RequestParam(name = "modelDescription", defaultValue = "") String modelDescription,
			HttpServletRequest request) {
		Message message = new Message();
		String modelId = null;
		try {
			if (StringUtils.isEmpty(modelName)) {
				throw new RefusedException("请输入模型名称！");
			} else if (StringUtils.isEmpty(modelKey)) {
				throw new RefusedException("请输入模型KEY！");
			}
			modelId = processModelService.addModel(modelKey, modelName, modelDescription);;
			if (StringUtils.isEmpty(modelId)) {
				throw new RefusedException(StatusCode.Sys.fail);
			}
			message.setSuccess(true);
			message.setMsg(StatusCode.Sys.success);
			message.setObj(modelId);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
//		ModelAndView view = new ModelAndView("redirect:" + Resources.ACTIVITI.getString("activiti.modeler.url") + Resources.ACTIVITI.getString("activiti.modeler.editor.path") + modelId);
//		return view;
		return getModelAndView(message);
	}
	
	/** 挂起、激活流程定义 */
	@RequestMapping("/edit.do")
	@Permissions("edit")
	public ModelAndView edit(HttpServletRequest request) {
		Message message = new Message();
		try {
			String status = request.getParameter("status");
			String processDefinitionId = request.getParameter("processDefinitionId");
			if (StringUtils.isEmpty(status)) {
				throw new RefusedException("请输入状态名称！");
			}
			if (StringUtils.isEmpty(processDefinitionId)) {
				throw new RefusedException("请输入流程定义ID！");
			}
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("修改失败！");
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/** 删除部署的流程，级联删除流程实例 */
	@RequestMapping("/delete.do")
	@Permissions("delete")
	public ModelAndView delete(HttpServletRequest request) {
		Message message = new Message();
		String id = request.getParameter("ids");
		try {
			if (id == null || "".equals(id)) {
				throw new RefusedException(StatusCode.Sys.emptyDeleteId);
			}
			String[] ids = id.split(",");
			for (String modelId : ids) {
				processModelService.deleteModel(modelId);
			}
			message.setMsg(StatusCode.Sys.success);
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/** 根据Model部署流程 */
	@RequestMapping("/deploy.do")
	@Permissions(value = {"add", "edit"}, logical = Logical.AND)
	public ModelAndView deploy(HttpServletRequest request) {
		Message message = new Message();
		String id = request.getParameter("ids");
		try {
			if (id == null || "".equals(id)) {
				throw new RefusedException(StatusCode.Sys.emptyUpdateId);// "请选择要部署的记录！"
			}
			String[] ids = id.split(",");
			for (String modelId : ids) {
				processModelService.deploy(modelId);
			}
			message.setMsg(StatusCode.Sys.success);
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("部署失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
     * 导出model的xml文件
     */
    @RequestMapping(value = "/export.do")
    @Permissions("view")
    public void export(@RequestParam(name = "modelId", defaultValue = "") String modelId, HttpServletRequest request, 
    		HttpServletResponse response) {
        try {
            Map<String, Object> map = processModelService.exportXML(modelId);
            String fileName = map.get("id") + ".bpmn20.xml";
	        if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
	        	fileName = new String(fileName.getBytes(Constants.encode), "ISO-8859-1");//firefox浏览器
	        } else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
	        	fileName = URLEncoder.encode(fileName, Constants.encode);//IE浏览器
	        }
	        response.reset(); 
			response.setContentType("application/octet-stream"); // MIME
			response.setHeader("Content-disposition", "attachment;  filename="+ fileName);
			IOUtils.copy((InputStream)map.get("is"), response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
	/**
	 * 流程模型 列表 返回json 给 easyUI 
	 * @return
	 */
	@RequestMapping("/datagrid")
	@ResponseBody
	@Permissions("view")
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request,
	        @ApiParam(required = false, value = "模型名称") @RequestParam(name = "modelName", defaultValue = "") String modelName,
            @ApiParam(required = false, value = "模型KEY") @RequestParam(name = "modelKey", defaultValue = "") String modelKey
            ) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
		    WorkflowModelQuery query = null;
			if (StringUtils.isNotEmpty(modelName)) {
			    query = workflowService.createModelQuery();
			    query.modelNameLike(modelName);
			}
			if (StringUtils.isNotEmpty(modelKey)) {
			    if (query == null) query = workflowService.createModelQuery();
				query.modelKey(modelKey);
			}
			ListPage listPage = processModelService.queryPage(dg.getPage(), dg.getRows(), query);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			json.setTotal(0L);
			json.setRows(Collections.EMPTY_LIST);
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
}
