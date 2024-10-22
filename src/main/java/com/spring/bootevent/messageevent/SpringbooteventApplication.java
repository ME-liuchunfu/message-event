package com.spring.bootevent.messageevent;

import com.spring.bootevent.messageevent.message.EnableMessageEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMessageEvent
public class SpringbooteventApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbooteventApplication.class, args);
    }

}
