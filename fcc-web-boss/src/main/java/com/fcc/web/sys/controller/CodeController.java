/*
 * @(#)CodeController.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年11月23日
 * 修改历史 : 
 *     1. [2016年11月23日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.common.StatusCode;
import com.fcc.commons.web.config.Resources;
import com.fcc.commons.web.util.SpringContextUtil;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.Message;
import com.fcc.framework.generator.GeneratorFacade;
import com.fcc.framework.generator.GeneratorProperties;
import com.fcc.framework.generator.provider.db.DataSourceProvider;
import com.fcc.framework.generator.provider.db.table.TableFactory;
import com.fcc.framework.generator.provider.db.table.model.Column;
import com.fcc.framework.generator.provider.db.table.model.Table;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 代码生成
 * @version 
 * @author 傅泉明
 */
@Controller
@RequestMapping(value={"/manage/sys/code"} )
public class CodeController extends AppWebController {
    
    private Logger logger = Logger.getLogger(CodeController.class);
    
    /** 显示列表 */
    @ApiOperation(value = "显示数据库表列表页面")
    @RequestMapping(value = "/view.do", method = RequestMethod.GET)
    @Permissions("view")
    public String view(HttpServletRequest request) {
        GeneratorProperties.setProperty("basepackage", "com.fcc.web.sys");
        GeneratorProperties.setProperty("namespace", "manage/sys");
        GeneratorProperties.setProperty("outRoot", "f:/test-temp/");
        GeneratorProperties.setProperty("jdbc.url", Resources.DB.getString("jdbc.url"));
        GeneratorProperties.setProperty("jdbc.driver", Resources.DB.getString("jdbc.driver"));
        GeneratorProperties.setProperty("jdbc.username", Resources.DB.getString("jdbc.username"));
        GeneratorProperties.setProperty("jdbc.password", Resources.DB.getString("jdbc.password"));
        GeneratorProperties.setProperty("jdbc.schema", Resources.DB.getString("jdbc.schema"));
        return "/manage/sys/code/code_list";
    }
    
    /** 跳转到新增页面 */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "显示新增页面")
    @RequestMapping(value = "/toAdd.do", method = RequestMethod.GET)
    @Permissions("add")
    public String toAdd(HttpServletRequest request, HttpServletResponse response) {
        GeneratorProperties.setProperty("basepackage", "com.fcc.web.sys");
        GeneratorProperties.setProperty("namespace", "manage/sys");
        GeneratorProperties.setProperty("outRoot", "f:/test-temp/");
        
        GeneratorProperties.setProperty("java_typemapping.java.sql.Timestamp", "java.util.Date");
        GeneratorProperties.setProperty("java_typemapping.java.sql.Date", "java.util.Date");
        GeneratorProperties.setProperty("java_typemapping.java.sql.Time", "java.util.Date");
        GeneratorProperties.setProperty("java_typemapping.java.lang.Byte", "Integer");
        GeneratorProperties.setProperty("java_typemapping.java.lang.Short", "Integer");
        GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "Long");
        GeneratorProperties.setProperty("java_typemapping.java.sql.Clob", "String");
        
//        GeneratorProperties.setProperty("jdbc.url", Resources.DB.getString("jdbc.url"));
//        GeneratorProperties.setProperty("jdbc.driver", Resources.DB.getString("jdbc.driver"));
//        GeneratorProperties.setProperty("jdbc.username", Resources.DB.getString("jdbc.username"));
//        GeneratorProperties.setProperty("jdbc.password", EncryptionUtil.decryptDES(DbPasswordCallback.DB_KEY, Resources.DB.getString("jdbc.password"), null));
//        GeneratorProperties.setProperty("jdbc.schema", Resources.DB.getString("jdbc.schema"));
        
        DataSourceProvider.setDataSource((DataSource)SpringContextUtil.getBean("dataSource"));
        List<Table> list = TableFactory.getInstance().getAllTables();
