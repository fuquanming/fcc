<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="java.util.Map"%>
<%-- 文件上传  --%>
<%@ attribute name="linkType" required="true" type="java.lang.String" description="上传附件关联类型"%>
<%@ attribute name="annexType" required="true" type="java.lang.String" description="上传附件类型"%>
<%@ attribute name="fileType" required="false" type="java.lang.String" description="上传文件类型：xls|jpg|png"%>
<%@ attribute name="maxFileSize" required="false" type="java.lang.String" description="上传文件大小：单位字节"%>
<%@ attribute name="maxFileTotal" required="false" type="java.lang.String" description="上传文件总数"%>
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
<input id="${annexType }-fileupload" type="file" name="${annexType }-upload" multiple />
<a href="javascript:void(0)" id="${annexType }-importAllFile" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">上传</span></span></a>
<a href="javascript:void(0)" id="${annexType }-cancelAllFile" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">取消</span></span></a>
<a href="javascript:void(0)" id="${annexType }-delAllFile" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">删除</span></span></a>
<a href="javascript:void(0)" id="${annexType }-clearAllFile" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">清除</span></span></a> 
<div><table id="${annexType }-fileuploadTable" style="width: 100%; border: 1px solid #DDDDDD;"></table></div>
<input type="hidden" id="${annexType }-uploadFileName" name="${annexType }-uploadFileName" value=""/>
<input type="hidden" id="${annexType }-uploadFileRealName" name="${annexType }-uploadFileRealName" value=""/>
<input type="hidden" name="linkType" value="${linkType }"/>
<input type="hidden" name="annexType" value="${annexType }"/>


<script type="text/javascript">
var ${annexType }_fileupload = {
	    fileuploadFiles : [],// 选择上传的文件列表
        fileuploadData : [],// 上传文件的对象
        currentUploadIndex : 0,// 当前上传的下标
        fileuploadAll : false,// 是否上传全部
        fileuploadName : [],// 上传成功的文件名称
        fileuploadRealName : [],// 上传成功的文件实际名称
        uploadFileNameId : '${annexType }-uploadFileName',// 保存上传文件名的ID
        uploadFileRealNameId : '${annexType }-uploadFileRealName',// 保存上传文件名的ID
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
        appendWaitImg : function(index) {
        	// 上传文件
            $('<img id="${annexType }-importWaitImg-' + index + '" src="images/wait1.gif"/>')
            .appendTo($('#${annexType }-fileuploadTdName-' + index));
        },
        removeWaitImg : function(index) {
            // 上传文件
            $('#${annexType }-importWaitImg-' + index).remove();
        },
        importFile : function(index) {// 上传文件
            if (index >= 0) {// 选择文件上传
            	this.appendWaitImg(index);
                //Tool.message.progress();
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
                    	if ($('#${annexType}-fileuploadBtn-' + i).length > 0) {
                            current_upload_index = i;
                            break;
                        }
                    }
                }
                if (current_upload_index != -1) {
                	this.appendWaitImg(current_upload_index);
                    //Tool.message.progress();
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
                $('#${annexType}-fileuploadTr-' + index).remove();
            } else {// 取消所有
                var length = this.fileuploadFiles.length;
                for (var i = 0; i < length; i++) {// 获取存在的button
                	var tempFile = this.fileuploadFiles[i]
                    if (tempFile && tempFile != null) {
                		if ($('#${annexType}-fileuploadBtn-' + i).length > 0) {
                            $('#${annexType}-fileuploadTr-' + i).remove();
                            this.fileuploadFiles[i] = null;
                        }
                	}
                }
            }
        },
        delFile : function(index) {
        	var str = '';
        	if (index >= 0) {
                var fileName = $('#${annexType }-fileuploadRealName-' + index).attr('filename');
                str = fileName;
                this.appendWaitImg(index);
        	} else {
        		var length = this.fileuploadRealName.length;
        		var fileName = [];
        		for (var i = 0; i < length; i++) {
        			var temp = this.fileuploadRealName[i];
        			if (temp && temp != null) {
        				fileName.push(temp);
        				this.appendWaitImg(i);
        			}
        		}
        		str = fileName.join('&fileName=');
        	}
        	if (str == '') return;
        	//Tool.message.progress();
        	$.ajax({
                url : Tool.urlAddParam('manage/sys/sysAnnex/delFile.do?fileName=' + str, 'random=' + Math.random()),
                cache : false,
                dataType : "json",
                success : function(d) {
                    //Tool.message.progress('close');
                    if (Tool.operate.check({'data':d,'message':false}) == true) {
                        if (index  >= 0) {
                        	$('#${annexType }-fileuploadTr-' + index).remove();
                            ${annexType }_fileupload.fileuploadName[index] = null;
                            ${annexType }_fileupload.fileuploadRealName[index] = null;
                            ${annexType }_fileupload.fileuploadFiles[index] = null;
                        } else {
                        	var length = ${annexType }_fileupload.fileuploadRealName.length;
                            for (var i = 0; i < length; i++) {
                                var temp = ${annexType }_fileupload.fileuploadRealName[i];
                                if (temp && temp != null) {
                                	$('#${annexType }-fileuploadTr-' + i).remove();
                                	${annexType }_fileupload.fileuploadName[i] = null;
                                    ${annexType }_fileupload.fileuploadRealName[i] = null;
                                    ${annexType }_fileupload.fileuploadFiles[i] = null;
                                }
                            }
                        }
                        // 构建存在的文件名表单
                        ${annexType }_fileupload.setUploadFileName();
                    }
                }
            });
        },
        clearFile : function() {// 清除数据
        	this.fileuploadFiles = [];// 选择上传的文件列表
        	this.fileuploadData = [];// 上传文件的对象
        	this.currentUploadIndex = 0;// 当前上传的下标
        	this.fileuploadAll = false;// 是否上传全部
        	this.fileuploadName = [];// 上传成功的文件名称
        	this.fileuploadRealName = [];// 上传成功的文件实际名称
        	$('#${annexType }-fileuploadTable').html('');
        	$('#${annexType }-uploadFileName').val('');
        	$('#${annexType }-uploadFileRealName').val('');
        }
}

