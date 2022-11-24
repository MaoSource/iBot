package com.source.service.impl;

import com.source.bean.bot.Message;
import com.source.factory.PrivateFactory;
import com.source.service.MessageHandler;
import com.source.service.PrivateHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * 私聊消息处理器
 *
 * @author Source
 * @date 2022/11/23/15:34
 */
@Service
@Slf4j
public class PrivateMessageHandler implements MessageHandler {

    private static final String PRIVATE = "private";

    @Resource
    private PrivateFactory privateFactory;

    /**
     * 根据Message进行解析
     *
     * @param message 消息对象
     * @return 消息类型
     */
    @Override
    public boolean canMessage(Message message) {
        String msg = message.getMessage();
        return StringUtils.isNotBlank(msg) && PRIVATE.equals(message.getMessage_type());
    }

    /**
     * 消息处理
     *
     * @param message 消息对象
     */
    @Override
    public void message(Message message) {
        log.info("私聊消息->" + message.getMessage());
        PrivateHandler privateHandler = privateFactory.getPrivateHandler(message);
        if (privateHandler != null) {
            privateHandler.analyze(message);
        }
    }
}
