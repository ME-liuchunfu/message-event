# 一个基于springboot事件驱动的事件依赖

设计初衷：为了在项目中业务解耦合、避免主业务逻辑过多复杂逻辑，将一些与业务逻辑没有上下文关系的业务解耦合。
例如：
1、某个业务中需要用到短信，短信在业务中作为中间件使用。
2、某个业务需要将操作的信息广播推送个其他业务，改业务是耗时操作，而且业务逻辑对广播的消息没有强要求结果情况下使用事件驱动（事件驱动模块实现异步处理）。
3、在操作主业务过程中将结果通关邮件通知其他人（事件驱动内部实现邮件推送功能）。
4、使用到消息中间件(可以是用拓展模块处理)

## 1、使用流程

~~~xml
<dependency>
    <groupId>io.github.me-liuchunfu</groupId>
    <artifactId>springboot.message-event</artifactId>
    <version>0.0.1-RELEASE</version>
</dependency>
~~~

application.yml中加上以下配置：
~~~yml


# spring 邮件配置
spring:
  mail:
    host: smtp.163.com
    port: 465
    username: ss123456ss1020@163.com
    #password: JVsaadJ49X25c7V8
    password: xxx
    default-encoding: UTF-8
    properties:
      mail:
        smtp:
          auth: true
          enable: true
          ssl:
            # 设为true时 端口号设为 465 设为false时 端口号设为25
            enable: true
          socketFactory:
            class: javax.net.ssl.SSLSocketFactory
        debug: true

# 事件驱动配置
message:
  event:
    message:
      pool:
        # 核心线程数，默认为8
        core-size: 8
        # 最大线程数
        max-size: 10240
        # 队列容量
        queue-capacity: 102400
        # 线程活 keep-alive 时间，默认是60（单位毫秒）
        keep-alive: 100000
        # 开启线程池
        enabled: true
      thread-name-prefix: messageThreadPoolExecutor
      # sms
      # email
      # 日志配置
      logs:
        log: true #开启消息事件日志推送
        default-log: true #开启默认找不到监听通道日志推送
~~~

如果需要开启log4j日志需要加上日志开启配置：
这是spring默认的日志开关，事件驱动的日志开关可以开启spring日志后再配置开关

~~~yaml
# 开启日志记录
logging:
    level:
        com:
            spring:
                bootevent:
                    messageevent: debug

~~~

业务模块使用
需要在Application启动类或者配置类中使用注解
@EnableMessageEvent

~~~java

public class TestService {

    AtomicLong incr = new AtomicLong(0);

    public String get() {
        // 消息广播， 向test通道广播一条消息，使用异步的方式
        String s = "["+incr.incrementAndGet()+"]" + UUID.randomUUID().toString();
        MessageEvent messageEvent = new MessageEvent(this, MessageWrap.builder().eventId("test").event(s).build());
        MessageEventProduct.event().message(Boolean.TRUE).publishEvent(messageEvent);
        return s;
    }

    public String email(String email) {
        // 消息广播，像email邮件通道广播一条邮件消息，使用异步的方式
        String message = "测试下邮件发送";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(message);
        simpleMailMessage.setText(String.valueOf(System.currentTimeMillis()));
        MessageEvent messageEvent = new MessageEvent(this, MessageWrap.builder().eventId("email").event(simpleMailMessage).build());
        MessageEventProduct.event().message(Boolean.TRUE).publishEvent(messageEvent);
        System.out.println(message);
        return message;
    }

}
~~~


同步和异步的方式说明：

~~~java
public class Test {
    
    public void test() {
        // 同步方式：
        //方式一：
        MessageEvent messageEvent = xxx;
        MessageEventProduct.event().message(Boolean.FALSE).publishEvent(messageEvent);
        //方式二：
        MessageEvent messageEvent = xxx;
        MessageEventProduct.event().sync().publishEvent(messageEvent);


        // 异步方式：
        //方式一：
        MessageEvent messageEvent = xxx;
        MessageEventProduct.event().message(Boolean.TRUE).publishEvent(messageEvent);

        //方式二：
        MessageEvent messageEvent = xxx;
        MessageEventProduct.event().async().publishEvent(messageEvent);
    }
    
}
~~~

## 2、消息通道

1、系统默认通道: eamil(邮件推送通道)
2、如果需要自定义通道，将在项目目录：src/main/resources/META-INF/services 下新建 com.spring.bootevent.messageevent.message.listener.dispatcher.MessageDispatcher 文件
~~~properties
# 文件内容填写自定义的通道实现类， 该类会被spring自动代理 不需要加@Service、@Component 等注解、目前暂未支持自定义bean名称，目前使用类的 包名+类名 作为bean名称
xxx.xxxMessageDispatcher

~~~
自定义通道名称请不要重复，重复名称会替换已存在的bean，如果想获取当前所有的分发器通道可以注入 MessageEventListener调用findAllDispatcherChannel()


3、自定义通道手动注册与注销

~~~java

import com.spring.bootevent.messageevent.message.listener.dispatcher.MessageDispatcher;
import com.spring.bootevent.messageevent.message.util.SpringUtil;

public class Test {

    public void test() {
        // 当容器启动或者特定情况下可以通过将消息事件分发实例注入到bean中，然后手动调用消息事件注册方法实现注册通道
        MessageEventListener listener = SpringUtil.getBean(MessageEventListener.class);
        MessageDispatcher<MessageEvent> dispatcher = new MessageDispatcher<MessageEvent>() {
            @Override
            public Object getEventId() {
                return "abc";
            }

            @Override
            public void onEvent(MessageEvent event) {
                System.out.println(event);
            }
        };
        // 该方式会被spring容器注册
        listener.registerBean(dispatcher);
        
        // 手动注销
        listener.unregisterBean(dispatcher);
    }

}

~~~


## 3、消息中已经集成邮件推送
后续版本研发中会将邮件推送分离与其他中间件合并为统一消息中心



