<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="java.util.Map"%>
<%-- 文件上传  --%>
<%@ attribute name="linkType" required="true" type="java.lang.String" description="上传文件关联类型"%>
<%@ attribute name="fid" required="true" type="java.lang.String" description="上传附件标识ID"%>
<%@ attribute name="fileType" required="false" type="java.lang.String" description="上传文件类型：xls|jpg|png"%>
<%@ attribute name="maxFileSize" required="false" type="java.lang.String" description="上传文件大小：单位字节"%>
<%@ attribute name="maxFileTotal" required="false" type="java.lang.String" description="上传文件总数"%>
<%-- <%@ include file="/WEB-INF/head/upload_js.jsp"%> --%>
<script type="text/javascript"
    src="js/jQuery-File-Upload-9.12.6/js/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript"
    src="js/jQuery-File-Upload-9.12.6/js/jquery.fileupload.js"></script>
<script type="text/javascript"
    src="js/jQuery-File-Upload-9.12.6/js/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="js/jQuery-File-Upload-9.12.6/js/jquery.fileupload-process.js"></script>
<script type="text/javascript" src="js/jQuery-File-Upload-9.12.6/js/jquery.fileupload-validate.js"></script>
<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
<!--[if (gte IE 8)&(lt IE 10)]>
<script src="js/cors/jquery.xdr-transport.js"></script>
<![endif]-->
<!-- <script src="js/jQuery-File-Upload-9.12.6/js/jquery.fileupload-process.js"/> -->    
<%-- 导入数据 --%>
<style type="text/css">
.fileuploadTable tr {
    background-color: #F9F9F9;
}
.fileuploadTable td {
    border: 1px solid #F9F9F9;
    font-size: 15px;
}
.a-upload {
}
.bar {
    height: 18px;
    background: green;
}
</style>
<%-- accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" --%>
<input id="${fid }-fileupload" type="file" name="${fid }-upload" multiple />
<a href="javascript:void(0)" id="${fid }-importAllFile" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">上传</span></span></a>
<a href="javascript:void(0)" id="${fid }-cancelAllFile" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">取消</span></span></a>
<a href="javascript:void(0)" id="${fid }-delAllFile" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">删除</span></span></a>    
<div><table id="${fid }-fileuploadTable" style="width: 100%; border: 1px solid #DDDDDD;"></table></div>
<input type="hidden" id="${fid }-uploadFileName" name="${fid }-uploadFileName" value=""/>
<input type="hidden" id="${fid }-uploadFileRealName" name="${fid }-uploadFileRealName" value=""/>


