package com.fcc.web.sys.service;

import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.model.Role;

public interface RoleService {

    //事务申明
    void create(Role role);

    //事务申明
    void create(Role role, String[] moduleRight) throws RefusedException;

    //事务申明
    void update(Role role, String[] moduleRight) throws RefusedException;

    //事务申明
    void delete(String[] roleIds) throws Exception;

    //只查事务申明
    Role getRole(String roleId);

    //只查事务申明
    Role getRoleWithModuleRight(String roleId);

    ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);

}