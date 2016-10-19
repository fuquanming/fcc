package com.fcc.commons.core.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@SuppressWarnings("rawtypes")
@Service
public class BaseServiceImpl implements BaseService {
	
    @Autowired
	private BaseDao baseDao;
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void delete(Object o) {
		baseDao.delete(o);
	}
	@Transactional(rollbackFor = Exception.class)//事务申明
	public int deleteById(Class<?> c, Object[] ids, String idName) {
		return baseDao.deleteById(c, ids, idName);
	}
	@Transactional(rollbackFor = Exception.class)//事务申明
	public Integer executeHql(String hql) {
		return baseDao.executeHql(hql);
	}
	@Transactional(rollbackFor = Exception.class)//事务申明
	public Integer executeHql(String hql, Object... param) {
		return baseDao.executeHql(hql, param);
	}
	@Transactional(rollbackFor = Exception.class)//事务申明
	public Integer executeHql(String hql, Map<String, Object> param) {
		return baseDao.executeHql(hql, param);
	}
	@Transactional(rollbackFor = Exception.class)//事务申明
	public Integer executeSql(String sql) {
		return baseDao.executeSql(sql);
	}
	@Transactional(rollbackFor = Exception.class)//事务申明
	public Integer executeSql(String sql, Object... param) {
		return baseDao.executeSql(sql, param);
	}
	@Transactional(rollbackFor = Exception.class)//事务申明
	public Integer executeSql(String sql, Map<String, Object> param) {
		return baseDao.executeSql(sql, param);
	}
	@Transactional(readOnly = true)//只查事务申明
	public List find(String hql, Object... param) {
		return baseDao.find(hql, param);
	}
	@Transactional(readOnly = true)//只查事务申明
	public List find(String hql, Map<String, Object> param) {
		return baseDao.find(hql, param);
	}
	@Transactional(readOnly = true)//只查事务申明
	public List findSQL(String sql, Object... param) {
		return baseDao.findSQL(sql, param);
	}
	@Transactional(readOnly = true)//只查事务申明
	public List findSQL(String sql, Map<String, Object> param) {
		return baseDao.findSQL(sql, param);
	}
	@Transactional(readOnly = true)//只查事务申明
	public Object get(Class<?> c, Serializable id) {
		return baseDao.get(c, id);
	}
	
    @Transactional(readOnly = true)//只查事务申明
	public List get(Class<?> c, List ids, String idName) {
		return baseDao.get(c, ids, idName);
	}
	@Transactional(readOnly = true)//只查事务申明
	public Object get(String hql, Object... param) {
		return baseDao.get(hql, param);
	}
	@Transactional(readOnly = true)//只查事务申明
	public Object get(String hql, List<Object> param) {
		return baseDao.get(hql, param);
	}
	@Transactional(readOnly = true)//只查事务申明
	public List getAll(Class<?> c) {
	    return baseDao.getAll(c);
	}
	@Transactional(readOnly = true)//只查事务申明
	public Object load(Class<?> c, Serializable id) {
		return baseDao.load(c, id);
	}
	@Transactional(readOnly = true)//只查事务申明
	public void merge(Object o) {
		baseDao.merge(o);
	}
	@Transactional(readOnly = true)//只查事务申明
	public ListPage queryPage(int pageNo, int pageSize, String cHql,
			String bHql, Map<String, Object> param, boolean isSQL) {
		return baseDao.queryPage(pageNo, pageSize, cHql, bHql, param, isSQL);
	}
	@Transactional(readOnly = true)//只查事务申明
	public List queryPage(int pageNo, int pageSize, String bHql,
			Map<String, Object> param, boolean isSQL) {
		return baseDao.queryPage(pageNo, pageSize, bHql, param, isSQL);
	}
	@Transactional(readOnly = true)//只查事务申明
	public List<String> queryColumns(String sql) {
		return baseDao.queryColumns(sql);
	}
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void create(Object o) {
		baseDao.save(o);
	}
    @Transactional(rollbackFor = Exception.class)//事务申明
	public void createList(List o) {
		for (Object data : o) baseDao.save(data);
	}
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void createOrUpdate(Object o) {
		baseDao.saveOrUpdate(o);
	}
	@Transactional(readOnly = true)//只查事务申明
	public Object uniqueResult(String hql, Object... param) {
		return baseDao.uniqueResult(hql, param);
	}
	@Transactional(readOnly = true)//只查事务申明
	public Object uniqueResult(String hql, Map<String, Object> param) {
		return baseDao.uniqueResult(hql, param);
	}
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void update(Object o) {
		baseDao.update(o);
	}
}
