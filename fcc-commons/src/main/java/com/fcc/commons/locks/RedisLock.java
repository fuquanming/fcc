/*
 * @(#)RedisLock.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2016年10月12日
 * 修改历史 : 
 *     1. [2016年10月12日]创建文件 by fqm
 */
package com.fcc.commons.locks;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 提供分布式锁：Redis 
 * @version v1.0
 * @author 傅泉明
 */
public class RedisLock implements Lock {

    /** 默认缓存时间 */
    private static final int DEFAULT_CACHE_SECONDS = 60 * 60 * 1;// 单位秒 设置成一个小时
    
    private ShardedJedisPool shardedJedisPool;
    /** key前缀 */
    private String lockPre = "fcc:lock:";
    
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
     * @see com.fcc.commons.locks.Lock#lock(java.lang.String)
     **/
    @Override
    public void lock(String lockKey) {
        ShardedJedis shardedJedis = null;
        boolean flag = false;
        try {
            shardedJedis = getShardedJedis();
            String lockName = lockPre + lockKey;
            String value = lockName + System.currentTimeMillis();
            while (flag == false) {
                flag = lock(shardedJedis, lockName, value, DEFAULT_CACHE_SECONDS);
                if (flag == true) break;
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeShardedJedis(shardedJedis);
        }
    }

    /**
     * @see com.fcc.commons.locks.Lock#unLock(java.lang.String)
     **/
    @Override
    public void unLock(String lockKey) {
        ShardedJedis conn = null;
        try {
            conn = getShardedJedis();
            conn.del(lockPre + lockKey);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeShardedJedis(conn);
        }
    }

    /**
     * @see com.fcc.commons.locks.Lock#tryLock(java.lang.String)
     **/
    @Override
    public boolean tryLock(String lockKey) {
//        JedisShardInfo info = new JedisShardInfo("");
        ShardedJedis shardedJedis = null;
        boolean flag = false;
        try {
            shardedJedis = getShardedJedis();
//            conn.watch(lockKey);
//            Transaction trans = conn.multi();
//            trans.setex(lockKey, 5000, identifier);
//            List<Object> results = trans.exec();
//            if (results != null && results.size() > 0) {
//                System.out.println(Thread.currentThread().getId() + ":value=" + results.get(0).toString());
//            }
//            conn.unwatch();
            String lockName = lockPre + lockKey;
            String value = lockName + System.currentTimeMillis();
            flag = lock(shardedJedis, lockName, value, DEFAULT_CACHE_SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeShardedJedis(shardedJedis);
        }
        return flag;
    }

    /**
     * @see com.fcc.commons.locks.Lock#tryLock(java.lang.String, long)
     **/
    @Override
    public boolean tryLock(String lockKey, long tryTimeoutMilliSeconds) throws InterruptedException {
        return tryLock(lockKey, tryTimeoutMilliSeconds, DEFAULT_CACHE_SECONDS);
    }

    /**
     * @see com.fcc.commons.locks.Lock#tryLock(java.lang.String, long, long)
     **/
    @Override
    public boolean tryLock(String lockKey, long tryTimeoutMilliSeconds, int lockTimeoutSeconds) throws InterruptedException {
        ShardedJedis shardedJedis = null;
        boolean flag = false;
        try {
            shardedJedis = getShardedJedis();
            String lockName = lockPre + lockKey;
            String value = lockName + System.currentTimeMillis();
            
            long end = System.currentTimeMillis() + tryTimeoutMilliSeconds;
            while (System.currentTimeMillis() < end) {
                flag = lock(shardedJedis, lockName, value, lockTimeoutSeconds);
                if (flag == true) break;
                end = end - 10;
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeShardedJedis(shardedJedis);
        }
        return flag;
    }
    /**
     * 锁定
     * @param shardedJedis
     * @param lockName              key
     * @param value                 value
     * @param lockTimeoutSeconds    time
     * @return
     * @throws Exception
     */
    private boolean lock(ShardedJedis shardedJedis, String lockName, String value, int lockTimeoutSeconds) throws Exception {
        boolean flag = false;
        if (shardedJedis.setnx(lockName, value) == 1) {
            shardedJedis.expire(lockName, lockTimeoutSeconds);
            flag = true;
        }
        if (shardedJedis.ttl(lockName) == -1) {
            shardedJedis.expire(lockName, lockTimeoutSeconds);
        }
        return flag;
    }
}
