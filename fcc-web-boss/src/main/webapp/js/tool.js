/**
 * 工具js
 * 依赖jquery.js
 * @author 傅泉明
 */
var Tool = $.extend({}, Tool);/* 全局对象 */
/**
 * select 下拉框移除
 * Tool.removeSelect({
 *		'sourceId':'sourceId',
 *		'targetId':'sourceId',
 *		'isAll':true,
 *      'callbackFun':''
 * })
 *
 */
Tool.removeSelect = function(params) {
	if (params.sourceId) {
		var str = null;
		if (params.callbackFun) str = new Array();
		if (params.isAll == true) {
			if (params.targetId) {
				$('#' + params.sourceId).children().each(function(){
					$('#' + params.targetId).append($(this));
				});	
			} else {
				$('#' + params.sourceId).children().each(function(){
					$(this).remove();
				});
			}
		} else {
			if (params.targetId) {
				$('#' + params.sourceId).children().each(function(){
					if ($(this).attr('selected')) {
						if (str) str.push($(this))
						$('#' + params.targetId).append($(this));
					}
				});
			} else {
				$('#' + params.sourceId).children().each(function(){
					if ($(this).attr('selected')) {
						$(this).remove();
					}
				});
			}
		}
		if (params.callbackFun) params.callbackFun(str);
	}
}
/**
 * 为select 下拉框添加选项
 * Tool.addOption({
 *		'sourceId':'sourceId',
 *		'text':'text',
 *		'val':'val'
 * })
 *
 */
Tool.addOption = function(params) {
	if (params.sourceId) {
		$('#' + params.sourceId).append('<option value="' + params.val + '">' + params.text + '</option>');
	}
}

Tool.dateFormat = function(params) {
	var value = params.value;
	if (value) {
		var format = params.format;
		if (format) {
			var date = new Date(value);
			var month = date.getMonth() + 1;
			var day = date.getDate();
			var hour = date.getHours();
			var minute = date.getMinutes();
			var second = date.getSeconds();
           	return date.getFullYear() + '-' + (month < 10 ? "0" : "") + month + '-' + (day < 10 ? "0" : "") + day 
           	+ " " + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "") + second;
		} else if (params.interval) {
			var totalSecond = parseInt(value) / 1000;
			var day = parseInt(totalSecond / 86400);
			var hour = parseInt(totalSecond / 3600 - day * 24);
			var minute = parseInt(totalSecond / 60 - (hour * 60 + day * 1440));
			var second = parseInt(totalSecond - (minute * 60 + hour * 3600 + day * 86400));
			return day + '天' + hour + '时' + minute + '分' + second + '秒';
		}
	}
	return '';
}

/**
 * 生成UUID
 */
Tool.random4 = function() {
	return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
};
Tool.UUID = function() {
	return (sy.random4() + sy.random4() + "-" + sy.random4() + "-" + sy.random4() + "-" + sy.random4() + "-" + sy.random4() + sy.random4() + sy.random4());
};

Tool.isLessThanIe8 = function() {/* 判断浏览器是否是IE并且版本小于8 */
	return ($.browser.msie && $.browser.version < 8);
};
/** 检测Flash */
Tool.isFlash = function() {
	var iFlash = null;
    var version = null;
    var isIE = navigator.userAgent.toLowerCase().indexOf("msie") != -1
    if(isIE){
        //for IE
        if (window.ActiveXObject) {
            var control = null;
            try {
                control = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
            } catch (e) {
                iFlash = false;
            }
            if (control) {
                iFlash = true;
                version = control.GetVariable('$version').substring(4);
                version = version.split(',');
                version = parseFloat(version[0] + '.' + version[1]);
                //alert("FLASH版本号："+version)
            }
        }
    }else{
        //for other
        if (navigator.plugins) {
          for (var i=0; i < navigator.plugins.length; i++) {
            if (navigator.plugins[i].name.toLowerCase().indexOf("shockwave flash") >= 0) {
              iFlash = true;
              version = navigator.plugins[i].description.substring(navigator.plugins[i].description.toLowerCase().lastIndexOf("Flash ") + 6, navigator.plugins[i].description.length);
            }
          }
        }
    }
    return iFlash;
}
Tool.message = function() {}
/** 显示进度条 */
Tool.message.progress = function(param) {
	var info = param.text;
	if (info) {
	} else {
		info = "数据处理中，请稍后....";
	}
	$.messager.progress({text : info});
}
/** 关闭进度条 */
Tool.message.progressClose = function(param) {
	$.messager.progress('close');
}
/** 增、删、改后显示信息 */
Tool.message.show = function(param) {
	$.messager.show({
	    msg : param.msg,
	    title : param.title
	});
}
/** 显示提示框 */
Tool.message.alert = function(param) {
	// icon:error、question、info、warning
	$.messager.alert(param.title, param.msg, param.icon, param.fn);
}
/** 显示确认消息窗口 */
Tool.message.confirm = function(title, msg, fn) {
	$.messager.confirm(title, msg, fn);
}

