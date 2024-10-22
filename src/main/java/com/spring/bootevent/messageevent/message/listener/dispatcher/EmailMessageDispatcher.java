package com.spring.bootevent.messageevent.message.listener.dispatcher;

import com.spring.bootevent.messageevent.message.event.MessageEvent;
import com.spring.bootevent.messageevent.message.event.MessageWrap;
import com.spring.bootevent.messageevent.message.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.yaml.snakeyaml.util.ArrayUtils;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;

/**
 * 消息事件：邮件消息分发器
 * @author spring
 * @date 2024-10-20
 */
@Slf4j
public class EmailMessageDispatcher implements MessageDispatcher<MessageEvent> {

    @Lazy
    @Resource
    JavaMailSender javaMailSender;

    @Lazy
    @Resource
    MailProperties mailProperties;

    @Override
    public Object getEventId() {
        return "email";
    }

    @Override
    public void onEvent(MessageEvent event) {
        MessageWrap wrap = event.getMessageWrap();
        SimpleMailMessage[] sendDatas;
        if (wrap.getEvent() != null && wrap.getEvent().getClass().isArray()) {
            sendDatas = (SimpleMailMessage[]) wrap.getEvent();
        } else {
            sendDatas = new SimpleMailMessage[] {(SimpleMailMessage) wrap.getEvent()};
        }
        String username = mailProperties.getUsername();
        for (SimpleMailMessage sendData : sendDatas) {
            sendData.setFrom(username);
        }
        String id = Optional.ofNullable(wrap).map(MessageWrap::getEventId).orElse("null");
        String msg = Optional.ofNullable(wrap).map(MessageWrap::getEvent).map(StrUtil::toJsonString).orElse("");
        log.debug("收到邮件消息,eventId:{},event:{}", id, msg);
        javaMailSender.send(sendDatas);
    }

}
