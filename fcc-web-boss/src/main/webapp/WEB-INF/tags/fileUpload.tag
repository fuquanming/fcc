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
<input id="${linkType }-${annexType }-fileupload" type="file" name="${linkType }-${annexType }-upload" multiple />
<a href="javascript:void(0)" id="${linkType }-${annexType }-importAllFile" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">上传</span></span></a>
<a href="javascript:void(0)" id="${linkType }-${annexType }-cancelAllFile" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">取消</span></span></a>
<a href="javascript:void(0)" id="${linkType }-${annexType }-delAllFile" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">删除</span></span></a>
<a href="javascript:void(0)" id="${linkType }-${annexType }-clearAllFile" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">清除</span></span></a> 
<div><table id="${linkType }-${annexType }-fileuploadTable" style="width: 100%; border: 1px solid #DDDDDD;"></table></div>
<input type="hidden" id="${linkType }-${annexType }-uploadFileName" name="${linkType }-${annexType }-uploadFileName" value=""/>
<input type="hidden" id="${linkType }-${annexType }-uploadFileRealName" name="${linkType }-${annexType }-uploadFileRealName" value=""/>
<input type="hidden" name="linkType" value="${linkType }"/>
<input type="hidden" name="annexType" value="${annexType }"/>
<%-- 标识当前上传哪一个linkType、annexType --%>
<input type="hidden" id="${linkType }-${annexType }" name="${linkType }-${annexType }" value=""/>

