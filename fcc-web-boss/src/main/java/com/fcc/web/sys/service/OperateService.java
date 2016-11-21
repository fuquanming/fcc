package com.fcc.web.sys.service;

import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.model.Operate;

public interface OperateService {
    /**
     * 新增操作
     * @param o     操作
     * @throws RefusedException
     */
    void add(Operate o) throws RefusedException;
    /**
     * 修改操作
     * @param o     操作
     */
    void edit(Operate o);
    /**
     * 删除操作
     * @param ids   操作ID
     * @return
     */
    Integer delete(String[] ids);
    /**
     * 分页查询操作
     * @param pageNo        页码
     * @param pageSize      页记录数
     * @param param         查询参数
     * @return
     */
    ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);

}