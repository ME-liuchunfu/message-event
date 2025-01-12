package io.github.me.liuchunfu.springboot.event.message.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Thread.NORM_PRIORITY;

/**
 * 消息事件： 线程池工厂
 * @author spring
 * date 2024-10-20
 */
public class MessageThreadFactory implements ThreadFactory {

    AtomicLong threadCount = new AtomicLong(0);

    String pref;

    public MessageThreadFactory(String pref) {
        this.pref = pref;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, pref + threadCount.incrementAndGet());
        if (thread.isDaemon()) {
            thread.setDaemon(Boolean.FALSE);
        }
        if (thread.getPriority() != NORM_PRIORITY) {
            thread.setPriority(NORM_PRIORITY);
        }
        return thread;
    }

}
