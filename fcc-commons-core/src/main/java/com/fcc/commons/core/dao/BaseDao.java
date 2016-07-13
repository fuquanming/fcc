package com.fcc.commons.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;


/**
 * 
 * @描述: 数据访问层基础支撑接口.
 * @作者: WuShuicheng .
 * @创建时间: 2013-7-22,下午4:52:52 .
 * @版本: 1.0 .
 * @param <T>
 */
public interface BaseDao<T> {


	/**
	 * 根据ID查找记录.
	 * 
	 * @param id
	 *            .
	 * @return entity .
	 */
	T getById(long id);

	/**
	 * 根据ID删除记录.
	 * 
	 * @param id
	 *            .
	 * @return
	 */
	int deleteById(long id);

	/**
	 * 根据条件查询 listBy: <br/>
	 * 
	 * @param paramMap
	 * @return 返回集合
	 */
	public List<T> listBy(Map<String, Object> paramMap);

	/**
	 * 根据条件查询 listBy: <br/>
	 * 
	 * @param paramMap
	 * @return 返回实体
	 */
	public T getBy(Map<String, Object> paramMap);

	public SqlSessionTemplate getSessionTemplate();
	
	public SqlSession getSqlSession();
}
