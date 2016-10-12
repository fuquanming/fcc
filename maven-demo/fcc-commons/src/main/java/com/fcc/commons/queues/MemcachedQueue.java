/*
 * @(#)MemcachedQueue.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2016年10月12日
 * 修改历史 : 
 *     1. [2016年10月12日]创建文件 by fqm
 */
package com.fcc.commons.queues;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alisoft.xplatform.asf.cache.IMemcachedCache;

/**
 * 提供分布式队列：Memcached
 * @version v1.0
 * @author 傅泉明
 */
public class MemcachedQueue implements Queue {

    private String queueOfferPrefix = "memcached:queue:offer_";
    private String queuePollPrefix = "memcached:queue:poll_";
    private String queuePeekPrefix = "memcached:queue:peek_";
    
    private String separator = "_";
    
    private IMemcachedCache memcachedCache;
    
    public IMemcachedCache getMemcachedCache() {
        return memcachedCache;
    }

    public void setMemcachedCache(IMemcachedCache memcachedCache) {
        this.memcachedCache = memcachedCache;
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.queues.Queue#offer(java.lang.String, java.lang.Object)
     **/
    @Override
    public boolean offer(String key, Object value) {
        String offerKey = queueOfferPrefix + key;
        long indexFlag = memcachedCache.addOrIncr(offerKey, 1L);
        StringBuffer sb = new StringBuffer();
        sb.append(offerKey).append(separator).append(indexFlag);
        boolean flag = memcachedCache.add(sb.toString(), value);
        if (flag == false) {
            return offer(key, value);
        }
        System.out.println(Thread.currentThread().getId() + ":put:" + value + ":index=" + indexFlag);
        return flag;
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.queues.Queue#poll(java.lang.String)
     **/
    @Override
    public Object poll(String key) {
        String pollKey = queuePollPrefix + key;
        long indexFlag = memcachedCache.addOrIncr(pollKey, 1L);
        StringBuilder sb = new StringBuilder();
        sb.append(queueOfferPrefix).append(key).append(separator).append(indexFlag);
        String offerKey = sb.toString();
        Object value = memcachedCache.get(offerKey);
        Object returnVal = memcachedCache.remove(offerKey);
        System.out.println(Thread.currentThread().getId() + ":out:" + value + ":index=" + indexFlag);
        if (returnVal instanceof Boolean) {
            if (((Boolean)returnVal) == false) {
                memcachedCache.addOrDecr(pollKey, 1L);
            }
        }
        return value;
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.queues.Queue#peek(java.lang.String)
     **/
    @Override
    public Object peek(String key) {
        String peekKey = queuePeekPrefix + key;
        long indexFlag = memcachedCache.addOrIncr(peekKey, 1L);
        String offerKey = queueOfferPrefix + key;
        StringBuilder sb = new StringBuilder();
        sb.append(offerKey).append(separator).append(indexFlag);
        Object value = memcachedCache.get(sb.toString());
        if (value == null) {
            memcachedCache.addOrDecr(peekKey, 1L);
        }
        return value;
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.queues.Queue#size(java.lang.String)
     **/
    @Override
    public long size(String key) {
        return getCurrentOfferIndex(key) - getCurrentPollIndex(key);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.queues.Queue#iterator(java.lang.String)
     **/
    @Override
    public Iterator<Object> iterator(String key) {
        long currentOfferIndex = getCurrentOfferIndex(key);
        long currentPollIndex = getCurrentPollIndex(key);
        String offerKey = queueOfferPrefix + key.toString();
        List<Object> list = new ArrayList<Object>();
        for (long i = currentPollIndex + 1; i <= currentOfferIndex; i++) {
            list.add(memcachedCache.get(offerKey + "_" + i));
        }
        return list.iterator();
    }
    
    /**
     * 返回当前 增长的size
     * @return
     */
    private long getCurrentOfferIndex(String key) {
        String offerKey = queueOfferPrefix + key;
        long offer = memcachedCache.getCounter(offerKey);
        return offer == -1 ? 0 : offer;
    }
    /**
     * 返回当前 移除的size
     * @return
     */
    private long getCurrentPollIndex(String key) {
        String pollKey = queuePollPrefix + key;
        long poll = memcachedCache.getCounter(pollKey);
        return poll == -1 ? 0 : poll;
    }

}
