/**
 * 初始化tree
 * 
 * @author 傅泉明
 */
function getTree(param) {
	var queryUrl = param.queryUrl;// 查询数据
	var id = param.id;// 绑定的ID
	var closed = param.closed;// 是否关闭
	if (queryUrl.indexOf("?") == -1) {
		queryUrl = queryUrl + '?random=' + Math.random();
	} else {
		queryUrl = queryUrl + '&random=' + Math.random();
	}
	return $('#' + id).tree({
        checkbox: true,
        multiple : false,
        url : queryUrl,
        animate : true,
        lines : !Tool.isLessThanIe8(),
        onClick : function(node) {
        },
        onLoadSuccess : function(node, data) {
            var t = $(this);
            if (data) {
            	if (closed == false) {
            		//console.log('open');
            		//t.tree('expandAll');
            	} else if (closed == true) {
            		//console.log('closed');
            		//t.tree('collapseAll');
            	}
            }
            if (data[0] && data[0].msg && data[0].msg != '') {
                Tool.message.alert(Lang.tip, data[0].msg, Tool.icon.error); 
            }
        },
        onLoadError : function(e) {
        	console.log(e)
            window.location.href = overUrl;
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