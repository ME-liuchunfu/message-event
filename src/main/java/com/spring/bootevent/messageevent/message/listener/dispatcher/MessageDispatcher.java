package com.spring.bootevent.messageevent.message.listener.dispatcher;

/**
 * 消息事件：消息分发器
 * @author spring
 * date 2024-10-20
 */
public interface MessageDispatcher<T> {

    String DEFAULT_EVENT_ID = "DEFAULT";

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

}
