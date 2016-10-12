package com.fcc.commons.queues;

import org.junit.Test;

import com.fcc.commons.utils.MemcachedUtil;

public class MemcachedQueueTest {

    @Test
    public void test() {
//        fail("Not yet implemented");
        String key = "123";
        MemcachedUtil.getMemcachedCache().put(key, 123);
        System.out.println(MemcachedUtil.getMemcachedCache().get(key));
        System.out.println(MemcachedUtil.getMemcachedCache().remove(key));
        System.out.println(MemcachedUtil.getMemcachedCache().remove(key));
        MemcachedUtil.destroy();
    }

    @Test
    public void testQueue() {
        MemcachedQueue queue = new MemcachedQueue();
        queue.setMemcachedCache(MemcachedUtil.getMemcachedCache());
        
        SimpleQueueTest.testMain(queue, true);
        System.out.println("size=" + queue.size("lock1"));
        SimpleQueueTest.testMain(queue, false);
        
        SimpleQueueTest.pool.shutdown();
        MemcachedUtil.destroy();
    }
}
