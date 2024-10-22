package com.spring.bootevent.messageevent.message.config;

import com.spring.bootevent.messageevent.message.thread.MessageAbortHandler;
import com.spring.bootevent.messageevent.message.thread.MessageThreadFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;

/**
 * 消息事件：消息配置中心，包括线程池、
 * @author spring
 * date 2024-10-20
 */
@Getter
@Setter
@Configuration
@ComponentScan(basePackages = "com.spring.bootevent.messageevent")
@ConfigurationProperties(prefix = "message.event.message")
public class MessageConfiguration {

    public static final String THREAD_PREF = "messageThreadPoolExecutor-";

    private MessagePoolProperties pool;

    private String threadNamePrefix = THREAD_PREF;

    private MessageLog logs = new MessageLog();

    @Bean("messageThreadPoolExecutor")
    @ConditionalOnProperty(name = "message.event.message.pool.enabled", havingValue = "true", matchIfMissing = false)
    public ThreadPoolExecutor messageThreadPoolExecutor(MessageConfiguration messageConfiguration) {
        MessagePoolProperties poolProperties = messageConfiguration.getPool();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(poolProperties.getCoreSize(), poolProperties.getMaxSize(),
                poolProperties.getKeepAlive(), TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(poolProperties.getQueueCapacity()), new MessageThreadFactory(THREAD_PREF));
        threadPoolExecutor.setRejectedExecutionHandler(new MessageAbortHandler());
        return threadPoolExecutor;
    }
}
