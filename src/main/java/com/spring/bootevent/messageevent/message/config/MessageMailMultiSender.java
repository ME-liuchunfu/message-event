package com.spring.bootevent.messageevent.message.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多邮件发送
 * date: 2024年12月25日16:54:31
 */
@Slf4j
@Getter
@Setter
public class MessageMailMultiSender {

    private final Map<Object, JavaMailSender> javaMallSenderPool = new ConcurrentHashMap<>();

    /**
     * 默认的发送
     */
    private JavaMailSender javaMailSender;

    private MessageMailProperties messageMailProperties;

    public void flushMailPool(MessageMailProperties messageMailProperties) {
        try {
            int beforeSize = javaMallSenderPool.size();
            Set<Object> beforeKeySet = new HashSet<>(javaMallSenderPool.keySet());
            Map<String, MessageMailProperties.MailProperties> messagePool = messageMailProperties.getMessagePool();
            Map<String, JavaMailSender> senderPool = new HashMap<>(messagePool.size());
            Set<String> keySet = messagePool.keySet();
            for (String key : keySet) {
                MessageMailProperties.MailProperties mailProperties = messagePool.get(key);
                JavaMailSenderImpl sender = new JavaMailSenderImpl();
                this.applyProperties(mailProperties, sender);
                senderPool.put(key, sender);
            }
            javaMallSenderPool.putAll(senderPool);
            int afterSize = javaMallSenderPool.size();
            Set<Object> afterKeySet = new HashSet<>(javaMallSenderPool.keySet());
            log.info("找打邮件消息实例数量:{},names:{},更新前数量:{},更新前names:{},更新后数量:{},更新后names:{}",
                    senderPool.size(), senderPool.keySet(), beforeSize, beforeKeySet, afterSize, afterKeySet);
            this.setMessageMailProperties(messageMailProperties);
        } catch (Exception e) {
            log.error("刷新邮件实例吃异常", e);
        }
    }

    public Optional<JavaMailSender> javaMailSender(Object sender) {
        try {
            JavaMailSender mailSender = javaMallSenderPool.get(sender);
            if (Objects.isNull(mailSender) && Objects.nonNull(javaMailSender)) {
                mailSender = javaMailSender;
            }
            return Optional.ofNullable(mailSender);
        } catch (Exception e) {
            log.error("获取邮件发送实例异常", e);
        }
        return Optional.empty();
    }

    private void applyProperties(MessageMailProperties.MailProperties properties, JavaMailSenderImpl sender) {
        sender.setHost(properties.getHost());
        if (properties.getPort() != null) {
            sender.setPort(properties.getPort());
        }
        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());
        sender.setProtocol(properties.getProtocol());
        if (properties.getDefaultEncoding() != null) {
            sender.setDefaultEncoding(properties.getDefaultEncoding().name());
        }
        if (!properties.getProperties().isEmpty()) {
            sender.setJavaMailProperties(asProperties(properties.getProperties()));
        }
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }

    public String javaMailSenderUserName(Object sender) {
        Optional<JavaMailSender> mailSender = this.javaMailSender(sender);
        if (mailSender.isEmpty()) {
            return null;
        }
        JavaMailSender mail = mailSender.get();
        if (mail instanceof JavaMailSenderImpl) {
            return ((JavaMailSenderImpl) mail).getUsername();
        }
        if (Objects.equals(mail, javaMailSender)) {
            return null;
        }
        String name = messageMailProperties.getMessagePool()
                .values()
                .stream()
                .filter(c -> Objects.equals(c.getSenderName(), sender))
                .findAny()
                .map(MessageMailProperties.MailProperties::getUsername)
                .orElse(null);
        return name;
    }

}
