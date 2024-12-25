package com.spring.bootevent.messageevent.message.listener.dispatcher;

import com.spring.bootevent.messageevent.message.event.MessageWrapEvent;
import com.spring.bootevent.messageevent.message.exception.MessageAbortExecution;
import com.spring.bootevent.messageevent.message.listener.EventChanel;
import com.spring.bootevent.messageevent.message.third.email.EmailMessage;
import com.spring.bootevent.messageevent.message.third.email.MessageType;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 消息事件：邮件消息分发器
 * @author spring
 * date 2024-10-20
 */
@Slf4j
public class EmailMessageDispatcher implements MessageDispatcher<MessageWrapEvent> {

    @Resource
    JavaMailSender javaMailSender;

    @Resource
    MailProperties mailProperties;

    @Override
    public Object getEventId() {
        return EventChanel.CHANEL_EMAIL;
    }

    @Override
    public void onEvent(MessageWrapEvent event) {
        if (!((event.getEvent() instanceof EmailMessage[]) || (event.getEvent() instanceof EmailMessage))) {
            throw new MessageAbortExecution("消息实例:"+ parseMsg(event.getEvent()) +",非SimpleMessage");
        }
        EmailMessage[] sendDatas;
        if (event.getEvent().getClass().isArray()) {
            sendDatas = (EmailMessage[]) event.getEvent();
        } else {
            sendDatas = new EmailMessage[] {(EmailMessage) event.getEvent()};
        }
        String username = mailProperties.getUsername();
        for (EmailMessage sendData : sendDatas) {
            if (Strings.isBlank(sendData.getFrom())) {
                sendData.setFrom(username);
            }
        }
        // 按照 邮件类型分组 分发
        Map<MessageType, List<EmailMessage>> groupMap = Arrays.stream(sendDatas).collect(Collectors.groupingBy(EmailMessage::getMessageType));
        // multi type
        Set<MessageType> messageTypes = groupMap.keySet();
        for (MessageType messageType : messageTypes) {
            List<EmailMessage> messageList = groupMap.get(messageType);
            try {
                switch (messageType){
                    case MARKDOWN:
                        this.messageContentMarkdownToHtml(messageList);
                        MimeMessagePreparator[] markdownMimeMessagePreparators = this.copyMimeMessagePreparator(messageList);
                        javaMailSender.send(markdownMimeMessagePreparators);
                        break;
                    case HTML:
                        MimeMessagePreparator[] mimeMessagePreparators = this.copyMimeMessagePreparator(messageList);
                        javaMailSender.send(mimeMessagePreparators);
                        break;
                    case SIMPLE:
                    default:
                        SimpleMailMessage[] simpleMailMessages = this.copytSimpleMailMessage(messageList);
                        javaMailSender.send(simpleMailMessages);
                        break;
                }
            } catch (Exception e) {
                log.error("发送邮件消息失败", e);
            }
        }
    }

    /**
     * 将markdown消息转换成html
     * @param messageList 消息集
     */
    private void messageContentMarkdownToHtml(List<EmailMessage> messageList) {
        Parser parser = Parser.builder().build();
        for (EmailMessage emailMessage : messageList) {
            Document document = parser.parse(emailMessage.getText());
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String render = renderer.render(document);
            emailMessage.setText(render);
        }
    }

    /**
     * 转换成html格式
     * @param list 消息集
     * @return html消息
     */
    private MimeMessagePreparator[] copyMimeMessagePreparator(List<EmailMessage> list) {
        MimeMessagePreparator[] mimeMessagePreparators = new MimeMessagePreparator[list.size()];
        for (int i = 0; i < list.size(); i++) {
            EmailMessage emailMessage = list.get(i);
            MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom(emailMessage.getFrom());
                if (Strings.isNotBlank(emailMessage.getReplyTo())) {
                    messageHelper.setReplyTo(emailMessage.getReplyTo());
                }
                messageHelper.setTo(emailMessage.getTo());
                if (Objects.nonNull(emailMessage.getCc())) {
                    messageHelper.setCc(emailMessage.getCc());
                }
                if (Objects.nonNull(emailMessage.getBcc())) {
                    messageHelper.setBcc(emailMessage.getBcc());
                }
                if (Objects.nonNull(emailMessage.getSentDate())) {
                    messageHelper.setSentDate(emailMessage.getSentDate());
                }
                messageHelper.setSubject(emailMessage.getSubject());
                // the 'true' parameter indicates this is a HTML email
                messageHelper.setText(emailMessage.getText(), true);
            };
            mimeMessagePreparators[i] = mimeMessagePreparator;
        }
        return mimeMessagePreparators;
    }

    /**
     * 转换普通消息
     * @param list 入参消息
     * @return 普通消息
     */
    private SimpleMailMessage[] copytSimpleMailMessage(List<EmailMessage> list) {
        return copytSimpleMailMessage(list.toArray(new EmailMessage[0]));
    }

    /**
     * 转换普通消息
     * @param sendDatas 入参消息
     * @return 普通消息
     */
    private SimpleMailMessage[] copytSimpleMailMessage(EmailMessage[] sendDatas) {
        SimpleMailMessage[] simpleMailMessages = new SimpleMailMessage[sendDatas.length];
        for (int i = 0; i < simpleMailMessages.length; i++) {
            simpleMailMessages[i] = new SimpleMailMessage();
            BeanUtils.copyProperties(sendDatas[i], simpleMailMessages[i]);
        }
        return simpleMailMessages;
    }

}
