package com.spring.bootevent.messageevent.message.listener;

import com.lmax.disruptor.RingBuffer;
import com.spring.bootevent.messageevent.message.event.MessageWrapEvent;
import com.spring.bootevent.messageevent.message.third.email.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.util.Optional;


/**
 * 消息事件：事件监听器
 * @author spring
 * date 2024-10-20
 */
@Component
@Slf4j
public class MessageEventListener implements ApplicationContextAware {

    public static MessageEventListener ins = null;

    public static Optional<MessageEventListener> getIns() {
        return Optional.ofNullable(ins);
    }

    private final RingBuffer<MessageWrapEvent> ringBuffer;

    public MessageEventListener(RingBuffer<MessageWrapEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 消息广播发送
     * @param channel 消息通道
     * @param wrap 消息
     */
    public void publishEvent(String channel, Object wrap) {
        ringBuffer.publishEvent((event, sequence) -> {
            event.setEventId(channel);
            event.setEvent(wrap);
        });
    }

    /**
     * 邮件发送
     * @param event 消息
     */
    public void publishEmail(EmailMessage event) {
        this.publishEvent(EventChanel.CHANEL_EMAIL, event);
    }

    /**
     * 邮件发送
     * @param event 消息
     */
    public void publishEmail(EmailMessage... event) {
        this.publishEvent(EventChanel.CHANEL_EMAIL, event);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ins = this;
    }

}
