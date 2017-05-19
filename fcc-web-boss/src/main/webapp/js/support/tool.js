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
 * select 下拉框复制
 * Tool.copySelect({
 *		'sourceId':'sourceId',
 *		'targetId':'sourceId',
 *		'isAll':true,
 *      'callbackFun':''
 * })
 *
 */
Tool.copySelect = function(params) {
	if (params.sourceId) {
		if (params.callbackFun) str = new Array();
		if (params.isAll == true) {
			if (params.targetId) {
				$('#' + params.sourceId).children().each(function(){
					Tool.addOption({
						'sourceId' : params.targetId,
						'text' : $(this).html(),
						'val' : $(this).val()
					})
				});	
			}
		} else {
			if (params.targetId) {
				$('#' + params.sourceId).children().each(function(){
					if ($(this).attr('selected')) {
						Tool.addOption({
							'sourceId' : params.targetId,
							'text' : $(this).html(),
							'val' : $(this).val()
						})
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
		var flag = false;
		$('#' + params.sourceId).children().each(function(){
			if ($(this).val() == params.val) {
				flag = true;
				return;
			}
		})
		if (flag == false) $('#' + params.sourceId).append('<option value="' + params.val + '">' + params.text + '</option>');
	}
}
/**
 * Tool.removeOperate({
 *		'sourceId':'sourceId',
 *		'isAll':true,
 */
Tool.removeOperate = function(params) {
	if (params.sourceId) {
		if (params.isAll == true) {
			$('#' + params.sourceId).html('');
		} else {
			$('#' + params.sourceId).children().each(function(){
				if ($(this).attr('selected')) {
					$(this).remove();
				}
			})
		}
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
//支持easyui取值
Tool.getValue = function(id) {
	return $("#" + id).textbox('getValue');
}
// 支持easyui赋值
Tool.setValue = function(id, value) {
	$("#" + id).textbox('setValue', value);
}

Tool.goPage = function(page) {
	window.location.href = page;
}
Tool.goNewPage = function(page) {
	window.location.href = page;
}
/** url添加参数 */
Tool.urlAddParam = function(url, param) {
	if (url.indexOf("?") == -1) {
		url = url + "?";
	} else {
		url = url + "&";
	}
	return url + param;
}
/** 控制iput easyui */
Tool.input = {}
/** 设置输入框为禁用 */
Tool.input.disabled = function(id, flag) {
	var disabled = (flag == true) ? 'disabled' : '';
	if (disabled == '') {
		$('#' + id).textbox('textbox').removeAttr('disabled');
	} else {
		$('#' + id).textbox('textbox').attr('disabled', 'disabled');
	}
}

Tool.icon = {}
Tool.icon.info = "info";
Tool.icon.error = "error";
Tool.icon.question = "question";
Tool.icon.warning = "warning";

Tool.grid = {}
Tool.grid.data = 'datagrid';
Tool.grid.tree = 'treegrid';
/** 消息提示 */
Tool.message = function() {}
/** 显示进度条 */
Tool.message.progress = function(param) {
	if ('close' == param) {
		$.messager.progress('close');
		return;
	}
	var info;
	if (param) info = param.text;
	if (!info) info = "数据处理中，请稍后....";
	$.messager.progress({text : info});
}
/** 增、删、改后显示信息 */
Tool.message.show = function(param) {
	$.messager.show({
	    msg : param.msg,
	    title : param.title
	});
}
var interval_alert;
/** 显示提示框 */
Tool.message.alert = function(title, msg, icon, autoClose, fn) {
	// icon:error、question、info、warning
	clearInterval(interval_alert);
	$.messager.alert(title, msg, icon, fn);
	//var interval;  
	var time = 1000;
	var x = 3;    //设置时间3s
	if (autoClose == true) {
		interval_alert = setInterval(fun, time);
		var buttonStr = '';
		try {
			buttonStr = $(".dialog-button span span").html();
			$(".dialog-button span span").html(buttonStr + '(' + x + ')')
		} catch (e) {
			console.log(e);
		}
		function fun() {  
			--x;
			try {
				$(".dialog-button span span").html(buttonStr + '(' + x + ')')
			} catch (e) {console.log(e)}
			if(x == 0) {  
		       clearInterval(interval_alert);
		       $(".dialog-button span span").html(buttonStr);
		       $(".messager-body").window('close');
		    }  
		};
	}
}
/** 显示确认消息窗口 */
Tool.message.confirm = function(title, msg, fn) {
	$.messager.confirm(title, msg, fn);
}

Tool.operate = function() {}
/** session检查、操作权限检查 
 *  param={'data':data,'message':true,'close':true}
 *  data=json数据，
 *  message=成功是否提示，默认true
 *  close=提示信息自动关闭时间，默认true，false不关闭
 *  data, autoClose
 **/
Tool.operate.check = function(param) {
	var data = param.data;
	console.log(param);
	var d = $.parseJSON(data);
	if (!d) {// 转化成功为json对象、form提交返回需要转化
		d = data;// 已经转化过
	}
	var msg = StatusCode.msg(d.msg);
	if (d.success == true) {
		if (param.message != false) param.message = true;
		if (param.message == true) {
			if (param.close != false) param.close = true;
			Tool.message.alert(Lang.tip, msg, Tool.icon.info, param.close);
		}
    	return true;
    } else if (d.success == false) {
        if (d.msg == 'sys_004') {
        	window.location.href = overUrl;
        	return false;
        } else if (d.msg == 'sys_005') {
        	Tool.message.alert(Lang.tip, msg.format(d.obj), Tool.icon.error);
        	return false;
        } else if (d.obj) {
        	msg = d.obj;
        }
        Tool.message.alert(Lang.tip, msg, Tool.icon.error);
        return false;
    }
}