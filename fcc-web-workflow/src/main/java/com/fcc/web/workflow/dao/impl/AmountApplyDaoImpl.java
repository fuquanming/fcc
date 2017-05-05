package com.fcc.web.workflow.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.data.ListPage;
import com.fcc.web.workflow.dao.AmountApplyDao;
import com.fcc.web.workflow.model.AmountApply;

/**
 * <p>Description:AmountApply</p>
 */
@Repository
public class AmountApplyDaoImpl implements AmountApplyDao {
    
    @Resource
    private BaseDao baseDao;
    
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        StringBuilder cHql = new StringBuilder("select count(t) from AmountApply t where 1=1 ");
        StringBuilder bHql = new StringBuilder("select t from AmountApply t where 1=1 ");
        if (param != null) {
            if(param.get("amountApplyId") != null) {
                bHql.append(" and  t.amountApplyId = :amountApplyId ");
                cHql.append(" and  t.amountApplyId = :amountApplyId ");
            }
            if(param.get("userId") != null) {
                bHql.append(" and  t.userId = :userId ");
                cHql.append(" and  t.userId = :userId ");
            }
            if(param.get("userName") != null) {
                bHql.append(" and  t.userName = :userName ");
                cHql.append(" and  t.userName = :userName ");
            }
            if(param.get("primaryAmount") != null) {
                bHql.append(" and  t.primaryAmount = :primaryAmount ");
                cHql.append(" and  t.primaryAmount = :primaryAmount ");
            }
            if(param.get("applyRemark") != null) {
                bHql.append(" and  t.applyRemark = :applyRemark ");
                cHql.append(" and  t.applyRemark = :applyRemark ");
            }
            if(param.get("applyTimeBegin") != null) {
                bHql.append(" and  t.applyTime >= :applyTimeBegin ");
                cHql.append(" and  t.applyTime >= :applyTimeBegin ");
            }
            if(param.get("applyTimeEnd") != null) {
                bHql.append(" and  t.applyTime <= :applyTimeEnd ");
                cHql.append(" and  t.applyTime <= :applyTimeEnd ");
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
                bHql.append(", t.amountApplyId desc");
            }
        }
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, false);
    }
    
    @SuppressWarnings("unchecked")
    public List<AmountApply> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        StringBuilder bHql = new StringBuilder("select t from AmountApply t where 1=1 ");
        if (param != null) {
            if(param.get("amountApplyId") != null) {
                bHql.append(" and  t.amountApplyId = :amountApplyId ");
            }
            if(param.get("userId") != null) {
                bHql.append(" and  t.userId = :userId ");
            }
            if(param.get("userName") != null) {
                bHql.append(" and  t.userName = :userName ");
            }
            if(param.get("primaryAmount") != null) {
                bHql.append(" and  t.primaryAmount = :primaryAmount ");
            }
            if(param.get("applyRemark") != null) {
                bHql.append(" and  t.applyRemark = :applyRemark ");
            }
            if(param.get("applyTimeBegin") != null) {
                bHql.append(" and  t.applyTime >= :applyTimeBegin ");
            }
            if(param.get("applyTimeEnd") != null) {
                bHql.append(" and  t.applyTime <= :applyTimeEnd ");
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
                bHql.append(", t.amountApplyId desc");
            }
        }
        return baseDao.queryPage(pageNo, pageSize, bHql.toString(), param, false);
    }
    
    public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        String groupBy = "t.amountApplyId";
        StringBuilder cHql = new StringBuilder("select count(t) from AmountApply t where 1=1  ");
        StringBuilder bHql = new StringBuilder("select new com.fcc.commons.web.view.ReportInfo(count(t), ");
        if (param != null) {
            if(param.get("reportGroupName") != null) {
                groupBy = (String) param.get("reportGroupName");
                param.remove("reportGroupName");
            }
            bHql.append(groupBy).append(") from AmountApply t where 1=1 ");
            if(param.get("amountApplyId") != null) {
                bHql.append(" and  t.amountApplyId = :amountApplyId ");
                cHql.append(" and  t.amountApplyId = :amountApplyId ");
            }
            if(param.get("userId") != null) {
                bHql.append(" and  t.userId = :userId ");
                cHql.append(" and  t.userId = :userId ");
            }
            if(param.get("userName") != null) {
                bHql.append(" and  t.userName = :userName ");
                cHql.append(" and  t.userName = :userName ");
            }
            if(param.get("primaryAmount") != null) {
                bHql.append(" and  t.primaryAmount = :primaryAmount ");
                cHql.append(" and  t.primaryAmount = :primaryAmount ");
            }
            if(param.get("applyRemark") != null) {
                bHql.append(" and  t.applyRemark = :applyRemark ");
                cHql.append(" and  t.applyRemark = :applyRemark ");
            }
            if(param.get("applyTimeBegin") != null) {
                bHql.append(" and  t.applyTime >= :applyTimeBegin ");
                cHql.append(" and  t.applyTime >= :applyTimeBegin ");
            }
            if(param.get("applyTimeEnd") != null) {
                bHql.append(" and  t.applyTime <= :applyTimeEnd ");
                cHql.append(" and  t.applyTime <= :applyTimeEnd ");
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
            bHql.append(groupBy).append(") from AmountApply t where 1=1 ");
        }
        bHql.append(" group by ").append(groupBy).append(" order by ").append(groupBy).append(" desc");
        cHql.append(" group by ").append(groupBy);
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, false);
    }
}
