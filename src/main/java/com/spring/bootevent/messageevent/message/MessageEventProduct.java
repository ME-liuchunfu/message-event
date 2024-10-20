package com.spring.bootevent.messageevent.message;

import com.spring.bootevent.messageevent.message.executor.AsyncMessageExecutor;
import com.spring.bootevent.messageevent.message.executor.MessageExecutor;
import com.spring.bootevent.messageevent.message.executor.SyncMessageExecutor;

/**
 * 消息事件：事件生产者
 * @author spring
 * @date 2024-10-20
 */
public class MessageEventProduct {

    private static class MessageEventProductHandler {
        private static MessageEventProduct INS = new MessageEventProduct();
    }

    public static MessageEventProduct event() {
        return MessageEventProductHandler.INS;
    }

    public MessageExecutor message(boolean isAsync) {
        return isAsync ? async() : sync();
    }

    public MessageExecutor async() {
        return new AsyncMessageExecutor();
    }

    public MessageExecutor sync() {
        return new SyncMessageExecutor();
    }

}
