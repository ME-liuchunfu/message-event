package io.github.me.liuchunfu.springboot.event.message.third.email;

/**
 * 简单消息
 * date：2024年12月25日09:58:34
 */
public class SimpleMessage extends EmailMessage {

    public SimpleMessage() {
        this.setMessageType(MessageType.SIMPLE);
    }

}
