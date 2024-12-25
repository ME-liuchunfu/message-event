package com.spring.bootevent.messageevent.message.third.email;

/**
 * markdown消息
 * date：2024年12月25日09:58:34
 */
public class MarkdownMessage extends EmailMessage {

    public MarkdownMessage() {
        this.setMessageType(MessageType.MARKDOWN);
    }

}
