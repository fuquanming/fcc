package com.fcc.web.sys.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.common.StatusCode;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.model.SysLock;
import com.fcc.web.sys.service.SysLockService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @email fuquanming@gmail.com
 */
@Controller
@RequestMapping(value={"/manage/sys/sysLock"} )
public class SysLockController extends AppWebController {
	
	private Logger logger = Logger.getLogger(SysLockController.class);
	
	@Resource
	private BaseService baseService;
	//默认多列排序,example: username desc,createTime asc
	@Resource
	private SysLockService sysLockService;
	
	public SysLockController() {
	}

	/** 显示列表 */
	@ApiOperation(value = "显示系统锁列表页面")
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	@Permissions("view")
	public String view(HttpServletRequest request) {
		return "manage/sys/sysLock/sysLock_list";
	}
	
	/**
	 * 删除
	 * @return
	 */
	@ApiOperation(value = "删除系统锁")
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	@Permissions("delete")
	public ModelAndView delete(HttpServletRequest request, 
	        @ApiParam(required = true, value = "操作ID、用，分割多个ID") @RequestParam(name = "ids", defaultValue = "") String id) {
		Message message = new Message();
		try {
			if (id == null || "".equals(id)) throw new RefusedException(StatusCode.Sys.emptyDeleteId);
			String[] ids = StringUtils.split(id, ",");
			baseService.deleteById(SysLock.class, ids, "lockKey");
			message.setSuccess(true);
			message.setMsg(StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除系统锁失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "查询系统锁")
	@RequestMapping(value = "/datagrid.do", method = RequestMethod.POST)
	@ResponseBody
	@Permissions("view")
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request,
	        @ApiParam(required = false, value = "锁状态") @RequestParam(name = "lockStatus", defaultValue = "") String lockStatus) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
			Map<String, Object> param = getParams(request);
			ListPage listPage = sysLockService.queryPage(dg.getPage(), dg.getRows(), param, false);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询系统锁失败！", e);
			json.setTotal(0L);
			json.setRows(new ArrayList<SysLock>());
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	private Map<String, Object> getParams(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
	        String lockKey = request.getParameter("lockKey");
	        if (StringUtils.isNotEmpty(lockKey)) {
	        	param.put("lockKey", lockKey);
	        }
	        String lockStatus = request.getParameter("lockStatus");
	        if (StringUtils.isNotEmpty(lockStatus)) {
	        	param.put("lockStatus", lockStatus);
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
}