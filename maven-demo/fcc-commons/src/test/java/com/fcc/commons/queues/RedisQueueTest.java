package com.fcc.commons.queues;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.fcc.commons.utils.RedisUtil;

public class RedisQueueTest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testQueue() {
        RedisQueue queue = new RedisQueue();
        queue.setShardedJedisPool(RedisUtil.getShardedJedisPool());
        
        SimpleQueueTest.testMain(queue, true);
        System.out.println("size=" + queue.size("lock1"));
        SimpleQueueTest.testMain(queue, false);
        
        SimpleQueueTest.pool.shutdown();
        RedisUtil.getShardedJedisPool().destroy();
    }

}
