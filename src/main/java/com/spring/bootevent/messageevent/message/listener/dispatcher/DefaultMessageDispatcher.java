package com.spring.bootevent.messageevent.message.listener.dispatcher;

import com.spring.bootevent.messageevent.TestCntroller;
import com.spring.bootevent.messageevent.message.event.MessageEvent;
import com.spring.bootevent.messageevent.message.event.MessageWrap;
import com.spring.bootevent.messageevent.message.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 消息事件：消息分发器(默认)
 * @author spring
 * @date 2024-10-20
 */
@Slf4j
public class DefaultMessageDispatcher implements MessageDispatcher<MessageEvent> {

    @Override
    public Object getEventId() {
        return DEFAULT_EVENT_ID;
    }

    @Override
    public void onEvent(MessageEvent event) {
        MessageWrap wrap = event.getMessageWrap();
        String id = Optional.ofNullable(wrap).map(MessageWrap::getEventId).orElse("null");
        String msg = Optional.ofNullable(wrap).map(MessageWrap::getEvent).map(StrUtil::toJsonString).orElse("");
        log.error("消息暂未匹配监听器,eventId:{},event:{}", id, msg);
    }

}
