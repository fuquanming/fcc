package com.fcc.web.sys.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.model.SysLog;
import com.fcc.web.sys.service.SysLogService;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @email fuquanming@gmail.com
 */
@Service
@SuppressWarnings("unchecked")
public class SysLogServiceImpl implements SysLogService {
    @Resource
    private BaseService baseService;

    @Transactional(readOnly = true)
    //只查事务申明
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        StringBuilder cHql = new StringBuilder("select count(t) from SysLog t where 1=1 ");
        StringBuilder bHql = new StringBuilder("select t from SysLog t where 1=1 ");
        if (param != null) {
            if (param.get("logId") != null) {
                bHql.append(" and  t.logId = :logId ");
                cHql.append(" and  t.logId = :logId ");
            }
            if (param.get("userId") != null) {
                bHql.append(" and  t.userId = :userId ");
                cHql.append(" and  t.userId = :userId ");
            }
            if (param.get("userName") != null) {
                bHql.append(" and  t.userName = :userName ");
                cHql.append(" and  t.userName = :userName ");
            }
            if (param.get("ipAddress") != null) {
                bHql.append(" and  t.ipAddress = :ipAddress ");
                cHql.append(" and  t.ipAddress = :ipAddress ");
            }
            if (param.get("logTimeBegin") != null) {
                bHql.append(" and  t.logTime >= :logTimeBegin ");
                cHql.append(" and  t.logTime >= :logTimeBegin ");
            }
            if (param.get("logTimeEnd") != null) {
                bHql.append(" and  t.logTime <= :logTimeEnd ");
                cHql.append(" and  t.logTime <= :logTimeEnd ");
            }
            if (param.get("moduleName") != null) {
                bHql.append(" and  t.moduleName = :moduleName ");
                cHql.append(" and  t.moduleName = :moduleName ");
            }
            if (param.get("operateName") != null) {
                bHql.append(" and  t.operateName = :operateName ");
                cHql.append(" and  t.operateName = :operateName ");
            }
            if (param.get("eventId") != null) {
                bHql.append(" and  t.eventId = :eventId ");
                cHql.append(" and  t.eventId = :eventId ");
            }
            if (param.get("eventObject") != null) {
                bHql.append(" and  t.eventObject = :eventObject ");
                cHql.append(" and  t.eventObject = :eventObject ");
            }
            if (param.get("eventResult") != null) {
                bHql.append(" and  t.eventResult = :eventResult ");
                cHql.append(" and  t.eventResult = :eventResult ");
            }
            if (param.get("sortColumns") != null) {
                bHql.append(" order by ").append(param.get("sortColumns"));
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    bHql.append(" ").append(param.get("orderType"));
                    param.remove("orderType");
                }
                bHql.append(", t.logId desc");
            }
        }
        return baseService.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, isSQL);
    }

    @Transactional(readOnly = true)
    //只查事务申明
    public List<SysLog> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        StringBuilder bHql = new StringBuilder("select t from SysLog t where 1=1 ");
        if (param != null) {
            if (param.get("logId") != null) {
                bHql.append(" and  t.logId = :logId ");
            }
            if (param.get("userId") != null) {
                bHql.append(" and  t.userId = :userId ");
            }
            if (param.get("userName") != null) {
                bHql.append(" and  t.userName = :userName ");
            }
            if (param.get("ipAddress") != null) {
                bHql.append(" and  t.ipAddress = :ipAddress ");
            }
            if (param.get("logTimeBegin") != null) {
                bHql.append(" and  t.logTime >= :logTimeBegin ");
            }
            if (param.get("logTimeEnd") != null) {
                bHql.append(" and  t.logTime <= :logTimeEnd ");
            }
            if (param.get("moduleName") != null) {
                bHql.append(" and  t.moduleName = :moduleName ");
            }
            if (param.get("operateName") != null) {
                bHql.append(" and  t.operateName = :operateName ");
            }
            if (param.get("eventId") != null) {
                bHql.append(" and  t.eventId = :eventId ");
            }
            if (param.get("eventObject") != null) {
                bHql.append(" and  t.eventObject = :eventObject ");
            }
            if (param.get("eventResult") != null) {
                bHql.append(" and  t.eventResult = :eventResult ");
            }
            if (param.get("sortColumns") != null) {
                bHql.append(" order by ").append(param.get("sortColumns"));
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    bHql.append(" ").append(param.get("orderType"));
                    param.remove("orderType");
                }
                bHql.append(", t.logId desc");
            }
        }
        return baseService.queryPage(pageNo, pageSize, bHql.toString(), param, isSQL);
    }

    @Transactional(readOnly = true) //只查事务申明
    public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        String groupBy = "t.logId";
        StringBuilder cHql = new StringBuilder("select count(t.logId) from SysLog t where 1=1  ");
        StringBuilder bHql = new StringBuilder("select new com.fcc.commons.web.view.ReportInfo(count(t.logId), ");
        if (param != null) {
            if (param.get("reportGroupName") != null) {
                groupBy = (String) param.get("reportGroupName");
                param.remove("reportGroupName");
            }
            bHql.append(groupBy).append(") from SysLog t where 1=1 ");
            if (param.get("logId") != null) {
                bHql.append(" and  t.logId = :logId ");
                cHql.append(" and  t.logId = :logId ");
            }
            if (param.get("userId") != null) {
                bHql.append(" and  t.userId = :userId ");
                cHql.append(" and  t.userId = :userId ");
            }
            if (param.get("userName") != null) {
                bHql.append(" and  t.userName = :userName ");
                cHql.append(" and  t.userName = :userName ");
            }
            if (param.get("ipAddress") != null) {
                bHql.append(" and  t.ipAddress = :ipAddress ");
                cHql.append(" and  t.ipAddress = :ipAddress ");
            }
            if (param.get("logTimeBegin") != null) {
                bHql.append(" and  t.logTime >= :logTimeBegin ");
                cHql.append(" and  t.logTime >= :logTimeBegin ");
            }
            if (param.get("logTimeEnd") != null) {
                bHql.append(" and  t.logTime <= :logTimeEnd ");
                cHql.append(" and  t.logTime <= :logTimeEnd ");
            }
            if (param.get("moduleName") != null) {
                bHql.append(" and  t.moduleName = :moduleName ");
                cHql.append(" and  t.moduleName = :moduleName ");
            }
            if (param.get("operateName") != null) {
                bHql.append(" and  t.operateName = :operateName ");
                cHql.append(" and  t.operateName = :operateName ");
            }
            if (param.get("eventId") != null) {
                bHql.append(" and  t.eventId = :eventId ");
                cHql.append(" and  t.eventId = :eventId ");
            }
            if (param.get("eventObject") != null) {
                bHql.append(" and  t.eventObject = :eventObject ");
                cHql.append(" and  t.eventObject = :eventObject ");
            }
            if (param.get("eventResult") != null) {
                bHql.append(" and  t.eventResult = :eventResult ");
                cHql.append(" and  t.eventResult = :eventResult ");
            }
            if (param.get("sortColumns") != null) {
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    param.remove("orderType");
                }
            }
        } else {
            bHql.append(groupBy).append(") from SysLog t where 1=1 ");
        }
        bHql.append(" group by ").append(groupBy).append(" order by ").append(groupBy).append(" desc");
        cHql.append(" group by ").append(groupBy);
        return baseService.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, isSQL);
    }
}
