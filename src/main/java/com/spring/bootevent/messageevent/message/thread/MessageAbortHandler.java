package com.spring.bootevent.messageevent.message.thread;

import com.spring.bootevent.messageevent.message.exception.MessageAbortExecution;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 消息事件：异常处理策略
 * @author spring
 * @date 2024-10-20
 */
public class MessageAbortHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        throw new MessageAbortExecution("Task " + r.toString() + " rejected from " + e.toString());
    }

}
