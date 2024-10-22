package com.spring.bootevent.messageevent.message;

import com.spring.bootevent.messageevent.message.config.MessageConfiguration;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * 消息事件：启动消息事件注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MessageConfiguration.class})
public @interface EnableMessageEvent {

}
