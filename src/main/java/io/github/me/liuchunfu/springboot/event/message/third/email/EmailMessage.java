package io.github.me.liuchunfu.springboot.event.message.third.email;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮件消息
 * date：2024年12月25日10:33:39
 */
@Getter
@Setter
public class EmailMessage implements Serializable {

    /**
     * 发送者邮箱
     */
    private String from;
    /**
     * 回复接受邮箱
     */
    private String replyTo;
    /**
     * 邮件接受者邮箱
     */
    private String[] to;
    /**
     * 邮件抄送邮箱
     */
    private String[] cc;
    /**
     * 邮件加密抄送邮箱
     */
    private String[] bcc;
    /**
     * 邮件发送时间
     */
    private Date sentDate;
    /**
     * 置邮件的主题
     */
    private String subject;
    /**
     * 邮件的正文
     */
    private String text;

    /**
     * 邮件类型
     */
    private MessageType messageType;

}
