package com.spring.bootevent.messageevent.message.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;

/**
 * 事件驱动：字符转处理
 * @author spring
 * @date 2024-10-20
 */
public class StrUtil {

    public static String toJsonString(Object value) {
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException ignored) {

        }
        return Objects.toString(value);
    }

}
