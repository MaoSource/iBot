package com.source.service;

import com.source.bean.bot.Message;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/23/15:53
 */
public interface GroupHandler {

    /**
     * 根据Message进行解析
     * @param message 消息对象
     * @return 消息类型
     */
    boolean canAnalyze(Message message);

    /**
     * 消息解析
     * @param message 消息
     */
    void analyze(Message message);
}
