package io.github.me.liuchunfu.springboot.event.message.disruptor;

import com.lmax.disruptor.EventFactory;
import io.github.me.liuchunfu.springboot.event.message.event.MessageWrapEvent;

/**
 * Disruptor消息工厂
 * date: 2024年12月24日15:58:02
 */
public class DisruptorMessageEventFactory implements EventFactory<MessageWrapEvent> {

    @Override
    public MessageWrapEvent newInstance() {
        return new MessageWrapEvent();
    }

}
