package com.spring.bootemail.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class SpringbootEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootEmailApplication.class, args);
//        javax.net.ssl.SSLSocketFactory
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setFrom(from);
//        simpleMailMessage.setTo(to);
//        simpleMailMessage.setCc(cc);
//        simpleMailMessage.setSubject(subject);
//        simpleMailMessage.setText(content);
//        javaMailSender.send(simpleMailMessage);
    }

}
