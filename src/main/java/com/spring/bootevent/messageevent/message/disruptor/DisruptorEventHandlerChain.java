package com.spring.bootevent.messageevent.message.disruptor;

import com.lmax.disruptor.EventHandler;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息事件：额外Disruptor 注册事件
 */
@Getter
@Setter
public class DisruptorEventHandlerChain<T> {

    /**
     * buff长度
     */
    private int bufferSize = 1024;

    /**
     * 监听事件
     */
    private List<EventHandler<T>> handlerChain = new ArrayList<>();

    public DisruptorEventHandlerChain<T> addHandlerChain(EventHandler<T> handler) {
        handlerChain.add(handler);
        return this;
    }

    /**
     * buff长度
     * @param bufferSize buff长度必须是2的指数幂
     * @return
     */
    public DisruptorEventHandlerChain<T> bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

}

