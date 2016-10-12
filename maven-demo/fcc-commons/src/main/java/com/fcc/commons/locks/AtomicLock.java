//package com.fcc.commons.locks;
//
//import java.util.UUID;
//import java.util.concurrent.CyclicBarrier;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.AtomicStampedReference;
//
//public class AtomicLock implements Lock {
//
//	static AtomicStampedReference<Integer> lockStr = new AtomicStampedReference<Integer>(1, 0);
//	
//	@Override
//	public String lock(String lockKey) {
//		Integer m = lockStr.getReference();
//		if (m == 1) {
//			int timestamp = lockStr.getStamp();
//			System.out.println("m=" + m + ",timestamp=" + timestamp);
//			if (lockStr.compareAndSet(m, m + 1, timestamp,
//                    timestamp + 1)) {
//				return lockKey + System.currentTimeMillis();
////				return UUID.randomUUID().toString();
//			}
//		}
//		return null;
//	}
//
//	@Override
//	public boolean unLock(String lockKey) {
//		Integer m = lockStr.getReference();
//		if (m == 2) {
//			int timestamp = lockStr.getStamp();
//			if (lockStr.compareAndSet(m, m - 1, timestamp,
//                    timestamp + 1)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public String lock(String lockKey, long getTimeoutInMS, long lockTimeoutInMS) {
//		return null;
//	}
//
//	@Override
//	public boolean unLock(String lockKey, String identifier) {
//		return false;
//	}
//
//	public static void main(String[] args) {
//		Lock lock = new AtomicLock();
//		int num = 10;
//		CyclicBarrier barrier = new CyclicBarrier(num, new Runnable() {
//			@Override
//			public void run() {
//				System.out.println("go on");
//			}
//		});
//		for (int i = 1; i <= num; i++) {
//			new Thread(new AtmoicLockWorker(i, barrier, lock)).start();
//		}
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//}
//
//class AtmoicLockWorker implements Runnable {
//
//	private int id;
//	private CyclicBarrier barrier;
//	private Lock lock;
//	
//	public AtmoicLockWorker(int id, CyclicBarrier barrier, Lock lock) {
//		this.id = id;
//		this.barrier = barrier;
//		this.lock = lock;
//	}
//	
//	@Override
//	public void run() {
//		try {
//			System.out.println(id + ":is wait");
//			barrier.await();
//			System.out.println(id + ":is go");
//			
//			String lockName = "lock1";
//			long acquireTimeoutInMS = 200;
//			long lockTimeoutInMS = 6000;
////			String lockVal = lock.lock(lockName, acquireTimeoutInMS, lockTimeoutInMS);
//			long startTime = System.currentTimeMillis();
//			String lockVal = lock.lock(lockName);
//			if (lockVal != null) {
//				long endTime = System.currentTimeMillis();
//				System.out.println(id + ":lock=" + lockVal + ":time=" + (endTime - startTime));
//				try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
////				System.out.println(id + ":unlock:" + lock.unLock(lockName, lockVal));
//				System.out.println(id + ":unlock:" + lock.unLock(lockName));
//			} else {
//				System.out.println(id + ":nolock:");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//}
