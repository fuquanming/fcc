package com.fcc.commons.workflow.controller;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.common.StatusCode;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.Message;
import com.fcc.commons.workflow.query.WorkflowHistoryQuery;
import com.fcc.commons.workflow.service.ProcessHistoryService;
import com.fcc.commons.workflow.view.ProcessTaskInfo;

import io.swagger.annotations.ApiParam;

/**
 * <p>Description: 工作流-流程历史管理</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/sys/workflow/processHistory")
public class ProcessHistoryController extends WorkflowController {

	private Logger logger = Logger.getLogger(ProcessInstanceController.class);
	
	@Resource
	private ProcessHistoryService processHistoryService;
	
	/** 显示流程历史列表 */
	@RequestMapping("/view.do")
	@Permissions("view")
	public String view(HttpServletRequest request) {
		return "manage/sys/workflow/processHistory_list";
	}
	
	/** 显示流程历史详情 */
    @RequestMapping("/toView.do")
//    @Permissions("view")
    public String toView(HttpServletRequest request,
            @ApiParam(required = false, value = "流程实例ID") @RequestParam(name = "id", defaultValue = "") String processInstanceId) {
        try {
            setProcessHistory(request, processInstanceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "manage/sys/workflow/processHistory_view";
    }
	
	
	/** 删除流程历史记录 */
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
			for (String hId : ids) {
				processHistoryService.deleteHistoryProcessInstance(hId);
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
	
	/**
	 * 流程模型 列表 返回json 给 easyUI 
	 * @return
	 */
	@RequestMapping("/datagrid")
	@ResponseBody
	@Permissions("view")
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request,
	        @ApiParam(required = false, value = "流程业务ID") @RequestParam(name = "businessKey", defaultValue = "") String businessKey,
            @ApiParam(required = false, value = "流程定义KEY") @RequestParam(name = "definitionKey", defaultValue = "") String definitionKey,
            @ApiParam(required = false, value = "流程用户发起") @RequestParam(name = "startedBy", defaultValue = "") String startedBy,
            @ApiParam(required = false, value = "流程用户参与") @RequestParam(name = "involvedUser", defaultValue = "") String involvedUser
            ) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
		    WorkflowHistoryQuery query = null;
			if (StringUtils.isNotEmpty(businessKey)) {
			    query = workflowService.createHistoryQuery();
				query.processInstanceBusinessKey(businessKey);
			}
			if (StringUtils.isNotEmpty(definitionKey)) {
			    if (query == null) query = workflowService.createHistoryQuery();
				query.processDefinitionKey(definitionKey);
			}
			if (StringUtils.isNotEmpty(startedBy)) {
                if (query == null) query = workflowService.createHistoryQuery();
                query.startedBy(startedBy);
            }
			if (StringUtils.isNotEmpty(involvedUser)) {
                if (query == null) query = workflowService.createHistoryQuery();
                query.involvedUser(involvedUser);
            }
			ListPage listPage = processHistoryService.queryPage(dg.getPage(), dg.getRows(), query);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询流程历史错误！", e);
			json.setTotal(0L);
			json.setRows(Collections.EMPTY_LIST);
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	/**
     * 流程任务 列表 返回json 给 easyUI 
     * @return
     */
    @RequestMapping("/viewDatagrid")
    @ResponseBody
//    @Permissions("view")
    public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request,
            @ApiParam(required = false, value = "流程实例ID") @RequestParam(name = "processInstanceId", defaultValue = "") String processInstanceId
            ) {
        EasyuiDataGridJson json = new EasyuiDataGridJson();
        try {
            List<ProcessTaskInfo> list = processHistoryService.query(processInstanceId);
            json.setTotal(Long.valueOf(list.size()));
            json.setRows(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询流程历史详情错误！", e);
            json.setTotal(0L);
            json.setRows(Collections.EMPTY_LIST);
            json.setMsg(e.getMessage());
        }
        return json;
    }
}