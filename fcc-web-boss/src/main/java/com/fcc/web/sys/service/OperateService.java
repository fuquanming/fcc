package com.fcc.web.sys.service;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.model.Operate;

public interface OperateService {

    void add(Operate o) throws RefusedException;

    void edit(Operate o);

    Integer delete(String[] ids);

    List<Operate> getOperates();

    Operate getOperate(String operateId);

    ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);

}