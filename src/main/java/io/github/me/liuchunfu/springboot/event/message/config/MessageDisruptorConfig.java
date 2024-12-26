package io.github.me.liuchunfu.springboot.event.message.config;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.github.me.liuchunfu.springboot.event.message.disruptor.DisruptorEventHandlerChain;
import io.github.me.liuchunfu.springboot.event.message.disruptor.DisruptorMessageEventFactory;
import io.github.me.liuchunfu.springboot.event.message.disruptor.DisruptorMessageWrapEventHandler;
import io.github.me.liuchunfu.springboot.event.message.event.MessageWrapEvent;
import io.github.me.liuchunfu.springboot.event.message.thread.MessageAbortHandler;
import io.github.me.liuchunfu.springboot.event.message.thread.MessageThreadFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import java.util.List;

/**
 * 消息事件：消息中心配置
 *
 *
 * @author spring
 * date 2024-12-24
 */
@Configuration
public class MessageDisruptorConfig {

    @Bean("messageDisruptor")
    @ConditionalOnMissingBean
    public Disruptor<MessageWrapEvent> messageDisruptor(DisruptorMessageWrapEventHandler disruptorMessageWrapEventHandler,
                                                        MessageThreadFactory messageThreadFactory,
                                                        DisruptorEventHandlerChain<MessageWrapEvent> disruptorEventHandlerChain,
                                                        MessageAbortHandler messageAbortHandler) {
        DisruptorMessageEventFactory factory = new DisruptorMessageEventFactory();
        int bufferSize = disruptorEventHandlerChain.getBufferSize();
        Disruptor<MessageWrapEvent> disruptor = new Disruptor<>(factory, bufferSize, messageThreadFactory, ProducerType.MULTI, new YieldingWaitStrategy());
        disruptor.handleEventsWith(disruptorMessageWrapEventHandler);

        // 其他的订阅消息
        List<? extends EventHandler<MessageWrapEvent>> handlerChain = disruptorEventHandlerChain.getHandlerChain();
        if (!CollectionUtils.isEmpty(handlerChain)) {
            for (EventHandler<MessageWrapEvent> handler : handlerChain) {
                disruptor.handleEventsWith(handler);
            }
        }
        disruptor.setDefaultExceptionHandler(messageAbortHandler);
        disruptor.start();
        return disruptor;
    }

    @Bean("messageAbortHandler")
    @ConditionalOnMissingBean
    public MessageAbortHandler messageAbortHandler() {
        return new MessageAbortHandler();
    }

    @Bean("disruptorEventHandlerChain")
    @ConditionalOnMissingBean
    public DisruptorEventHandlerChain<MessageWrapEvent> disruptorEventHandlerChain() {
        DisruptorEventHandlerChain<MessageWrapEvent> disruptorEventHandlerChain = new DisruptorEventHandlerChain<>();
        int floor = 1024;
        disruptorEventHandlerChain.bufferSize(1024 * floor);
        return disruptorEventHandlerChain;
    }

    @Bean("messageThreadFactory")
    @ConditionalOnMissingBean
    public MessageThreadFactory messageThreadFactory() {
        return new MessageThreadFactory("Disruptor");
    }

    @Bean("disruptorMessageWrapEventHandler")
    @ConditionalOnMissingBean
    public DisruptorMessageWrapEventHandler disruptorMessageWrapEventHandler() {
        return new DisruptorMessageWrapEventHandler();
    }

    @Bean("messageRingBuffer")
    @ConditionalOnMissingBean
    public RingBuffer<MessageWrapEvent> messageRingBuffer(Disruptor<MessageWrapEvent> messageDisruptor) {
        return messageDisruptor.getRingBuffer();
    }


}
