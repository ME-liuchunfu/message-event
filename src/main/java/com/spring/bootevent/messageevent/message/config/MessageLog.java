package com.spring.bootevent.messageevent.message.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 消息事件：消息配置中心，日志配置
 * @author spring
 * date 2024-10-20
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "message.event.message.logs")
public class MessageLog {

    /**
     * 开启消息事件日志推送
     */
    private boolean log = Boolean.TRUE;

    /**
     * 开启默认找不到监听通道日志推送
     */
    private boolean defaultLog = Boolean.TRUE;

}
