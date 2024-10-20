package com.spring.bootevent.messageevent.message.executor;


/**
 * 事件广播：同步消息广播
 * @author spring
 * @date 2024-10-20
 */
public class SyncMessageExecutor extends MessageExecutor {

    @Override
    public void onEvent(Object event) {
        message(event);
    }

}