<script type="text/javascript">
var ${fid }_fileupload = {
	    fileuploadFiles : [],// 选择上传的文件列表
        fileuploadData : [],// 上传文件的对象
        currentUploadIndex : 0,// 当前上传的下标
        fileuploadAll : false,// 是否上传全部
        fileuploadName : [],// 上传成功的文件名称
        fileuploadRealName : [],// 上传成功的文件实际名称
        uploadFileNameId : '${fid }-uploadFileName',// 保存上传文件名的ID
        uploadFileRealNameId : '${fid }-uploadFileRealName',// 保存上传文件名的ID
        setUploadFileName : function() {// 上传成功赋值文件名到表单
            // 构建存在的文件名表单
            var uploadFileRealName = [];
            for (var i in this.fileuploadRealName) {
            	var temp = this.fileuploadRealName[i];
                if (temp && temp != null) {
                    uploadFileRealName.push(temp);
                }
            }
            $('#' + this.uploadFileRealNameId).val(uploadFileRealName.join(','));
            var uploadFileName = [];
            for (var i in this.fileuploadName) {
            	var temp = this.fileuploadName[i];
                if (temp && temp != null) {
                    uploadFileName.push(temp);
                }
            }
            $('#' + this.uploadFileNameId).val(uploadFileName.join(','));
            //console.log($('#' + this.uploadFileNameId).val());
        },
        importFile : function(index) {// 上传文件
            if (index >= 0) {// 选择文件上传
                Tool.message.progress();
                this.currentUploadIndex = index;
                this.fileuploadAll = false;
                this.fileuploadData.files[0] = this.fileuploadFiles[index];
                this.fileuploadData.submit();
            } else {// 上传所有
                var current_upload_index = -1;
                var length = this.fileuploadFiles.length;
                for (var i = 0; i < length; i++) {// 获取存在的button
                    var tempFile = this.fileuploadFiles[i]
                	if (tempFile && tempFile != null) {
                    	if ($('#${fid}-fileuploadBtn-' + i).length > 0) {
                            current_upload_index = i;
                            break;
                        }
                    }
                }
                if (current_upload_index != -1) {
                    Tool.message.progress();
                    this.fileuploadAll = true;
                    this.fileuploadData.files[0] = this.fileuploadFiles[current_upload_index];
                    this.currentUploadIndex = current_upload_index;
                    this.fileuploadData.submit();
                } else {
                    this.fileuploadAll = false;
                }
            }
        },
        cancelFile : function(index) {// 取消上文件
        	if (index >= 0) {// 选择文件取消
                this.fileuploadFiles[index] = null;
                $('#${fid}-fileuploadTr-' + index).remove();
            } else {// 取消所有
                var length = this.fileuploadFiles.length;
                for (var i = 0; i < length; i++) {// 获取存在的button
                	var tempFile = this.fileuploadFiles[i]
                    if (tempFile && tempFile != null) {
                		if ($('#${fid}-fileuploadBtn-' + i).length > 0) {
                            $('#${fid}-fileuploadTr-' + i).remove();
                            this.fileuploadFiles[i] = null;
                        }
                	}
                }
            }
        },
        delFile : function(index) {
        	var str = '';
        	if (index >= 0) {
                var fileName = $('#${fid }-fileuploadRealName-' + index).attr('filename');
                str = fileName;
        	} else {
        		var length = this.fileuploadRealName.length;
        		var fileName = [];
        		for (var i = 0; i < length; i++) {
        			var temp = this.fileuploadRealName[i];
        			if (temp && temp != null) {
        				fileName.push(temp);
        			}
        		}
        		str = fileName.join('&fileName=');
        	}
        	if (str == '') return;
        	console.log(str)
        	Tool.message.progress();
        	$.ajax({
                url : Tool.urlAddParam('manage/sys/sysAnnex/delFile.do?fileName=' + str, 'random=' + Math.random()),
                cache : false,
                dataType : "json",
                success : function(d) {
                    Tool.message.progress('close');
                    if (Tool.operate.check(d, true) == true) {
                        if (index  >= 0) {
                        	$('#${fid }-fileuploadTr-' + index).remove();
                            ${fid }_fileupload.fileuploadName[index] = null;
                            ${fid }_fileupload.fileuploadRealName[index] = null;
                            ${fid }_fileupload.fileuploadFiles[index] = null;
                        } else {
                        	var length = ${fid }_fileupload.fileuploadRealName.length;
                            for (var i = 0; i < length; i++) {
                                var temp = ${fid }_fileupload.fileuploadRealName[i];
                                if (temp && temp != null) {
                                	$('#${fid }-fileuploadTr-' + i).remove();
                                	${fid }_fileupload.fileuploadName[i] = null;
                                    ${fid }_fileupload.fileuploadRealName[i] = null;
                                    ${fid }_fileupload.fileuploadFiles[i] = null;
                                }
                            }
                        }
                        // 构建存在的文件名表单
                        ${fid }_fileupload.setUploadFileName();
                    }
                }
            });
        }
}

var ${fid }_fileuploadTable;

