package com.freelancer.misc.lock;

import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Soyee.Deng on 2016/3/17.
 */
public class LockBenchMark {

    public static void main(String[] args) throws Exception {

        final Integer threadsCount = 20;
        final Integer iteration = 1000000;
        final AtomicBoolean done = new AtomicBoolean(false);
        final AtomicBoolean done2 = new AtomicBoolean(false);
        final Object lock = new Object();

        class SynchronizedThread extends Thread {

            private static final String THREAD_NAME_PREFIX = "SynchronizedThread";

            public SynchronizedThread() {
                setName(THREAD_NAME_PREFIX + "----");
            }

            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    // Wake up early
                }
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                int counter = 0;
                for (int i = 0; i < iteration; i++) {
                    synchronized (lock) {
                        counter++;
                    }
                }
                done.set(true);
                stopWatch.stop();
                System.err.println("Thread : " + Thread.currentThread().getName() + " ----------------- SynchronizedThread consumed milliseconds : " + stopWatch.getTime());
            }
        };

        class SynchronizedPeerThread extends Thread {

            private static final String THREAD_NAME_PREFIX = "SynchronizedPeerThread";

            public SynchronizedPeerThread() {
                setName(THREAD_NAME_PREFIX + "----");
            }

            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    // Wake up early
                }
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                int counter = 0;
                while (!done.get()) {
                    synchronized (lock) {
                        counter++;
                    }
                }
                done.set(true);
                stopWatch.stop();
                System.err.println("Thread : " + Thread.currentThread().getName() + " ----------------- SynchronizedPeerThread Maximum counter : " + counter);
                System.err.println("Thread : " + Thread.currentThread().getName() + " ----------------- SynchronizedPeerThread consumed milliseconds : " + stopWatch.getTime());
            }
        };

        System.out.println("----------------------------------------------- Separator ------------------------------------------");
        Thread synchronizedThread = new SynchronizedThread();
        Thread synchronizedPeerThread = new SynchronizedPeerThread();
        synchronizedThread.start();
        synchronizedPeerThread.start();
        synchronizedThread.join();
        synchronizedPeerThread.join();

        final ReentrantLock reentrantLock = new ReentrantLock();

        class ReentrantLockThread extends Thread {

            private static final String THREAD_NAME_PREFIX = "ReentrantLockThread";

            public ReentrantLockThread() {
                setName(THREAD_NAME_PREFIX + "----");
            }

            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    // Wake up early
                }
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                int counter = 0;
                for (int i = 0; i < iteration; i++) {
                    reentrantLock.lock();
                    counter++;
                    reentrantLock.unlock();
                }
                done2.set(true);
                stopWatch.stop();
                System.err.println("Thread : " + Thread.currentThread().getName() + " ----------------- ReentrantLockThread consumed milliseconds : " + stopWatch.getTime());
            }
        };

        class ReentrantLockPeerThread extends Thread {

            private static final String THREAD_NAME_PREFIX = "ReentrantLockPeerThread";

            public ReentrantLockPeerThread() {
                setName(THREAD_NAME_PREFIX + "----");
            }

            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    // Wake up early
                }
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                int counter = 0;
                while (!done2.get()) {
                    reentrantLock.lock();
                    counter++;
                    reentrantLock.unlock();
                }
                done2.set(true);
                stopWatch.stop();
                System.err.println("Thread : " + Thread.currentThread().getName() + " ----------------- ReentrantLockPeerThread Maximum counter : " + counter);
                System.err.println("Thread : " + Thread.currentThread().getName() + " ----------------- ReentrantLockPeerThread consumed milliseconds : " + stopWatch.getTime());
            }
        };

        System.out.println("----------------------------------------------- Separator ------------------------------------------");
        Thread reentrantLockThread = new ReentrantLockThread();
        Thread reentrantLockPeerThread = new ReentrantLockPeerThread();
        reentrantLockThread.start();
        reentrantLockPeerThread.start();
        reentrantLockThread.join();
        reentrantLockPeerThread.join();

        /// Test bulk threads to compete against one shared resource
        System.out.println("----------------------------------------------- Separator ------------------------------------------");
        Thread[] synchronizedThreads = new SynchronizedThread[threadsCount];
        Thread[] synchronizedPeerThreads = new SynchronizedPeerThread[threadsCount];

        // Start and join into the current thread
        for (int index = 0; index < threadsCount; index++) {
            synchronizedThreads[index] = new SynchronizedThread();
            synchronizedPeerThreads[index] = new SynchronizedPeerThread();
            synchronizedThreads[index].start();
            synchronizedPeerThreads[index].start();
        }

        for (int index = 0; index < threadsCount; index++) {
            synchronizedThreads[index].join();
            synchronizedPeerThreads[index].join();
        }

        /// Test bulk threads to compete against one shared resource
        System.out.println("----------------------------------------------- Separator ------------------------------------------");
        Thread[] reentrantLockThreads = new ReentrantLockThread[threadsCount];
        Thread[] reentrantLockPeerThreads = new ReentrantLockPeerThread[threadsCount];

        // Start and join into the current thread
        for (int index = 0; index < threadsCount; index++) {
            reentrantLockThreads[index] = new ReentrantLockThread();
            reentrantLockPeerThreads[index] = new ReentrantLockPeerThread();
            reentrantLockThreads[index].start();
            reentrantLockPeerThreads[index].start();
        }

        for (int index = 0; index < threadsCount; index++) {
            reentrantLockThreads[index].join();
            reentrantLockPeerThreads[index].join();
        }

    }
}
