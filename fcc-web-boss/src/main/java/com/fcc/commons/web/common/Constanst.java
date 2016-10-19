package com.fcc.commons.web.common;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>Description:常量</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class Constanst {
	
	public static final String ENCODING = "UTF-8";
	
	public static final String CONTENT_TYPE_HTML = "text/html;charset=UTF-8";
	public static final String CONTENT_TYPE_XML = "text/xml;charset=UTF-8";
	
	/** 系统随机验证码 */
	public static final String RAND_CODE_KEY = "randCode";
	/** 系统提供导出数据下载文件夹 */
	public static final String EXPORT_DATA_FILENAME = "exportData";
	/** 系统提供导入数据文件夹 */
	public static final String IMPORT_DATA_FILENAME = "importData";
	/** 系统提供导出数据每次查询数据量 */
	public static final int EXPORT_DATA_PAGE_SIZE = 5000;
	/** 系统APPNAME缓存 */
	public static final String APP_NAME = "APP_NAME";
	
	/** 系统用户session中的变量 */
	public interface SysUserSession {
		/** 登陆系统的用户 */
		public final static String USER_LOGIN = "USER_LOGIN_SYS";
		/** 当前调用的action **/
		public final static String USER_ACTION = "USER_ACTION_SYS";
		/** 当前用户选择的语言  **/
		public final static String USER_LANGUAGE = "USER_LANGUAGE";
		
		public final static String USER_MENU = "USER_MENU";
		
		public final static String USER_MENU_UI = "USER_MENU_UI";
		/** 查看、修改角色信息 */
		public final static String SESSION_ROLE = "sessionRole";
		/** 查看、修改角色权值 */
		public final static String SESSION_ROLE_RIGHT_MAP = "sessionRoleRightMap";
	}
	
	public static final String MODULE_MAP = "MODULE_MAP"; 
	/** 注册用户状态 未激活 0 */
	public final static String USER_STATE_INACTIVE = "0";
	/** 注册用户状态 激活 1 */
	public final static String USER_STATE_ACTIVATION = "1";
	/** 注册用户状态 注销 2 */
	public final static String USER_STATE_OFF = "2";
	/** 注册用户状态 锁定 3 */
	public final static String USER_STATE_LOCK = "3";
	/** 默认分页数 */
	public static int PAGE_SIZE = 20;
	/** 上传文件的临时数据 */
	public final static String UPLOAD_FILE_TEMP = "UPLOAD_FILE_TEMP";
	
	/** 有效 */
	public final static String VALID_TRUE = "1";
	/** 失效 */
	public final static String VALID_FALSE = "0";
	
	/** 自定义访问模块 */
	public interface MODULE {
		/** 访问系统 */
		public final static String REQUEST_APP = "access";
		
		public static class Text {
			public static Map<String, String> TEXT_MAP = new HashMap<String, String>();
			static {
				TEXT_MAP.put(REQUEST_APP, "访问系统");
			}
		}
	}
	
	/** 自定义访问操作 */
	public interface OPERATE {
		/** 登陆 */
		public final static String LOGIN = "login";
		/** 退出 */
		public final static String LOGOUT = "logout";
		
		public static class Text {
			public static Map<String, String> TEXT_MAP = new HashMap<String, String>();
			static {
				TEXT_MAP.put(LOGIN, "登录");
				TEXT_MAP.put(LOGOUT, "退出");
			}
		}
	}
	/** 请求对象 */
	public interface REQUEST {
		/** 请求的模块 */
		public final static String MODULE = "REQUEST_MODULE";
		/** 请求的操作 */
		public final static String OPERATE = "REQUEST_OPERATE";
		/** 请求的处理结果 */
		public final static String MESSAGE = "REQUEST_MESSAGE";
	}
	
	/**
	 * serviceException
	 * 业务异常消息使用保留的消息标识符‘SVC’，并使用从0001到0999的号码来定义
	 * 策略异常消息使用保留的消息标识符‘POL’，并使用从0001到0999的号码来定义。 
	 * @author 傅泉明
	 *
	 * 2011-1-12
	 */
	public interface SERVICE_EXCEPTION {
		/**
		 * 业务异常消息使用保留的消息标识符‘SVC’，并使用从0001到0999的号码来定义
		 */
		public interface SVC {
			public interface ID {
				/** 服务器发生异常 */
				public static final String ID_0999 = "SVC0999";
				
			}
			public static class Text {
				public static Map<String, String> TEXT_MAP = new HashMap<String, String>();
				static {
					TEXT_MAP.put(ID.ID_0999, "服务器发生异常");
				}
			}
		}
		/**
		 * 策略异常消息使用保留的消息标识符‘POL’，并使用从0001到0999的号码来定义。 
		 */
		public interface POL {
			public interface ID {
				/** 鉴权失败— */
				public static final String ID_0001 = "POL0001";
				
			}
			public static class Text {
				public static Map<String, String> TEXT_MAP = new HashMap<String, String>();
				static {
					TEXT_MAP.put(ID.ID_0001, "鉴权失败—");
				}
			}
		}
	}
	
}
