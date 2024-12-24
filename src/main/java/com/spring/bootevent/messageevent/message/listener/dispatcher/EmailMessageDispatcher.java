package com.spring.bootevent.messageevent.message.listener.dispatcher;

import com.spring.bootevent.messageevent.message.event.MessageWrapEvent;
import com.spring.bootevent.messageevent.message.listener.EventChanel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import javax.annotation.Resource;

/**
 * 消息事件：邮件消息分发器
 * @author spring
 * date 2024-10-20
 */
@Slf4j
public class EmailMessageDispatcher implements MessageDispatcher<MessageWrapEvent> {

    @Resource
    JavaMailSender javaMailSender;

    @Resource
    MailProperties mailProperties;

    @Override
    public Object getEventId() {
        return EventChanel.CHANEL_EMAIL;
    }

    @Override
    public void onEvent(MessageWrapEvent event) {
        SimpleMailMessage[] sendDatas;
        if (event.getEvent().getClass().isArray()) {
            sendDatas = (SimpleMailMessage[]) event.getEvent();
        } else {
            sendDatas = new SimpleMailMessage[] {(SimpleMailMessage) event.getEvent()};
        }
        String username = mailProperties.getUsername();
        for (SimpleMailMessage sendData : sendDatas) {
            sendData.setFrom(username);
        }
        javaMailSender.send(sendDatas);
    }

}
