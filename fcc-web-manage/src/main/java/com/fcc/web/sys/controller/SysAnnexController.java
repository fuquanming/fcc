package com.fcc.web.sys.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.common.StatusCode;
import com.fcc.web.sys.config.ConfigUtil;
import com.fcc.web.sys.model.SysAnnex;
import com.fcc.web.sys.service.SysAnnexService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.coobird.thumbnailator.Thumbnails;

/**
 * <p>Description:SysAnnex</p>
 */

@Controller
@RequestMapping(value={"/manage/sys/sysAnnex"} )
public class SysAnnexController extends AppWebController {
    
    private Logger logger = Logger.getLogger(SysAnnexController.class);
    
    @Resource
    private BaseService baseService;
    //默认多列排序,example: username desc,createTime asc
    @Resource
    private SysAnnexService sysAnnexService;
    
    /** 显示列表 */
    @ApiOperation(value = "显示SysAnnex列表页面")
    @RequestMapping(value = "/view.do", method = RequestMethod.GET)
    @Permissions("view")
    public String view(HttpServletRequest request) {
        return "/manage/sys/sysAnnex/sysAnnex_list";
    }
    
    /** 上传附件 */
    @ApiOperation(value = "上传附件")
    @RequestMapping(value = "/upload.do", method = {RequestMethod.POST})
    public ModelAndView upload(HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        FileOutputStream fos = null;
        MultipartFile file = null;
        try {
            String prefix = request.getParameter("annexType");
            file = ((MultipartHttpServletRequest) request).getFile(prefix + "-upload");
            if (file != null && file.isEmpty() == false) {
                logger.info("上传文件的大小：" + file.getSize());
                logger.info("上传文件的类型：" + file.getContentType());
                logger.info("上传文件的名称：" + file.getOriginalFilename());
                logger.info("上传表单的名称：" + file.getName());
                String fileName = file.getOriginalFilename();
                File files = new File(ConfigUtil.getUploadFileTempPath());
                if (files.exists() == false) {
                    files.mkdirs();
                }
                // 文件后缀
                String fileSuffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                // 保存文件
                String saveFilePath = new StringBuilder().append(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS"))
                .append(Constants.uploadFileNameSplit).append(getSysUser().getUserId())
                .append(fileSuffix).toString();
                
                fos = new FileOutputStream(new File(files, saveFilePath));
                IOUtils.copy(file.getInputStream(), fos);
                message.setSuccess(true);
                message.setMsg(StatusCode.Sys.success);
                UploadFileItem item = new UploadFileItem();
                item.setFileRealName(saveFilePath);
                item.setUrl(new StringBuilder(ConfigUtil.getFileAccessPath()).append(Constants.uploadFileTempPath)
                        .append("/").append(saveFilePath).toString());
                message.setObj(item);
            } else {
                message.setMsg(StatusCode.Import.emptyFile);
            }
        } catch (Exception e) {
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
            logger.error("上传附件失败！", e);
            e.printStackTrace();
        } finally {
            try {
                if (file != null) IOUtils.closeQuietly(file.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            IOUtils.closeQuietly(fos);
        }
        return getModelAndView(message);
    }
    
    /** 删除上传的临时附件 */
    @ApiOperation(value = "删除上传附件")
    @RequestMapping(value = "/delFile.do", method = {RequestMethod.POST})
    public ModelAndView delFile(HttpServletRequest request) {
        Message message = new Message();
        try {
            String[] fileNames = request.getParameterValues("fileName");
            File file = new File(ConfigUtil.getUploadFileTempPath());
            if (file.exists() == false) file.mkdirs();
            for (String fileName : fileNames) {
                File files = new File(file, fileName);
                if (files.exists()) files.delete();
            }
            message.setSuccess(true);
            message.setMsg(StatusCode.Sys.success);
        } catch (Exception e) {
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
            logger.error("上传附件失败！", e);
            e.printStackTrace();
        } finally {
        }
        return getModelAndView(message);
    }
    
    /** 获取缩略图 */
    @ApiOperation(value = "获取缩略图")
    @RequestMapping(value = "/image.do", method = {RequestMethod.GET})
    public void image(
            @ApiParam(required = true, value = "图片ID") @RequestParam(name = "id", defaultValue = "") String id,
            @ApiParam(required = false, value = "图片宽度") @RequestParam(name = "width", defaultValue = "-1") int width,
            @ApiParam(required = false, value = "图片高度") @RequestParam(name = "height", defaultValue = "-1") int height,
            @ApiParam(required = false, value = "图片比例") @RequestParam(name = "scale", defaultValue = "-1") float scale,
            HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(id)) return;
        SysAnnex sysAnnex = null;
        try {
            sysAnnex = (SysAnnex) baseService.get(SysAnnex.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sysAnnex == null) return;
        String fileType = sysAnnex.getFileType();
        if ("jpg".equals(fileType) || "jpeg".equals(fileType) || "png".equals(fileType) || "gif".equals(fileType)) {
        } else {
            return;
        }
        File file = new File(sysAnnex.getFilePath());
        if (file.exists()) {
            File parentFile = file.getParentFile();
            String oldFileName = file.getName().substring(0, file.getName().lastIndexOf("."));
            if (width > 0 && height > 0) {// 输入宽度、高度
                try {
                    StringBuilder nameSb = new StringBuilder();
                    nameSb.append(oldFileName).append(Constants.uploadFileNameSplit)
                    .append(width).append("X").append(height).append(".").append(sysAnnex.getFileType());
                    String fileName = nameSb.toString();
                    File newFile = new File(parentFile, fileName);
                    if (newFile.exists() == false) {
                        Thumbnails.of(file).size(width, height).toFile(newFile);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(ConfigUtil.getFileAccessPath()).append(sysAnnex.getFileUrl()).append("/").append(fileName);
                    response.sendRedirect(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            } else if (scale > 0) {// 输入比例
                try {
                    StringBuilder nameSb = new StringBuilder();
                    nameSb.append(oldFileName).append(Constants.uploadFileNameSplit)
                    .append(scale).append(".").append(sysAnnex.getFileType());
                    String fileName = nameSb.toString();
                    File newFile = new File(parentFile, fileName);
                    if (newFile.exists() == false) {
                        Thumbnails.of(file).scale(scale).toFile(newFile);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(ConfigUtil.getFileAccessPath()).append(sysAnnex.getFileUrl()).append("/").append(fileName);
                    response.sendRedirect(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(ConfigUtil.getFileAccessPath()).append(sysAnnex.getFileUrl()).append("/").append(file.getName());
                try {
                    response.sendRedirect(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
    
    /**
     * 删除
     * @return
     */
    @ApiOperation("删除SysAnnex")
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @Permissions("delete")
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        Message message = new Message();
        String id = request.getParameter("ids");
        try {
            if (id == null || "".equals(id)) throw new RefusedException(StatusCode.Sys.emptyDeleteId);
            String[] ids = StringUtils.split(id, ",");
            sysAnnexService.delete(ids);
            message.setSuccess(true);
            message.setMsg(StatusCode.Sys.success);
        } catch (RefusedException e) {
            message.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除SysAnnex失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
    
    /**
     * 列表 返回json 给 easyUI 
     * @return
     */
    @ApiOperation(value = "查询SysAnnex")
    @RequestMapping(value = "/datagrid.do", method = RequestMethod.POST)
    @ResponseBody
    @Permissions("view")
    public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request) {
        EasyuiDataGridJson json = new EasyuiDataGridJson();
        try {
            Map<String, Object> param = getParams(request);
            ListPage listPage = sysAnnexService.queryPage(dg.getPage(), dg.getRows(), param, false);
            json.setTotal(Long.valueOf(listPage.getTotalSize()));
            json.setRows(listPage.getDataList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载查询SysAnnex数据失败！", e);
            json.setTotal(0L);
            json.setRows(new ArrayList<SysAnnex>());
            json.setMsg(e.getMessage());
        }
        return json;
    }
   
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<String, Object>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String annexId = request.getParameter("annexId");
            if (StringUtils.isNotEmpty(annexId)) {
                param.put("annexId", annexId);
            }
            String linkId = request.getParameter("linkId");
            if (StringUtils.isNotEmpty(linkId)) {
                param.put("linkId", linkId);
            }
            String linkType = request.getParameter("linkType");
            if (StringUtils.isNotEmpty(linkType)) {
                param.put("linkType", linkType);
            }
            String annexName = request.getParameter("annexName");
            if (StringUtils.isNotEmpty(annexName)) {
                param.put("annexName", annexName);
            }
            String fileName = request.getParameter("fileName");
            if (StringUtils.isNotEmpty(fileName)) {
                param.put("fileName", fileName);
            }
            String fileType = request.getParameter("fileType");
            if (StringUtils.isNotEmpty(fileType)) {
                param.put("fileType", fileType);
            }
            String fileUrl = request.getParameter("fileUrl");
            if (StringUtils.isNotEmpty(fileUrl)) {
                param.put("fileUrl", fileUrl);
            }
            String fileSize = request.getParameter("fileSize");
            if (StringUtils.isNotEmpty(fileSize)) {
                param.put("fileSize", fileSize);
            }
            String remark = request.getParameter("remark");
            if (StringUtils.isNotEmpty(remark)) {
                param.put("remark", remark);
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
            String updateUser = request.getParameter("updateUser");
            if (StringUtils.isNotEmpty(updateUser)) {
                param.put("updateUser", updateUser);
            }
            String updateTimeBegin = request.getParameter("updateTimeBegin");
            if (StringUtils.isNotEmpty(updateTimeBegin)) {
                param.put("updateTimeBegin", format.parse(updateTimeBegin + " 00:00:00"));
            }
            String updateTimeEnd = request.getParameter("updateTimeEnd");
            if (StringUtils.isNotEmpty(updateTimeEnd)) {
                param.put("updateTimeEnd", format.parse(updateTimeEnd + " 23:59:59"));
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

class UploadFileItem {
    private String fileRealName;
    private String url;
    public String getFileRealName() {
        return fileRealName;
    }
    public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
