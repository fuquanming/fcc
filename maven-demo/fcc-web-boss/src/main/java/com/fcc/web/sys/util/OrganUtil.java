package com.fcc.web.sys.util;

import org.apache.commons.lang3.StringUtils;

import com.fcc.web.sys.model.Organization;
import com.fcc.web.sys.model.SysUser;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class OrganUtil {
	/** 检查当前用户是否选择上级的组织机构 */
	public static boolean checkParent(SysUser sysUser, String selectOrganId) {
		String organId = sysUser.getDept();
		if (StringUtils.isEmpty(organId)) return false;
		if (selectOrganId == null || "".equals(selectOrganId) || Organization.ROOT.getOrganId().equals(selectOrganId)) return true;
		int indexOf = organId.indexOf(selectOrganId);
		if (indexOf == 0 && organId.length() != selectOrganId.length()) {
			return true;	
		}
		return false;
	}
}
