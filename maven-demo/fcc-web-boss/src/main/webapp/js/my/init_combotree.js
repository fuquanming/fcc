/**
 * 初始化下拉tree
 * 
 * @author 傅泉明
 */
function getComboTree(param) {
	var queryUrl = param.queryUrl;// 查询数据
	var id = param.id;// 绑定的ID
	var closed = param.closed;// 是否关闭
	if (queryUrl.indexOf("?") == -1) {
		queryUrl = queryUrl + '?random=' + Math.random();
	} else {
		queryUrl = queryUrl + '&random=' + Math.random();
	}
	return $('#' + id).combotree({
        url : queryUrl,
        animate : false,
        lines : !Tool.isLessThanIe8(),
        checkbox : true,
        multiple : false,
        onLoadSuccess : function(node, data) {
            var t = $(this);
            if (data) {
            	if (closed == false) {
            		t.tree('expandAll');
            	} else if (closed == true) {
            		t.tree('collapseAll');
            	}
            }
        },
        onLoadError : function(e) {
        	console.log(e)
            window.location.href = overUrl;
        },
        loadFilter : function(data) {
            var flag = Tool.operate.check(data);
            if (flag != true || flag != false) {
                return data;                                            
            }
        }
    });
}