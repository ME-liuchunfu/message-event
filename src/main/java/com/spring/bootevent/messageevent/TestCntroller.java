package com.spring.bootevent.messageevent;

import com.spring.bootevent.messageevent.message.event.MessageEvent;
import com.spring.bootevent.messageevent.message.MessageEventProduct;
import com.spring.bootevent.messageevent.message.event.MessageWrap;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class TestCntroller {

    AtomicLong incr = new AtomicLong(0);

    @GetMapping
    public String get() {
        String s = "["+incr.incrementAndGet()+"]" + UUID.randomUUID().toString();
        MessageEvent messageEvent = new MessageEvent(this, MessageWrap.builder().eventId("test").event(s).build());
        MessageEventProduct.event().message(Boolean.TRUE).publishEvent(messageEvent);
        System.out.println(Thread.currentThread().getName() + "--request:" + s);
        return s;
    }

    @GetMapping("/email/{email}")
    public String email(@PathVariable String email) {
        String message = "测试下邮件发送";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(message);
        simpleMailMessage.setText(String.valueOf(System.currentTimeMillis()));
        MessageEvent messageEvent = new MessageEvent(this, MessageWrap.builder().eventId("email").event(simpleMailMessage).build());
        MessageEventProduct.event().message(Boolean.TRUE).publishEvent(messageEvent);
        System.out.println(message);
        return message;
    }

    @Override
    public String toString() {
        return "TestCntroller";
    }
}
