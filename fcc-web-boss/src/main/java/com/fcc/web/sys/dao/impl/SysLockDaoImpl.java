/*
 * @(#)SysLockDaoImpl.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-app-web Maven Webapp
 * 创建日期 : 2016年10月17日
 * 修改历史 : 
 *     1. [2016年10月17日]创建文件 by fqm
 */
package com.fcc.web.sys.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.dao.SysLockDao;
import com.fcc.web.sys.model.SysLock;

/**
 *
 * @version v1.0
 * @author 傅泉明
 */
@Repository
public class SysLockDaoImpl implements SysLockDao {

    @Resource
    private BaseDao baseDao;
    /**
     * 
     * @see com.fcc.web.sys.dao.SysLockDao#update(com.fcc.web.sys.model.SysLock, java.lang.String)
     **/
    @Override
    public Integer update(SysLock oldSysLock, String newLockStatus) {
        Map<String, Object> param = new HashMap<String, Object>(5);
        param.put("lockKey", oldSysLock.getLockKey());
        param.put("oldCreateTime", oldSysLock.getCreateTime());
        param.put("createTime", new Date());
        param.put("oldLockStatus", oldSysLock.getLockStatus());
        param.put("newLockStatus", newLockStatus);
        return baseDao.executeHql("update SysLock set lockStatus=:newLockStatus, createTime=:createTime where lockKey=:lockKey and lockStatus=:oldLockStatus and createTime=:oldCreateTime", param);
    }

    @Override
    public ListPage queryPage(int pageNo, int pageSize,
            Map<String, Object> param, boolean isSQL) {
        StringBuilder cHql = new StringBuilder("select count(t) from SysLock t where 1=1 ");
        StringBuilder bHql = new StringBuilder("select t from SysLock t where 1=1 ");
        if (param != null) {
            if(param.get("lockKey") != null) {
                bHql.append(" and  t.lockKey = :lockKey ");
                cHql.append(" and  t.lockKey = :lockKey ");
            }
            if(param.get("lockStatus") != null) {
                bHql.append(" and  t.lockStatus = :lockStatus ");
                cHql.append(" and  t.lockStatus = :lockStatus ");
            }
            if(param.get("createTimeBegin") != null) {
                bHql.append(" and  t.createTime >= :createTimeBegin ");
                cHql.append(" and  t.createTime >= :createTimeBegin ");
            }
            if(param.get("createTimeEnd") != null) {
                bHql.append(" and  t.createTime <= :createTimeEnd ");
                cHql.append(" and  t.createTime <= :createTimeEnd ");
            }
            if(param.get("sortColumns") != null) {
                bHql.append(" order by t.").append(param.get("sortColumns"));
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    bHql.append(" ").append(param.get("orderType"));
                    param.remove("orderType");
                }
                bHql.append(", t.lockKey desc");
            }
        }
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, isSQL);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<SysLock> query(int pageNo, int pageSize,
            Map<String, Object> param, boolean isSQL) {
        StringBuilder bHql = new StringBuilder("select t from SysLock t where 1=1 ");
        if (param != null) {
            if(param.get("lockKey") != null) {
                bHql.append(" and  t.lockKey = :lockKey ");
            }
            if(param.get("lockStatus") != null) {
                bHql.append(" and  t.lockStatus = :lockStatus ");
            }
            if(param.get("createTimeBegin") != null) {
                bHql.append(" and  t.createTime >= :createTimeBegin ");
            }
            if(param.get("createTimeEnd") != null) {
                bHql.append(" and  t.createTime <= :createTimeEnd ");
            }
            if(param.get("sortColumns") != null) {
                bHql.append(" order by t.").append(param.get("sortColumns"));
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    bHql.append(" ").append(param.get("orderType"));
                    param.remove("orderType");
                }
                bHql.append(", t.lockKey desc");
            }
        }
        return baseDao.queryPage(pageNo, pageSize, bHql.toString(), param, isSQL);
    }
    @Override
    public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        String groupBy = "t.LockKey";
        StringBuilder cHql = new StringBuilder("select count(t) from SysLock t where 1=1  ");
        StringBuilder bHql = new StringBuilder("select new com.fcc.app.view.ReportInfo(count(t), ");
        if (param != null) {
            if(param.get("reportGroupName") != null) {
                groupBy = (String) param.get("reportGroupName");
                param.remove("reportGroupName");
            }
            bHql.append(groupBy).append(") from SysLock t where 1=1 ");
            if(param.get("lockKey") != null) {
                bHql.append(" and  t.lockKey = :lockKey ");
                cHql.append(" and  t.lockKey = :lockKey ");
            }
            if(param.get("lockStatus") != null) {
                bHql.append(" and  t.lockStatus = :lockStatus ");
                cHql.append(" and  t.lockStatus = :lockStatus ");
            }
            if(param.get("createTimeBegin") != null) {
                bHql.append(" and  t.createTime >= :createTimeBegin ");
                cHql.append(" and  t.createTime >= :createTimeBegin ");
            }
            if(param.get("createTimeEnd") != null) {
                bHql.append(" and  t.createTime <= :createTimeEnd ");
                cHql.append(" and  t.createTime <= :createTimeEnd ");
            }
            if(param.get("sortColumns") != null) {
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    param.remove("orderType");
                }
            }
        } else {
            bHql.append(groupBy).append(") from SysLock t where 1=1 ");
        }
        bHql.append(" group by ").append(groupBy).append(" order by ").append(groupBy).append(" desc");
        cHql.append(" group by ").append(groupBy);
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, isSQL);
    }
}
