/**
 * 初始化Dialog
 * {modal:true,title:'标题',buttons:[{text:'确定',handler:function(){}}]}
 * @author 傅泉明
 */

function getDialog(param) {
	var id = param.id;// 哪个id生成dialog
	var modal = param.modal;// 模式 true
	var title = param.title;// 显示标题
	if (!modal) modal = true;
	return $('#' + param.id).show().dialog({
        modal : modal,
        title : title,
        buttons : param.buttons
    }).dialog('close');
}