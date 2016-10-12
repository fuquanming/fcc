package com.fcc.commons.locks;

import org.junit.Test;

import com.fcc.commons.utils.MemcachedUtil;

public class MemcachedLockTest {

    @Test
    public void test() {
//        fail("Not yet implemented");
    }
    
    @Test
    public void testLock() {
        //        fail("Not yet implemented");
        MemcachedLock lock = new MemcachedLock();
        lock.setMemcachedCache(MemcachedUtil.getMemcachedCache());
        SimpleLockTest.testMain(lock, false);
        MemcachedUtil.destroy();
    }
    
    @Test
    public void testTryLock() {
        MemcachedLock lock = new MemcachedLock();
        lock.setMemcachedCache(MemcachedUtil.getMemcachedCache());
        SimpleLockTest.testMain(lock, true);
        MemcachedUtil.destroy();
    }

}
