<%@ page language="java" pageEncoding="UTF-8"%>
<%-- 添加、修改数据 --%>
<script type="text/javascript">
var saveParam_form;// 提交的Form
var saveParam_saveUrl;// 保存URL地址
var saveParam_backUrl;// 保存后跳转地址
var saveParam_beforeCallback;// save前执行的方法,返回false 将不执行保存
var saveParam_afterCallback;// save后执行的方法,返回false 将不执行backUrl
function save() {
    if (saveParam_beforeCallback) {
    	if (saveParam_beforeCallback() == false) return;
    }
    var userForm = $('#' + saveParam_form).form();
    // 保存表单
    userForm.form('submit', {
        url : saveParam_saveUrl,
        success : function(data) {
            try {
                Tool.message.progress('close');
                if (Tool.operate.check(data, true)) {
                	if (saveParam_afterCallback) {
                		if (saveParam_afterCallback(data) == false) return;
                	}
                	if (saveParam_backUrl) setTimeout(function() {toBack();}, 3000);
                }
            } catch(e) {
                console.log(e)
                window.location.href = overUrl;
            }
        },
        onSubmit : function() {
            var isValid = $(this).form('validate');
            if (isValid) {
                Tool.message.progress();
            }
            return isValid;
        }
    });
}
function toBack() {
    if (saveParam_backUrl) window.location.href = saveParam_backUrl;
}
</script>