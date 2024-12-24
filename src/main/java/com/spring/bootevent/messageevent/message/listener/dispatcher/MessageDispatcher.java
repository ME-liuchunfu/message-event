package com.spring.bootevent.messageevent.message.listener.dispatcher;

import com.lmax.disruptor.EventHandler;

/**
 * 消息事件：消息分发器
 * @author spring
 * date 2024-10-20
 */
public interface MessageDispatcher<T> extends EventHandler<T> {

    /**
     * 获取事件 id
     * @return 返回事件通道标识
     */
    Object getEventId();

    /**
     * 事件分发
     * @param event 消息
     */
    void onEvent(T event);

    @Override
    default void onEvent(T event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

}
