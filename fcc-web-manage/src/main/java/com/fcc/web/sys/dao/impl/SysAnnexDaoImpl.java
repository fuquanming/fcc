package com.fcc.web.sys.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.dao.SysAnnexDao;
import com.fcc.web.sys.model.SysAnnex;

/**
 * <p>Description:SysAnnex</p>
 */
@Repository
public class SysAnnexDaoImpl implements SysAnnexDao {
    
    @Resource
    private BaseDao baseDao;
    
    @SuppressWarnings("unchecked")
    @Override
    public List<SysAnnex> query(String linkId, String linkType, String annexType) {
        return baseDao.find("from SysAnnex where linkId=? and linkType=? and annexType=?", linkId, linkType, annexType);
    }
    
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        StringBuilder cHql = new StringBuilder("select count(t) from SysAnnex t where 1=1 ");
        StringBuilder bHql = new StringBuilder("select t from SysAnnex t where 1=1 ");
        if (param != null) {
            if(param.get("annexId") != null) {
                bHql.append(" and  t.annexId = :annexId ");
                cHql.append(" and  t.annexId = :annexId ");
            }
            if(param.get("linkId") != null) {
                bHql.append(" and  t.linkId = :linkId ");
                cHql.append(" and  t.linkId = :linkId ");
            }
            if(param.get("linkType") != null) {
                bHql.append(" and  t.linkType = :linkType ");
                cHql.append(" and  t.linkType = :linkType ");
            }
            if(param.get("annexName") != null) {
                bHql.append(" and  t.annexName = :annexName ");
                cHql.append(" and  t.annexName = :annexName ");
            }
            if(param.get("annexType") != null) {
                bHql.append(" and  t.annexType = :annexType ");
                cHql.append(" and  t.annexType = :annexType ");
            }
            if(param.get("fileName") != null) {
                bHql.append(" and  t.fileName = :fileName ");
                cHql.append(" and  t.fileName = :fileName ");
            }
            if(param.get("fileType") != null) {
                bHql.append(" and  t.fileType = :fileType ");
                cHql.append(" and  t.fileType = :fileType ");
            }
            if(param.get("fileUrl") != null) {
                bHql.append(" and  t.fileUrl = :fileUrl ");
                cHql.append(" and  t.fileUrl = :fileUrl ");
            }
            if(param.get("fileSize") != null) {
                bHql.append(" and  t.fileSize = :fileSize ");
                cHql.append(" and  t.fileSize = :fileSize ");
            }
            if(param.get("remark") != null) {
                bHql.append(" and  t.remark = :remark ");
                cHql.append(" and  t.remark = :remark ");
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
                bHql.append(", t.annexId desc");
            }
        }
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, false);
    }
    
    @SuppressWarnings("unchecked")
    public List<SysAnnex> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        StringBuilder bHql = new StringBuilder("select t from SysAnnex t where 1=1 ");
        if (param != null) {
            if(param.get("annexId") != null) {
                bHql.append(" and  t.annexId = :annexId ");
            }
            if(param.get("linkId") != null) {
                bHql.append(" and  t.linkId = :linkId ");
            }
            if(param.get("linkType") != null) {
                bHql.append(" and  t.linkType = :linkType ");
            }
            if(param.get("annexName") != null) {
                bHql.append(" and  t.annexName = :annexName ");
            }
            if(param.get("annexType") != null) {
                bHql.append(" and  t.annexType = :annexType ");
            }
            if(param.get("annexType") != null) {
                bHql.append(" and  t.annexType = :annexType ");
            }
            if(param.get("fileName") != null) {
                bHql.append(" and  t.fileName = :fileName ");
            }
            if(param.get("fileType") != null) {
                bHql.append(" and  t.fileType = :fileType ");
            }
            if(param.get("fileUrl") != null) {
                bHql.append(" and  t.fileUrl = :fileUrl ");
            }
            if(param.get("fileSize") != null) {
                bHql.append(" and  t.fileSize = :fileSize ");
            }
            if(param.get("remark") != null) {
                bHql.append(" and  t.remark = :remark ");
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
                bHql.append(", t.annexId desc");
            }
        }
        return baseDao.queryPage(pageNo, pageSize, bHql.toString(), param, false);
    }
    
    public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        String groupBy = "t.annexId";
        StringBuilder cHql = new StringBuilder("select count(t) from SysAnnex t where 1=1  ");
        StringBuilder bHql = new StringBuilder("select new com.fcc.commons.web.view.ReportInfo(count(t), ");
        if (param != null) {
            if(param.get("reportGroupName") != null) {
                groupBy = (String) param.get("reportGroupName");
                param.remove("reportGroupName");
            }
            bHql.append(groupBy).append(") from SysAnnex t where 1=1 ");
            if(param.get("annexId") != null) {
                bHql.append(" and  t.annexId = :annexId ");
                cHql.append(" and  t.annexId = :annexId ");
            }
            if(param.get("linkId") != null) {
                bHql.append(" and  t.linkId = :linkId ");
                cHql.append(" and  t.linkId = :linkId ");
            }
            if(param.get("linkType") != null) {
                bHql.append(" and  t.linkType = :linkType ");
                cHql.append(" and  t.linkType = :linkType ");
            }
            if(param.get("annexName") != null) {
                bHql.append(" and  t.annexName = :annexName ");
                cHql.append(" and  t.annexName = :annexName ");
            }
            if(param.get("annexType") != null) {
                bHql.append(" and  t.annexType = :annexType ");
                cHql.append(" and  t.annexType = :annexType ");
            }
            if(param.get("fileName") != null) {
                bHql.append(" and  t.fileName = :fileName ");
                cHql.append(" and  t.fileName = :fileName ");
            }
            if(param.get("fileType") != null) {
                bHql.append(" and  t.fileType = :fileType ");
                cHql.append(" and  t.fileType = :fileType ");
            }
            if(param.get("fileUrl") != null) {
                bHql.append(" and  t.fileUrl = :fileUrl ");
                cHql.append(" and  t.fileUrl = :fileUrl ");
            }
            if(param.get("fileSize") != null) {
                bHql.append(" and  t.fileSize = :fileSize ");
                cHql.append(" and  t.fileSize = :fileSize ");
            }
            if(param.get("remark") != null) {
                bHql.append(" and  t.remark = :remark ");
                cHql.append(" and  t.remark = :remark ");
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
            bHql.append(groupBy).append(") from SysAnnex t where 1=1 ");
        }
        bHql.append(" group by ").append(groupBy).append(" order by ").append(groupBy).append(" desc");
        cHql.append(" group by ").append(groupBy);
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, false);
    }
}
