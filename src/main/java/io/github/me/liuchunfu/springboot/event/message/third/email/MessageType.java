package io.github.me.liuchunfu.springboot.event.message.third.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型
 * date：2024年12月25日10:38:10
 */
@Getter
@AllArgsConstructor
public enum MessageType {

    SIMPLE("simple", "普通文本邮件"),
    HTML("html", "html邮件"),
    MARKDOWN("markdown", "Markdown邮件"),

    ;

    private String type;
    private String desc;

}
