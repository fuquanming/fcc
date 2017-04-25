package com.fcc.web.sys.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.dao.SysKeyDao;
import com.fcc.web.sys.model.SysKey;

/**
 * <p>Description:SysKey</p>
 */
@Repository
public class SysKeyDaoImpl implements SysKeyDao {
    
    @Resource
    private BaseDao baseDao;
    
    @Override
    public void delete(String linkType, String[] linkIds) {
        Map<String, Object> param = new HashMap<String, Object>(2);
        param.put("linkType", linkType);
        param.put("linkId", linkIds);
        baseDao.executeHql("delete from SysKey where linkType=:linkType and linkId in(:linkId)", param);
    }
    
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        StringBuilder cHql = new StringBuilder("select count(t) from SysKey t where 1=1 ");
        StringBuilder bHql = new StringBuilder("select t from SysKey t where 1=1 ");
        if (param != null) {
            if(param.get("kyeId") != null) {
                bHql.append(" and  t.kyeId = :kyeId ");
                cHql.append(" and  t.kyeId = :kyeId ");
            }
            if(param.get("linkType") != null) {
                bHql.append(" and  t.linkType = :linkType ");
                cHql.append(" and  t.linkType = :linkType ");
            }
            if(param.get("linkId") != null) {
                bHql.append(" and  t.linkId = :linkId ");
                cHql.append(" and  t.linkId = :linkId ");
            }
            if(param.get("keyValue") != null) {
                bHql.append(" and  t.keyValue = :keyValue ");
                cHql.append(" and  t.keyValue = :keyValue ");
            }
            if(param.get("sortColumns") != null) {
                bHql.append(" order by t.").append(param.get("sortColumns"));
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    bHql.append(" ").append(param.get("orderType"));
                    param.remove("orderType");
                }
                bHql.append(", t.kyeId desc");
            }
        }
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, false);
    }
    
    @SuppressWarnings("unchecked")
    public List<SysKey> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        StringBuilder bHql = new StringBuilder("select t from SysKey t where 1=1 ");
        if (param != null) {
            if(param.get("kyeId") != null) {
                bHql.append(" and  t.kyeId = :kyeId ");
            }
            if(param.get("linkType") != null) {
                bHql.append(" and  t.linkType = :linkType ");
            }
            if(param.get("linkId") != null) {
                bHql.append(" and  t.linkId = :linkId ");
            }
            if(param.get("keyValue") != null) {
                bHql.append(" and  t.keyValue = :keyValue ");
            }
            if(param.get("sortColumns") != null) {
                bHql.append(" order by t.").append(param.get("sortColumns"));
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    bHql.append(" ").append(param.get("orderType"));
                    param.remove("orderType");
                }
                bHql.append(", t.kyeId desc");
            }
        }
        return baseDao.queryPage(pageNo, pageSize, bHql.toString(), param, false);
    }
    
    public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        String groupBy = "t.kyeId";
        StringBuilder cHql = new StringBuilder("select count(t) from SysKey t where 1=1  ");
        StringBuilder bHql = new StringBuilder("select new com.fcc.commons.web.view.ReportInfo(count(t), ");
        if (param != null) {
            if(param.get("reportGroupName") != null) {
                groupBy = (String) param.get("reportGroupName");
                param.remove("reportGroupName");
            }
            bHql.append(groupBy).append(") from SysKey t where 1=1 ");
            if(param.get("kyeId") != null) {
                bHql.append(" and  t.kyeId = :kyeId ");
                cHql.append(" and  t.kyeId = :kyeId ");
            }
            if(param.get("linkType") != null) {
                bHql.append(" and  t.linkType = :linkType ");
                cHql.append(" and  t.linkType = :linkType ");
            }
            if(param.get("linkId") != null) {
                bHql.append(" and  t.linkId = :linkId ");
                cHql.append(" and  t.linkId = :linkId ");
            }
            if(param.get("keyValue") != null) {
                bHql.append(" and  t.keyValue = :keyValue ");
                cHql.append(" and  t.keyValue = :keyValue ");
            }
            if(param.get("sortColumns") != null) {
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    param.remove("orderType");
                }
            }
        } else {
            bHql.append(groupBy).append(") from SysKey t where 1=1 ");
        }
        bHql.append(" group by ").append(groupBy).append(" order by ").append(groupBy).append(" desc");
        cHql.append(" group by ").append(groupBy);
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, false);
    }
}
