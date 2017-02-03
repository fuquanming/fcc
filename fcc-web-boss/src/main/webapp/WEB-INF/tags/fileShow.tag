<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="java.util.Map"%>
<%-- 文件显示  --%>
<%@ attribute name="linkId" required="true" type="java.lang.String" description="上传附件关联ID"%>
<%@ attribute name="linkType" required="true" type="java.lang.String" description="上传附件关联类型"%>
<%@ attribute name="annexType" required="true" type="java.lang.String" description="上传附件类型"%>
<%@ attribute name="annexSize" required="false" type="java.lang.Integer" description="附件总数"%>

<style>
.fileShowButton {
    vertical-align:baseline;
}
</style>

<div id="annexDiv"></div>
<%-- <img id="userLogo" src="${sysAnnex.url }" width="60px" height="80px"/>
<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small l-btn-plain" iconcls="icon-remove" onclick="delFile();" plain="true">删除</a>
<br/> --%>

<script type="text/javascript">
var annexDiv;
var annexSize = 100;
$(function() {
	annexDiv = $('#annexDiv');
	<c:if test="${not empty annexSize}">annexSize = ${annexSize}</c:if>
	// 获取附件
	$.ajax({
        url : Tool.urlAddParam('manage/sys/sysAnnex/datagrid.do?linkId=${linkId}&linkType=${linkType}&annexType=${annexType}&rows=' + annexSize, 'random=' + Math.random()),
        cache : false,
        dataType : "json",
        success : function(d) {
            for (var i in d.rows) {
            	var data = d.rows[i];
            	var fileType = data.fileType.toLowerCase();
            	var str = '';
            	if (fileType == 'jpg' || fileType == 'jpeg' || fileType == 'bmp' || fileType == 'png'
            			|| fileType == 'gif') {
            		str = '<img src="' + data.url + '" width="60px" height="80px"/>';
            	} else {
            		str = '<a href="' + data.url + '" target="_blank" >' + d.annexName + '</a>';
            	}
            	$('<div id="${annexType }-fileShow-' + i + '" ids="' + data.annexId + '">' + str + '    <a href="javascript:void(0)" class="l-btn l-btn-small fileShowButton" iconcls="icon-remove" onclick="delFile(' + i + ');"><span class="l-btn-left"><span class="l-btn-text">删除</span></span></a><br/></div>')
            	.appendTo(annexDiv);
            }
        }
    });
	
})
function delFile(index) {
	var file = $('#${annexType }-fileShow-' + index);
    var ids = file.attr('ids');
    $.ajax({
        url : Tool.urlAddParam('manage/sys/sysAnnex/delete.do?ids=' + ids, 'random=' + Math.random()),
        cache : false,
        dataType : "json",
        success : function(data) {
        	var flag = Tool.operate.check(data, true);
        	if (flag == true) {
        		// 删除成功
        		file.remove();
        	}
        }
    });
}
</script>

