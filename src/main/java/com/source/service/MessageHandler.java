package com.source.service;

import com.source.bean.bot.Message;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/23/15:13
 */
public interface MessageHandler {

    /**
     * 根据Message进行解析
     * @param message 消息对象
     * @return 消息类型
     */
    boolean canMessage(Message message);

    /**
     * 消息处理
     * @param message 消息对象
     */
    void message(Message message);
}
