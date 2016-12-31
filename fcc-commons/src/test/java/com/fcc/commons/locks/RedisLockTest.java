package com.fcc.commons.locks;

import org.junit.Test;

import redis.clients.jedis.ShardedJedis;

import com.fcc.commons.utils.RedisUtil;

public class RedisLockTest {

    @Test
    public void test() {
//        fail("Not yet implemented");
        String key = "redis:lock:lock1";
        ShardedJedis conn = RedisUtil.getShardedJedisPool().getResource();
        System.out.println(conn.get(key));
        System.out.println(conn.del(key));
        RedisUtil.getShardedJedisPool().destroy();
    }
    
    @Test
    public void testLock() {
        //        fail("Not yet implemented");
        RedisLock lock = new RedisLock();
        lock.setShardedJedisPool(RedisUtil.getShardedJedisPool());
        SimpleLockTest.testMain(lock, false);
        RedisUtil.getShardedJedisPool().destroy();
    }
    
    @Test
    public void testTryLock() {
        RedisLock lock = new RedisLock();
        RedisUtil.init();
        lock.setShardedJedisPool(RedisUtil.getShardedJedisPool());
        SimpleLockTest.testMain(lock, true);
        RedisUtil.getShardedJedisPool().destroy();
    }

}
