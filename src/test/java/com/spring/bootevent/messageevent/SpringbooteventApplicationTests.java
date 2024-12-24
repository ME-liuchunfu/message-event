package com.spring.bootevent.messageevent;

import com.spring.bootevent.messageevent.message.SpringbooteventApplication;
import com.spring.bootevent.messageevent.message.event.MessageWrapEvent;
import com.spring.bootevent.messageevent.message.listener.EventChanel;
import com.spring.bootevent.messageevent.message.listener.MessageEventListener;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest(classes = SpringbooteventApplication.class)
class SpringbooteventApplicationTests {

    @Resource
    MessageEventListener messageEventListener;

    @Test
    void runTestChannelAsyncMessage() throws InterruptedException {
        int runTime = 10;
        for (int i = 1; i <= runTime; i++) {
            String s = "["+i+"]" + UUID.randomUUID().toString();
            messageEventListener.publishEvent("", s);
            System.out.println(Thread.currentThread().getName() + "--request:" + s);
        }
        Thread.sleep(5000);
    }

    @Test
    void runTestChannelEmailMessage() throws InterruptedException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setText("你好啊");
        simpleMailMessage.setSubject("这里是标题");
        simpleMailMessage.setTo("429829320@qq.com");
        messageEventListener.publishEmail(simpleMailMessage);
        System.out.println(Thread.currentThread().getName() + "--request:");
        Thread.sleep(5000);
    }

}
