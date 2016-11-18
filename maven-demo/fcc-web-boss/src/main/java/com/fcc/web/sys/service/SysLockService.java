package com.fcc.web.sys.service;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.model.SysLock;

public interface SysLockService {

    Integer update(SysLock oldSysLock, String newLockStatus);

    ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);

    List<SysLock> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);

    ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);

}