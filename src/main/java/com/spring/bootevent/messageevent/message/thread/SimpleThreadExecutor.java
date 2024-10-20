package com.spring.bootevent.messageevent.message.thread;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 简单线程执行器
 *
 * @author spring
 * @date 2024-10-20
 */
public class SimpleThreadExecutor extends Thread {

    public static final String PREF = "simple-messageThreadPoolExecutor";

    public static final AtomicLong threadCount = new AtomicLong(0);

    public SimpleThreadExecutor(Runnable runnable) {
        super(runnable, String.format("%s-%d", PREF, threadCount.incrementAndGet()));
    }

    public static SimpleThreadExecutor thread(Runnable runnable) {
        return new SimpleThreadExecutor(runnable);
    }

    public static void start(Runnable runnable) {
        thread(runnable).start();
    }

}
