package io.github.me.liuchunfu.springboot.event.message.listener;

import com.lmax.disruptor.RingBuffer;
import io.github.me.liuchunfu.springboot.event.message.event.MessageWrapEvent;
import io.github.me.liuchunfu.springboot.event.message.exception.MessageAbortExecution;
import io.github.me.liuchunfu.springboot.event.message.third.email.EmailMessage;
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
     * @param uuid 事件id
     * @param channel 消息通道
     * @param wrap 消息
     */
    public void publishEvent(String channel, Object wrap, Object uuid) {
        ringBuffer.publishEvent((event, sequence) -> {
            event.setEventId(channel);
            event.setEvent(wrap);
            event.setUuid(uuid);
        });
    }

    /**
     * 邮件发送
     * @param sender 邮件发送者id
     * @param event 消息
     */
    public void publishEmail(Object sender, EmailMessage event) {
        this.publishEvent(EventChanel.CHANEL_EMAIL, event, sender);
    }

    /**
     * 邮件发送
     * @param sender 邮件发送者id
     * @param event 消息
     */
    public void publishEmail(Object sender, EmailMessage... event) {
        if (event.length == 0) {
            throw new MessageAbortExecution("邮件消息为空");
        }
        this.publishEvent(EventChanel.CHANEL_EMAIL, event, sender);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ins = this;
    }

}
