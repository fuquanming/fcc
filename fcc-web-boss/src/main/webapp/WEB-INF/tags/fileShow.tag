<%@tag import="com.fcc.web.sys.model.SysAnnex"%>
<%@tag import="java.util.HashMap"%>
<%@tag import="com.fcc.commons.web.util.SpringContextUtil"%>
<%@tag import="com.fcc.web.sys.service.SysAnnexService"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="java.util.Map"%>
<%@ tag import="java.util.List"%>
<%-- 文件显示  --%>
<%@ attribute name="linkId" required="true" type="java.lang.String" description="上传附件关联ID"%>
<%@ attribute name="linkType" required="true" type="java.lang.String" description="上传附件关联类型"%>
<%@ attribute name="annexType" required="true" type="java.lang.String" description="上传附件类型"%>
<%@ attribute name="annexSize" required="false" type="java.lang.Integer" description="附件总数"%>
<%@ attribute name="imageWidth" required="false" type="java.lang.Integer" description="图片宽度"%>
<%@ attribute name="imageHeight" required="false" type="java.lang.Integer" description="图片高度"%>
<%@ attribute name="imageScale" required="false" type="java.lang.Float" description="图片比例"%>
<%@ attribute name="deleteFlag" required="false" type="java.lang.Boolean" description="删除按钮"%>

<%
SysAnnexService sysAnnexService = SpringContextUtil.getBean(SysAnnexService.class);
Map<String, Object> param = new HashMap<String, Object>();
param.put("linkId", linkId);
param.put("linkType", linkType);
param.put("annexType", annexType);
List<SysAnnex> list = sysAnnexService.query(1, annexSize == null ? 100 : annexSize, param, false);
request.setAttribute("annexList", list);
%>
<style>
.fileShowButton {
    vertical-align:baseline;
}
</style>

<div id="annexDiv">
<%
   int dataSize = list.size();
   for (int i = 0; i < dataSize; i++) {
       SysAnnex data = (SysAnnex) list.get(i);
       %>
       <div id="${annexType }-fileShow-<%=i %>" ids="<%=data.getAnnexId()%>">
       <%
       String fileType = data.getFileType().toLowerCase();
       if ("jpg".equals(fileType) || "jpeg".equals(fileType) || "png".equals(fileType) || "bmp".equals(fileType) || "gif".equals(fileType)) {
           // 是否设定高、宽、比例 manage/sys/sysAnnex/image.do?
           if (imageHeight != null && imageWidth != null) {
               %>
               <img src="manage/sys/sysAnnex/image.do?id=<%=data.getAnnexId() %>&height=${imageHeight }&with=${imageWidth}" />
               <%
           } else if (imageScale != null) {
               %>
               <img src="manage/sys/sysAnnex/image.do?id=<%=data.getAnnexId() %>&scale=${imageScale }" />
               <%
           } else {
               %>
               <img src="<%=data.getUrl() %>" width="60px" height="80px"/>
               <%
           }
       } else {
           %>
           <a href="<%=data.getUrl() %>" target="_blank" ><%=data.getAnnexName() %></a>
           <%
       }
       %>
       <% if (deleteFlag == null || deleteFlag == true) { %>
           <a href="javascript:void(0)" class="l-btn l-btn-small fileShowButton" iconcls="icon-remove" onclick="javascript:${annexType }_file.delFile('<%=i%>')"><span class="l-btn-left"><span class="l-btn-text">删除</span></span></a><br/></div>
       <% } %>
       <%
   }
%>
<%-- <img id="userLogo" src="${sysAnnex.url }" width="60px" height="80px"/>
<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small l-btn-plain" iconcls="icon-remove" onclick="delFile();" plain="true">删除</a>
<br/> --%>
<script type="text/javascript">
var ${annexType }_file = {
        delFile : function(index) {
            var file = $('#${annexType }-fileShow-' + index);
            var ids = file.attr('ids');
            Tool.message.progress();
            $.ajax({
                url : Tool.urlAddParam('manage/sys/sysAnnex/delete.do?ids=' + ids, 'random=' + Math.random()),
                cache : false,
                dataType : "json",
                success : function(data) {
                    Tool.message.progress('close');
                    var flag = Tool.operate.check(data, true);
                    if (flag == true) {
                        // 删除成功
                        file.remove();
                    }
                }
            });
        }
}
</script>