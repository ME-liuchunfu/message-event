package com.spring.bootevent.messageevent.message.disruptor;

import com.lmax.disruptor.EventHandler;
import com.spring.bootevent.messageevent.message.event.MessageWrapEvent;
import com.spring.bootevent.messageevent.message.listener.EventChanel;
import com.spring.bootevent.messageevent.message.listener.dispatcher.MessageDispatcher;
import com.spring.bootevent.messageevent.message.util.SpringUtil;
import com.spring.bootevent.messageevent.message.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Disruptor: 默认的日志记录
 */
@Slf4j
public class DisruptorMessageWrapEventHandler implements EventHandler<MessageWrapEvent>{

    private Map<Object, MessageDispatcher<MessageWrapEvent>>  channelMap = new HashMap<>();

    private MessageDispatcher<MessageWrapEvent> defaultDispatcher;

    public DisruptorMessageWrapEventHandler() {
        ServiceLoader<MessageDispatcher> loader = ServiceLoader.load(MessageDispatcher.class);
        Iterator<MessageDispatcher> iterator = loader.iterator();
        while (iterator.hasNext()) {
            MessageDispatcher next = iterator.next();
            channelMap.put(next.getEventId(), next);
            SpringUtil.registerBean(next.getClass().getSimpleName(), next);
        }
        defaultDispatcher = channelMap.get(EventChanel.CHANEL_DEFAULT);
    }

    @Override
    public void onEvent(MessageWrapEvent event, long sequence, boolean endOfBatch) throws Exception {
        if (Objects.isNull(event)) {
            log.warn("Processing Disruptor message,event is null");
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Processing Disruptor message,ID:{},event:{}", event.getEventId(), parseMsg(event.getEvent()));
        }
        MessageDispatcher<MessageWrapEvent> dispatcher = channelMap.getOrDefault(event.getEventId(), defaultDispatcher);
        dispatcher.onEvent(event);
    }

    private String parseMsg(Object obj) {
        String jsonString = StrUtil.toJsonString(obj);
        if (jsonString.length() > Short.MAX_VALUE) {
            return jsonString.substring(0, Short.MAX_VALUE);
        }
        return jsonString;
    }

}
