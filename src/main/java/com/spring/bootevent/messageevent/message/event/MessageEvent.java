package com.spring.bootevent.messageevent.message.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 消息事件：消息
 * @author spring
 * date 2024-10-20
 */
@Getter
@Setter
public class MessageEvent extends ApplicationEvent {

    private MessageWrap messageWrap;

    public MessageEvent(Object source, MessageWrap wrap) {
        super(source);
        this.messageWrap = wrap;
    }

}
