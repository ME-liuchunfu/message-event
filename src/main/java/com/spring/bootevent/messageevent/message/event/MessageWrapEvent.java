package com.spring.bootevent.messageevent.message.event;

import lombok.*;

/**
 * 消息体
 * @author spring
 * date 2024-10-20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageWrapEvent {

    private String eventId;

    private Object event;

    private Object uuid;

}
