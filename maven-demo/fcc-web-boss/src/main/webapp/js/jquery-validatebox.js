/**
 * 扩展easyUi-validatebox 验证规则
 *
 **/
$.extend($.fn.validatebox.defaults.rules, { 
	minLength: { 
		validator: function(value, param){ 
			return value.length >= param[0]; 
		}, 
		message: 'Please enter at least {0} characters.' 
	},
	telephone: {
		validator: function(value){
			var reg =/^(\d{3,4}\-)?[1-9]\d{6,7}$/;
			return reg.test(value);
		}, 
		message: 'Please enter a valid telephone.'
	},
	mobile: {
		validator: function(value){
			var reg =/^(\+\d{2,3}\-)?\d{11}$/;
			return reg.test(value) 
		}, 
		message: 'Please enter a valid mobile.'
	},
	integer: {
		validator: function(value){
			var reg =/^[-+]?\d+$/;
			return reg.test(value) 
		}, 
		message: 'Please enter a correct figures.'
	},
	string: {
		validator: function(value){
			var reg =/^[a-z,A-Z]+$/;
			return reg.test(value) 
		}, 
		message: 'Please enter a correct character.'
	}
});

if ($.fn.validatebox){
	$.fn.validatebox.defaults.rules.minLength.message = '输入内容长度最小为{0}';
	$.fn.validatebox.defaults.rules.telephone.message = '请输入有效的电话号码';
	$.fn.validatebox.defaults.rules.mobile.message = '请输入有效的手机号码';
	$.fn.validatebox.defaults.rules.integer.message = '请输入有效的数字';
	$.fn.validatebox.defaults.rules.string.message = '请输入有效的字母';	
}