package com.spring.bootevent.messageevent.message.executor;

import com.spring.bootevent.messageevent.message.thread.SimpleThreadExecutor;
import com.spring.bootevent.messageevent.message.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 事件广播：异步消息广播
 * 需要定义：messageThreadPoolExecutor 线程池，如果没有定义线程池，默认使用
 * @author spring
 * @date 2024-10-20
 */
@Slf4j
public class AsyncMessageExecutor extends MessageExecutor {

    public static final String ASYNC_BEAN_NAME = "messageThreadPoolExecutor";

    @Override
    public void onEvent(Object event) {
        boolean execDefault = Boolean.FALSE;
        try {
            ExecutorService executor = SpringUtil.getBean(ASYNC_BEAN_NAME, ExecutorService.class);
            executor.submit(()->message(event));
        } catch (Exception e) {
            log.debug("异步消息线程执行器错误【未定义线程池】msg:{}", e.getMessage());
            execDefault = Boolean.TRUE;
        }
        if (execDefault) {
            SimpleThreadExecutor.start(()-> message(event));
        }
    }
}
