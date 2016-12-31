/*
 * @(#)RedisUtil.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2016年10月12日
 * 修改历史 : 
 *     1. [2016年10月12日]创建文件 by fqm
 */
package com.fcc.commons.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Redis 工具类
 * @version v1.0
 * @author 傅泉明
 */
public class RedisUtil {

    private static Properties prop = new Properties();
    
    private static ShardedJedisPool shardedJedisPool;
    
    private static JedisPool jedisPool;
    
    // 读取配置文件
    public static synchronized void init() {
        try {
            prop.load(RedisUtil.class.getClassLoader().getResourceAsStream("config/redis.properties"));

            // 初始化连接
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(Integer.parseInt(prop.getProperty("redis.maxIdle")));
            config.setMinIdle(Integer.parseInt(prop.getProperty("redis.minIdle")));
            config.setMaxTotal(Integer.parseInt(prop.getProperty("redis.maxTotal")));
            config.setMaxWaitMillis(1000L);
            config.setTestOnBorrow(false);

            int size = Integer.parseInt(getProperty("redis.size"));
            List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(size);
            for (int i = 1; i <= size; i++) {
                shards.add(new JedisShardInfo(getProperty("redis.host" + size), 
                        Integer.parseInt(getProperty("redis.port" + size)), 
                        getProperty("redis.name" + size)));
            }
            shardedJedisPool = new ShardedJedisPool(config, shards);
            jedisPool = new JedisPool(config, getProperty("redis.host1"), Integer.parseInt(getProperty("redis.port1")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
    
    public static ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }
    
    public static JedisPool getJedisPool() {
        return jedisPool;
    }
    
    public static void close() {
        if (shardedJedisPool != null) shardedJedisPool.close();
        if (jedisPool != null) jedisPool.close();
    }

    public static void main(String[] args) {
        String str = RedisUtil.getProperty("redis.host1");
        System.out.println(str);
    }
}
