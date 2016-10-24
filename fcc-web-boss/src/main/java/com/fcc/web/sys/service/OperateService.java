package com.fcc.web.sys.service;

import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.model.Operate;

/**
 * <p>Description:系统模块操作</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface OperateService {
	/**
	 * 保存操作
	 * @param o
	 * @throws RefusedException
	 */
	public void create(Operate o) throws RefusedException;
	/**
	 * 更新操作
	 * @param o
	 */
	public void update(Operate o);
	/**
	 * 删除操作
	 * @param ids
	 */
	public Integer delete(String[] ids);
	/**
	 * 分页查询操作
	 * @param pageNo
	 * @param pageSize
	 * @param param
	 * @return
	 */
	public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);
	
}
