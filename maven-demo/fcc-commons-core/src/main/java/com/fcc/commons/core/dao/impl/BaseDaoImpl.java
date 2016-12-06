package com.fcc.commons.core.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.data.ListPage;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@SuppressWarnings({"deprecation", "rawtypes"})
@Repository("baseDao")
public class BaseDaoImpl implements BaseDao {

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public void launchParamValues(Query query, Map<String, Object> param) {
		if (param == null) return;
		for (Iterator<String> it = param.keySet().iterator(); it.hasNext(); ) {
			String name = it.next();
			Object value = param.get(name);
			if (value instanceof String) {
				query.setString(name , (String)value);
			} else if (value instanceof Integer) {
				query.setInteger(name , ( (Integer)value ).intValue());	
			} else if (value instanceof Long) {
				query.setLong(name, ( (Long)value ).longValue());
			} else if (value instanceof Float) {
				query.setFloat(name, ( (Float)value ).floatValue());
			} else if (value instanceof Double) {
				query.setDouble(name, ( (Double)value ).doubleValue());
			} else if (value instanceof Timestamp) {
				query.setTimestamp(name, (Timestamp)value);
			} else if (value instanceof Date) {
				query.setDate(name, (Date)value);
			} else if (value instanceof Collection) {
				query.setParameterList(name, (Collection<?>)value);
			} else if (value instanceof Object[]) {
				query.setParameterList(name, (Object[])value);
			} else {
				query.setParameter(name, value);
			}
		}
	}

	public void add(Object o) {
		this.getCurrentSession().save(o);
	}

	public void edit(Object o) {
		this.getCurrentSession().update(o);
	}

	public void addOrEdit(Object o) {
		this.getCurrentSession().saveOrUpdate(o);
	}

	public void merge(Object o) {
		this.getCurrentSession().merge(o);
	}

	public void delete(Object o) {
		this.getCurrentSession().delete(o);
	}

	public int deleteById(Class<?> c, Object[] ids, String idName) {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from ").append(c.getName()).append(" where ").append(idName).append(" in (:ids)");
		return this.getCurrentSession().createQuery(sb.toString())
				.setParameterList("ids", ids)
				.executeUpdate();
	}
	
    public List find(String hql, Object... param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.list();
	}
	
	public List find(String hql, Map<String, Object> param) {
		Query q = this.getCurrentSession().createQuery(hql);
		launchParamValues(q, param);
		return q.list();
	}
	
