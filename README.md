# 一个基于springboot事件驱动的事件依赖

设计初衷：为了在项目中业务解耦合、避免主业务逻辑过多复杂逻辑，将一些与业务逻辑没有上下文关系的业务解耦合。
例如：
1、某个业务中需要用到短信，短信在业务中作为中间件使用。
2、某个业务需要将操作的信息广播推送个其他业务，改业务是耗时操作，而且业务逻辑对广播的消息没有强要求结果情况下使用事件驱动（事件驱动模块实现异步处理）。
3、在操作主业务过程中将结果通关邮件通知其他人（事件驱动内部实现邮件推送功能）。
4、使用到消息中间件(可以是用拓展模块处理)

## 1、使用流程

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
      thread-name-prefix: messageThreadPoolExecutor
      # sms
      # email
~~~



业务模块使用

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
// 同步方式：
方式一：
MessageEvent messageEvent = xxx;
MessageEventProduct.event().message(Boolean.FALSE).publishEvent(messageEvent);
方式二：
MessageEvent messageEvent = xxx;
MessageEventProduct.event().sync().publishEvent(messageEvent);


// 异步方式：

方式一：
MessageEvent messageEvent = xxx;
MessageEventProduct.event().message(Boolean.TRUE).publishEvent(messageEvent);

方式二：
MessageEvent messageEvent = xxx;
MessageEventProduct.event().async().publishEvent(messageEvent);
~~~



