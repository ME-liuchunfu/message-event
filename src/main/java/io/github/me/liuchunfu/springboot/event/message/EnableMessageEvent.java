package io.github.me.liuchunfu.springboot.event.message;

import io.github.me.liuchunfu.springboot.event.message.config.MessageDisruptorConfig;
import io.github.me.liuchunfu.springboot.event.message.config.MessageScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 消息事件：启动消息事件注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MessageScan.class, MessageDisruptorConfig.class})
public @interface EnableMessageEvent {

}
