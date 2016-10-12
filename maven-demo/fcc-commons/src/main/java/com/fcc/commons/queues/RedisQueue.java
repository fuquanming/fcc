/*
 * @(#)RedisQueue.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2016年10月12日
 * 修改历史 : 
 *     1. [2016年10月12日]创建文件 by fqm
 */
package com.fcc.commons.queues;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 提供分布式队列：Redis 
 * @version v1.0
 * @author 傅泉明
 */
public class RedisQueue implements Queue, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String charset = "UTF-8";

    private Map<String, Boolean> valueObjectMap = new ConcurrentHashMap<String, Boolean>();

    private ShardedJedisPool shardedJedisPool;

    /** key前缀 */
    private String queuePre = "redis:queue:";

    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    public ShardedJedis getShardedJedis() {
        return shardedJedisPool.getResource();
    }

    public void closeShardedJedis(ShardedJedis shardedJedis) {
        if (shardedJedis != null) shardedJedis.close();
    }
    
    /**
     * 对象转byte[]
     * @param obj
     * @return
     * @throws IOException
     */
    public byte[] objectToBytes(Object obj) throws Exception{
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        byte[] bytes = null;
        try {
            oo.writeObject(obj);
            bytes = bo.toByteArray();
            return bytes;
        } finally {
            IOUtils.closeQuietly(bo);
            IOUtils.closeQuietly(oo);
        }
    }
    /**
     * byte[]转对象
     * @param bytes
     * @return
     * @throws Exception
     */
    public Object bytesToObject(byte[] bytes) throws Exception{
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn = new ObjectInputStream(in);
        try {
            return sIn.readObject();
        } finally {
            IOUtils.closeQuietly(sIn);
            IOUtils.closeQuietly(in);
        }
    }
    
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean offer(String key, Object value) {
        ShardedJedis conn = null;
        try {
            conn = getShardedJedis();
            String queueKey = queuePre + key;
            if (value instanceof String) {
                conn.lpush(queueKey, value.toString());
                if (!valueObjectMap.containsKey(queueKey)) {
                    valueObjectMap.put(queueKey, false);
                }
            } else {
                byte[] objectBytes = objectToBytes(value);
                conn.lpush(queueKey.getBytes(charset), objectBytes);
                if (!valueObjectMap.containsKey(queueKey)) {
                    valueObjectMap.put(queueKey, true);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeShardedJedis(conn);
        }
        return false;
    }

    @Override
    public Object poll(String key) {
        ShardedJedis conn = null;
        try {
            conn = getShardedJedis();
            String queueKey = queuePre + key;
            Boolean valueObjectFlag = valueObjectMap.get(queueKey);
            if (valueObjectFlag != null) {
                if (valueObjectFlag) {
                    byte[] bytes = conn.rpop(queueKey.getBytes(charset));
                    if (bytes != null) return bytesToObject(bytes);
                } else {
                    return conn.rpop(queueKey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeShardedJedis(conn);
        }
        return null;
    }

    @Override
    public Object peek(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long size(String key) {
        ShardedJedis conn = null;
        try {
            conn = getShardedJedis();
            String queueKey = queuePre + key;
            return conn.llen(queueKey);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeShardedJedis(conn);
        }
        return 0;
    }

    @Override
    public Iterator<Object> iterator(String key) {
        ShardedJedis conn = null;
        try {
            conn = getShardedJedis();
            String queueKey = queuePre + key;
            Boolean valueObjectFlag = valueObjectMap.get(queueKey);
            if (valueObjectFlag != null) {
                if (valueObjectFlag) {
                    List<byte[]> dataList = conn.lrange(queueKey.getBytes(charset), 0, size(queueKey));
                    if (dataList != null && dataList.size() > 0) {
                        List<Object> list = new ArrayList<Object>();
                        for (byte[] data : dataList) {
                            Object obj = bytesToObject(data);
                            list.add(obj);
                        }
                        return list.iterator();
                    }
                } else {
                    List<String> dataList = conn.lrange(queueKey, 0, size(queueKey));
                    if (dataList != null && dataList.size() > 0) {
                        List<Object> list = new ArrayList<Object>();
                        for (String data : dataList) {
                            list.add(data);
                        }
                        return list.iterator();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeShardedJedis(conn);
        }
        return null;
    }

}
