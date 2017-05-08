package com.fcc.commons.workflow.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.common.Constants;
import com.fcc.commons.web.common.StatusCode;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.ImportMessage;
import com.fcc.commons.web.view.Message;
import com.fcc.commons.workflow.query.WorkflowDefinitionQuery;
import com.fcc.commons.workflow.service.ProcessDefinitionService;
import com.fcc.commons.workflow.view.ProcessDefinitionInfo;

import io.swagger.annotations.ApiParam;

/**
 * <p>Description: 工作流-流程定义管理</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/sys/workflow/processDefinition")
public class ProcessDefinitionController extends WorkflowController {

	private Logger logger = Logger.getLogger(ProcessDefinitionController.class);
	
	@Resource
	private ProcessDefinitionService processDefinitionService;
	@Resource
	private RepositoryService repositoryService; 
	
	private static String importDataPath;
	
	private static Boolean importDataFlag = false;
	
	private static ImportMessage im = null;
	
	private static ReentrantLock lockImportData = new ReentrantLock();

	/** 显示流程定义列表 */
	@RequestMapping("/view.do")
	@Permissions("view")
	public String view(HttpServletRequest request) {
		return "manage/sys/workflow/processDefinition_list";
	}
	
	/** 部署流程定义 */
	@RequestMapping("/add.do")
	@Permissions("add")
	public ModelAndView add(HttpServletRequest request) {
		Message message = new Message();
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
					
					if (importDataPath == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(WebUtils.getRealPath(request.getServletContext(), "/")).append(Constants.importDataFileName)
                                .append(File.separatorChar).append("processDefinitionImport").append(File.separatorChar);
                        importDataPath = sb.toString();
                    }
					
					File importDataPathFile = new File(importDataPath);
		            if (importDataPathFile.exists() == false) importDataPathFile.mkdirs();
		            
					// 文件名
					String fileName = file.getOriginalFilename();
					// 保存文件
					String saveFilePath = new StringBuilder().append(importDataPath).append(fileName).toString();
					File saveFile = new File(saveFilePath);
					fos = new FileOutputStream(saveFile);
					IOUtils.copy(file.getInputStream(), fos);
					
					im = new ImportMessage();
					execute(new ProcessDefinitionImport(im, saveFile));
					message.setSuccess(true);
					message.setMsg(StatusCode.Import.importNow);
				} else {
					message.setMsg(StatusCode.Import.emptyFile);
				}
			} else {
				message.setMsg(StatusCode.Import.importBusy);
			}
		} catch (Exception e) {
			message.setMsg("上传失败！");
			message.setObj(e.getMessage());
			logger.error("上传失败！", e);
			importDataFlag = false;
		} finally {
			try {
				if (file != null) IOUtils.closeQuietly(file.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			IOUtils.closeQuietly(fos);
			lockImportData.unlock();
		}
		return getModelAndView(message);
	}
	/** 挂起、激活流程定义 */
	@RequestMapping("/edit.do")
	@Permissions("edit")
	public ModelAndView edit(HttpServletRequest request,
	        @ApiParam(required = false, value = "状态名称") @RequestParam(name = "status", defaultValue = "") String status,
            @ApiParam(required = true, value = "流程定义ID") @RequestParam(name = "processDefinitionId", defaultValue = "") String processDefinitionId
            ) {
		Message message = new Message();
		try {
			if (StringUtils.isEmpty(status)) {
				throw new RefusedException("请输入状态名称！");
			}
			if (StringUtils.isEmpty(processDefinitionId)) {
				throw new RefusedException("请输入流程定义ID！");
			}
			if ("activate".equals(status)) {
				processDefinitionService.activateProcessDefinitionById(processDefinitionId, true, null);
				message.setMsg(StatusCode.Sys.success);// 激活成功！
			} else if ("suspend".equals(status)) {
				processDefinitionService.suspendProcessDefinitionById(processDefinitionId, true, null);
				message.setMsg(StatusCode.Sys.success);// 挂起成功！
			}
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("挂起、激活流程失败！", e);
			message.setMsg(StatusCode.Sys.fail);
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
			for (String deploymentId : ids) {
				processDefinitionService.deleteDeployment(deploymentId, true);
			}
			message.setMsg(StatusCode.Sys.success);
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除流程定义失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 流程定义 列表 返回json 给 easyUI 
	 * @return
	 */
	@RequestMapping("/datagrid.do")
	@ResponseBody
	@Permissions("view")
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request,
	        @ApiParam(required = false, value = "流程定义名称") @RequestParam(name = "definitionName", defaultValue = "") String definitionName,
            @ApiParam(required = false, value = "流程定义KEY") @RequestParam(name = "definitionKey", defaultValue = "") String definitionKey
            ) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
		    WorkflowDefinitionQuery query = null;
			if (StringUtils.isNotEmpty(definitionName)) {
			    query = workflowService.createDefinitionQuery();
				query.processDefinitionNameLike(definitionName);
			}
			if (StringUtils.isNotEmpty(definitionKey)) {
			    if (query == null)  query = workflowService.createDefinitionQuery();
				query.processDefinitionKey(definitionKey);
			}
			ListPage listPage = processDefinitionService.queryPage(dg.getPage(), dg.getRows(), query);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			json.setTotal(0L);
			json.setRows(new ArrayList<ProcessDefinitionInfo>());
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	/** 流程定义转换模型 */
	@RequestMapping("/convertToModel.do")
	public ModelAndView convertToModel(HttpServletRequest request) {
		Message message = new Message();
		try {
			String processDefinitionId = request.getParameter("processDefinitionId");
			if (StringUtils.isNotEmpty(processDefinitionId)) {
				processDefinitionService.convertToModel(processDefinitionId);
				message.setMsg(StatusCode.Sys.success);
				message.setSuccess(true);
			} else {
				throw new RefusedException(StatusCode.Sys.emptyUpdateId);
			}
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("转化失败！");
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	
	/**
	 * 读取资源，通过部署ID
	 * @param processDefinitionId	流程定义ID
	 * @param resourceType			资源类型(xml|image)
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/resource/read.do")
//	@Permissions("add")
	public void resourceRead(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resourceType") String resourceType,
                                 HttpServletResponse response) throws Exception {
        try {
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
			String resourceName = "";
			if (resourceType.equals("image")) {
			    resourceName = processDefinition.getDiagramResourceName();
			} else if (resourceType.equals("xml")) {
			    resourceName = processDefinition.getResourceName();
			}
			InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			    response.getOutputStream().write(b, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@RequestMapping("/queryImport.do")
	@ResponseBody
	public ImportMessage queryImport(HttpServletRequest request, HttpServletResponse response) {
		if (im == null) {
			im = new ImportMessage();
			im.setImportFlag(true);
		} else {
//			if (im.isImportFlag()) im = null;
		}
		return im;
	}
	
	class ProcessDefinitionImport implements Runnable {
		private ImportMessage message;
		private File saveFile;
		public ProcessDefinitionImport(ImportMessage message, File saveFile) {
			this.message = message;
			this.saveFile = saveFile;
		}
		public void run() {
			long startTime = System.currentTimeMillis();
			int totalSize = 0;
			FileInputStream fis = null;
			try {
				// 读取文件
				try {
					fis = new FileInputStream(saveFile);
//					Deployment deployment = null;
					
					String fileName = saveFile.getName().toLowerCase();
					String extension = FilenameUtils.getExtension(fileName).toLowerCase();
					if (extension.equals("zip") || extension.equals("bar")) { // 压缩包
						ZipInputStream zip = new ZipInputStream(fis);
						repositoryService.createDeployment().addZipInputStream(zip).deploy();
		            } else {
		                repositoryService.createDeployment().addInputStream(fileName, fis).deploy();
		            }
					totalSize ++;
				} catch (Exception e) {
					logger.error("打开文件出错：" + e);
					return;
				} finally {
					IOUtils.closeQuietly(fis);
				}
				message.setCurrentSize(totalSize);
			} catch (Exception e) {
				logger.error(e);
			} finally {
				message.setImportFlag(true);
				long endTime = System.currentTimeMillis();
				logger.info("time=" + (endTime - startTime) + ",totalSize=" + totalSize);
				importDataFlag = false;
			}
		}
	}
	
}