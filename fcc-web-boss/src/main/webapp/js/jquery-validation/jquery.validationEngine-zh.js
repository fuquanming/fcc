

(function($) {
	$.fn.validationEngineLanguage = function() {};
	$.validationEngineLanguage = {
		newLang: function() {
			//  电话号码/^0\d{2,3}[1-9]\d{6,7}$/
			$.validationEngineLanguage.allRules = 	{"required":{    			// Add your regex rules here, you can take telephone as an example
						"regex":"none",
						"alertText":"* 此字段是必填",
						"alertTextCheckboxMultiple":"* 请选择一项",
						"alertTextCheckboxe":"* 这复选框是必填"},
					"length":{
						"regex":"none",
						"alertText":"* 字符长度在 ",
						"alertText2":" 和 ",
						"alertText3": " 之间"},
					"maxCheckbox":{
						"regex":"none",
						"alertText":"* Checks allowed Exceeded"},	
					"minCheckbox":{
						"regex":"none",
						"alertText":"* Please select ",
						"alertText2":" options"},	
					"confirm":{
						"regex":"none",
						"alertText":"* Your field is not matching"},		
					"telephone":{
						"regex":"/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/",
						"alertText":"* 电话号码无效"},	
					"mobile":{
						"regex":"/^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/",
						"alertText":"* 手机号码无效"},
					"fax":{
						"regex":"/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/",
						"alertText":"* 传真号码无效"},	
					"email":{
						"regex":"/^[a-zA-Z0-9_\.\-]+\@([a-zA-Z0-9\-]+\.)+[a-zA-Z0-9]{2,4}$/",
						"alertText":"* 电子邮件地址无效"},	
					"url":{
						"regex":"/[a-zA-z]+://[^\s]*/",
						"alertText":"* url地址无效"},
					"chinese":{
						"regex":"/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/",
						"alertText":"* 只能是中文"},
					"date":{
                         "regex":"/^[0-9]{4}\-\[0-9]{1,2}\-\[0-9]{1,2}$/",
                         "alertText":"* 无效的日期，必须为YYYY - MM - DD格式"},
                         // /^[-\+]?\d+\.?\d+?$/
                    "onlyDouble":{
						"regex":"/^[-\+]?[0-9]+\.?[0-9]+?$/",
						"alertText":"* 只能是实数"},
					"onlyNumber":{
						"regex":"/^[0-9]+$/",
						"alertText":"* 只能是数字"},	
					"noSpecialCaracters":{
						"regex":"/^[0-9a-zA-Z]+$/",
						"alertText":"* 只能是字母和数字"},	
					"ajaxUser":{
						"file":"validateUser.php",
						"extraData":"name=eric",
						"alertTextOk":"* This user is available",	
						"alertTextLoad":"* Loading, please wait",
						"alertText":"* This user is already taken"},	
					"ajaxName":{
						"file":"validateUser.php",
						"alertText":"* This name is already taken",
						"alertTextOk":"* This name is available",	
						"alertTextLoad":"* Loading, please wait"},		
					"onlyLetter":{
						"regex":"/^[a-zA-Z\ \']+$/",
						"alertText":"* 只能是字母"},
					"IdCard":{
						"regex":"/^([0-9]{15}|[0-9]{18})$/",
						"alertText":"* 身份证不正确"}
					}
					
		}
	}
})(jQuery);

$(document).ready(function() {	
	$.validationEngineLanguage.newLang()
});