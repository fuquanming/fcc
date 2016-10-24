/*
 * @(#)OperateDaoImpl.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-app-web Maven Webapp
 * 创建日期 : 2016年10月17日
 * 修改历史 : 
 *     1. [2016年10月17日]创建文件 by fqm
 */
package com.fcc.web.sys.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.dao.OperateDao;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.view.OperateValueCount;

/**
 *
 * @version v1.0
 * @author 傅泉明
 */
@Repository
public class OperateDaoImpl implements OperateDao {

    @Resource
    private BaseDao baseDao;

    @Override
    public Integer delete(String[] ids) {
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("operateId", ids);
        baseDao.executeSql("delete from sys_rbac_mod2op where operate_id in (:operateId)", param);
        return baseDao.executeHql("delete Operate where operateId in(:operateId)", param);
    }

    @Override
    public OperateValueCount getMaxOperateValueAndCount() {
        Object[] obj = (Object[]) baseDao.uniqueResult("select max(o.operateValue),count(o) from Operate o");
        OperateValueCount data = new OperateValueCount();
        data.setCount(Long.parseLong(obj[1].toString()));
        if (obj[0] != null) {
            data.setValue(Long.parseLong(obj[0].toString()));
        }
        return data;
    }

    /**
     * 
     * @see com.fcc.web.sys.dao.OperateDao#queryPage(int, int, java.util.Map)
     **/
    @Override
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param) {
        StringBuilder cHql = new StringBuilder();
        StringBuilder bHql = new StringBuilder();
        HashMap<String, Object> map = new HashMap<String, Object>();
        cHql.append("select count(r) from Operate r where 1=1 ");
        bHql.append("from Operate r where 1=1 ");
        if (param != null) {
            String operateName = (String) param.get("operateName");
            if (operateName != null && !"".equals(operateName)) {
                map.put("operateName", "%" + operateName + "%");
                cHql.append(" and r.operateName like:operateName");
                bHql.append(" and r.operateName like:operateName");
            }
        }
        bHql.append(" order by operateValue ");
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), map, false);
    }

}
