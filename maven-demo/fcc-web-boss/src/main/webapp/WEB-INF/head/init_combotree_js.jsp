<%@ page language="java" pageEncoding="UTF-8"%>
<%-- 添加、修改数据 --%>
<script type="text/javascript">
var saveParam = {}
saveParam.saveUrl;// 保存URL地址
saveParam.toBack;// 是否保存成功后跳转 true、false
saveParam.backUrl;// 跳转地址
saveParam.beforeSaveFun;// save前执行的方法
saveParam.afterSaveFun;// save后执行的方法

var userForm;
$(function() {
    userForm = $('#userForm').form();
});

function save() {
    if (saveParam.beforeSaveFun) {
        saveParam.beforeSaveFun();
    }
    // 获取模块操作
    userForm.form('submit', {
        url : saveParam.saveUrl,
        success : function(data) {
            try {
                Tool.message.progress('close');
                if (Tool.operate.check(data, true)) {
                    if (saveParam.toBack == true) setTimeout(function() {toBack();}, 3000);
                    if (saveParam.afterSaveFun) saveParam.afterSaveFun();
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
    if (saveParam.backUrl) window.location.href = saveParam.backUrl;
}
</script>