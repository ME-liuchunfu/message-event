package com.spring.bootevent.messageevent.message.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 消息事件：线程池配置
 * @author spring
 * @date 2024-10-20
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "message.event.message.pool")
public class MessagePoolProperties {

    /**
     * 核心线程数，默认为8
     */
    private int coreSize = 8;

    /**
     * 最大线程数，默认为1024
     */
    private int maxSize = 1024;

    /**
     * 队列容量，默认为1024
     */
    private int queueCapacity;

    /**
     * 线程活 keep-alive 时间，默认是60(单位毫秒)
     */
    private int keepAlive = 60;

}
