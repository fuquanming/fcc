package com.fcc.web.sys.service;

import java.util.Date;
import java.util.List;

import com.fcc.web.sys.model.SysUserOnline;

public interface SysUserOnlineService {

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserOnlineService#online(com.fcc.web.sys.model.SysUserOnline)
     **/
    void online(SysUserOnline sysUserOnline);

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserOnlineService#offline(java.lang.String)
     **/
    void offline(String sid);

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserOnlineService#batchOffline(java.util.List)
     **/
    void batchOffline(List<String> needOfflineIdList);

    List<SysUserOnline> findExpiredUserOnlineList(Date expiredDate, int pageSize);

}