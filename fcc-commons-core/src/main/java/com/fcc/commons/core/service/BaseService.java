package com.fcc.commons.core.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@SuppressWarnings("rawtypes")
public interface BaseService {

	/**
	 * 保存一个对象
	 * 
	 * @param o
	 *            对象
	 */
	public void add(Object o);
	/**
	 * 保存
	 * @param list
	 */
    public void addList(List list);
    
    /**
     * 保存
     * @param list
     */
    public void addListBatch(List list);

	/**
	 * 更新一个对象
	 * 
	 * @param o
	 *            对象
	 */
	public void edit(Object o);

	/**
	 * 保存或更新对象
	 * 
	 * @param o
	 *            对象
	 */
	public void addOrEdit(Object o);

	/**
	 * 合并一个对象
	 * 
	 * @param o
	 *            对象
	 */
	public void merge(Object o);

	/**
	 * 删除一个对象
	 * 
	 * @param o
	 *            对象
	 */
	public void delete(Object o);
	
	/**
	 * 删除
	 * @param c
	 * @param ids
	 * @param idName
	 * @return
	 */
	public int deleteById(Class<?> c, Object[] ids, String idName);

	/**
	 * 查找对象集合, 不支持 in(?) 语法
	 * 
	 * @param hql
	 * @param param
	 * @return List<T>
	 */
	public List find(String hql, Object... param);

	/**
	 * 查找对象集合
	 * 
	 * @param hql
	 * @param param
	 * @return List<T>
	 */
	public List find(String hql, Map<String, Object> param);
	
	/**
	 * 查找对象集合, 不支持 in(?) 语法
	 * 
	 * @param hql
	 * @param param
	 * @return List<T>
	 */
	public List findSQL(String sql, Object... param);

	/**
	 * 查找对象集合
	 * 
	 * @param hql
	 * @param param
	 * @return List<T>
	 */
	public List findSQL(String sql, Map<String, Object> param);
	
	/**
     * 查找对象集合, 不支持 in(?) 语法
     * 
     * @param hql
     * @param param
     * @return List<T>
     */
    public List findSQL(Class<?> c, String sql, Object... param);

    /**
     * 查找对象集合
     * 
     * @param hql
     * @param param
     * @return List<T>
     */
    public List findSQL(Class<?> c, String sql, Map<String, Object> param);
	
	/**
	 * 分页查询
	 * @param pageNo	第几页
	 * @param pageSize	每页数据大小
	 * @param cHql		
	 * @param bHql
	 * @param params
	 * @param isSQL
	 * @return
	 */
	public ListPage queryPage(int pageNo, int pageSize, String cHql,
			String bHql, Map<String, Object> param, boolean isSQL);
	
	/**
	 * 分页查询
	 * @param pageNo	第几页
	 * @param pageSize	每页数据大小
	 * @param bHql
	 * @param params
	 * @param isSQL
	 * @return
	 */
	public List queryPage(int pageNo, int pageSize, String bHql, 
			Map<String, Object> param, boolean isSQL);
	
	/**
	 * 查询sql语句返回的列名
	 * @param sql	执行的sql语句
	 * @return
	 */
	public List<String> queryColumns(String sql);
	
	/**
	 * 获得一个对象
	 * 
	 * @param c
	 *            对象类型
	 * @param id
	 * @return Object
	 */
	public Object get(Class<?> c, Serializable id);
	
	/**
	 * 获取多个对象
	 * @param c
	 * @param ids
	 * @param idName
	 * @return
	 */
    public List get(Class<?> c, List ids, String idName);

	/**
	 * 获得一个对象
	 * 
	 * @param hql
	 * @param param
	 * @return Object
	 */
	public Object get(String hql, Object... param);

	/**
	 * 获得一个对象
	 * 
	 * @param hql
	 * @param param
	 * @return Object
	 */
	public Object get(String hql, List<Object> param);

	/**
     * 获取所有对象
     * 
     * @param c
     * @return
     */
    public List getAll(Class<?> c);
	/**
	 * 获得一个对象
	 * 
	 * @param c
	 *            对象类型
	 * @param id
	 * @return Object
	 */
	public Object load(Class<?> c, Serializable id);

	/**
	 * @param hql
	 * @param param
	 * @return Long
	 */
	public Object uniqueResult(String hql, Object... param);

	/**
	 * @param hql
	 * @param param
	 * @return Long
	 */
	public Object uniqueResult(String hql, Map<String, Object> param);

	/**
	 * 执行HQL语句
	 * 
	 * @param hql
	 * @return 相应数目
	 */
	public Integer executeHql(String hql);
	
	/**
	 * 执行HQL语句
	 * 
	 * @param hql
	 * @return 相应数目
	 */
	public Integer executeHql(String hql, Object... param);
	
	/**
	 * 执行HQL语句
	 * 
	 * @param hql
	 * @return 相应数目
	 */
	public Integer executeHql(String hql, Map<String, Object> param);
	
	/**
	 * 执行SQL语句
	 * 
	 * @param hql
	 * @return 相应数目
	 */
	public Integer executeSql(String sql);
	
	/**
	 * 执行SQL语句
	 * 
	 * @param hql
	 * @return 相应数目
	 */
	public Integer executeSql(String sql, Object... param);
	
	/**
	 * 执行SQL语句
	 * 
	 * @param hql
	 * @return 相应数目
	 */
	public Integer executeSql(String sql, Map<String, Object> param);
}