var ${annexType }_fileuploadTable;
var ${annexType }_maxFiles = 1;
$(function() {
	<c:if test="${not empty maxFileTotal}">${annexType }_maxFiles = ${maxFileTotal}</c:if>
	${annexType }_fileuploadTable = $('#${annexType }-fileuploadTable');
	// 绑定按钮
	$('#${annexType }-importAllFile').bind('click', function() {
		${annexType }_fileupload.importFile();
	})
	$('#${annexType }-cancelAllFile').bind('click', function() {
        ${annexType }_fileupload.cancelFile();
    })
    $('#${annexType }-delAllFile').bind('click', function() {
        ${annexType }_fileupload.delFile();
    })
    $('#${annexType }-clearAllFile').bind('click', function() {
        ${annexType }_fileupload.clearFile();
    })
	
    $('#${annexType }-fileupload').attr('data-url', 'manage/sys/sysAnnex/upload.do?annexType=${annexType }');
    $('#${annexType }-fileupload').fileupload({
        dataType: 'json',
        acceptFileTypes:  /(\.|\/)(${fileType })$/i,// 文件类型
        maxFileSize: 52428800,// 50M 52428800
        autoUpload: false,// 不主动上传
        maxNumberOfFiles : ${annexType }_maxFiles,// 上传一个文件
        /* add: function (e, data) {
            //var length = data.fileInput.context.files.length;
            //var fileSize = data.fileInput.context.files[i].size;
            //var fileName = data.fileInput.context.files[i].name;
            // fileupload_data.originalFiles 文件
        }, */
        done: function (e, data) {
            //Tool.message.progress('close');
            if (Tool.operate.check({'data':data.result,'message':false}) == true) {
            	var cIndex = ${annexType }_fileupload.currentUploadIndex;
            	// 移除上传、取消按钮
            	$('#${annexType }-fileuploadBtn-' + cIndex).remove();
                $('#${annexType }-filecancelBtn-' + cIndex).remove();
                ${annexType }_fileupload.removeWaitImg(cIndex);// 移除等待图片
                // 添加删除按钮
                $('<a id="${annexType }-filedelBtn-' + cIndex + '" href="javascript:void(0)" index="' + cIndex + '" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">删除</span></span></a>')
                .appendTo($('#${annexType }-fileuploadTdBtn-' + cIndex));
                // 添加返回的文件名
                var fileName = $('#${annexType }-fileuploadTdName-' + cIndex).html();
                $('#${annexType }-fileuploadTdName-' + cIndex).html('');
                $('<a id="${annexType }-fileuploadRealName-' + cIndex + '" href="' + data.result.obj.url + '" filename="' + data.result.obj.fileRealName + '" target="_blank" >' + fileName + '</a>')
                .appendTo($('#${annexType }-fileuploadTdName-' + cIndex));
                //console.log(data.result.obj);
                ${annexType }_fileupload.fileuploadRealName[cIndex] = data.result.obj.fileRealName;
                ${annexType }_fileupload.fileuploadName[cIndex] = fileName;
                
                // 构建存在的文件名表单
                ${annexType }_fileupload.setUploadFileName();
                // 点击删除
                $('#${annexType }-filedelBtn-' + cIndex).bind('click', function() {
                	${annexType }_fileupload.delFile($(this).attr('index'));
                });
                // 批量上传
                if (${annexType }_fileupload.fileuploadAll == true) {
                	${annexType }_fileupload.importFile();
                }
            }
        }
    }).bind('fileuploadadd', function(e, data) {
    	// 判断文件类型
    	var fileName = data.files[0].name;
    	var fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    	var ${annexType}_fileType = '${fileType }';
    	if (${annexType}_fileType != '') {// 文件判断
    		${annexType}_fileType = ${annexType}_fileType.toLowerCase();
    		${annexType}_fileType_param = ${annexType}_fileType.split('|');
    		var findFlag = false;
    		for (var i in ${annexType}_fileType_param) {
    			if (${annexType}_fileType_param[i].toLowerCase() == fileType) {
    				findFlag = true;
    			}
    		}
    		if (findFlag == false) {// 文件后缀名不对
    			return;
    		}
    	}
    	
    	// 判断上传文件最大数
    	var currentFileNum = $('tr[id^="${annexType}-fileuploadTr-"]').length;
    	//console.log(${annexType }_maxFiles + ":" + currentFileNum);
        if (${annexType }_maxFiles > currentFileNum) {
        	var fileSize = data.files[0].size;
            var fileName = data.files[0].name;
            fileSize = fileSize / 1024; // Kb
            //var tableChildren = $('#fileuploadTable tbody').children();
            //var i = tableChildren.length;
            //if (i != 0) i = parseInt(tableChildren.last().attr('index')) + 1;
            var i = ${annexType }_fileupload.fileuploadFiles.length;
            $('<tr id="${annexType}-fileuploadTr-' + i + '" index="' + i + '">' +
            '<td width="45%" id="${annexType}-fileuploadTdName-' + i + '">' + fileName + '</td>' + 
            '<td width="20%">' + fileSize.toFixed(2) + 'KB</td>' + 
            '<td width="35%" id="${annexType}-fileuploadTdBtn-' + i + '"><a id="${annexType}-fileuploadBtn-' + i +'" href="javascript:void(0)" index="' + i + '" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">上传文件</span></span></a>' +
            '    <a id="${annexType}-filecancelBtn-' + i +'" href="javascript:void(0)" index="' + i + '" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">取消</span></span></a></td></tr>')
            .appendTo(${annexType }_fileuploadTable);
            ${annexType }_fileupload.fileuploadData = data;
            ${annexType }_fileupload.fileuploadFiles.push(data.files[0]);
            // 添加上传
            $('#${annexType}-fileuploadBtn-' + i).bind('click', function() {
                ${annexType }_fileupload.importFile($(this).attr('index'));
            });
            // 点击取消
            $('#${annexType}-filecancelBtn-' + i).bind('click', function() {
                ${annexType }_fileupload.cancelFile($(this).attr('index'));
            });
        } else {
        	return;
        }
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