<script type="text/javascript">
var ${linkType }_${annexType }_fileupload = {
        fileuploadFiles : [],// 选择上传的文件列表
        fileuploadData : [],// 上传文件的对象
        currentUploadIndex : 0,// 当前上传的下标
        fileuploadAll : false,// 是否上传全部
        fileuploadName : [],// 上传成功的文件名称
        fileuploadRealName : [],// 上传成功的文件实际名称
        uploadFileNameId : '${linkType }-${annexType }-uploadFileName',// 保存上传文件名的ID
        uploadFileRealNameId : '${linkType }-${annexType }-uploadFileRealName',// 保存上传文件名的ID
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
            $('<img id="${linkType }-${annexType }-importWaitImg-' + index + '" src="images/wait1.gif"/>')
            .appendTo($('#${linkType }-${annexType }-fileuploadTdName-' + index));
        },
        removeWaitImg : function(index) {
            // 上传文件
            $('#${linkType }-${annexType }-importWaitImg-' + index).remove();
        },
        importFile : function(index) {// 上传文件
            if (index >= 0) {// 选择文件上传
                this.appendWaitImg(index);
                //Tool.message.progress();
                this.currentUploadIndex = index;
                this.fileuploadAll = false;
                this.fileuploadData.files[0] = this.fileuploadFiles[index];
                // 标识上传哪个
                $('#${linkType }-${annexType }').val('upload');
                this.fileuploadData.submit();
            } else {// 上传所有
                var current_upload_index = -1;
                var length = this.fileuploadFiles.length;
                for (var i = 0; i < length; i++) {// 获取存在的button
                    var tempFile = this.fileuploadFiles[i]
                    if (tempFile && tempFile != null) {
                        if ($('#${linkType }-${annexType}-fileuploadBtn-' + i).length > 0) {
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
                    // 标识上传哪个
                    $('#${linkType }-${annexType }').val('upload');
                    this.fileuploadData.submit();
                } else {
                    this.fileuploadAll = false;
                }
            }
        },
        cancelFile : function(index) {// 取消上文件
            if (index >= 0) {// 选择文件取消
                this.fileuploadFiles[index] = null;
                $('#${linkType }-${annexType}-fileuploadTr-' + index).remove();
            } else {// 取消所有
                var length = this.fileuploadFiles.length;
                for (var i = 0; i < length; i++) {// 获取存在的button
                    var tempFile = this.fileuploadFiles[i]
                    if (tempFile && tempFile != null) {
                        if ($('#${linkType }-${annexType}-fileuploadBtn-' + i).length > 0) {
                            $('#${linkType }-${annexType}-fileuploadTr-' + i).remove();
                            this.fileuploadFiles[i] = null;
                        }
                    }
                }
            }
        },
        delFile : function(index) {
            var str = '';
            if (index >= 0) {
                var fileName = $('#${linkType }-${annexType }-fileuploadRealName-' + index).attr('filename');
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
                            $('#${linkType }-${annexType }-fileuploadTr-' + index).remove();
                            ${linkType }_${annexType }_fileupload.fileuploadName[index] = null;
                            ${linkType }_${annexType }_fileupload.fileuploadRealName[index] = null;
                            ${linkType }_${annexType }_fileupload.fileuploadFiles[index] = null;
                        } else {
                            var length = ${linkType }_${annexType }_fileupload.fileuploadRealName.length;
                            for (var i = 0; i < length; i++) {
                                var temp = ${linkType }_${annexType }_fileupload.fileuploadRealName[i];
                                if (temp && temp != null) {
                                    $('#${linkType }-${annexType }-fileuploadTr-' + i).remove();
                                    ${linkType }_${annexType }_fileupload.fileuploadName[i] = null;
                                    ${linkType }_${annexType }_fileupload.fileuploadRealName[i] = null;
                                    ${linkType }_${annexType }_fileupload.fileuploadFiles[i] = null;
                                }
                            }
                        }
                        // 构建存在的文件名表单
                        ${linkType }_${annexType }_fileupload.setUploadFileName();
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
            $('#${linkType }-${annexType }-fileuploadTable').html('');
            $('#${linkType }-${annexType }-uploadFileName').val('');
            $('#${linkType }-${annexType }-uploadFileRealName').val('');
        }
}

var ${linkType }_${annexType }_fileuploadTable;
var ${linkType }_${annexType }_maxFiles = 1;
$(function() {
    <c:if test="${not empty maxFileTotal}">${linkType }_${annexType }_maxFiles = ${maxFileTotal}</c:if>
    ${linkType }_${annexType }_fileuploadTable = $('#${linkType }-${annexType }-fileuploadTable');
    // 绑定按钮
    $('#${linkType }-${annexType }-importAllFile').bind('click', function() {
        ${linkType }_${annexType }_fileupload.importFile();
    })
    $('#${linkType }-${annexType }-cancelAllFile').bind('click', function() {
        ${linkType }_${annexType }_fileupload.cancelFile();
    })
    $('#${linkType }-${annexType }-delAllFile').bind('click', function() {
        ${linkType }_${annexType }_fileupload.delFile();
    })
    $('#${linkType }-${annexType }-clearAllFile').bind('click', function() {
        ${linkType }_${annexType }_fileupload.clearFile();
    })
    
    $('#${linkType }-${annexType }-fileupload').attr('data-url', 'manage/sys/sysAnnex/upload.do?annexType=${annexType }');
    $('#${linkType }-${annexType }-fileupload').fileupload({
        dataType: 'json',
        acceptFileTypes:  /(\.|\/)(${fileType })$/i,// 文件类型
        maxFileSize: 52428800,// 50M 52428800
        autoUpload: false,// 不主动上传
        maxNumberOfFiles : ${linkType }_${annexType }_maxFiles,// 上传一个文件
        /* add: function (e, data) {
            //var length = data.fileInput.context.files.length;
            //var fileSize = data.fileInput.context.files[i].size;
            //var fileName = data.fileInput.context.files[i].name;
            // fileupload_data.originalFiles 文件
        }, */
        done: function (e, data) {
            //Tool.message.progress('close');
            var cIndex = ${linkType }_${annexType }_fileupload.currentUploadIndex;
            ${linkType }_${annexType }_fileupload.removeWaitImg(cIndex);// 移除等待图片
            // 标识上传哪个,清空
            $('#${linkType }-${annexType }').val('');
            
            if (Tool.operate.check({'data':data.result,'message':false}) == true) {
                // 移除上传、取消按钮
                $('#${linkType }-${annexType }-fileuploadBtn-' + cIndex).remove();
                $('#${linkType }-${annexType }-filecancelBtn-' + cIndex).remove();
                // 添加删除按钮
                $('<a id="${linkType }-${annexType }-filedelBtn-' + cIndex + '" href="javascript:void(0)" index="' + cIndex + '" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">删除</span></span></a>')
                .appendTo($('#${linkType }-${annexType }-fileuploadTdBtn-' + cIndex));
                // 添加返回的文件名
                var fileName = $('#${linkType }-${annexType }-fileuploadTdName-' + cIndex).html();
                $('#${linkType }-${annexType }-fileuploadTdName-' + cIndex).html('');
                $('<a id="${linkType }-${annexType }-fileuploadRealName-' + cIndex + '" href="' + data.result.obj.url + '" filename="' + data.result.obj.fileRealName + '" target="_blank" >' + fileName + '</a>')
                .appendTo($('#${linkType }-${annexType }-fileuploadTdName-' + cIndex));
                //console.log(data.result.obj);
                ${linkType }_${annexType }_fileupload.fileuploadRealName[cIndex] = data.result.obj.fileRealName;
                ${linkType }_${annexType }_fileupload.fileuploadName[cIndex] = fileName;
                
                // 构建存在的文件名表单
                ${linkType }_${annexType }_fileupload.setUploadFileName();
                // 点击删除
                $('#${linkType }-${annexType }-filedelBtn-' + cIndex).bind('click', function() {
                    ${linkType }_${annexType }_fileupload.delFile($(this).attr('index'));
                });
                // 批量上传
                if (${linkType }_${annexType }_fileupload.fileuploadAll == true) {
                    ${linkType }_${annexType }_fileupload.importFile();
                }
            }
        }
    }).bind('fileuploadadd', function(e, data) {
        // 判断文件类型
        var fileName = data.files[0].name;
        var fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        var ${linkType }_${annexType}_fileType = '${fileType }';
        if (${linkType }_${annexType}_fileType != '') {// 文件判断
            ${linkType }_${annexType}_fileType = ${linkType }_${annexType}_fileType.toLowerCase();
            ${linkType }_${annexType}_fileType_param = ${linkType }_${annexType}_fileType.split('|');
            var findFlag = false;
            for (var i in ${linkType }_${annexType}_fileType_param) {
                if (${linkType }_${annexType}_fileType_param[i].toLowerCase() == fileType) {
                    findFlag = true;
                }
            }
            if (findFlag == false) {// 文件后缀名不对
                return;
            }
        }
        
        // 判断上传文件最大数
        var currentFileNum = $('tr[id^="${linkType }-${annexType}-fileuploadTr-"]').length;
        //console.log(${annexType }_maxFiles + ":" + currentFileNum);
        if (${linkType }_${annexType }_maxFiles > currentFileNum) {
            var fileSize = data.files[0].size;
            var fileName = data.files[0].name;
            fileSize = fileSize / 1024; // Kb
            //var tableChildren = $('#fileuploadTable tbody').children();
            //var i = tableChildren.length;
            //if (i != 0) i = parseInt(tableChildren.last().attr('index')) + 1;
            var i = ${linkType }_${annexType }_fileupload.fileuploadFiles.length;
            $('<tr id="${linkType }-${annexType}-fileuploadTr-' + i + '" index="' + i + '">' +
            '<td width="45%" id="${linkType }-${annexType}-fileuploadTdName-' + i + '">' + fileName + '</td>' + 
            '<td width="20%">' + fileSize.toFixed(2) + 'KB</td>' + 
            '<td width="35%" id="${linkType }-${annexType}-fileuploadTdBtn-' + i + '"><a id="${linkType }-${annexType}-fileuploadBtn-' + i +'" href="javascript:void(0)" index="' + i + '" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">上传文件</span></span></a>' +
            '    <a id="${linkType }-${annexType}-filecancelBtn-' + i +'" href="javascript:void(0)" index="' + i + '" class="l-btn l-btn-small"><span class="l-btn-left"><span class="l-btn-text">取消</span></span></a></td></tr>')
            .appendTo(${linkType }_${annexType }_fileuploadTable);
            ${linkType }_${annexType }_fileupload.fileuploadData = data;
            ${linkType }_${annexType }_fileupload.fileuploadFiles.push(data.files[0]);
            // 添加上传
            $('#${linkType }-${annexType}-fileuploadBtn-' + i).bind('click', function() {
                ${linkType }_${annexType }_fileupload.importFile($(this).attr('index'));
            });
            // 点击取消
            $('#${linkType }-${annexType}-filecancelBtn-' + i).bind('click', function() {
                ${linkType }_${annexType }_fileupload.cancelFile($(this).attr('index'));
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

