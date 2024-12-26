package io.github.me.liuchunfu.springboot.event.message.thread;

import com.lmax.disruptor.ExceptionHandler;
import io.github.me.liuchunfu.springboot.event.message.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 消息事件：异常处理策略
 * @author spring
 * date 2024-10-20
 */
@Slf4j
public class MessageAbortHandler implements RejectedExecutionHandler, ExceptionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        log.error("消息处理异常:Task " + r.toString() + " rejected from " + e.toString(), e);
    }

    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        // 处理事件时发生的异常
        String json = StrUtil.toJsonString(event);
        log.error("消息处理异常,event:" + json, ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        // 处理启动时发生的异常
        log.error("消息处理异常,处理启动时发生的异常", ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        // 处理关闭时发生的异常
        log.error("消息处理异常,处理关闭时发生的异常", ex);
    }

}
