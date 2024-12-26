package io.github.me.liuchunfu.springboot.event.message.third.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.Date;

/**
 * 邮件消息建造
 */
public class EmailBuilder {

    /**
     * 建造实例
     * @return 返回实例
     */
    public static EmailBuilder builder() {
        return new EmailBuilder();
    }

    /**
     * 构造简单消息实例
     * @return 返回实例
     */
    public SimpleBuilder simpleMessage() {
        return new SimpleBuilder();
    }

    /**
     * 构造 html 消息实例
     * @return 返回实例
     */
    public HtmlBuilder htmlMessage() {
        return new HtmlBuilder();
    }

    /**
     * 构造 markdown 消息实例
     * @return 返回实例
     */
    public MarkdownBuilder markdownMessage() {
        return new MarkdownBuilder();
    }

    /**
     * 简单消息实例
     */
    public class SimpleBuilder extends AbsBuilder<SimpleMessage> {

        @Override
        public Class<SimpleMessage> buildClass() {
            return SimpleMessage.class;
        }

    }

    /**
     * html消息实例
     */
    public class HtmlBuilder extends AbsBuilder<HtmlMessage> {

        @Override
        public Class<HtmlMessage> buildClass() {
            return HtmlMessage.class;
        }

    }

    /**
     * markdown消息实例
     */
    public class MarkdownBuilder extends AbsBuilder<MarkdownMessage> {

        @Override
        public Class<MarkdownMessage> buildClass() {
            return MarkdownMessage.class;
        }

    }

    @Getter
    @Setter
    public abstract class AbsBuilder<T> implements Builder<T> {

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

        public AbsBuilder<T> from(String from) {
            this.from = from;
            return this;
        }

        public AbsBuilder<T> replyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        public AbsBuilder<T> to(String... to) {
            this.to = to;
            return this;
        }

        public AbsBuilder<T> cc(String... cc) {
            this.cc = cc;
            return this;
        }

        public AbsBuilder<T> bcc(String... bcc) {
            this.bcc = bcc;
            return this;
        }

        public AbsBuilder<T> sentDate(Date sentDate) {
            this.sentDate = sentDate;
            return this;
        }

        public AbsBuilder<T> subject(String subject) {
            this.subject = subject;
            return this;
        }

        public AbsBuilder<T> text(String text) {
            this.text = text;
            return this;
        }

    }

}



/**
 * 建造器
 */
interface Builder<T> {

    /**
     * 构建的类
     * @return 返回 构建类
     */
    Class<T> buildClass();

    /**
     * 构建
     * @return 返回实现此接口的实例， 如果构建失败，抛出运行时异常
     */
    default T build() {
        try {
            Constructor<T> constructor = buildClass().getConstructor();
            T instance = constructor.newInstance();
            // 复制值
            BeanUtils.copyProperties(this, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}