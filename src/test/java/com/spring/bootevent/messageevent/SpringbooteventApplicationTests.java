package com.spring.bootevent.messageevent;

import com.spring.bootevent.messageevent.message.MessageEventProduct;
import com.spring.bootevent.messageevent.message.event.MessageEvent;
import com.spring.bootevent.messageevent.message.event.MessageWrap;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
class SpringbooteventApplicationTests {

    @Test
    void runTestChannelAsyncMessage() throws InterruptedException {
        int runTime = 10;
        for (int i = 1; i <= runTime; i++) {
            String s = "["+i+"]" + UUID.randomUUID().toString();
            MessageEvent messageEvent = new MessageEvent(this, MessageWrap.builder().eventId("test").event(s).build());
            MessageEventProduct.event().message(Boolean.TRUE).publishEvent(messageEvent);
            System.out.println(Thread.currentThread().getName() + "--request:" + s);
        }
        Thread.sleep(5000);
    }

    @Test
    void runTestChannelSyncMessage() throws InterruptedException {
        int runTime = 10;
        for (int i = 1; i <= runTime; i++) {
            String s = "["+i+"]" + UUID.randomUUID().toString();
            MessageEvent messageEvent = new MessageEvent(this, MessageWrap.builder().eventId("test").event(s).build());
            MessageEventProduct.event().sync().publishEvent(messageEvent);
            System.out.println(Thread.currentThread().getName() + "--request:" + s);
        }
        Thread.sleep(5000);
    }

}
