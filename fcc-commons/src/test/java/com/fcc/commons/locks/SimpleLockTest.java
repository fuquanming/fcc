package com.fcc.commons.locks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SimpleLockTest {

    static ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    
    public static void testMain(Lock lock, boolean isTry) {
        int num = 10;
        // 等待多线程完成后执行
        CountDownLatch latch = new CountDownLatch(num);
        // 多线程一起等待一个事件开始
        CyclicBarrier barrier = new CyclicBarrier(num, new Runnable() {
            @Override
            public void run() {
                System.out.println("go on");
            }
        });
        for (int i = 1; i <= num; i++) {
            if (isTry) {
                pool.execute(new TryLockWorker(barrier, latch, lock));
            } else {
                pool.execute(new LockWorker(barrier, latch, lock));
            }
        }
        try {
            System.out.println("main is wait " + num + ",working");
            latch.await();
            System.out.println("main is end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }
    
    @Test
    public void testLock() {
        //        fail("Not yet implemented");
        Lock lock = new SimpleLock();
        testMain(lock, false);
    }
    
    @Test
    public void testTryLock() {
        Lock lock = new SimpleLock();
        testMain(lock, true);
    }

}

class LockWorker implements Runnable {

    private CyclicBarrier barrier;
    
    private CountDownLatch latch;

    private Lock lock;

    public LockWorker(CyclicBarrier barrier, CountDownLatch latch, Lock lock) {
        this.barrier = barrier;
        this.latch = latch;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            long id = Thread.currentThread().getId();
            System.out.println(id + ":is wait");
            barrier.await();
            System.out.println(id + ":is go");

            String lockName = "lock1";
            long startTime = System.currentTimeMillis();
            lock.lock(lockName);
            System.out.println(id + ":lock");
            try {
                try {
                    System.out.println(id + ":is doing");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unLock(lockName);
            }
            long endTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getId() + ":unlock,time=" + (endTime - startTime));
            latch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class TryLockWorker implements Runnable {

    private CyclicBarrier barrier;
    
    private CountDownLatch latch;

    private Lock lock;

    public TryLockWorker(CyclicBarrier barrier, CountDownLatch latch, Lock lock) {
        this.barrier = barrier;
        this.latch = latch;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            long id = Thread.currentThread().getId();
            System.out.println(id + ":is wait");
            barrier.await();
            System.out.println(id + ":is go");

            String lockName = "lock1";
            long startTime = System.currentTimeMillis();
            boolean flag = lock.tryLock(lockName, 6000, 4000);
            if (flag) {
                System.out.println(id + ":lock");
                try {
                    try {
                        System.out.println(id + ":is doing");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unLock(lockName);
                }
                long endTime = System.currentTimeMillis();
                System.out.println(id + ":unlock,time=" + (endTime - startTime));
            } else {
                System.out.println(id + ":no lock");
            }
            latch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}