/**
 *	工具js
 *	依赖jquery.js
 *	@author 傅泉明
 */
/**
 *	文本格式化
  	var str =  hello {NAME} ,welcome to {CITY};
  	str.format({NAME:'tom',CITY:'厦门'});
	var str = hello {0} ,welcome to {1}
	str.format('tom','厦门');
 */
String.prototype.format = function(args) {
	if (arguments.length > 0) {
		var result = this;
		if (arguments.length == 1 && typeof (args) == "object") {
			for (var key in args) {
				var reg = new RegExp("({" + key + "})", "g");
				result = result.replace(reg, args[key]);
			}
		} else {
			for (var i = 0; i < arguments.length; i++) {
				if (arguments[i] == undefined) {
					return "";
				} else {
					var reg = new RegExp("({[" + i + "]})", "g");
					result = result.replace(reg, arguments[i]);
				}
			}
		}
		return result;
	} else {
		return this;
	}
};