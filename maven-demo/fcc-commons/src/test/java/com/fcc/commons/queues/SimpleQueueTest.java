package com.fcc.commons.queues;

import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SimpleQueueTest {
    
    static ThreadPoolExecutor pool = new ThreadPoolExecutor(20, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public static void testMain(Queue queue, boolean isOffer) {
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
            if (isOffer == false) {
                pool.execute(new QueryPollWorker(barrier, latch, queue));
            } else {
                pool.execute(new QueryOfferWorker(barrier, latch, queue));
            }
        }
        try {
            System.out.println("main is wait " + num + ",working");
            latch.await();
            System.out.println("main is end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void test() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testQueue() {
        Queue queue = new SimpleQueue();
        testMain(queue, true);
        System.out.println("size=" + queue.size("lock1"));
        testMain(queue, false);
        pool.shutdown();
    }

}

class QueryOfferWorker implements Runnable {

    private CyclicBarrier barrier;
    
    private CountDownLatch latch;

    private Queue queue;

    public QueryOfferWorker(CyclicBarrier barrier, CountDownLatch latch, Queue queue) {
        this.barrier = barrier;
        this.latch = latch;
        this.queue = queue;
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
            for (int i = 0; i < 5; i++) {
                String val = id + ":" + i;
                // id + ":" + i
//                RedisQueue data = new RedisQueue();
                if (queue.offer(lockName, val)) {
                    System.out.println(id + ":offer:" + val);
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println(id + ",time=" + (endTime - startTime));
            
            latch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class QueryPollWorker implements Runnable {

    private CyclicBarrier barrier;
    
    private CountDownLatch latch;

    private Queue queue;

    public QueryPollWorker(CyclicBarrier barrier, CountDownLatch latch, Queue queue) {
        this.barrier = barrier;
        this.latch = latch;
        this.queue = queue;
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
            while(true) {
               Object obj = queue.poll(lockName);
               if (obj != null) {
                   System.out.println(id + ":get:" + obj);
               } else {
                   break;
               }
            }
            long endTime = System.currentTimeMillis();
            System.out.println(id + ",time=" + (endTime - startTime));
            
            latch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