$(function() {
	${fid }_fileuploadTable = $('#${fid }-fileuploadTable');
	// 绑定按钮
	$('#${fid }-importAllFile').bind('click', function() {
		${fid }_fileupload.importFile();
	})
	$('#${fid }-cancelAllFile').bind('click', function() {
        ${fid }_fileupload.cancelFile();
    })
    $('#${fid }-delAllFile').bind('click', function() {
        ${fid }_fileupload.delFile();
    })
	
    $('#${fid }-fileupload').attr('data-url', 'manage/sys/sysAnnex/upload.do?fid=${fid }');
    $('#${fid }-fileupload').fileupload({
        dataType: 'json',
        acceptFileTypes:  /(\.|\/)(${fileType })$/i,// 文件类型
        maxFileSize: 52428800,// 50M
        autoUpload: false,// 不主动上传
        maxNumberOfFiles : <c:if test="${empty maxFileTotal}">1</c:if><c:if test="${not empty maxFileTotal}">${maxFileTotal}</c:if>,// 上传一个文件
        /* add: function (e, data) {
            //var length = data.fileInput.context.files.length;
            //var fileSize = data.fileInput.context.files[i].size;
            //var fileName = data.fileInput.context.files[i].name;
            // fileupload_data.originalFiles 文件
        }, */
        done: function (e, data) {
            Tool.message.progress('close');
            if (Tool.operate.check(data.result, true) == true) {
            	var cIndex = ${fid }_fileupload.currentUploadIndex;
            	// 移除上传、取消按钮
            	$('#${fid }-fileuploadBtn-' + cIndex).remove();
                $('#${fid }-filecancelBtn-' + cIndex).remove();
                // 添加删除按钮
                $('<a id="${fid }-filedelBtn-' + cIndex + '" href="javascript:void(0)" index="' + cIndex + '" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">删除</span></span></a>')
                .appendTo($('#${fid }-fileuploadTdBtn-' + cIndex));
                // 添加返回的文件名
                var fileName = $('#${fid }-fileuploadTdName-' + cIndex).html();
                $('#${fid }-fileuploadTdName-' + cIndex).html('');
                $('<a id="${fid }-fileuploadRealName-' + cIndex + '" href="' + data.result.obj.url + '" filename="' + data.result.obj.fileRealName + '" target="_blank" >' + fileName + '</a>')
                .appendTo($('#${fid }-fileuploadTdName-' + cIndex));
                console.log(data.result.obj);
                ${fid }_fileupload.fileuploadRealName[cIndex] = data.result.obj.fileRealName;
                ${fid }_fileupload.fileuploadName[cIndex] = fileName;
                
                // 构建存在的文件名表单
                ${fid }_fileupload.setUploadFileName();
                // 点击删除
                $('#${fid }-filedelBtn-' + cIndex).bind('click', function() {
                	${fid }_fileupload.delFile($(this).attr('index'));
                });
                // 批量上传
                if (${fid }_fileupload.fileuploadAll == true) {
                	${fid }_fileupload.importFile();
                }
            }
        }
    }).bind('fileuploadadd', function(e, data) {
    	var fileSize = data.files[0].size;
        var fileName = data.files[0].name;
        fileSize = fileSize / 1024; // Kb
        //var tableChildren = $('#fileuploadTable tbody').children();
        //var i = tableChildren.length;
        //if (i != 0) i = parseInt(tableChildren.last().attr('index')) + 1;
        var i = ${fid }_fileupload.fileuploadFiles.length;
        $('<tr id="${fid}-fileuploadTr-' + i + '" index="' + i + '">' +
        '<td width="45%" id="${fid}-fileuploadTdName-' + i + '">' + fileName + '</td>' + 
        '<td width="20%">' + fileSize.toFixed(2) + 'KB</td>' + 
        '<td width="35%" id="${fid}-fileuploadTdBtn-' + i + '"><a id="${fid}-fileuploadBtn-' + i +'" href="javascript:void(0)" index="' + i + '" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">上传文件</span></span></a>' +
        '    <a id="${fid}-filecancelBtn-' + i +'" href="javascript:void(0)" index="' + i + '" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">取消</span></span></a></td></tr>')
        .appendTo(${fid }_fileuploadTable);
        ${fid }_fileupload.fileuploadData = data;
        ${fid }_fileupload.fileuploadFiles.push(data.files[0]);
        // 添加上传
        $('#${fid}-fileuploadBtn-' + i).bind('click', function() {
        	${fid }_fileupload.importFile($(this).attr('index'));
        });
        // 点击取消
        $('#${fid}-filecancelBtn-' + i).bind('click', function() {
            ${fid }_fileupload.cancelFile($(this).attr('index'));
        });
    });
});
/* function Fileupload() {
    this.fileuploadFiles = [];// 选择上传的文件列表
    this.fileuploadData;// 上传文件的对象
    this.currentUploadIndex = 0;// 当前上传的下标
    this.fileuploadAll = false;// 是否上传全部
    this.fileuploadRealName = [];// 上传成功的文件名称
}
// 成员变量
Fileupload.prototype.sayHello = function () {
    alert('Hello:' + this.fileuploadFiles.length);
}
// 静态方法
Fileupload.hasName = function (p) {
} */
</script>

