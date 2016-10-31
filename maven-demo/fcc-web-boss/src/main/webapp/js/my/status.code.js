/**
 * 状态码js
 * 依赖jquery.js
 * @author 傅泉明
 */
var StatusCode = $.extend({}, StatusCode);/* 全局对象 */
StatusCode.msg = function(code) {
	return codeMap[code];
}

var codeMap = {
	'sys_000':'操作成功',
	'sys_001':'操作失败',
	'sys_002':'请选择需要修改的记录',
	'sys_003':'请选择需要删除的记录',
	'sys_004':'未登录用户或用户访问已超时，您可以重新登陆',
	
	'export_000':'已上传成功！系统正在导出数据...',
	'export_001':'系统正在执行上次导出数据，请稍后...',
	'export_002':'请选择上传文件',

	'login_000':'用户名为空',
	'login_001':'密码为空',
	'login_002':'验证码为空',
	'login_003':'验证码错误',
	'login_004':'用户名不存在',
	'login_005':'密码错误',
	'login_006':'用户锁定',
	
	'module_000':'模块名称为空',
	'module_001':'父模块为空',
	'module_002':'不能修改根节点',
	'module_003':'修改的模块不存在',
	
	'operate_000':'操作ID为空',
	'operate_001':'操作名称为空',
	'operate_002':'操作ID已存在',
	'operate_003':'超过最大操作个数',
	
	'role_000':'角色名称为空',
	
	'organization_000':'机构名称为空',
	'organization_001':'父机构为空',
	'organization_002':'不能修改根节点',
	'organization_003':'修改的机构不存在',
	'organization_004':'该组织机构下有人员',
	
	'sysUser_000':'用户账号为空',
	'sysUser_001':'机构ID为空',
	'sysUser_002':'用户账号已存在',
	
	'userPassword_000':'旧密码为空',
	'userPassword_001':'新密码为空',
	'userPassword_002':'确认码为空',
	'userPassword_003':'旧密码错误',
	'userPassword_004':'新密码和确认码不一致',
        
}