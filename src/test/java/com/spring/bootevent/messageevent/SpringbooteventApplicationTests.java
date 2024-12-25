package com.spring.bootevent.messageevent;

import com.spring.bootevent.messageevent.message.SpringbooteventApplication;
import com.spring.bootevent.messageevent.message.listener.MessageEventListener;
import com.spring.bootevent.messageevent.message.third.email.EmailBuilder;
import com.spring.bootevent.messageevent.message.third.email.HtmlMessage;
import com.spring.bootevent.messageevent.message.third.email.MarkdownMessage;
import com.spring.bootevent.messageevent.message.third.email.SimpleMessage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@SpringBootTest(classes = SpringbooteventApplication.class)
class SpringbooteventApplicationTests {

    @Resource
    MessageEventListener messageEventListener;

    @Test
    void runTestChannelAsyncMessage() throws InterruptedException {
        int runTime = 10;
        for (int i = 1; i <= runTime; i++) {
            String s = "["+i+"]" + UUID.randomUUID().toString();
            messageEventListener.publishEvent("", s);
            System.out.println(Thread.currentThread().getName() + "--request:" + s);
        }
        Thread.sleep(5000);
    }

    @Test
    void runTestChannelEmailSimpleMessage() throws InterruptedException {
        SimpleMessage simpleMessage = EmailBuilder.builder()
                .simpleMessage()
                .text("你好啊")
                .sentDate(new Date())
                .subject("这里是标题")
                .to("429829320@qq.com")
                .build();
        messageEventListener.publishEmail(simpleMessage);
        System.out.println(Thread.currentThread().getName() + "--request:");
        Thread.sleep(5000);
    }

    @Test
    void runTestChannelEmailHtmlMessage() throws InterruptedException {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>简单的HTML页面</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>欢迎来到我的网页</h1>\n" +
                "    <p>这是一个简单的HTML页面，包含一段文本和一个图像。</p>\n" +
                "    <img src=\"https://dss2.bdstatic.com/lfoZeXSm1A5BphGlnYG/skin/877.jpg?2\" alt=\"示例图像\">\n" +
                "</body>\n" +
                "</html>";
        HtmlMessage htmlMessage = EmailBuilder.builder()
                .htmlMessage()
                .text(html)
                .sentDate(new Date())
                .subject("这里是标题")
                .to("429829320@qq.com")
                .build();
        messageEventListener.publishEmail(htmlMessage);
        System.out.println(Thread.currentThread().getName() + "--request:");
        Thread.sleep(5000);
    }

    @Test
    void runTestChannelEmailMarkdownMessage() throws InterruptedException {
        String markdown = "# 欢迎来到我的文件\n" +
                "\n" +
                "这是一个简单的Markdown文件，包含一段文本和一个图像。\n" +
                "\n" +
                "![示例图像](https://dss2.bdstatic.com/lfoZeXSm1A5BphGlnYG/skin/877.jpg?2)";
        MarkdownMessage markdownMessage = EmailBuilder.builder()
                .markdownMessage()
                .text(markdown)
                .sentDate(new Date())
                .subject("这里是标题")
                .to("429829320@qq.com")
                .build();
        messageEventListener.publishEmail(markdownMessage);
        System.out.println(Thread.currentThread().getName() + "--request:");
        Thread.sleep(5000);
    }

}
