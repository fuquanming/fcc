<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/**
 * 初始化下拉box
 * 
 * @author 傅泉明
 */
function getComboBoxByUrl(param) {
    var queryUrl = param.queryUrl;// 查询数据
    var id = param.id;// 绑定的ID
    var closed = param.closed;// 是否关闭
    if (queryUrl.indexOf("?") == -1) {
        queryUrl = queryUrl + '?random=' + Math.random();
    } else {
        queryUrl = queryUrl + '&random=' + Math.random();
    }
    return $('#' + id).combobox({
        url : queryUrl,
        animate : false,
        lines : !Tool.isLessThanIe8(),
        checkbox : true,
        multiple : false,
        valueField : param.valueField,
        textField : param.textField,
        onLoadSuccess : function(node, data) {
            
        },
        onLoadError : function(e) {
            console.log(e)
        },
        loadFilter : function(data) {
            var flag = Tool.operate.check({'data':data});
            if (flag != true || flag != false) {
                return data;                                            
            }
            return {};
        }
    });
}
function getComboBoxByData(param) {
    var id = param.id;// 绑定的ID
    var editable = true;
    if (param.editable == false) editable = false;
    return $('#' + id).combobox({
        data : param.data,
        valueField : param.valueField,
        textField : param.textField,
        editable : editable,
        panelHeight : 'auto',
        onLoadSuccess : function(node, data) {
        	$('#' + id).combobox('setValue', param.selectValue);
        }
    });
}
</script>