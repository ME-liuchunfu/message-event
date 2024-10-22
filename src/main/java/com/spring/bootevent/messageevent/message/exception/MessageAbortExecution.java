package com.spring.bootevent.messageevent.message.exception;

/**
 * 消息事件：异常处理类
 * @author spring
 * date 2024-10-20
 */
public class MessageAbortExecution extends RuntimeException {

    public MessageAbortExecution() {
    }

    public MessageAbortExecution(String message) {
        super(message);
    }

    public MessageAbortExecution(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageAbortExecution(Throwable cause) {
        super(cause);
    }

    public MessageAbortExecution(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
