package com.fcc.web.workflow.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.data.ListPage;
import com.fcc.web.workflow.dao.LeaveDao;
import com.fcc.web.workflow.model.Leave;

/**
 * <p>Description:Leave</p>
 */
@Repository
public class LeaveDaoImpl implements LeaveDao {
    
    @Resource
    private BaseDao baseDao;
    
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        StringBuilder cHql = new StringBuilder("select count(t) from Leave t where 1=1 ");
        StringBuilder bHql = new StringBuilder("select t from Leave t where 1=1 ");
        if (param != null) {
            if(param.get("leaveId") != null) {
                bHql.append(" and  t.leaveId = :leaveId ");
                cHql.append(" and  t.leaveId = :leaveId ");
            }
            if(param.get("startTimeBegin") != null) {
                bHql.append(" and  t.startTime >= :startTimeBegin ");
                cHql.append(" and  t.startTime >= :startTimeBegin ");
            }
            if(param.get("startTimeEnd") != null) {
                bHql.append(" and  t.startTime <= :startTimeEnd ");
                cHql.append(" and  t.startTime <= :startTimeEnd ");
            }
            if(param.get("endTimeBegin") != null) {
                bHql.append(" and  t.endTime >= :endTimeBegin ");
                cHql.append(" and  t.endTime >= :endTimeBegin ");
            }
            if(param.get("endTimeEnd") != null) {
                bHql.append(" and  t.endTime <= :endTimeEnd ");
                cHql.append(" and  t.endTime <= :endTimeEnd ");
            }
            if(param.get("content") != null) {
                bHql.append(" and  t.content = :content ");
                cHql.append(" and  t.content = :content ");
            }
            if(param.get("processInstanceId") != null) {
                bHql.append(" and  t.processInstanceId = :processInstanceId ");
                cHql.append(" and  t.processInstanceId = :processInstanceId ");
            }
            if(param.get("processDefinitionId") != null) {
                bHql.append(" and  t.processDefinitionId = :processDefinitionId ");
                cHql.append(" and  t.processDefinitionId = :processDefinitionId ");
            }
            if(param.get("processNodeName") != null) {
                bHql.append(" and  t.processNodeName = :processNodeName ");
                cHql.append(" and  t.processNodeName = :processNodeName ");
            }
            if(param.get("status") != null) {
                bHql.append(" and  t.status = :status ");
                cHql.append(" and  t.status = :status ");
            }
            if(param.get("createUser") != null) {
                bHql.append(" and  t.createUser = :createUser ");
                cHql.append(" and  t.createUser = :createUser ");
            }
            if(param.get("createTimeBegin") != null) {
                bHql.append(" and  t.createTime >= :createTimeBegin ");
                cHql.append(" and  t.createTime >= :createTimeBegin ");
            }
            if(param.get("createTimeEnd") != null) {
                bHql.append(" and  t.createTime <= :createTimeEnd ");
                cHql.append(" and  t.createTime <= :createTimeEnd ");
            }
            if(param.get("updateUser") != null) {
                bHql.append(" and  t.updateUser = :updateUser ");
                cHql.append(" and  t.updateUser = :updateUser ");
            }
            if(param.get("updateTimeBegin") != null) {
                bHql.append(" and  t.updateTime >= :updateTimeBegin ");
                cHql.append(" and  t.updateTime >= :updateTimeBegin ");
            }
            if(param.get("updateTimeEnd") != null) {
                bHql.append(" and  t.updateTime <= :updateTimeEnd ");
                cHql.append(" and  t.updateTime <= :updateTimeEnd ");
            }
            if(param.get("sortColumns") != null) {
                bHql.append(" order by t.").append(param.get("sortColumns"));
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    bHql.append(" ").append(param.get("orderType"));
                    param.remove("orderType");
                }
                bHql.append(", t.leaveId desc");
            }
        }
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, false);
    }
    
    @SuppressWarnings("unchecked")
    public List<Leave> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        StringBuilder bHql = new StringBuilder("select t from Leave t where 1=1 ");
        if (param != null) {
            if(param.get("leaveId") != null) {
                bHql.append(" and  t.leaveId = :leaveId ");
            }
            if(param.get("startTimeBegin") != null) {
                bHql.append(" and  t.startTime >= :startTimeBegin ");
            }
            if(param.get("startTimeEnd") != null) {
                bHql.append(" and  t.startTime <= :startTimeEnd ");
            }
            if(param.get("endTimeBegin") != null) {
                bHql.append(" and  t.endTime >= :endTimeBegin ");
            }
            if(param.get("endTimeEnd") != null) {
                bHql.append(" and  t.endTime <= :endTimeEnd ");
            }
            if(param.get("content") != null) {
                bHql.append(" and  t.content = :content ");
            }
            if(param.get("processInstanceId") != null) {
                bHql.append(" and  t.processInstanceId = :processInstanceId ");
            }
            if(param.get("processDefinitionId") != null) {
                bHql.append(" and  t.processDefinitionId = :processDefinitionId ");
            }
            if(param.get("processNodeName") != null) {
                bHql.append(" and  t.processNodeName = :processNodeName ");
            }
            if(param.get("status") != null) {
                bHql.append(" and  t.status = :status ");
            }
            if(param.get("createUser") != null) {
                bHql.append(" and  t.createUser = :createUser ");
            }
            if(param.get("createTimeBegin") != null) {
                bHql.append(" and  t.createTime >= :createTimeBegin ");
            }
            if(param.get("createTimeEnd") != null) {
                bHql.append(" and  t.createTime <= :createTimeEnd ");
            }
            if(param.get("updateUser") != null) {
                bHql.append(" and  t.updateUser = :updateUser ");
            }
            if(param.get("updateTimeBegin") != null) {
                bHql.append(" and  t.updateTime >= :updateTimeBegin ");
            }
            if(param.get("updateTimeEnd") != null) {
                bHql.append(" and  t.updateTime <= :updateTimeEnd ");
            }
            if(param.get("sortColumns") != null) {
                bHql.append(" order by t.").append(param.get("sortColumns"));
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    bHql.append(" ").append(param.get("orderType"));
                    param.remove("orderType");
                }
                bHql.append(", t.leaveId desc");
            }
        }
        return baseDao.queryPage(pageNo, pageSize, bHql.toString(), param, false);
    }
    
    public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        String groupBy = "t.leaveId";
        StringBuilder cHql = new StringBuilder("select count(t) from Leave t where 1=1  ");
        StringBuilder bHql = new StringBuilder("select new com.fcc.commons.web.view.ReportInfo(count(t), ");
        if (param != null) {
            if(param.get("reportGroupName") != null) {
                groupBy = (String) param.get("reportGroupName");
                param.remove("reportGroupName");
            }
            bHql.append(groupBy).append(") from Leave t where 1=1 ");
            if(param.get("leaveId") != null) {
                bHql.append(" and  t.leaveId = :leaveId ");
                cHql.append(" and  t.leaveId = :leaveId ");
            }
            if(param.get("startTimeBegin") != null) {
                bHql.append(" and  t.startTime >= :startTimeBegin ");
                cHql.append(" and  t.startTime >= :startTimeBegin ");
            }
            if(param.get("startTimeEnd") != null) {
                bHql.append(" and  t.startTime <= :startTimeEnd ");
                cHql.append(" and  t.startTime <= :startTimeEnd ");
            }
            if(param.get("endTimeBegin") != null) {
                bHql.append(" and  t.endTime >= :endTimeBegin ");
                cHql.append(" and  t.endTime >= :endTimeBegin ");
            }
            if(param.get("endTimeEnd") != null) {
                bHql.append(" and  t.endTime <= :endTimeEnd ");
                cHql.append(" and  t.endTime <= :endTimeEnd ");
            }
            if(param.get("content") != null) {
                bHql.append(" and  t.content = :content ");
                cHql.append(" and  t.content = :content ");
            }
            if(param.get("processInstanceId") != null) {
                bHql.append(" and  t.processInstanceId = :processInstanceId ");
                cHql.append(" and  t.processInstanceId = :processInstanceId ");
            }
            if(param.get("processDefinitionId") != null) {
                bHql.append(" and  t.processDefinitionId = :processDefinitionId ");
                cHql.append(" and  t.processDefinitionId = :processDefinitionId ");
            }
            if(param.get("processNodeName") != null) {
                bHql.append(" and  t.processNodeName = :processNodeName ");
                cHql.append(" and  t.processNodeName = :processNodeName ");
            }
            if(param.get("status") != null) {
                bHql.append(" and  t.status = :status ");
                cHql.append(" and  t.status = :status ");
            }
            if(param.get("createUser") != null) {
                bHql.append(" and  t.createUser = :createUser ");
                cHql.append(" and  t.createUser = :createUser ");
            }
            if(param.get("createTimeBegin") != null) {
                bHql.append(" and  t.createTime >= :createTimeBegin ");
                cHql.append(" and  t.createTime >= :createTimeBegin ");
            }
            if(param.get("createTimeEnd") != null) {
                bHql.append(" and  t.createTime <= :createTimeEnd ");
                cHql.append(" and  t.createTime <= :createTimeEnd ");
            }
            if(param.get("updateUser") != null) {
                bHql.append(" and  t.updateUser = :updateUser ");
                cHql.append(" and  t.updateUser = :updateUser ");
            }
            if(param.get("updateTimeBegin") != null) {
                bHql.append(" and  t.updateTime >= :updateTimeBegin ");
                cHql.append(" and  t.updateTime >= :updateTimeBegin ");
            }
            if(param.get("updateTimeEnd") != null) {
                bHql.append(" and  t.updateTime <= :updateTimeEnd ");
                cHql.append(" and  t.updateTime <= :updateTimeEnd ");
            }
            if(param.get("sortColumns") != null) {
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    param.remove("orderType");
                }
            }
        } else {
            bHql.append(groupBy).append(") from Leave t where 1=1 ");
        }
        bHql.append(" group by ").append(groupBy).append(" order by ").append(groupBy).append(" desc");
        cHql.append(" group by ").append(groupBy);
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, false);
    }
}
