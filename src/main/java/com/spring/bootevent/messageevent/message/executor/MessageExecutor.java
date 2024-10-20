package com.spring.bootevent.messageevent.message.executor;

import com.spring.bootevent.messageevent.message.event.MessageEvent;
import com.spring.bootevent.messageevent.message.event.MessageWrap;
import com.spring.bootevent.messageevent.message.util.SpringUtil;
import com.spring.bootevent.messageevent.message.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 事件驱动：消息执行器
 * @author spring
 * @date 2024-10-20
 */
@Slf4j
public abstract class MessageExecutor {

    private static final String NULL = "null";
    private static final String EMPTY = "";

    /**
     * 发布消息
     * @param event 消息
     */
    public void publishEvent(MessageEvent event) {
        try {
            this.onEvent(event);
        } catch (Exception e) {
            MessageWrap wrap = event.getMessageWrap();
            String id = Optional.ofNullable(wrap).map(MessageWrap::getEventId).orElse(NULL);
            String msg = Optional.ofNullable(wrap).map(MessageWrap::getEvent).map(StrUtil::toJsonString).orElse(EMPTY);
            log.error("消息事件发布异常,eventId:{},event:{}", id, msg);
        }
    }

    /**
     * 消息处理
     * @param event
     */
    public abstract void onEvent(Object event);

    /**
     * 消息广播
     * @param event
     */
    protected void message(Object event) {
        SpringUtil.publishEvent(event);
    }

}
