package com.spring.bootevent.messageevent.message.listener.dispatcher;

import com.spring.bootevent.messageevent.message.event.MessageWrapEvent;
import com.spring.bootevent.messageevent.message.listener.EventChanel;
import com.spring.bootevent.messageevent.message.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

/**
 * 消息事件：消息分发器(默认)
 * @author spring
 * date 2024-10-20
 */
@Slf4j
public class DefaultMessageDispatcher implements MessageDispatcher<MessageWrapEvent> {

    @Override
    public Object getEventId() {
        return EventChanel.CHANEL_DEFAULT;
    }

    @Override
    public void onEvent(MessageWrapEvent event) {
        String id = Optional.ofNullable(event).map(MessageWrapEvent::getEventId).orElse("null");
        String msg = Optional.ofNullable(event).map(MessageWrapEvent::getEvent).map(StrUtil::toJsonString).orElse("");
        if (log.isInfoEnabled()) {
            log.error("Disruptor消息暂未匹配监听器,eventId:{},event:{}", id, msg);
        }
    }

}
