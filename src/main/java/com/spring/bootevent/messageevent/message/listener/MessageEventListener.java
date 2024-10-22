package com.spring.bootevent.messageevent.message.listener;

import com.spring.bootevent.messageevent.message.config.MessageConfiguration;
import com.spring.bootevent.messageevent.message.event.MessageEvent;
import com.spring.bootevent.messageevent.message.event.MessageWrap;
import com.spring.bootevent.messageevent.message.listener.dispatcher.MessageDispatcher;
import com.spring.bootevent.messageevent.message.util.SpringUtil;
import com.spring.bootevent.messageevent.message.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;


/**
 * 消息事件：事件监听器
 * @author spring
 * date 2024-10-20
 */
@Component
@Slf4j
public class MessageEventListener implements ApplicationListener<MessageEvent> {

    private static final String NULL = "null";
    private static final String EMPTY = "";

    private Map<Object, MessageDispatcher<MessageEvent>> dispatcherMap;

    private MessageDispatcher<MessageEvent> defaultDispatcher;

    @Resource
    MessageConfiguration messageConfiguration;

    @Lazy
    @PostConstruct
    public void init() {
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
        if (messageConfiguration.getLogs().isLog()) {
            log.debug("收到广播消息,eventId:{},event:{}", id, msg);
        }
        dispatcherMap.getOrDefault(id, defaultDispatcher).onEvent(event);
    }

    /**
     * 手动注册通道管理器
     * @param dispatcher    通道实例对象
     * @param registerSpring  是否向spring容器注册
     */
    public void registerBean(MessageDispatcher<MessageEvent> dispatcher, boolean registerSpring) {
        if (registerSpring) {
            if (!SpringUtil.containsBean(dispatcher.getClass())) {
                SpringUtil.registerBean(dispatcher.getClass().getName(), dispatcher);
            }
        }
        dispatcherMap.put(dispatcher.getEventId(), dispatcher);
    }

    /**
     * 手动注册通道管理器，会向spring容器注册bean
     * @param dispatcher    通道实例对象
     */
    public void registerBean(MessageDispatcher<MessageEvent> dispatcher) {
        registerBean(dispatcher, Boolean.TRUE);
    }

    /**
     * 手动注销通道管理器，会向spring容器注销bean
     * @param id    通道唯一标识（如果该通道的标识与已存在的标识同名，则会覆盖掉已存在标识）
     */
    public void unregisterBean(Object id) {
        dispatcherMap.remove(id);
    }

    /**
     * 手动注销通道管理器，会向spring容器注销bean
     * @param dispatcher    通道实例对象
     */
    public void unregisterBean(MessageDispatcher<MessageEvent> dispatcher) {
        dispatcherMap.remove(dispatcher.getEventId());
    }

    /**
     * 获取所有通道类名称，该名称为自定义通道名称
     * @return 返会当前注册的通道标识列表
     */
    public Set<Object> findAllDispatcherChannel() {
        return new HashSet<>(dispatcherMap.keySet());
    }

}
