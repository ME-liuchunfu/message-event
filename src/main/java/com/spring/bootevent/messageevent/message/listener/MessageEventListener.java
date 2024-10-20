package com.spring.bootevent.messageevent.message.listener;

import com.spring.bootevent.messageevent.message.event.MessageEvent;
import com.spring.bootevent.messageevent.message.event.MessageWrap;
import com.spring.bootevent.messageevent.message.listener.dispatcher.MessageDispatcher;
import com.spring.bootevent.messageevent.message.util.SpringUtil;
import com.spring.bootevent.messageevent.message.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.util.*;


/**
 * 消息事件：事件监听器
 * @author spring
 * @date 2024-10-20
 */
@Component
@Slf4j
public class MessageEventListener implements ApplicationListener<MessageEvent> {

    private static final String NULL = "null";
    private static final String EMPTY = "";

    private Map<Object, MessageDispatcher<MessageEvent>> dispatcherMap;

    private MessageDispatcher<MessageEvent> defaultDispatcher;

    public MessageEventListener() {
        ServiceLoader<MessageDispatcher> loader = ServiceLoader.load(MessageDispatcher.class);
        dispatcherMap = new HashMap<>();
        Iterator<MessageDispatcher> iterator = loader.iterator();
        while (iterator.hasNext()) {
            MessageDispatcher dispatcher = iterator.next();
            SpringUtil.registerBean(dispatcher.getClass().getName(), dispatcher);
            dispatcherMap.put(dispatcher.getEventId(), dispatcher);
        }
        defaultDispatcher = dispatcherMap.get(MessageDispatcher.DEFAULT_EVENT_ID);
    }

    @Override
    public void onApplicationEvent(MessageEvent event) {
        MessageWrap wrap = event.getMessageWrap();
        String id = Optional.ofNullable(wrap).map(MessageWrap::getEventId).orElse(NULL);
        String msg = Optional.ofNullable(wrap).map(MessageWrap::getEvent).map(StrUtil::toJsonString).orElse(EMPTY);
        log.debug("收到广播消息,eventId:{},event:{}", id, msg);
        dispatcherMap.getOrDefault(id, defaultDispatcher).onEvent(event);
    }

}
