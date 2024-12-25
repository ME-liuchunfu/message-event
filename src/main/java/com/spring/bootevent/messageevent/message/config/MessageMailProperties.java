package com.spring.bootevent.messageevent.message.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.message-event.mail")
public class MessageMailProperties {

    private Map<String, MailProperties> messagePool = new HashMap<>();

    @Getter
    @Setter
    public static class MailProperties {

        /**
         * 实例发送者id
         */
        private String senderName;

        /**
         * SMTP server host. For instance, 'smtp.example.com'.
         */
        private String host;

        /**
         * SMTP server port.
         */
        private Integer port;

        /**
         * Login user of the SMTP server.
         */
        private String username;

        /**
         * Login password of the SMTP server.
         */
        private String password;

        /**
         * Protocol used by the SMTP server.
         */
        private String protocol = "smtp";

        /**
         * Default MimeMessage encoding.
         */
        private Charset defaultEncoding = StandardCharsets.UTF_8;

        /**
         * Additional JavaMail Session properties.
         */
        private Map<String, String> properties = new HashMap<>();

        /**
         * Session JNDI name. When set, takes precedence over other Session settings.
         */
        private String jndiName;
    }

}
