/*
 * @(#)DynamicDataSource.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-core
 * 创建日期 : 2016年12月21日
 * 修改历史 : 
 *     1. [2016年12月21日]创建文件 by 傅泉明
 */
package com.fcc.commons.core.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 定义动态数据源，实现通过集成Spring提供的AbstractRoutingDataSource，只需要实现determineCurrentLookupKey方法即可 
 * 由于DynamicDataSource是单例的，线程不安全的，所以采用ThreadLocal保证线程安全，由DynamicDataSourceHolder完成。 
 * @version 
 * @author 傅泉明
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /** 记录主从数据源 */
    public static Map<String, List<String>> DATASOURCE_TYPE = new HashMap<String, List<String>>();

    /**
     * //TODO 添加override说明
     * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
     **/
    @Override
    protected Object determineCurrentLookupKey() {
        String key = DynamicDataSourceHolder.getDataSourceKey();
        return key;
    }
    /**
     * 记录主从数据源
     * @param map
     */
    public void setDataSourceType(Map<String, String> map) {
        for (String key : map.keySet()) {
            String[] types = StringUtils.split(map.get(key), ",");
            List<String> v = new ArrayList<String>(types.length);
            for (String type : types) {
                if (StringUtils.isNotBlank(type)) {
                    v.add(type);
                }
            }
            DATASOURCE_TYPE.put(key, v);
        }
    }

}