	public List findSQL(String sql, Object... param) {
		Query q = this.getCurrentSession().createSQLQuery(sql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.list();
	}
	
	public List findSQL(String sql, Map<String, Object> param) {
		Query q = this.getCurrentSession().createSQLQuery(sql);
		launchParamValues(q, param);
		return q.list();
	}

    public ListPage queryPage(int pageNo, int pageSize, String cHql,
			String bHql, Map<String, Object> param, boolean isSQL) {
		ListPage listPage = null;
        if(bHql != null){
        	//如果pageCount为0,则认为无限制,返回所有记录
        	int firstResultIndex = 0;
        	if (pageSize <= 0){
        		firstResultIndex = 0;
        		pageSize = Integer.MAX_VALUE;
        	} else {
        		firstResultIndex = (pageNo - 1) * pageSize;
        	}
    		Query query = null;
    		if (isSQL) {
    			query = getCurrentSession().createSQLQuery(cHql);
    		} else {
    			query = getCurrentSession().createQuery(cHql);
    		}
    		launchParamValues(query, param);
    		Object tempCount = "0";
    		List tempCountList = query.list();
    		if (tempCountList != null) {
    			int size = tempCountList.size();
    			if (size == 1) {
    				tempCount = tempCountList.get(0);
    			} else if (size > 1) {
    				tempCount = size + "";
    			}
    		}
    		int totalCount = Integer.valueOf(tempCount.toString());
            if (totalCount == 0 || firstResultIndex > totalCount) {
		        listPage = ListPage.EMPTY_PAGE;
		        listPage.setDataList(new ArrayList(1));
            } else {
            	if (isSQL) {
            		query = getCurrentSession().createSQLQuery(bHql);
            	} else {
            		query = getCurrentSession().createQuery(bHql);
            	}
	          	launchParamValues(query, param);
	          	query.setFirstResult(firstResultIndex);
	          	query.setMaxResults(pageSize);
	          	
	          	List result = query.list();
	          	
	          	listPage = new ListPage();
	          	listPage.setTotalSize(totalCount);
	          	listPage.setDataList(result);
            }
        	listPage.setCurrentPageNo(pageNo);
          	listPage.setCurrentPageSize(pageSize);
        }
        return listPage;
	}
	
	public List queryPage(int pageNo, int pageSize, String bHql,
			Map<String, Object> param, boolean isSQL) {
        List result = null;
        if(bHql != null){
        	//如果pageCount为0,则认为无限制,返回所有记录
        	int firstResultIndex = 0;
        	if (pageSize <= 0){
        		firstResultIndex = 0;
        		pageSize = Integer.MAX_VALUE;
        	} else {
        		firstResultIndex = (pageNo - 1) * pageSize;
        	}
    		Query query = null;
        	if (isSQL) {
        		query = getCurrentSession().createSQLQuery(bHql);
        	} else {
        		query = getCurrentSession().createQuery(bHql);
        	}
          	launchParamValues(query, param);
          	query.setFirstResult(firstResultIndex);
          	query.setMaxResults(pageSize);
          	result = query.list();
        }
		return result;
	}
	
	public List<String> queryColumns(String sql) {
		List<String> list = new ArrayList<String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConnectionProvider cp = null;
		Connection conn = null;
		try {
			// 只查询 前1数据,获取列名
			SessionFactoryImplementor factory = ((SessionImpl)getCurrentSession()).getFactory();
            String limitSql = factory.getDialect().getLimitString(sql, 0, 1);
			cp = factory.getConnectionProvider();
			conn = cp.getConnection();
			ps = conn.prepareStatement(limitSql);
			ps.setLong(1, 1L);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				list.add(rsmd.getColumnName(i + 1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (cp != null) {
				try {
					cp.closeConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	public Object get(Class<?> c, Serializable id) {
		return this.getCurrentSession().get(c, id);
	}
	
	public List get(Class<?> c, List ids, String idName) {
		StringBuilder sb = new StringBuilder();
		sb.append("from ").append(c.getName()).append(" where ").append(idName).append(" in (:ids)");
		return this.getCurrentSession().createQuery(sb.toString())
				.setParameterList("ids", ids)
				.list();
	}

	public Object get(String hql, Object... param) {
		List<?> l = this.find(hql, param);
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	public Object get(String hql, List<Object> param) {
		List<?> l = this.find(hql, param);
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}
	
	@Override
	public List getAll(Class<?> c) {
	    Map<String, Object> param = null;
	    return this.find("from " + c.getName(), param);
	}

	public Object load(Class<?> c, Serializable id) {
		return this.getCurrentSession().load(c, id);
	}

	public Object uniqueResult(String hql, Object... param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.uniqueResult();
	}

	public Object uniqueResult(String hql, Map<String, Object> param) {
		Query q = this.getCurrentSession().createQuery(hql);
		launchParamValues(q, param);
		return q.uniqueResult();
	}

	public Integer executeHql(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		return q.executeUpdate();
	}
	
	public Integer executeHql(String hql, Map<String, Object> param) {
		Query q = this.getCurrentSession().createQuery(hql);
		launchParamValues(q, param);
		return q.executeUpdate();
	}
	
	public Integer executeHql(String hql, Object... param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.executeUpdate();
	}
	
	public Integer executeSql(String sql) {
		Query q = this.getCurrentSession().createSQLQuery(sql);
		return q.executeUpdate();
	}

	public Integer executeSql(String sql, Map<String, Object> param) {
		Query q = this.getCurrentSession().createSQLQuery(sql);
		launchParamValues(q, param);
		return q.executeUpdate();
	}
	
	public Integer executeSql(String sql, Object... param) {
		Query q = this.getCurrentSession().createSQLQuery(sql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.executeUpdate();
	}
}
