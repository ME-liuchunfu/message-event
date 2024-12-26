package io.github.me.liuchunfu.springboot.event.message.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.Objects;

/**
 * 启用 邮件池化 发送
 * date：2024年12月25日16:51:15
 */
@Configuration(proxyBeanMethods = false)
public class MessageMailSenderPropertiesConfiguration {

    @Autowired(required = false)
    JavaMailSender javaMailSender;

    @Bean
    @ConditionalOnMissingBean(MessageMailMultiSender.class)
    @ConditionalOnBean(MessageMailProperties.class)
    MessageMailMultiSender messageMailMultiSender(MessageMailProperties messageMailProperties) {
        MessageMailMultiSender messageMailMultiSender = new MessageMailMultiSender();
        messageMailMultiSender.flushMailPool(messageMailProperties);
        if (Objects.nonNull(javaMailSender)) {
            messageMailMultiSender.setJavaMailSender(javaMailSender);
        }
        return messageMailMultiSender;
    }

}