//        List<Table> list = new ArrayList<Table>();
        int size = list.size();
        List<String> tableNameList = new ArrayList<String>(size);
        for (Table table : list) {
            tableNameList.add(table.getSqlName());
        }
        request.setAttribute("tableList", tableNameList);
        return "/manage/sys/code/code_add";
    }
    
    /**
     * 查询table的列
     * @return
     */
    @ApiOperation(value = "查询table列名")
    @RequestMapping(value = "/column.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    @Permissions("add")
    public List<ColumnView> getTable(HttpServletRequest request,
            @RequestParam(name = "tableName", defaultValue = "") String tableName) {
        // 获取table的列
        List<ColumnView> columnList = new ArrayList<ColumnView>();
        try {
            if (StringUtils.isNotEmpty(tableName)) {
                Set<Column> columnSet = TableFactory.getInstance().getTable(tableName).getColumns();
                for (Column column : columnSet) {
                    ColumnView view = new ColumnView();
                    view.setColumnName(column.getSqlName());
                    view.setColumnAlias(column.getColumnAlias());
                    columnList.add(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnList;
    }
    
    /** 新增 */
    @ApiIgnore
    @RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @Permissions("add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        try {
            String basepackage = request.getParameter("basepackage");
            String namespace = request.getParameter("namespace");
            String outRoot = request.getParameter("outRoot");
            String tableName = request.getParameter("tableName");
            String className = request.getParameter("className");
            String type = request.getParameter("type");
            
            GeneratorProperties.setProperty("basepackage", basepackage);
            GeneratorProperties.setProperty("namespace", namespace);
            GeneratorProperties.setProperty("outRoot", outRoot);
            
            String listQuery = request.getParameter("listQuery");// 列表查询字段
            String listShow = request.getParameter("listShow");// 新增显示字段
            String addShow = request.getParameter("addShow");// 修改显示字段
            String editShow = request.getParameter("editShow");// 查看显示字段
            String viewShow = request.getParameter("viewShow");// 查看显示字段
            String reportQuery = request.getParameter("reportQuery");// 报表查询字段
            
            HashMap<String, Object> map = new HashMap<String, Object>();
            List<String> list = null;
            if (StringUtils.isNotEmpty(listQuery)) {
                list = new ArrayList<String>();
                String[] listQuerys = StringUtils.split(listQuery, ",");
                for (String data : listQuerys) {
                    list.add(data);
                }
                map.put(Table.LIST_JSP_QUERY_COLUMNS, list);
            }
            
            if (StringUtils.isNotEmpty(listShow)) {
                list = new ArrayList<String>();
                String[] listShows = StringUtils.split(listShow, ",");
                for (String data : listShows) {
                    list.add(data);
                }
                map.put(Table.LIST_JSP_SHOW_COLUMNS, list);
            }
            
            if (StringUtils.isNotEmpty(addShow)) {
                list = new ArrayList<String>();
                String[] addShows = StringUtils.split(addShow, ",");
                for (String data : addShows) {
                    list.add(data);
                }
                map.put(Table.ADD_JSP_SHOW_COLUMNS, list);
            }
            
            if (StringUtils.isNotEmpty(editShow)) {
                list = new ArrayList<String>();
                String[] editShows = StringUtils.split(editShow, ",");
                for (String data : editShows) {
                    list.add(data);
                }
                map.put(Table.EDIT_JSP_SHOW_COLUMNS, list);
            }
            
            if (StringUtils.isNotEmpty(viewShow)) {
                list = new ArrayList<String>();
                String[] viewShows = StringUtils.split(viewShow, ",");
                for (String data : viewShows) {
                    list.add(data);
                }
                map.put(Table.VIEW_JSP_SHOW_COLUMNS, list);
            }
            
            if (StringUtils.isNotEmpty(reportQuery)) {
                list = new ArrayList<String>();
                String[] reportQuerys = StringUtils.split(reportQuery, ",");
                for (String data : reportQuerys) {
                    list.add(data);
                }
                map.put(Table.REPORT_JSP_QUERY_COLUMNS, list);
            }
            GeneratorFacade g = new GeneratorFacade();
            StringBuilder sb = new StringBuilder();
            sb.append(WebUtils.getRealPath(request.getServletContext(), "/")).append("WEB-INF").append(File.separatorChar);
            if ("tree".equals(type)) {
                sb.append("fcc_tree_template");
            } else {
                sb.append("fcc_template");
            }
            sb.append(File.separatorChar);
            g.generateByTable(tableName, className, sb.toString(), map);
            
            //打开文件夹
            Runtime.getRuntime().exec("cmd.exe /c start "+GeneratorProperties.getRequiredProperty("outRoot"));
            
            message.setSuccess(true);
            message.setMsg(StatusCode.Sys.success);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("生成代码失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
    
    /**
     * 列表 返回json 给 easyUI 
     * @return
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "查询数据库表")
    @RequestMapping(value = "/datagrid.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    @Permissions("view")
    public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request) {
        EasyuiDataGridJson json = new EasyuiDataGridJson();
        try {
            List<Table> list = TableFactory.getInstance().getAllTables();
            int size = list.size();
            List<String> nameList = new ArrayList<String>(size);
            for (Table table : list) {
                nameList.add(table.getSqlName());
            }
            nameList.contains("");
            ListPage listPage = new ListPage();
            listPage.setCurrentPageNo(1);
            listPage.setCurrentPageSize(size);
            listPage.setDataList(nameList);
            listPage.setTotalSize(size);
            listPage.setDataSize(size);
            json.setTotal(Long.valueOf(listPage.getTotalSize()));
            json.setRows(listPage.getDataList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载查询表名数据失败！", e);
            json.setTotal(0L);
            json.setRows(Collections.EMPTY_LIST);
            json.setMsg(e.getMessage());
        }
        return json;
    }
    
}

class ColumnView {
    private String columnName;
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public String getColumnAlias() {
        return columnAlias;
    }
    public void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }
    private String columnAlias;
}
