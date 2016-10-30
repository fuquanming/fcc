package com.fcc.web.sys.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fcc.commons.locks.Lock;
import com.fcc.commons.web.common.Constants;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.LoginService;
import com.fcc.web.sys.service.ModuleService;
import com.fcc.web.sys.service.OperateService;
import com.fcc.web.sys.service.RoleModuleRightService;

/**
 * <p>Description:管理系统缓存</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@SuppressWarnings("unchecked")
public class CacheUtil {

	private static Logger logger = Logger.getLogger(CacheUtil.class);
	/** 模块地址 key:moduleId */
	public static Map<String, Module> moduleMap = new HashMap<String, Module>();
	/** 模块地址 key:moduleDesc */
	public static Map<String, Module> moduleUrlMap = new HashMap<String, Module>();
	/** 模块操作 key:operateId */
	public static Map<String, Operate> operateMap = new HashMap<String, Operate>();
	/** 管理员账号 */
	public static List<String> adminAccountList = new ArrayList<String>();
	/** 应用路径 */
//	public static String basePath;
	
	public static final String UPLOAD_SESSION = "upload_session";
	
	/** 取得应用本地路径 */
	public static String realPath;
	/** 导出数据总量 */
	private static Integer exportDataTotalSize = 500000;
	/** 应用名称 */
	private static String appName;
	
	/** 线程池 */
	private static ThreadPoolExecutor pool;
	/** 线程池 大小 */
	private static int threadPoolSize;
	
	/** 应用是否注销 */
	private static boolean appDestroy = false;
	/** 应用是否停止功能 */
	private static boolean appStop = false;
	/** 正在执行的功能 */
	private static Set<String> runningApp = new HashSet<String>();
	
	private static LoginService loginService;
	private static ModuleService moduleService;
	private static OperateService operateService;
	private static RoleModuleRightService roleModuleRightService;
	
	private static Lock lock;
	
	public void init() {
		pool = new ThreadPoolExecutor(threadPoolSize, threadPoolSize * 2, 60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		initModule();
		initOperate();
	}
	
	/** 加载系统模块中有URL地址的模块 */
	public static synchronized void initModule() {
		try {
			List<Module> dataList = moduleService.getModuleWithOperate();
			Map<String, Module> tempModuleMap = new HashMap<String, Module>();
			Map<String, Module> tempModuleUrlMap = new HashMap<String, Module>();
			for (Module data : dataList) {
				tempModuleMap.put(data.getModuleId(), data);
				String moduleDesc = data.getModuleDesc();
				if (moduleDesc != null && !"".equals(moduleDesc)) {
					tempModuleUrlMap.put(moduleDesc, data);
				}
			}
			moduleMap.clear();
			moduleMap = tempModuleMap;
			moduleUrlMap.clear();
			moduleUrlMap = tempModuleUrlMap;
			logger.info("加载系统模块成功，共：" + dataList.size() + "条!");
			dataList.clear();
			dataList = null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}
	/** 加载系统模块操作 */
	public static synchronized void initOperate() {
		try {
			Map<String, Object> param = null;
			List<Operate> dataList = operateService.queryPage(1, 0, param).getDataList();
			Map<String, Operate> tempOperateMap = new HashMap<String, Operate>();
			for (Operate data : dataList) {
				tempOperateMap.put(data.getOperateId(), data);
			}
			operateMap.clear();
			operateMap = tempOperateMap;
			logger.info("加载系统模块操作成功，共：" + dataList.size() + "条!");
			dataList.clear();
			dataList = null;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	public void destroy() {
		logger.info("系统停止开始。。。");
		appDestroy = true;
		if (pool != null) {
			pool.shutdown();
			logger.info("系统线程池停止完成！");
		}
		while (runningApp.size() != 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("系统任务停止完成。。。");
		
		while (QueueUtil.getCreateQueue().size() != 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("系统队列停止完成。。。");
		
		appStop = true;
		logger.info("系统停止完成。。。");
	}
	/** 返回唯一key */
	public static String getRunningAppKey(String key) {
		return key + UUID.randomUUID().toString();
	}
	
	/** 加载用户模块 */
	public static TreeSet<Module> loadUserMenu(HttpServletRequest request) {
		// 设置菜单
    	HttpSession session = request.getSession();
    	TreeSet<Module> menuSet = null;
    	SysUser user = CacheUtil.getSysUser(request);
		Set<Role> roleSet = user.getRoles();
		String[] roleIds = null;
		if (roleSet == null) {
			roleIds = new String[0];
		} else {
			roleIds = new String[roleSet.size()];
			int i = 0;
			for (Iterator<Role> it = roleSet.iterator(); it.hasNext();) {
				Role role = it.next();
				roleIds[i] = role.getRoleId();
				i++;
			}
		}
		menuSet = new TreeSet<Module>();
		if (roleSet != null && roleSet.size() > 0) {
			List<RoleModuleRight> moduleRightList = roleModuleRightService.getModuleRightByRoleId(roleIds);
			List<String> moduleIdList = new ArrayList<String>();
			HashMap<String, List<RoleModuleRight>> rightMap = new HashMap<String, List<RoleModuleRight>>();
			if (moduleRightList != null) {
				for (RoleModuleRight data : moduleRightList) {
					moduleIdList.add(data.getModuleId());
					List<RoleModuleRight> tempList = rightMap.get(data.getRoleId());
					if (tempList == null) tempList = new ArrayList<RoleModuleRight>();
					tempList.add(data);
					rightMap.put(data.getRoleId(), tempList);
				}
			}
			if (moduleIdList.size() > 0) {
				if (roleSet != null) {
					for (Iterator<Role> it = roleSet.iterator(); it.hasNext();) {
						Role role = it.next();
						Set<RoleModuleRight> rightSet = role.getRoleModuleRights();
						if (rightSet == null) rightSet = new HashSet<RoleModuleRight>();
						List<RoleModuleRight> tempList = rightMap.get(role.getRoleId());
						if (tempList != null) rightSet.addAll(tempList);
						role.setRoleModuleRights(rightSet);
					}
				}
				
				CacheUtil.setSysUser(request, user);
				
				List<Module> moduleList = null;
//				if (isAdmin) {// 管理员获取所有模块
//					moduleList = new ArrayList<Module>();
//					moduleList.addAll(CacheUtil.moduleMap.values());
//				} else {
					moduleList = moduleService.findModules(moduleIdList);
//				}
				Map<String, Module> moduleMap = new HashMap<String, Module>();
				for (Module module : moduleList) {
					if (module.getOperates() == null || module.getOperates().size() == 0) {
						continue;
					}
					
					menuSet.add(module);
					String parentModuleId = module.getParentId();
					Module parentModule = moduleMap.get(parentModuleId);
					if (parentModule == null) {
//						parentModule = moduleService.getParentModule(module);
						// 缓存获取模块
						parentModule = CacheUtil.moduleMap.get(parentModuleId);
						while (parentModule != null) {
							menuSet.add(parentModule);
							moduleMap.put(parentModuleId, parentModule);
							
							parentModuleId = parentModule.getParentId();
							parentModule = moduleMap.get(parentModuleId);
							
							if (parentModule == null) {
								// 缓存获取模块
								parentModule = CacheUtil.moduleMap.get(parentModuleId);
							}
							if (parentModule == Module.ROOT) break;
						}
					}
				}
			}
		}
		session.setAttribute(Constants.SysUserSession.USER_MENU, menuSet);
		return menuSet;
	}
	
	public static String getBasePath(HttpServletRequest request) {
		String path = request.getContextPath();
		String sPort = (request.getServerPort() == 80)?"":(":" + request.getServerPort());
		String base = request.getScheme() + "://" + request.getServerName() + sPort;
		return base + path + "/";
	}
	
	/** 加载用户菜单easyUI */
	public static List<EasyuiTreeNode> loadUserMenuUI(HttpServletRequest request, TreeSet<Module> menuSet) {
		HttpSession session = request.getSession();
		
		List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
		// 不使用ROOT
//		EasyuiTreeNode rootNode = new EasyuiTreeNode();
//		rootNode.setId(Module.ROOT.getModuleId());
//		rootNode.setText(Module.ROOT.getModuleName());
//		nodeList.add(rootNode);
		
		Map<String, EasyuiTreeNode> nodeMap = new TreeMap<String, EasyuiTreeNode>();
		// 不使用ROOT
//		nodeMap.put(rootNode.getId(), rootNode);
		
		for (Iterator<Module> it = menuSet.iterator(); it.hasNext();) {
			Module m = it.next();
			
			EasyuiTreeNode node = new EasyuiTreeNode();
			node.setId(m.getModuleId());
			node.setText(m.getModuleName());
			String desc = m.getModuleDesc();
			if (desc != null && !"".equals(desc)) {
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put("src", desc);
				node.setAttributes(attributes);
			}
			
			nodeMap.put(node.getId(), node);
			
			String parendId = m.getParentId();
			EasyuiTreeNode cacheNode = nodeMap.get(parendId);
			if (cacheNode != null) {
				List<EasyuiTreeNode> children = cacheNode.getChildren();
				if (children == null) children = new ArrayList<EasyuiTreeNode>();
				children.add(node);
				cacheNode.setChildren(children);
			} else {
				// 添加一级菜单
				nodeList.add(node);
			}
		}
		session.setAttribute(Constants.SysUserSession.USER_MENU_UI, nodeList);
		nodeMap.clear();
		nodeMap = null;
		return nodeList;
	}
	/** 获取登录系统的用户信息 */
	public static SysUser getSysUser(HttpServletRequest request) {
		return (SysUser)request.getSession().getAttribute(Constants.SysUserSession.USER_LOGIN);
	}
	
	/** 设置登录系统的用户信息 */
	public static void setSysUser(HttpServletRequest request, SysUser user) {
		request.getSession().setAttribute(Constants.SysUserSession.USER_LOGIN, user);
	}
	
	/** 初始化登录系统的用户信息 */
	public static void initLoginUser(HttpServletRequest request, SysUser user) {
		CacheUtil.setSysUser(request, user);
		request.getSession().getServletContext().setAttribute(request.getSession().getId(), request.getSession());
		CacheUtil.loadUserMenu(request);
		loginService.postLogin(request);
	}
	
	public static ThreadPoolExecutor getPool() {
		return pool;
	}
	public static void setPool(ThreadPoolExecutor pool) {
		CacheUtil.pool = pool;
	}
	public static boolean isAppDestroy() {
		return appDestroy;
	}
	public static void setAppDestroy(boolean appDestroy) {
		CacheUtil.appDestroy = appDestroy;
	}
	public static boolean isAppStop() {
		return appStop;
	}
	public static void setAppStop(boolean appStop) {
		CacheUtil.appStop = appStop;
	}
	public static void addRunningApp(String key) {
		runningApp.add(key);
	}
	public static void removeRunningApp(String key) {
		runningApp.remove(key);
	}
	
	public static LoginService getLoginService() {
		return loginService;
	}
	@Autowired
	public void setLoginService(LoginService loginService) {
		CacheUtil.loginService = loginService;
	}
	public static ModuleService getModuleService() {
		return moduleService;
	}
	@Autowired
	public void setModuleService(ModuleService moduleService) {
		CacheUtil.moduleService = moduleService;
	}
	public static OperateService getOperateService() {
		return operateService;
	}
	@Autowired
	public void setOperateService(OperateService operateService) {
		CacheUtil.operateService = operateService;
	}
	public static Integer getExportDataTotalSize() {
		return exportDataTotalSize;
	}
	@Autowired
	public void setExportDataTotalSize(Integer exportDataTotalSize) {
		CacheUtil.exportDataTotalSize = exportDataTotalSize;
	}
	public static RoleModuleRightService getRoleModuleRightService() {
		return roleModuleRightService;
	}
	@Autowired
	public void setRoleModuleRightService(RoleModuleRightService roleModuleRightService) {
		CacheUtil.roleModuleRightService = roleModuleRightService;
	}
	public static String getAppName() {
		return appName;
	}
	@Autowired
	public void setAppName(String appName) {
		CacheUtil.appName = appName;
	}
	public static int getThreadPoolSize() {
		return threadPoolSize;
	}
	@Autowired
	public void setThreadPoolSize(int threadPoolSize) {
		CacheUtil.threadPoolSize = threadPoolSize;
	}

	public static Lock getLock() {
		return lock;
	}
	@Autowired
	public void setLock(Lock lock) {
		CacheUtil.lock = lock;
	}
	
}
