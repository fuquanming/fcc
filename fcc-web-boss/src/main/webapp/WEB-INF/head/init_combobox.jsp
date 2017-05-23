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
    var editable = true;// 是否可以编辑
    if (param.editable == false) editable = false;
    var multiple = false;// 是否多选
    if (param.multiple == true) multiple = true;
    var async = true;// 默认异步请求
    if (param.async == false) async = false;
    if (async == true) {// 异步
        return $('#' + id).combobox({
            url : queryUrl,
            animate : false,
            lines : !Tool.isLessThanIe8(),
            checkbox : true,
            multiple : multiple,
            valueField : param.valueField,
            textField : param.textField,
            editable : editable,
            onLoadSuccess : function(node, data) {
                if (param.selectValue) $('#' + id).combobox('setValue', param.selectValue);
            },
            onLoadError : function(e) {
                console.log(e)
            },
            loadFilter : function(data) {
                var flag = Tool.operate.check({'data':data});
                if (flag != true || flag != false) {
                    // 是否添加--请选择---
                    if (param.selectFlag) {
                        var length = data.length;
                        for (var i = length; i > 0; i--) {
                            data[i] = data[i - 1];
                        }
                        data[0] = {'id':'', 'text':'--请选择--'}
                    }
                    if (param.getData) param.getData(data);
                    return data;                                            
                }
                return {};
            }
        });
    } else {// 同步
        $.ajax({
            type: "POST",
            async : false,
            url: queryUrl,
            dataType: "json",
            success: function(data){
                var flag = Tool.operate.check({'data':data});
                if (flag != true || flag != false) {
                    getComboBoxByData({
                        id : param.id,
                        editable : param.editable,
                        multiple : param.multiple,
                        valueField : param.valueField,
                        textField : param.textField,
                        selectValue : param.selectValue,
                        selectFlag : param.selectFlag,
                        getData : param.getData,
                        data : data
                    });
                }
            }
        });
    }
}
function getComboBoxByData(param) {
    var id = param.id;// 绑定的ID
    var editable = true;
    if (param.editable == false) editable = false;
    var multiple = false;// 是否多选
    if (param.multiple == true) multiple = true;
    return $('#' + id).combobox({
        data : param.data,
        valueField : param.valueField,
        textField : param.textField,
        editable : editable,
        checkbox : true,
        multiple : multiple,
        //panelHeight : 'auto',
        onLoadSuccess : function(node, data) {
            if (param.selectValue) $('#' + id).combobox('setValue', param.selectValue);
        },
        loadFilter : function(data) {
            // 是否添加--请选择---
            if (param.selectFlag) {
                var length = data.length;
                for (var i = length; i > 0; i--) {
                    data[i] = data[i - 1];
                }
                data[0] = {'id':'', 'text':'--请选择--'}
            }
            if (param.getData) param.getData(data);
            return data;
        }
    });
}
/** 通过ID获取comboBox的文本，传入data原数据，匹配的id */
function getComboBoxText(data, id) {
    for (var d in data) {
        if (data[d].id == id) {
            return data[d].text; 
        }
    }
    return '';
}
</script>